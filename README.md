# PDF Tool Test

## 需求
符合以下功能的，PDF 套件評比
1. Convert docx to PDF
2. Add watermark to PDF
3. Encrypt PDF

## 符合的套件
### iText
> https://itextpdf.com/

使用產品:
- itext-core
- pdfOffice

試用方式: 到官網 ( https://itextpdf.com/get-started ) 可以申請一個月試用 license

### Aspose
> https://www.aspose.com/

使用產品:
- Aspose.Words for Java
- Aspose.PDF for Java

試用方式: 可以直接使用，但是產出的檔案會有浮水印及說明文字

## 套件比較
### 轉檔效率
使用約 10 頁面的檔案，運行 10 次取平均值

|                      | iText  | Aspose  |
|----------------------|--------|---------|
| Convert docx to PDF  | 5.358s | 0.534 s |
| Add watermark to PDF | 0.143s | 0.285 s |
| Encrypt PDF          | 0.035s | 0.363 s |


## 總結
- iText 在加密及浮水印的速度較快，但時間差距不大
- Aspose 在轉檔的速度較快，且時間差距很大