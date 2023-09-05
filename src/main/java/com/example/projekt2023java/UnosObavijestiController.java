package com.example.projekt2023java;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UnosObavijestiController {

    @FXML
    private TextField radnimDanTextField;

    @FXML
    private TextField subotaTextField;

    @FXML
    private TextField nedjeljaTextField;

    public void spremiObavijesti() {
        String radnimDanObavijest = radnimDanTextField.getText();
        String subotaObavijest = subotaTextField.getText();
        String nedjeljaObavijest = nedjeljaTextField.getText();


        List<String> existingContent = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/obavijesti.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                existingContent.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Boolean promjena = false;

        if (!radnimDanObavijest.isEmpty()) {
            existingContent.set(0, radnimDanObavijest);
            promjena=true;
        }
        if (!subotaObavijest.isEmpty()) {
            existingContent.set(1, subotaObavijest);
            promjena=true;
        }
        if (!nedjeljaObavijest.isEmpty()) {
            existingContent.set(2, nedjeljaObavijest);
            promjena=true;
        }


        try (FileWriter writer = new FileWriter("dat/obavijesti.txt")) {
            for (String line : existingContent) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(promjena) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Spremljeno");
            alert.setHeaderText("Uspješno spremljeno");
            alert.setContentText("Dodana nova vremena za mise!");
            alert.showAndWait();
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Nema promjene");
            alert.setHeaderText("Niste unjeli ništa");
            alert.setContentText("Morate unjeti nešto ako želite promjenu");
            alert.showAndWait();
        }

    }

}