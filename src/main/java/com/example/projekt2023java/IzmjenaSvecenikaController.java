package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import entitet.SvecenikBuilder;
import iznimke.DuplikatSifreException;
import iznimke.TekstualniZapisException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 *  ekran izmjene svećenika.
 */
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
    private static final Logger logger = LoggerFactory.getLogger(IzmjenaSvecenikaController.class);
    /**
     * Metoda za ažuriranje podataka o svećeniku.
     *
     * @throws Exception Ako dođe do greške pri pristupu bazi podataka ili pri unosu podataka.
     */
    public void azurirajSvecenike() throws Exception {

        String imeSvecenika = imeSvecenikaTextField.getText();
        String prezimeSvecenika = prezimeSvecenikaTextField.getText();

        String sifraSvecenika= sifraSvecenikaTextField.getText();
        String titulaSvecenika = titulaSvecenikaTextField.getText();
        List<Svecenik> sviSvecenici = BazaPodataka.dohvatiSveSvecenike();
        Svecenik ovajSvecenik = new SvecenikBuilder().createSvecenik();
        ObservableList<String> selectedItems = odabirSvecenikaListView.getSelectionModel().getSelectedItems();
        Boolean errorOccured = false;
        if (selectedItems.isEmpty()) {
            displayAlert("Greška", "Morate odabrati svećenika za ažuriranje.");
            return;
        }

        try {
            sadrziBrojeve(imeSvecenika);
            sadrziBrojeve(prezimeSvecenika);

        } catch (TekstualniZapisException e) {
            imeSvecenika = "";
            prezimeSvecenika = "";
            logger.error("Sadrži nedozvoljene znakove(brojeve)", e);
            displayAlert("Greška", e.getMessage());
        }
        for(Object o : selectedItems){
            for (Svecenik svecenik:sviSvecenici) {
                if(o.equals(svecenik.getSifra() + "-----"+svecenik.getIme() + " " +svecenik.getPrezime())){
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
        }else {
            try {
                ovajSvecenik.provjeraSifre(sifraSvecenika);
                sifra = sifraSvecenika;
            } catch (DuplikatSifreException e) {
                sifra = "";
                logger.error("Krivi unos! Dupla sifra, vec je registrirana u bazi!", e);
                displayAlert("Greška", e.getMessage());
                errorOccured = true;
            }
        }
        if(titulaSvecenika.isEmpty()){
            titula = ovajSvecenik.getTitula();
        }else{titula = titulaSvecenika;}
        if(!errorOccured) {
            Svecenik noviSvecenik = new Svecenik(ovajSvecenik.getId(), ime, prezime, sifra, titula, ovajSvecenik.getDatumRodjenja());

            BazaPodataka.azurirajSvecenika(noviSvecenik);
        }
        initialize();
    }
    /**
     * Metoda za inicijalizaciju ekrana.
     */
    public void initialize(){
        List<Svecenik> listaSvecenika= BazaPodataka.dohvatiSveSvecenike();
        List<String> svecenikList = listaSvecenika.stream().map(p -> p.getSifra() + "-----"+p.getIme() + " " + p.getPrezime()).toList();
        odabirSvecenikaListView.setItems(FXCollections.observableList(svecenikList));

    }
    /**
     * Metoda za provjeru da li tekst sadrži brojeve.
     *
     * @param text Tekst koji se provjerava.
     * @throws TekstualniZapisException Ako tekst sadrži brojeve.
     */
    private void sadrziBrojeve(String text) throws TekstualniZapisException {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                throw new TekstualniZapisException("Tekst ne smije sadržavati brojeve.");
            }
        }
    }
    /**
     * Metoda za prikazivanje upozorenja (Alert) korisniku.
     *
     * @param title   Naslov upozorenja.
     * @param message Poruka upozorenja.
     */
    private void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




}
