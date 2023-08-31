package com.example.projekt2023java;
import entitet.Promjene;
import entitet.PromjeneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
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
    private PromjeneManager promjeneManager = new PromjeneManager();

    public void setPromjeneManager(PromjeneManager promjeneManager) {
        this.promjeneManager = promjeneManager;
    }

    public void initialize() throws InterruptedException {



        try{
            List<Promjene<?,?>> promjeneList = promjeneManager.dohvatiSvePromjene();
            if (promjeneList.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Recent Changes");
                alert.setHeaderText("Warning");
                alert.setContentText("There are no recent changes available.");
                alert.showAndWait();
            }
            staraVrijednostColumn.setCellValueFactory(new PropertyValueFactory<>("staraVrijednost"));
            novaVrijednostColumn.setCellValueFactory(new PropertyValueFactory<>("novaVrijednost"));
            objektPromjeneColumn.setCellValueFactory(new PropertyValueFactory<>("objektPromjene"));
            opisColumn.setCellValueFactory(new PropertyValueFactory<>("opis"));
            rolaColumn.setCellValueFactory(new PropertyValueFactory<>("rola"));
            datumIVrijemeColumn.setCellValueFactory(new PropertyValueFactory<>("datumIVrijeme"));
            promjeneTableView.getItems().setAll(promjeneList);
            Thread refreshThread = new Thread(() -> {
                try {
                    Thread.sleep(50000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Platform.runLater(() -> {
                    try {
                        List<Promjene<?,?>> novePromjeneList = promjeneManager.dohvatiSvePromjene();
                        promjeneTableView.getItems().setAll(novePromjeneList);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            });
            refreshThread.setDaemon(true);//Set the thread as daemon to allow it to exit when the application exits
            refreshThread.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }



/*                  SEMAFOR
        try {
            SemaphoreManager.acquire();

            // Update the display of promjene
            promjeneTableView.getItems().addAll(promjene);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            SemaphoreManager.release();
        }
 */



    }




}

