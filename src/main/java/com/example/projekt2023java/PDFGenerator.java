package com.example.projekt2023java;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
public class PDFGenerator {

    public static void generatePDF(String fileName, List<String> sakramenti) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();

            // Dodajte sadržaj u PDF
            Paragraph paragraph = new Paragraph("Popis sakramenata za župljanina:\n");

            for (String sakrament : sakramenti) {
                paragraph.add(sakrament + "\n");
            }

            document.add(paragraph);

            document.close();
            System.out.println("PDF je generiran.");
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}