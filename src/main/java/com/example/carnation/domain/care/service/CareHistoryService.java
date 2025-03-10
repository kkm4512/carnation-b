package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.constans.MediaType;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.cqrs.CareHistoryCommand;
import com.example.carnation.domain.care.dto.CareHistoryRequestDto;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.entity.CareHistory;
import com.example.carnation.domain.care.entity.CareMedia;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import com.example.carnation.domain.file.validation.FileValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CareHistoryService {
    private final CareAssignmentQuery careAssignmentQuery;
    private final CareHistoryCommand careHistoryCommand;

    public void create(AuthUser authUser, Long careAssignmentId, CareHistoryRequestDto dto) {
        User user = User.of(authUser);
        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());
        CareHistory careHistory = CareHistory.of(careAssignment,dto);
        List<CareMedia> medias = new ArrayList<>();
        for (MultipartFile m: dto.getImageFiles()) {
            FileValidation.validateImageFile(m);
            CareMedia careMedia = new CareMedia(
                    "images/careAssignment" + "/" + careAssignment.getId() + "/" + m.getOriginalFilename(),
                    MediaType.IMAGE,
                    careHistory,
                    m.getName(),
                    m.getOriginalFilename(),
                    String.valueOf(m.getSize())
            );
            medias.add(careMedia);
        }
        for (MultipartFile m: dto.getVideoFiles()) {
            FileValidation.validateVideoFile(m);
            CareMedia careMedia = new CareMedia(
                    "videos/careAssignment" + "/" + careAssignment.getId() + "/" + m.getOriginalFilename(),
                    MediaType.VIDEO,
                    careHistory,
                    m.getName(),
                    m.getOriginalFilename(),
                    String.valueOf(m.getSize())
            );
            medias.add(careMedia);
        }
        careHistory.getMedias().addAll(medias);
        careHistoryCommand.save(careHistory);
    }
}
