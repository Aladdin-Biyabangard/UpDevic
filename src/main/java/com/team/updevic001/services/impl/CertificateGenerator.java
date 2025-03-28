package com.team.updevic001.services.impl;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;

public class CertificateGenerator {

    public void certificateGenerator(String studentName) throws FileNotFoundException {
        String filePath = "certificate.pdf"; // Faylın adı
        File file = new File(filePath);
        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        // Sertifikat başlığı
        document.add(new Paragraph("TƏLİMİ BAŞA ÇATMA SERTİFİKATI").setBold().setFontSize(20));

        // İstifadəçinin adı (bunu dinamik edə bilərsən)
        document.add(new Paragraph("Bu sertifikat " + studentName + " istifadəçisinə təqdim edilir"));

        // Bitmə tarixi və imza hissəsi
        document.add(new Paragraph("Təlimi uğurla tamamladınız!").setFontSize(14));
        document.add(new Paragraph("Tarix: " + LocalDateTime.now()));
        document.add(new Paragraph("İmza: ___________________"));

        document.close();
        System.out.println("Sertifikat yaradıldı: " + filePath);
    }
}
