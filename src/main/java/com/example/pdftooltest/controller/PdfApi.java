package com.example.pdftooltest.controller;

import com.example.pdftooltest.model.dto.PdfAddWatermarkRequestDTO;
import com.example.pdftooltest.model.dto.PdfConvertRequestDTO;
import com.example.pdftooltest.model.dto.PdfEncryptRequestDTO;
import com.example.pdftooltest.model.dto.PdfResponseDTO;
import org.springframework.http.ResponseEntity;

public interface PdfApi {

    ResponseEntity<PdfResponseDTO> convertToPdf(
        PdfConvertRequestDTO pdfConvertRequestDTO
    );

    ResponseEntity<PdfResponseDTO> encryptPdf(
        PdfEncryptRequestDTO pdfEncryptRequestDTO
    );

    ResponseEntity<PdfResponseDTO> addWatermark(
        PdfAddWatermarkRequestDTO pdfAddWatermarkRequestDTO
    );

}
