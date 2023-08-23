package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.LiturgijskoRazdoblje;
import entitet.Sakrament;
import entitet.Zupljanin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.*;

public class UnosSakramentaController {



    @FXML
    private ComboBox<String> odabirSakramentaComboBox;

    @FXML
    private TextField liturgijaTextField;


    @FXML
    private ListView<String> odabirZupljanaListView ;



    public static LiturgijskoRazdoblje liturgija(int broj_liturgije) {

        return switch (broj_liturgije) {
            case 1 -> LiturgijskoRazdoblje.DOSASCE;
            case 2 -> LiturgijskoRazdoblje.KORIZMA;
            default -> LiturgijskoRazdoblje.OSTATAK;
        };

    }





    public void unesiSakrament() throws Exception {

        StringBuilder errorMessages = new StringBuilder();

        if(odabirSakramentaComboBox.getValue() == null){
            errorMessages.append("Morate odabrati sakrament!\n");
        }

        String liturgijaSakramentaString = liturgijaTextField.getText();
        if (liturgijaSakramentaString.isEmpty()) {
            errorMessages.append("Polje liturgije ne bi smjelo bit prazno!\n");
        }


        if (errorMessages.isEmpty()) {

            LiturgijskoRazdoblje liturgijskoRazdoblje = liturgija(Integer.parseInt(liturgijaSakramentaString));

            List<Sakrament> sviSakramenti=BazaPodataka.dohvatiSveSakramente();
            Sakrament ovajSakrament=sviSakramenti.get(0);

            for (Sakrament sakrament: sviSakramenti) {
                if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())){
                    ovajSakrament= sakrament;
                }
            }


            Set<Zupljanin> oviZupljani = new HashSet<>();

            List<Zupljanin> sviZupljani = BazaPodataka.dohvatiSveZupljane();

            ObservableList<String> selectedItems = odabirZupljanaListView.getSelectionModel().getSelectedItems();
            for (Object o : selectedItems) {
                for (Zupljanin zupljanin : sviZupljani) {
                    System.out.println("o.prezime = " + o);
                    System.out.println("zupljanin= " + zupljanin.getPrezime());
                    if (o.equals(zupljanin.getIme() + " " + zupljanin.getPrezime())) {
                        oviZupljani.add(zupljanin);
                    }
                }
            }
            System.out.println("Ovi zupljani id:");
            oviZupljani.stream().map(s -> s.getId()).forEach(System.out::println);


            for (Zupljanin noviZupljanin:oviZupljani){
                ovajSakrament.getZupljani().add(noviZupljanin);
                BazaPodataka.spojiZupljaninaNaSakrament(ovajSakrament.getId().intValue(),noviZupljanin.getId().intValue());
            }





            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje sakramenta");
            alert.setHeaderText("Uspješno dodan novi sakrament");
            alert.setContentText("Sakrament " + ovajSakrament + " je uspješno dodan u aplikaciju!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje sakramenta NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi sakrament");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }

    }
    public void initialize(){

        List<Sakrament> sakramenti = BazaPodataka.dohvatiSveSakramente();
        List<String> sakramentiList = sakramenti.stream().map(s -> s.getNaziv()).toList();


        odabirSakramentaComboBox.setItems(FXCollections.observableList(sakramentiList));

        List<Zupljanin> sviZupljani= BazaPodataka.dohvatiSveZupljane();

        List<Zupljanin> zupljani = new ArrayList<>();
        Sakrament ovajSakrament = sakramenti.get(0);
        for (Sakrament sakrament:sakramenti) {
            System.out.println("odabirSCombo.getValue() = " +odabirSakramentaComboBox.getValue() + " sakrament.getNaziv() = "+sakrament.getNaziv());
            if(Objects.equals(odabirSakramentaComboBox.getValue(), sakrament.getNaziv())) {
                ovajSakrament=sakrament;


            }

        }
        System.out.println(ovajSakrament.getNaziv() + "------------");
        ovajSakrament.getZupljani().stream().forEach(System.out::println);


        System.out.println("-------------------");
        zupljani.stream().forEach(System.out::println);

        System.out.println("-------------------");

        for (Zupljanin z: sviZupljani) {
            boolean nadjen = false;
            for (Zupljanin n:ovajSakrament.getZupljani()) {
                if(z.getSifra().equals(n.getSifra())){
                    nadjen = true;
                    break;
                }

            }
            if(!nadjen){
                zupljani.add(z);
            }

        }
        System.out.println("Zupljani");
        zupljani.stream().forEach(System.out::println);




        List<String> zupljaniList = zupljani.stream().map(s -> s.getIme() + " " + s.getPrezime()).toList();
        odabirZupljanaListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        odabirZupljanaListView.setItems(FXCollections.observableList(zupljaniList));



    }



}
