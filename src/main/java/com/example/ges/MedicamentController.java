package com.example.ges;

import data.Medicament;
import data.MedicamentUtil;
import javafx.collections.FXCollections;
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
import java.sql.*;
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

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionpatients?user=root&password=";

    public void setPatientCin(String cin) {
        this.patientCin = cin;
        showMedicaments();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        configureTableColumns();
        ecouteurs();
        clearFields();
        setupTableViewSelectionListener();
    }

    private void configureTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        refColumn.setCellValueFactory(new PropertyValueFactory<>("ref"));
        libelleColumn.setCellValueFactory(new PropertyValueFactory<>("libelle"));
        prixColumn.setCellValueFactory(new PropertyValueFactory<>("prix"));
    }

    @FXML
    public ObservableList<Medicament> getMedicamentsList(String patientCin) {
        ObservableList<Medicament> liste = FXCollections.observableArrayList();
        String query = "SELECT * FROM medicament WHERE ref = ? ORDER BY id";
        try {
            Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, patientCin);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String ref = rs.getString("ref");
                        String libelle = rs.getString("libelle");
                        String prix = rs.getString("prix");

                        Medicament medicament = new Medicament(id, ref, libelle, prix);
                        liste.add(medicament);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return liste;
    }

    public void showMedicaments() {
        if (patientCin != null) {
            ObservableList<Medicament> list = getMedicamentsList(patientCin);
            TableView.setItems(list);
        }
    }

    public ObservableList<Medicament> getListMedicament() throws SQLException {
        ObservableList<Medicament> booksList = FXCollections.observableArrayList();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM medicament")) {

            while (rs.next()) {
                Medicament medicaments = new Medicament(rs.getInt("Id"), rs.getString("ref"), rs.getString("libelle"), rs.getString("prix"));
                booksList.add(medicaments);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return booksList;
    }

    private void ecouteurs() {
        insertButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ajouterMedicament();
                clearFields();
                showMedicaments();
            }
        });

        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                deleteMedicament();
                clearFields();
                showMedicaments();
            }
        });

        updateButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modifierMedicament();
                clearFields();
                showMedicaments();
            }
        });
    }

    @FXML
    private void ajouterMedicament() {
        if (MedicamentUtil.ajouterMedicament(patientCin, tfLibelle.getText(), tfPrix.getText())) {
            showMedicaments();
        }
    }

    private void clearFields() {
        tfLibelle.clear();
        tfPrix.clear();
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
            if (MedicamentUtil.modifierMedicament(selectedMedicament.getId(), tfLibelle.getText(), tfPrix.getText())) {
                showMedicaments();
            }
        }
    }

    private void setupTableViewSelectionListener() {
        TableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                populateFieldsWithSelectedMedicament(newValue);
            }
        });
    }

    private void populateFieldsWithSelectedMedicament(Medicament medicament) {
        tfLibelle.setText(medicament.getLibelle());
        tfPrix.setText(medicament.getPrix());
    }
}
