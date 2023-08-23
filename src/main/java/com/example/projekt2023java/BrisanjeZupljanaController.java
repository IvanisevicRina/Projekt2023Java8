package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;

public class BrisanjeZupljanaController {
    @FXML
    private ListView<String> odabirZupljaninaListView ;


    public void obrisiZupljane() throws Exception {

        List<Zupljanin> sviZupljani= BazaPodataka.dohvatiSveZupljane();
        List<Zupljanin> oviZupljani= new ArrayList<>();
        ObservableList<String> selectedItems = odabirZupljaninaListView.getSelectionModel().getSelectedItems();
        for(Object o : selectedItems){
            for (Zupljanin zupljanin:sviZupljani) {
                System.out.println("o.prezime = " + o);
                System.out.println("student= " +zupljanin.getPrezime());
                if(o.equals(zupljanin.getIme() + " " +zupljanin.getPrezime())){
                    oviZupljani.add(zupljanin);
                }
            }
        }
        for (Zupljanin zupljanin:oviZupljani) {
            BazaPodataka.obrisiZupljanina(zupljanin.getId().intValue());
        }
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Brisanje zupljanina");
        alert.setHeaderText("Uspješno obrisan zupljanin");
        alert.showAndWait();

    }



    public void initialize(){
        List<Zupljanin> zupljanin = BazaPodataka.dohvatiSveZupljane();
        List<String> zupljaninList = zupljanin.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirZupljaninaListView.setItems(FXCollections.observableList(zupljaninList));

    }
}
