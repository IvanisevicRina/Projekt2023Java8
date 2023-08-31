package com.example.projekt2023java;
import entitet.Promjene;
import entitet.PromjeneManager;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrikazPromjenaController   {

    @FXML
    private TableView<Promjene<?, ?>> promjeneTableView;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> staraVrijednostColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> novaVrijednostColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> objektPromjeneColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> opisColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> rolaColumn;

    @FXML
    private TableColumn<Promjene<?, ?>, ?> datumIVrijemeColumn;
    private final PromjeneManager promjeneManager = new PromjeneManager();


    public void initialize() {

        staraVrijednostColumn.setCellValueFactory(new PropertyValueFactory<>("staraVrijednost"));
        novaVrijednostColumn.setCellValueFactory(new PropertyValueFactory<>("novaVrijednost"));
        objektPromjeneColumn.setCellValueFactory(new PropertyValueFactory<>("objektPromjene"));
        opisColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
        rolaColumn.setCellValueFactory(new PropertyValueFactory<>("rola"));


        datumIVrijemeColumn.setCellValueFactory(new PropertyValueFactory<>("datumIVrijeme"));

        List<Promjene<?, ?>> promjene = promjeneManager.dohvatiPromjene();
        promjeneTableView.getItems().addAll(promjene);

    }


}

