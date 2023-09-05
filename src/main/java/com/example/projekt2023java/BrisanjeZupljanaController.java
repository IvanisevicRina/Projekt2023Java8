package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BrisanjeZupljanaController {
    @FXML
    private ListView<String> odabirZupljaninaListView ;



    public void obrisiZupljane() throws Exception {

        List<Zupljanin> sviZupljani= BazaPodataka.dohvatiSveZupljane();
        List<Zupljanin> oviZupljani= new ArrayList<>();
        ObservableList<String> selectedItems = odabirZupljaninaListView.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            Alert noSelectionAlert = new Alert(Alert.AlertType.WARNING);
            noSelectionAlert.setTitle("Nema odabranih župljana");
            noSelectionAlert.setHeaderText(null);
            noSelectionAlert.setContentText("Molimo odaberite župljane koje želite obrisati.");
            noSelectionAlert.showAndWait();
            return;
        }



        for(Object o : selectedItems){
            for (Zupljanin zupljanin:sviZupljani) {
                if(o.equals(zupljanin.getIme() + " " +zupljanin.getPrezime())){
                    oviZupljani.add(zupljanin);
                }
            }
        }

        Alert potvrdaAlert = new Alert(Alert.AlertType.CONFIRMATION);
        potvrdaAlert.setTitle("Potvrda brisanja");
        potvrdaAlert.setHeaderText("Jeste li sigurni da želite obrisati župljanina?");


        ButtonType daButton = new ButtonType("Da");
        ButtonType neButton = new ButtonType("Ne");

        potvrdaAlert.getButtonTypes().setAll(daButton, neButton);

        Optional<ButtonType> rezultat = potvrdaAlert.showAndWait();

        if (rezultat.isPresent() && rezultat.get() == daButton) {
            for (Zupljanin zupljanin : oviZupljani) {
                BazaPodataka.obrisiZupljanina(zupljanin.getId().intValue());
            }

            initialize();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Brisanje zupljanina");
            alert.setHeaderText("Uspješno obrisan zupljanin");
            alert.showAndWait();
        }
    }


    public void initialize(){
        List<Zupljanin> zupljanin = BazaPodataka.dohvatiSveZupljane();
        List<String> zupljaninList = zupljanin.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirZupljaninaListView.setItems(FXCollections.observableList(zupljaninList));

    }
}
