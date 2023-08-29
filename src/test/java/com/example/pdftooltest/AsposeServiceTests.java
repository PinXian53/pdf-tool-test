package com.example.pdftooltest;

import com.example.pdftooltest.model.dto.PdfAddWatermarkRequestDTO;
import com.example.pdftooltest.model.dto.PdfConvertRequestDTO;
import com.example.pdftooltest.model.dto.PdfEncryptRequestDTO;
import com.example.pdftooltest.service.AsposeService;
import com.example.pdftooltest.util.PdfTestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AsposeServiceTests {
    @Autowired
    AsposeService asposeService;

    @Test
    void testConvertToPdf() throws Exception {
        var base64Docx = PdfTestUtils.getBase64Docx();
        var pdfResponseDTO = asposeService.convertToPdf(
            PdfConvertRequestDTO.builder()
                .docx(base64Docx)
                .build()
        );
        PdfTestUtils.saveBase64ToFile(pdfResponseDTO.getPdf());
    }

    @Test
    void testEncryptPdf() throws Exception {
        var base64Pdf = PdfTestUtils.getBase64Pdf();
        var pdfResponseDTO = asposeService.encryptPdf(
            PdfEncryptRequestDTO.builder()
                .pdf(base64Pdf)
                .password("password")
                .build()
        );
        PdfTestUtils.saveBase64ToFile(pdfResponseDTO.getPdf());
    }

    @Test
    void testAddWatermarkPdf() throws Exception {
        var base64Pdf = PdfTestUtils.getBase64Pdf();
        var pdfResponseDTO = asposeService.addWatermark(
            PdfAddWatermarkRequestDTO.builder()
                .pdf(base64Pdf)
                .watermark("watermark")
                .build()
        );
        PdfTestUtils.saveBase64ToFile(pdfResponseDTO.getPdf());
    }
}
