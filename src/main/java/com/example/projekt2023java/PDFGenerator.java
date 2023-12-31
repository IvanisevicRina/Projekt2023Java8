package com.example.projekt2023java;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import entitet.Zupljanin;
import javafx.scene.control.Alert;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class PDFGenerator {

    public static void generatePDF(String fileName, List<String> sakramenti, Zupljanin zupljanin) {
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName));
            document.open();



            FontFactory.register("css/NoticiaText-Italic.ttf", "custom_font");


            Font headerFont = FontFactory.getFont("custom_font", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 20);
            Paragraph header = new Paragraph("Popis sakramenata za župljanina " + zupljanin.getIme() + " " + zupljanin.getPrezime(), headerFont);
            header.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(header);


            Font contentFont = FontFactory.getFont("custom_font", BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12);
            Paragraph content = new Paragraph();
            content.add(new Paragraph("\n\n\n\n                     Sakramenti:", contentFont));
            for (String sakrament : sakramenti) {
                content.add(new Paragraph("                                                " +sakrament, contentFont));
            }

            document.add(content);



            Image image = Image.getInstance("css/image2.png");
            image.scaleAbsolute(100, 100);
            image.setAbsolutePosition(411, 420);
            document.add(image);





            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("GENERIRANO");
            alert.setHeaderText(null);
            alert.setContentText("Uspješno generiran PDF dokument");
            alert.showAndWait();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
