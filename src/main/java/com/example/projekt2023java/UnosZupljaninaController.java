package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Sakrament;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class UnosZupljaninaController {



    @FXML
    private TextField prezimeZupljaninaTextField;
    @FXML
    private TextField imeZupljaninaTextField;


    @FXML
    private TextField sifraZupljaninaTextField;


    @FXML
    private DatePicker datumRodjenjaDatePicker;
    @FXML
    private ListView<String> odabirSakramentaListView;

    public static void writeResult(String writeFileName, String text)
    {
        File f = new File(writeFileName);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(f, true));
            bw.append( "\n" +text);
            bw.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void spremiZupljanina() throws Exception {



        StringBuilder errorMessages = new StringBuilder();





        String imeZupljanina = imeZupljaninaTextField.getText();
        if(imeZupljanina.isEmpty()){
            errorMessages.append("Ime ne bi smjelo bit prazno!\n");
        }

        String prezimeZupljanina = prezimeZupljaninaTextField.getText();

        if(prezimeZupljanina.isEmpty()){
            errorMessages.append("Prezime ne bi smjelo bit prazno!\n");
        }


        String sifraZupljanina = sifraZupljaninaTextField.getText();

        if(sifraZupljanina.isEmpty()){
            errorMessages.append("Polje sifrane bi smjelo bit prazno!\n");
        }


        if(datumRodjenjaDatePicker.getValue() == null){
            errorMessages.append("Datum rodjenja ne bi smjelo bit prazno!\n");
        }

        if(errorMessages.isEmpty()) {


            String datumRodjenjaStudentaString = datumRodjenjaDatePicker.getValue().format(DateTimeFormatter.ofPattern("dd.MM.yyyy."));

            DateTimeFormatter formatterDatumaIspita = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

            LocalDate datumRodjenjaStudenta = LocalDate.parse(datumRodjenjaStudentaString, formatterDatumaIspita);
            List<Zupljanin> zupljani = BazaPodataka.dohvatiSveZupljane();

            OptionalLong maksimalniId = zupljani.stream()
                    .mapToLong(students -> students.getId()).max();


            if(maksimalniId.isEmpty()){
                maksimalniId = OptionalLong.of(0L);
            }


            Zupljanin noviZupljanin = new Zupljanin( maksimalniId.getAsLong()+1,imeZupljanina,prezimeZupljanina,sifraZupljanina,datumRodjenjaStudenta);

            zupljani.add(noviZupljanin);

            try {
                BazaPodataka.spremiZupljanina(noviZupljanin);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            List<Sakrament> sviSakramenti = BazaPodataka.dohvatiSveSakramente();
            List<Sakrament> oviSakramenti = new ArrayList<>();
            ObservableList<String> selectedItems = odabirSakramentaListView.getSelectionModel().getSelectedItems();

            for(Object o : selectedItems){
                for (Sakrament sakrament:sviSakramenti) {
                    if(o.equals(sakrament.getNaziv())){
                        BazaPodataka.spojiZupljaninaNaSakrament(sakrament.getId().intValue(),noviZupljanin.getId().intValue());
                    }
                }
            }





            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje zupljanina");
            alert.setHeaderText("Uspješno dodan novi zupljanin");
            alert.setContentText("Zupljanin " + prezimeZupljanina + " " + imeZupljanina + " je uspješno dodan u aplikaciju!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje zupljanina NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi zupljanin");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }


    }
    public void initialize(){
        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        System.out.println("sakramentii");
        sakramenti.stream().map(p->p.getNaziv()).forEach(System.out::println);
        List<String> predmetList = sakramenti.stream().map(s -> s.getNaziv()).toList();
        odabirSakramentaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirSakramentaListView.setItems(FXCollections.observableList(predmetList));

    }



}
