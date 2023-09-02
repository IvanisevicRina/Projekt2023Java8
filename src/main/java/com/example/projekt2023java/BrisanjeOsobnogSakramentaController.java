package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.OsobniSakrament;
import entitet.Svecenik;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;

import java.util.ArrayList;
import java.util.List;

public class BrisanjeOsobnogSakramentaController {
    @FXML
    private ListView<String> odabirOsobnogSakramentaListView ;


    public void obrisiOsobniSakrament() throws Exception {
        List<OsobniSakrament> osobniSakramentList = BazaPodataka.dohvatiSveOsobneSakramente();
        List<OsobniSakrament> oviOsobniSakramenti = new ArrayList<>();
        ObservableList<String> selectedItems = odabirOsobnogSakramentaListView.getSelectionModel().getSelectedItems();
        for(Object o : selectedItems){
            for (OsobniSakrament osobniSakrament:osobniSakramentList) {
                if(o.equals(osobniSakrament.getId()+ "-----"+osobniSakrament.getSakrament().getNaziv() + " " + osobniSakrament.getZupljanin().getIme() + " " + osobniSakrament.getZupljanin().getPrezime())){
                    oviOsobniSakramenti.add(osobniSakrament);
                }
            }
        }
        for(OsobniSakrament osobniSakrament:oviOsobniSakramenti){
            BazaPodataka.obrisiOsobniSakrament(osobniSakrament.getId().intValue());
        }
        initialize();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Brisanje osobnog sakramenta");
        alert.setHeaderText("Uspješno obrisan osobni sakrament");
        alert.showAndWait();



    }

    public void initialize(){
        List<OsobniSakrament> listaOsobnihSakramenata = BazaPodataka.dohvatiSveOsobneSakramente();
        List<String> osobniSakramentiList = listaOsobnihSakramenata.stream().map(p -> p.getId()+ "-----"+ p.getSakrament().getNaziv() + " " + p.getZupljanin().getIme() + " " + p.getZupljanin().getPrezime()).toList();
        odabirOsobnogSakramentaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirOsobnogSakramentaListView.setItems(FXCollections.observableList(osobniSakramentiList));

    }
}
