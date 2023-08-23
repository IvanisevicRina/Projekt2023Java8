package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.OptionalLong;

public class UnosOsobnogSakramentaController {


    @FXML
    private TextField vrijemeOdrzavanjaTextField;

    @FXML
    private TextField crkvaOsobnogSakramentaTextField;
    @FXML
    private DatePicker datumOdrzavanjaDatePicker;


    @FXML
    private ComboBox<String> odabirSakramentaComboBox;

    @FXML
    private ComboBox<String> odabirZupljaninaComboBox;



    public void unesiOsobnogSakramenta() throws Exception {
        StringBuilder errorMessages = new StringBuilder();



        if(odabirSakramentaComboBox.getValue() == null){
            errorMessages.append("Morate odabrati sakrament!\n");
        }
        if(odabirZupljaninaComboBox.getValue()== null){
            errorMessages.append("Morate odabrati zupljanina!\n");
        }


        String vrijemeOdrzavanja= vrijemeOdrzavanjaTextField.getText();

        if(vrijemeOdrzavanja.isEmpty()){
            errorMessages.append("Vrijeme ne bi smjelo bit prazno!\n");
        }

        if(datumOdrzavanjaDatePicker.getValue() == null){
            errorMessages.append("Datum ne bi smjelo bit prazno!\n");
        }

        String crkvaOsobnogSakramenta = crkvaOsobnogSakramentaTextField.getText();

        if(crkvaOsobnogSakramenta.isEmpty()){
            errorMessages.append("Crkva ne bi smjelo bit prazno!\n");
        }



        if(errorMessages.isEmpty()) {

            String datumOdrzavanjaString = datumOdrzavanjaDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));

            String datumIVrijemeOsobnogSakramenta = datumOdrzavanjaString + vrijemeOdrzavanja;


            List<OsobniSakrament> osobniSakramenti = BazaPodataka.dohvatiSveOsobneSakramente();
            OptionalLong maksimalniId = OptionalLong.of(0L);
            maksimalniId=
                    osobniSakramenti.stream()
                            .mapToLong(s -> s.getId()).max();

            long id;
            if(maksimalniId.isEmpty()){
                id = 1L;
            }else {
                id = maksimalniId.getAsLong()+1;
            }

            List<Sakrament> sviSakramenti=BazaPodataka.dohvatiSveSakramente();
            Sakrament ovajSakrament=sviSakramenti.get(0);

            for (Sakrament sakrament: sviSakramenti) {
                if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())){
                    ovajSakrament= sakrament;
                }
            }
            List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

            Zupljanin ovajStudent=sviZupljani.get(0);

            for (Zupljanin zupljanin:sviZupljani) {
                if(Objects.equals(odabirZupljaninaComboBox.getValue(), (zupljanin.getIme() + " " + zupljanin.getPrezime()))){
                    ovajStudent = zupljanin;
                }
            }

            DateTimeFormatter formatterDatumaIspita = DateTimeFormatter.ofPattern("dd.MM.yyyy.HH:mm");

            LocalDateTime datumIVrijeme = LocalDateTime.parse(datumIVrijemeOsobnogSakramenta, formatterDatumaIspita);

            OsobniSakrament noviOsobniSakrament= new OsobniSakrament(id,ovajSakrament,ovajStudent,datumIVrijeme,new Crkva(crkvaOsobnogSakramenta));

            try {
                BazaPodataka.spremiOsobniSakrament(noviOsobniSakrament);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje osobnog sakramenta");
            alert.setHeaderText("Uspješno dodan novi osobni sakrament");
            alert.setContentText("Osobni sakrament je uspješno dodan u aplikaciju!");
            alert.showAndWait();
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
                Sakrament ovajSakrament= sakrament;

                System.out.println("Zupljani sa sakramentom: "+ovajSakrament.getNaziv());
                for (Zupljanin z:ovajSakrament.getZupljani()) {
                    zupljaniNaSakramentu.add(z);
                    System.out.println(z.getPrezime());
                }

            }
        }

        List<String> zupljaninList = zupljaniNaSakramentu.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljaninaComboBox.setItems(FXCollections.observableList(zupljaninList));



    }





}
