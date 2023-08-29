package com.example.pdftooltest.controller;

import com.example.pdftooltest.model.dto.PdfAddWatermarkRequestDTO;
import com.example.pdftooltest.model.dto.PdfConvertRequestDTO;
import com.example.pdftooltest.model.dto.PdfEncryptRequestDTO;
import com.example.pdftooltest.model.dto.PdfResponseDTO;
import com.example.pdftooltest.service.ItextService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("itext")
@RestController
public class ItextController implements PdfApi {

    private final ItextService itextService;

    @Operation(summary = "Convert to pdf")
    @PostMapping("convertToPdf")
    @Override
    public ResponseEntity<PdfResponseDTO> convertToPdf(@RequestBody PdfConvertRequestDTO pdfConvertRequestDTO) {
        StopWatch stopWatch = new StopWatch("itext convertToPdf");
        stopWatch.start();
        var result = itextService.convertToPdf(pdfConvertRequestDTO);
        stopWatch.stop();
        log.info(stopWatch.getId() + ": " + stopWatch.getLastTaskTimeMillis() / 1000f + "s");
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Encrypt pdf")
    @PostMapping("encryptPdf")
    @Override
    public ResponseEntity<PdfResponseDTO> encryptPdf(@RequestBody PdfEncryptRequestDTO pdfEncryptRequestDTO) {
        StopWatch stopWatch = new StopWatch("itext encryptPdf");
        stopWatch.start();
        var result = itextService.encryptPdf(pdfEncryptRequestDTO);
        stopWatch.stop();
        log.info(stopWatch.getId() + ": " + stopWatch.getLastTaskTimeMillis() / 1000f + "s");
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Add watermark")
    @PostMapping("addWatermark")
    @Override
    public ResponseEntity<PdfResponseDTO> addWatermark(
        @RequestBody PdfAddWatermarkRequestDTO pdfAddWatermarkRequestDTO) {
        StopWatch stopWatch = new StopWatch("itext addWatermark");
        stopWatch.start();
        var result = itextService.addWatermark(pdfAddWatermarkRequestDTO);
        stopWatch.stop();
        log.info(stopWatch.getId() + ": " + stopWatch.getLastTaskTimeMillis() / 1000f + "s");
        return ResponseEntity.ok(result);
    }
}
