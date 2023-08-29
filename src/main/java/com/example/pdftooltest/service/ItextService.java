package com.example.pdftooltest.service;

import com.example.pdftooltest.model.dto.PdfAddWatermarkRequestDTO;
import com.example.pdftooltest.model.dto.PdfConvertRequestDTO;
import com.example.pdftooltest.model.dto.PdfEncryptRequestDTO;
import com.example.pdftooltest.model.dto.PdfResponseDTO;
import com.example.pdftooltest.util.Base64Utils;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.source.RandomAccessSourceFactory;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.extgstate.PdfExtGState;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.licensing.base.LicenseKey;
import com.itextpdf.pdfoffice.OfficeConverter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Slf4j
@Service
public class ItextService {

    private static final RandomAccessSourceFactory RANDOM_ACCESS_SOURCE_FACTORY = new RandomAccessSourceFactory();
    private static final ReaderProperties READER_PROPERTIES = new ReaderProperties();
    private static final int ENCRYPTION_ALGORITHM = EncryptionConstants.ENCRYPTION_AES_128;
    private static final int PROTECTED_PDF_PERMISSIONS = EncryptionConstants.ALLOW_PRINTING
        | EncryptionConstants.ALLOW_COPY
        | EncryptionConstants.ALLOW_DEGRADED_PRINTING
        | EncryptionConstants.ALLOW_SCREENREADERS;
    private static final byte[] OWNER_PASSWORD = null;

    public ItextService(@Value("${itext.license-key}") String licenseKey) {
        if(StringUtils.hasLength(licenseKey)){
            LicenseKey.loadLicenseFile(new ByteArrayInputStream(licenseKey.getBytes()));
        }else {
            log.warn("Cant not load itext license key");
        }

    }

    @SneakyThrows
    public PdfResponseDTO convertToPdf(PdfConvertRequestDTO pdfConvertRequestDTO) {
        byte[] docxBytes = Base64Utils.convertToBytes(pdfConvertRequestDTO.getDocx());
        try (var input = new ByteArrayInputStream(docxBytes);
            var output = new ByteArrayOutputStream()) {
            OfficeConverter.convertOfficeDocumentToPdf(input, output);
            var pdfBytes = output.toByteArray();
            var pdf = Base64Utils.convertToBase64(pdfBytes);
            return PdfResponseDTO.builder().pdf(pdf).build();
        }
    }

    @SneakyThrows
    public PdfResponseDTO encryptPdf(PdfEncryptRequestDTO pdfEncryptRequestDTO) {
        byte[] pdfBytes = Base64Utils.convertToBytes(pdfEncryptRequestDTO.getPdf());
        var source = RANDOM_ACCESS_SOURCE_FACTORY.createSource(pdfBytes);
        var outputStream = new ByteArrayOutputStream();
        var writerProperties = new WriterProperties();
        writerProperties.setStandardEncryption(
            pdfEncryptRequestDTO.getPassword().getBytes(),
            OWNER_PASSWORD,
            PROTECTED_PDF_PERMISSIONS,
            ENCRYPTION_ALGORITHM);
        //noinspection EmptyTryBlock
        try (var reader = new PdfReader(source, READER_PROPERTIES);
            var writer = new PdfWriter(outputStream, writerProperties);
            var ignored = new PdfDocument(reader, writer)) {
            // do nothing, just close resources
        }
        var outputPdfBytes = outputStream.toByteArray();
        var pdf = Base64Utils.convertToBase64(outputPdfBytes);
        return PdfResponseDTO.builder().pdf(pdf).build();
    }

    @SneakyThrows
    public PdfResponseDTO addWatermark(PdfAddWatermarkRequestDTO pdfAddWatermarkRequestDTO) {
        byte[] pdfBytes = Base64Utils.convertToBytes(pdfAddWatermarkRequestDTO.getPdf());
        var input = new ByteArrayInputStream(pdfBytes);
        var outputStream = new ByteArrayOutputStream();
        try (var pdfDoc = new PdfDocument(new PdfReader(input), new PdfWriter(outputStream));
            var doc = new Document(pdfDoc)) {
            var font = PdfFontFactory.createFont(FontProgramFactory.createFont(StandardFonts.HELVETICA));
            var paragraph = new Paragraph(pdfAddWatermarkRequestDTO.getWatermark())
                .setFont(font)
                .setFontSize(100);
            var extGState = new PdfExtGState().setFillOpacity(0.1f);
            for (int i = 1; i <= pdfDoc.getNumberOfPages(); i++) {
                var pdfPage = pdfDoc.getPage(i);
                var pageSize = pdfPage.getPageSize();
                var horizontalCenter = (pageSize.getLeft() + pageSize.getRight()) / 2;
                var verticalCenter = (pageSize.getTop() + pageSize.getBottom()) / 2;
                var over = new PdfCanvas(pdfPage);
                over.saveState();
                over.setExtGState(extGState);
                doc.showTextAligned(paragraph, horizontalCenter, verticalCenter, i, TextAlignment.CENTER,
                    VerticalAlignment.TOP, 45);
                over.restoreState();
            }
        }
        var outputPdfBytes = outputStream.toByteArray();
        var pdf = Base64Utils.convertToBase64(outputPdfBytes);
        return PdfResponseDTO.builder().pdf(pdf).build();
    }
}
