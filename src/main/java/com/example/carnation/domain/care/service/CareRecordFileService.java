package com.example.carnation.domain.care.service;

import com.example.carnation.domain.care.cqrs.CareMatchingQuery;
import com.example.carnation.domain.care.entity.CareMatching;
import com.example.carnation.domain.care.entity.Caregiver;
import com.example.carnation.domain.user.common.entity.User;
import com.example.carnation.security.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "CareRecordFileService")
public class CareRecordFileService {
    private final CareMatchingQuery careMatchingQuery;

    public byte[] createPdf(final AuthUser authUser, final Long careMatchingId) {
        User user = User.of(authUser);
        CareMatching careMatching = careMatchingQuery.readById(careMatchingId);
        Caregiver caregiver = careMatching.getCaregiver();
        user.validateIsMe(caregiver.getUser());
        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            PDPage page = new PDPage();
            document.addPage(page);

            // 한글 폰트 로드
            InputStream fontStream = getClass().getResourceAsStream("/static/fonts/NotoSansKR-Regular.ttf");
            PDType0Font font = PDType0Font.load(document, fontStream);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 18);
                contentStream.beginText();
                contentStream.newLineAtOffset(150, 750);
                contentStream.showText("카네이션 간병 서비스 이용내역 확인서");
                contentStream.endText();
                contentStream.setFont(font, 12);
                int y = 710;

                // 날짜 포맷 지정
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시");
                String startDate = careMatching.getStartDateTime().format(formatter);
                String endDate = careMatching.getEndDateTime().format(formatter);

                // 총 이용 시간 계산
                Duration duration = Duration.between(careMatching.getStartDateTime(), careMatching.getEndDateTime());
                long days = duration.toDays();
                long hours = duration.toHours() % 24;
                String totalUsage = "(" + days + "일 " + hours + "시간)";

                // **전체 테두리 추가**
                int boxHeight = 550;  // 전체 박스 높이
                contentStream.setLineWidth(1.5f);
                contentStream.addRect(60, y - boxHeight, 500, boxHeight);
                contentStream.stroke();

                // **표 내용 추가**
                y -= 30;
                drawTableRow(contentStream, font, "환자 성명", careMatching.getPatient().getName(), y);
                y -= 25;
                drawTableRow(contentStream, font, "간병인 연락처", careMatching.getCaregiver().getUser().getPhoneNumber(), y);
                y -= 25;
                drawTableRow(contentStream, font, "간병인 이름", careMatching.getCaregiver().getName(), y);
                y -= 25;
                drawTableRow(contentStream, font, "간병인 주민번호", careMatching.getCaregiver().getUser().getResidentRegistrationNumber() , y);
                y -= 25;
                drawTableRow(contentStream, font, "서비스 이용 기간", startDate + " ~ " + endDate, y);
                y -= 25;
                drawTableRow(contentStream, font, "총 이용 시간", totalUsage, y);

                // **중간 문구**
                y -= 150;
                contentStream.setFont(font, 14);
                contentStream.beginText();
                contentStream.newLineAtOffset(150, y);
                contentStream.showText("상기와 같이 카네이션 간병 서비스 이용을 증명합니다.");
                contentStream.endText();

                // 오늘날짜
                y -= 30;
                formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
                String now = LocalDateTime.now().format(formatter);
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(250, y);
                contentStream.showText(now);
                contentStream.endText();

                // **회사명 & 대표이사**
                y -= 150;
                contentStream.setFont(font, 24);
                contentStream.beginText();
                contentStream.newLineAtOffset(220, y);
                contentStream.showText("(주) 카네이션");
                contentStream.endText();

                y -= 25;
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(240, y);
                contentStream.showText("대 표 이 사 이 채 은");
                contentStream.endText();

                // 도장 이미지 로드 (try-with-resources 적용)
                try (InputStream imageStream = getClass().getResourceAsStream("/static/stamp/carnationStamp.png")) {
                    if (imageStream != null) {
                        PDImageXObject stampImage = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "stamp");

                        // **도장 이미지 삽입 (오른쪽)**
                        int imageX = 350; // 도장을 오른쪽에 배치
                        int imageY = y - 20; // 텍스트 아래로 약간 내려서 정렬
                        int imageWidth = 70; // 도장 크기 조정
                        int imageHeight = 70;
                        contentStream.drawImage(stampImage, imageX, imageY, imageWidth, imageHeight);
                    } else {
                        log.error("도장 이미지 파일을 찾을 수 없습니다.");
                    }
                }

                // **왼쪽 하단에 주의사항 배치**
                y -= 60;
                contentStream.setFont(font, 10);
                contentStream.beginText();
                contentStream.newLineAtOffset(70, y);
                contentStream.showText("※ 본 증명서는 열람용이므로, 법적인 효력을 갖지 않습니다.");
                contentStream.endText();

            }

            document.save(outputStream);
            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF 생성 중 오류 발생", e);
        }
    }

    private void drawTableRow(PDPageContentStream contentStream, PDType0Font font, String label, String value, int y) throws Exception {
        contentStream.setFont(font, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(80, y);
        contentStream.showText(label + ": " + value);
        contentStream.endText();
    }
}
