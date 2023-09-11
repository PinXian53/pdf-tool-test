package com.example.pdftooltest.service;

import com.aspose.pdf.CryptoAlgorithm;
import com.aspose.pdf.HorizontalAlignment;
import com.aspose.pdf.VerticalAlignment;
import com.aspose.pdf.WatermarkArtifact;
import com.aspose.pdf.facades.DocumentPrivilege;
import com.aspose.pdf.facades.EncodingType;
import com.aspose.pdf.facades.FontStyle;
import com.aspose.pdf.facades.FormattedText;
import com.example.pdftooltest.model.dto.PdfAddWatermarkRequestDTO;
import com.example.pdftooltest.model.dto.PdfConvertRequestDTO;
import com.example.pdftooltest.model.dto.PdfEncryptRequestDTO;
import com.example.pdftooltest.model.dto.PdfResponseDTO;
import com.example.pdftooltest.util.Base64Utils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class AsposeService {

    private static final int PDF_SAVE_FORMAT = 40;
    private static final int PROTECTED_PDF_PERMISSIONS = DocumentPrivilege.getPrint().getValue()
        | DocumentPrivilege.getCopy().getValue()
        | DocumentPrivilege.getDegradedPrinting().getValue()
        | DocumentPrivilege.getScreenReaders().getValue();
    private static final CryptoAlgorithm ENCRYPTION_ALGORITHM = CryptoAlgorithm.AESx256;

    public AsposeService(
        @Value("${aspose.license-key.word}") String wordLicenseKey,
        @Value("${aspose.license-key.pdf}") String pdfLicenseKey) throws Exception {
        if (StringUtils.hasLength(wordLicenseKey)) {
            var wordLicense = new com.aspose.words.License();
            wordLicense.setLicense(new ByteArrayInputStream(wordLicenseKey.getBytes()));
            log.info("A license for the product \"aspose word\" was loaded successfully!");
        }
        if (StringUtils.hasLength(pdfLicenseKey)) {
            var pdfLicense = new com.aspose.pdf.License();
            pdfLicense.setLicense(new ByteArrayInputStream(pdfLicenseKey.getBytes()));
            log.info("A license for the product \"aspose pdf\" was loaded successfully!");
        }
    }

    @SneakyThrows
    public PdfResponseDTO convertToPdf(PdfConvertRequestDTO pdfConvertRequestDTO) {
        byte[] docxBytes = Base64Utils.convertToBytes(pdfConvertRequestDTO.getDocx());
        try (var input = new ByteArrayInputStream(docxBytes);
            var output = new ByteArrayOutputStream()) {
            var wordDoc = new com.aspose.words.Document(input);
            wordDoc.save(output, PDF_SAVE_FORMAT);
            var pdfBytes = output.toByteArray();
            var pdf = Base64Utils.convertToBase64(pdfBytes);
            return PdfResponseDTO.builder().pdf(pdf).build();
        }
    }

    @SneakyThrows
    public PdfResponseDTO encryptPdf(PdfEncryptRequestDTO pdfEncryptRequestDTO) {
        byte[] pdfBytes = Base64Utils.convertToBytes(pdfEncryptRequestDTO.getPdf());
        try (var input = new ByteArrayInputStream(pdfBytes);
            var pdfDoc = new com.aspose.pdf.Document(input);
            var output = new ByteArrayOutputStream()) {
            pdfDoc.encrypt(pdfEncryptRequestDTO.getPassword(), null, PROTECTED_PDF_PERMISSIONS,
                ENCRYPTION_ALGORITHM, false);
            pdfDoc.save(output);
            var outputPdfBytes = output.toByteArray();
            var pdf = Base64Utils.convertToBase64(outputPdfBytes);
            return PdfResponseDTO.builder().pdf(pdf).build();
        }
    }

    @SneakyThrows
    public PdfResponseDTO addWatermark(PdfAddWatermarkRequestDTO pdfAddWatermarkRequestDTO) {
        byte[] pdfBytes = Base64Utils.convertToBytes(pdfAddWatermarkRequestDTO.getPdf());
        try (var input = new ByteArrayInputStream(pdfBytes);
            var pdfDoc = new com.aspose.pdf.Document(input);
            var output = new ByteArrayOutputStream()) {
            var formattedText = new FormattedText(pdfAddWatermarkRequestDTO.getWatermark(), Color.GRAY,
                FontStyle.CjkFont, EncodingType.Identity_h, true, 72.0F);
            // 需要每頁設定一個新的 artifact
            // 若只建立一個 artifact 設定到每一頁，第二頁開始 Rotation 就不會生效，感覺是 BUG
            pdfDoc.getPages().forEach(page -> {
                try (var artifact = new WatermarkArtifact()) {
                    artifact.setText(formattedText);
                    artifact.setArtifactHorizontalAlignment(HorizontalAlignment.Center);
                    artifact.setArtifactVerticalAlignment(VerticalAlignment.Center);
                    artifact.setRotation(45);
                    artifact.setOpacity(0.1f);
                    artifact.setBackground(true);
                    page.getArtifacts().add(artifact);
                }
            });

            pdfDoc.save(output);
            var outputPdfBytes = output.toByteArray();
            var pdf = Base64Utils.convertToBase64(outputPdfBytes);
            return PdfResponseDTO.builder().pdf(pdf).build();
        }
    }
}
