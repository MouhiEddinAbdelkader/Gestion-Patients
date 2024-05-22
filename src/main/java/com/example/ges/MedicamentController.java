package com.example.ges;

import data.Medicament;
import data.MedicamentUtil;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class MedicamentController implements Initializable {

    private String patientCin;



    @FXML
    private TextField tfLibelle;

    @FXML
    private TextField tfPrix;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Medicament> TableView;

    @FXML
    private TableColumn<Medicament, Integer> idColumn;

    @FXML
    private TableColumn<Medicament, String> refColumn;

    @FXML
    private TableColumn<Medicament, String> libelleColumn;

    @FXML
    private TableColumn<Medicament, Integer> prixColumn;

    public void setPatientCin(String cin) {
        this.patientCin = cin;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showMedicaments();
        ecouteurs();
    }

    public void showMedicaments() {
        try {
            ObservableList<Medicament> list = MedicamentUtil.getMedicamentsList(patientCin);

            idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
            refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
            libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
            prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));

            TableView.setItems(list);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ecouteurs() {
        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ajouterMedicament();
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteMedicament();
            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modifierMedicament();
            }
        });
    }

    @FXML
    private void ajouterMedicament() {
        if (MedicamentUtil.ajouterMedicament(
                patientCin,
                tfLibelle.getText(),
                tfPrix.getText())) {
            showMedicaments();
        }
    }

    @FXML
    private void deleteMedicament() {
        Medicament selectedMedicament = TableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            if (MedicamentUtil.supprimerMedicament(selectedMedicament.getId())) {
                showMedicaments();
            }
        }
    }

    @FXML
    private void modifierMedicament() {
        Medicament selectedMedicament = TableView.getSelectionModel().getSelectedItem();
        if (selectedMedicament != null) {
            if (MedicamentUtil.modifierMedicament(
                    selectedMedicament.getId(),
                    tfLibelle.getText(),
                    tfPrix.getText())) {
                showMedicaments();
            }
        }
    }
}
