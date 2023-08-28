package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import iznimke.TekstualniZapisException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.List;

public class IzmjenaSvecenikaController {

    @FXML
    private TextField imeSvecenikaTextField;
    @FXML
    private TextField sifraSvecenikaTextField;
    @FXML
    private TextField prezimeSvecenikaTextField;
    @FXML
    private TextField titulaSvecenikaTextField;
    @FXML
    private ListView<String> odabirSvecenikaListView ;


    public void azurirajSvecenike() throws Exception {
        String imeSvecenika = imeSvecenikaTextField.getText();
        String prezimeSvecenika = prezimeSvecenikaTextField.getText();

        String sifraSvecenika= sifraSvecenikaTextField.getText();
        String titulaSvecenika = titulaSvecenikaTextField.getText();
        List<Svecenik> sviSvecenici = BazaPodataka.dohvatiSveSvecenike();
        Svecenik ovajSvecenik = null;
        ObservableList<String> selectedItems = odabirSvecenikaListView.getSelectionModel().getSelectedItems();

        if (selectedItems.isEmpty()) {
            displayAlert("Greška", "Morate odabrati svećenika za ažuriranje.");
            return;
        }

        try {
            sadrziBrojeve(imeSvecenika);
            sadrziBrojeve(prezimeSvecenika);

        } catch (TekstualniZapisException e) {
            displayAlert("Greška", e.getMessage());
        }
        for(Object o : selectedItems){
            for (Svecenik svecenik:sviSvecenici) {
                if(o.equals(svecenik.getIme() + " " +svecenik.getPrezime())){
                    ovajSvecenik = svecenik;}}}
        String ime,prezime,sifra,titula;
        if(imeSvecenika.isEmpty()){
            ime = ovajSvecenik.getIme();
        }else{
            ime = imeSvecenika;}
        if(prezimeSvecenika.isEmpty()){
            prezime = ovajSvecenik.getPrezime();
        }else{prezime = prezimeSvecenika;}
        if(sifraSvecenika.isEmpty()){
            sifra = ovajSvecenik.getSifra();
        }else{sifra = sifraSvecenika;}
        if(titulaSvecenika.isEmpty()){
            titula = ovajSvecenik.getTitula();
        }else{titula = titulaSvecenika;}

        Svecenik noviSvecenik = new Svecenik(ovajSvecenik.getId(),ime, prezime,sifra,titula,ovajSvecenik.getDatumRodjenja());

        BazaPodataka.azurirajSvecenika(noviSvecenik);
        initialize();
    }
    public void initialize(){
        List<Svecenik> listaSvecenika= BazaPodataka.dohvatiSveSvecenike();
        List<String> svecenikList = listaSvecenika.stream().map(p -> p.getIme() + " " + p.getPrezime()).toList();
        odabirSvecenikaListView.setItems(FXCollections.observableList(svecenikList));

    }
    private void sadrziBrojeve(String text) throws TekstualniZapisException {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new TekstualniZapisException("Tekst ne smije sadržavati brojeve.");
            }
        }
    }
    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
