package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Sakrament;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PotvrdaPrimljenihSakramenataController {

    @FXML
    private ComboBox<String> odabirZupljaninaComboBox;




    public void initialize() {
        List<String> zupljaninList = BazaPodataka.dohvatiSveZupljane().stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));

    }

    public void izdajPotvrdu(){
        List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

        Zupljanin ovajZupljanin=sviZupljani.get(0);

        for (Zupljanin zupljanin:sviZupljani) {
            if(Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))){
                ovajZupljanin = zupljanin;
            }
        }
        List<String> popisSakramenata = new ArrayList<>();
        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        for (Sakrament sakrament:sakramenti) {
            for (Zupljanin zupljanin:sakrament.getZupljani()) {
                if(zupljanin.getId().equals(ovajZupljanin.getId())){
                    popisSakramenata.add(sakrament.getNaziv());
                }
            }
        }
        PDFGenerator.generatePDF("dat/sakramenti.pdf", popisSakramenata);





    }


}
