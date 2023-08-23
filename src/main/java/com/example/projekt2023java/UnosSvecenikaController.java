package com.example.projekt2023java;

import baza.BazaPodataka;
import entitet.Svecenik;
import entitet.SvecenikBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalLong;

public class UnosSvecenikaController implements Serializable {


    @FXML
    private TextField imeSvecenikaTextField;
    @FXML
    private TextField sifraSvecenikaTextField;
    @FXML
    private TextField prezimeSvecenikaTextField;
    @FXML
    private TextField titulaSvecenikaTextField;


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

    public void spremiSvecenika() throws Exception {

        StringBuilder errorMessages = new StringBuilder();

        String imeSvecenika = imeSvecenikaTextField.getText();

        if(imeSvecenika.isEmpty()){
            errorMessages.append("Ime ne bi smjelo bit prazno!\n");
        }

        String prezimeSvecenika = prezimeSvecenikaTextField.getText();

        if(prezimeSvecenika.isEmpty()){
            errorMessages.append("Prezime ne bi smjelo bit prazno!\n");
        }


        String sifraSvecenika= sifraSvecenikaTextField.getText();

        if(sifraSvecenika.isEmpty()){
            errorMessages.append("Polje sifra ne bi smjelo bit prazno!\n");
        }

        String titulaSvecenika = titulaSvecenikaTextField.getText();


        if(titulaSvecenika.isEmpty()){
            errorMessages.append("Polje titula ne bi smjelo bit prazno!\n");
        }




/*
        writeResult("dat/profesori.txt",idProfesora);
        writeResult("dat/profesori.txt",sifraProfesora);
        writeResult("dat/profesori.txt",imeProfesora);
        writeResult("dat/profesori.txt",prezimeProfesora);
        writeResult("dat/profesori.txt",titulaProfesora);
*/


        if(errorMessages.isEmpty()) {



            //citam iz datoteke sto je bilo prijee
            List<Svecenik> svecenik = new ArrayList<>();
            try (ObjectInputStream objectReader = new ObjectInputStream(new FileInputStream("dat/sveceniciSerijalizirani.ser"))) {
                svecenik = (List<Svecenik>) objectReader.readObject();
            } catch (IOException | ClassNotFoundException e) {
                svecenik = new ArrayList<>();
            }

            OptionalLong maksimalniId =svecenik.stream()
                    .mapToLong(sveceniks -> sveceniks.getId()).max();
            long id;
            if(maksimalniId.isEmpty()){
                id = 1L;
            }else {
                id = maksimalniId.getAsLong()+1;
            }

            Svecenik noviSvecenik= new SvecenikBuilder().setId(id).setSifra(sifraSvecenika).setIme(imeSvecenika).setPrezime(prezimeSvecenika).setTitula(titulaSvecenika).createSvecenik();

                try {
                    BazaPodataka.spremiSvecenika(noviSvecenik);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            //Serijaliziram(spremam) listu sa novim profesorima u file
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("dat/sveceniciSerijalizirani.ser"))) {
                out.writeObject(svecenik);
            } catch (IOException ex) {
                System.err.println(ex);
            }


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremanje svecenika");
            alert.setHeaderText("Uspješno dodan novi svecenik");
            alert.setContentText("Svecenik " + prezimeSvecenika + " " + imeSvecenika+ " je uspješno dodan u aplikaciju!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Spremanje svecenika NEUSPJELO");
            alert.setHeaderText("Neuspješno dodan novi svecenik");
            alert.setContentText(errorMessages.toString());
            alert.showAndWait();
        }

    }




}
