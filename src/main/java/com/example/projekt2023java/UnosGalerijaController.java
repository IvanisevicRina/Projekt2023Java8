package com.example.projekt2023java;
import baza.BazaPodataka;
import entitet.Slike;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UnosGalerijaController {
    @FXML
    private TextField nazivSlikeField;

    @FXML
    private Button odaberiSlikuButton;

    @FXML
    private ListView<String> odabirZupljanaListView ;


    private File odabranaSlikaFile;



    public void odaberiSliku() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Odaberi sliku");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Slike", "*.jpg", "*.jpeg", "*.png"));
        odabranaSlikaFile = fileChooser.showOpenDialog(odaberiSlikuButton.getScene().getWindow());

        if (odabranaSlikaFile != null) {
            nazivSlikeField.setText(odabranaSlikaFile.getName());
        }
    }

    public void spremiSliku() {
        if (odabranaSlikaFile != null) {
            try {
                byte[] slikaBytes = ucitajSlikuBytes(odabranaSlikaFile);

                BazaPodataka.spremiSliku(nazivSlikeField.getText(), slikaBytes);

                Slike slika = new Slike(nazivSlikeField.getText(), slikaBytes);

                List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

                ObservableList<String> selectedItems = odabirZupljanaListView.getSelectionModel().getSelectedItems();
                for (Object o : selectedItems) {
                    for (Zupljanin zupljanin : sviZupljani) {
                        System.out.println("o.prezime = " + o);
                        System.out.println("zupljanin= " + zupljanin.getPrezime());
                        if (o.equals(zupljanin.getIme() + " " + zupljanin.getPrezime())) {
                            List<Slike> slike_zupljanina = new ArrayList<>();
                            if(zupljanin.getSlike().isEmpty()){
                                slike_zupljanina.add(slika);
                            }else {
                                slike_zupljanina = zupljanin.getSlike();
                                slike_zupljanina.add(slika);
                            }

                            BazaPodataka.spremiSlikuZupljanina(zupljanin.getId().intValue(),nazivSlikeField.getText(),slikaBytes);

                            zupljanin.setSlike(slike_zupljanina);

                            BazaPodataka.azurirajZupljanineSlike(zupljanin);

                        }
                    }
                }


                System.out.println("Slika je uspješno spremljena.");

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Spremanje slike");
                alert.setHeaderText("Uspješno dodana slika");
                alert.setContentText("SLIKA dodana u aplikaciju!");
                alert.showAndWait();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Spremanje slike NEUSPJELO");
                alert.setHeaderText("Neuspješno dodana slika");
                alert.showAndWait();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    private byte[] ucitajSlikuBytes(File slikaFile) throws IOException {
        try (FileInputStream fileInputStream = new FileInputStream(slikaFile)) {
            byte[] buffer = new byte[(int) slikaFile.length()];
            fileInputStream.read(buffer);
            return buffer;
        }
    }

    public void initialize(){

        List<Zupljanin> zupljani = BazaPodataka.dohvatiSveZupljane();
        List<String> zupljaniList = zupljani.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljanaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirZupljanaListView.setItems(FXCollections.observableList(zupljaniList));
    }
}
