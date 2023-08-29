package com.example.pdftooltest.util;

import org.apache.commons.io.FileUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class PdfTestUtils {
    private static final Path inputDocxPath = Paths.get("src/test/resources/file/Word.docx");

    private static final Path inputPdfPath = Paths.get("src/test/resources/file/Pdf.pdf");

    private static final Path outputPdfPath = Paths.get("src/test/resources/file/Output.pdf");

    public static String getBase64Docx() throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(inputDocxPath.toFile());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static String getBase64Pdf() throws IOException {
        byte[] fileContent = FileUtils.readFileToByteArray(inputPdfPath.toFile());
        return Base64.getEncoder().encodeToString(fileContent);
    }

    public static void saveBase64ToFile(String base64) throws IOException {
        var data = Base64.getDecoder().decode(base64);
        try (OutputStream stream = new FileOutputStream(outputPdfPath.toFile())) {
            stream.write(data);
        }
    }
}
