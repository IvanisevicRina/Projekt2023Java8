package com.example.projekt2023java;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ZupneObavijestiController {
    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    @FXML
    private Node izbornikZupljaninaInclude;

    @FXML
    private Node izbornikInclude;

    private static final int NUMBER_OF_LABELS = 3;


    public void initialize() {
        boolean useIzbornikZupljanina = !getUserRole().equals("Svecenik");
        System.out.println("rola: " + getUserRole());
        izbornikInclude.setVisible(!useIzbornikZupljanina);
        izbornikZupljaninaInclude.setVisible(useIzbornikZupljanina);

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("dat/obavijesti.txt"))) {
            List<String> lines = bufferedReader.lines().collect(Collectors.toList());


            for (int i = 0; i < lines.size()/NUMBER_OF_LABELS; i++) {
                String linija1 = lines.get(i*NUMBER_OF_LABELS);
                label1.setText(linija1);
                String linija2 = lines.get(i*NUMBER_OF_LABELS+1);
                label2.setText(linija2);
                String linija3 = lines.get(i*NUMBER_OF_LABELS+2);
                label3.setText(linija3);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String getUserRole() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dat/rola.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    return parts[1]; // Return the role part
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
