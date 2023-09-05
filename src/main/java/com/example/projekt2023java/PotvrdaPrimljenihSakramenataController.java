package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Sakrament;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PotvrdaPrimljenihSakramenataController {

    @FXML
    private ComboBox<String> odabirZupljaninaComboBox;

    @FXML
    private Node izbornikInclude;

    @FXML
    private Node izbornikZupljaninaInclude;


    public void initialize() {
        List<String> zupljaninList = BazaPodataka.dohvatiSveZupljane().stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));
        boolean useIzbornikZupljanina = !getUserRole().equals("Svecenik");
        System.out.println("rola: " + getUserRole());
        izbornikInclude.setVisible(!useIzbornikZupljanina);
        izbornikZupljaninaInclude.setVisible(useIzbornikZupljanina);
    }

    public void izdajPotvrdu() {
        List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

        Zupljanin ovajZupljanin = sviZupljani.get(0);

        for (Zupljanin zupljanin : sviZupljani) {
            if (Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))) {
                ovajZupljanin = zupljanin;
            }
        }
        List<String> popisSakramenata = new ArrayList<>();
        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        for (Sakrament sakrament : sakramenti) {
            for (Zupljanin zupljanin : sakrament.getZupljani()) {
                if (zupljanin.getId().equals(ovajZupljanin.getId())) {
                    popisSakramenata.add(sakrament.getNaziv());
                }
            }
        }
        PDFGenerator.generatePDF("dat/sakramenti.pdf", popisSakramenata, ovajZupljanin);

    }
    @FXML
    private void downloadPDF(ActionEvent event) {

        String selectedZupljaninName = odabirZupljaninaComboBox.getValue();
        String zupljaninFirstName = selectedZupljaninName.split(" ")[0];


        String fileName = zupljaninFirstName + "_sakramenti.pdf";

        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(fileName);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File selectedFile = fileChooser.showSaveDialog(null);
        if (selectedFile != null) {
            try {
                FileInputStream inputStream = new FileInputStream("dat/sakramenti.pdf");
                FileOutputStream outputStream = new FileOutputStream(selectedFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                inputStream.close();
                outputStream.close();

                showDownloadSuccessfulAlert();
            } catch (IOException e) {
                e.printStackTrace();
                showDownloadFailedAlert();
            }
        }
    }

    private void showDownloadSuccessfulAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Download Successful");
        alert.setHeaderText(null);
        alert.setContentText("PDF file downloaded successfully.");
        alert.showAndWait();
    }

    private void showDownloadFailedAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Download Failed");
        alert.setHeaderText(null);
        alert.setContentText("Failed to download the PDF file.");
        alert.showAndWait();
    }


    private String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
