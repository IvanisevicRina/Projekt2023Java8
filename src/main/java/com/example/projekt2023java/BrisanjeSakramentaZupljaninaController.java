package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.OsobniSakrament;
import entitet.Sakrament;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.util.*;

public class BrisanjeSakramentaZupljaninaController {



    @FXML
    private ComboBox<String> odabirSakramentaComboBox;

    @FXML
    private ComboBox<String> odabirZupljaninaComboBox;

    public void odspojiZupljaninaOdSakramenta() throws Exception {
        StringBuilder errorMessages = new StringBuilder();



        if(odabirSakramentaComboBox.getValue() == null){
            errorMessages.append("Morate odabrati sakrament!\n");
        }
        if(odabirZupljaninaComboBox.getValue()== null){
            errorMessages.append("Morate odabrati zupljanina!\n");
        }
        if(errorMessages.isEmpty()) {
        List<Sakrament> sviSakramenti=BazaPodataka.dohvatiSveSakramente();
        Sakrament ovajSakrament=sviSakramenti.get(0);

        for (Sakrament sakrament: sviSakramenti) {
            if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())){
                ovajSakrament= sakrament;
            }
        }
        List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

        Zupljanin ovajZupljanin=sviZupljani.get(0);

        for (Zupljanin zupljanin:sviZupljani) {
            if(Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))){
                ovajZupljanin = zupljanin;
            }
        }
        BazaPodataka.odspojiZupljaninaOdSakramenta(ovajSakrament.getId().intValue(), ovajZupljanin.getId().intValue());

        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje osobnog sakramenta NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi osobni sakrament");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }



    }

    public void initialize(){

        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        List<String> sakramentiList = sakramenti.stream().map(p->p.getNaziv()).toList();

        odabirSakramentaComboBox.setItems(FXCollections.observableList(sakramentiList));

        List<Zupljanin> zupljaniNaSakramentu= new ArrayList<>();
        for (Sakrament sakrament:sakramenti) {
            if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())){
                zupljaniNaSakramentu=BazaPodataka.dohvatiZupljaneSakramenta(sakrament.getId()).stream().toList();
            }
        }

        List<String> zupljaninList = zupljaniNaSakramentu.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));

    }

}
