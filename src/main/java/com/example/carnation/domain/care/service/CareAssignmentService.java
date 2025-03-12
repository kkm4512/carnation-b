package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.cqrs.CareAssignmentCommand;
import com.example.carnation.domain.care.cqrs.CareAssignmentQuery;
import com.example.carnation.domain.care.dto.CareAssignmentResponse;
import com.example.carnation.domain.care.dto.CaregiverRequestDto;
import com.example.carnation.domain.care.dto.PatientRequestDto;
import com.example.carnation.domain.care.entity.CareAssignment;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.care.entity.Patient;
import com.example.carnation.domain.user.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CareRecordService")
public class CareAssignmentService {
    private final CareAssignmentQuery careAssignmentQuery;
    private final CareAssignmentCommand careAssignmentCommand;

    public CareAssignmentResponse create(final AuthUser authUser, final CaregiverRequestDto caregiverDto, final PatientRequestDto patientDto) {
        User user = User.of(authUser);
        Patient patient = Patient.of(user, patientDto);
        Caregiver caregiver = Caregiver.of(user, caregiverDto);
        CareAssignment careAssignment = CareAssignment.of(user, patient, caregiver);
        return CareAssignmentResponse.of(careAssignmentCommand.save(careAssignment));
    }

    public Page<CareAssignmentResponse> readAllMePage(final AuthUser authUser, final Pageable pageable) {
        User user = User.of(authUser);

        try {
            Page<CareAssignment> responses = careAssignmentQuery.readAllMePage(user, pageable);
            return responses.map(CareAssignmentResponse::of);
        } catch (Exception e) {
            throw e;
        }
    }

    public byte[] generateAssignmentPdf(final AuthUser authUser, final Long careAssignmentId) {
        User user = User.of(authUser);
        CareAssignment careAssignment = careAssignmentQuery.findOne(careAssignmentId);
        user.isMe(careAssignment.getUser().getId());

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            // 한글 폰트 로드
            InputStream fontStream = getClass().getResourceAsStream("/static/fonts/NotoSansKR-Regular.ttf");
            PDType0Font font = PDType0Font.load(document, fontStream);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 16);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 750);
                contentStream.showText("Care Assignment Report");
                contentStream.endText();

                contentStream.setFont(font, 12);
                int y = 700;

                // 간병 배정 정보 추가
                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText("Assignment ID: " + careAssignment.getId());
                contentStream.endText();
                y -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText("Patient Name: " + careAssignment.getPatient().getName());
                contentStream.endText();
                y -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText("Caregiver: " + careAssignment.getCaregiver().getName());
                contentStream.endText();
                y -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText("Start Date: " + careAssignment.getCaregiver().getStartDateTime());
                contentStream.endText();
                y -= 20;

                contentStream.beginText();
                contentStream.newLineAtOffset(100, y);
                contentStream.showText("End Date: " + careAssignment.getCaregiver().getEndDateTime());
                contentStream.endText();
            }

            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {  // 여기 catch 블록이 try 블록과 같은 레벨로 정리됨
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
