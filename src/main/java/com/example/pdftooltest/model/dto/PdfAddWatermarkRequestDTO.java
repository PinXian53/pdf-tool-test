package com.example.pdftooltest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class PdfAddWatermarkRequestDTO {

    private String pdf;
    private String watermark;
}

