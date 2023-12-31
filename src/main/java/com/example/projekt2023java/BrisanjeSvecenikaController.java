package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class BrisanjeSvecenikaController {
    @FXML
    private ListView<String> odabirSvecenikaListView ;

    public void obrisiSvecenike() throws Exception {
        List<Svecenik> sviSvecenici = BazaPodataka.dohvatiSveSvecenike();
        List<Svecenik> oviSvecenici = new ArrayList<>();
        ObservableList<String> selectedItems = odabirSvecenikaListView.getSelectionModel().getSelectedItems();
        for(Object o : selectedItems){
            for (Svecenik svecenik:sviSvecenici) {
                if(o.equals(svecenik.getSifra()+ "-----"+ svecenik.getIme() + " " + svecenik.getPrezime())){
                    oviSvecenici.add(svecenik);
                }
            }
        }


        Alert potvrdaAlert = new Alert(Alert.AlertType.CONFIRMATION);
        potvrdaAlert.setTitle("Potvrda brisanja");
        potvrdaAlert.setHeaderText("Jeste li sigurni da želite obrisati označene svećenike?");


        ButtonType daButton = new ButtonType("Da");
        ButtonType neButton = new ButtonType("Ne");

        potvrdaAlert.getButtonTypes().setAll(daButton, neButton);

        Optional<ButtonType> rezultat = potvrdaAlert.showAndWait();

        if (rezultat.isPresent() && rezultat.get() == daButton) {

            for (Svecenik svecenik : oviSvecenici) {
                BazaPodataka.obrisiSvecenika(svecenik.getId().intValue());
            }

            initialize();
            Alert uspjehAlert = new Alert(Alert.AlertType.INFORMATION);
            uspjehAlert.setTitle("Brisanje svećenika");
            uspjehAlert.setHeaderText("Uspješno obrisan svećenik");
            uspjehAlert.showAndWait();
        }
    }



    public void initialize(){
        List<Svecenik> listaSvecenika = BazaPodataka.dohvatiSveSvecenike();
        List<String> svecenikList = listaSvecenika.stream().map(p -> p.getSifra()+ "-----"+ p.getIme() + " " + p.getPrezime()).toList();
        odabirSvecenikaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirSvecenikaListView.setItems(FXCollections.observableList(svecenikList));

    }

}
