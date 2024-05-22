package com.example.ges;

import data.Patient;
import data.PatientUtil;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PatientsController implements Initializable {
    @FXML
    private TableView<Patient> tabPatients;
    @FXML
    private TextField rnomP;
    @FXML
    private TextField tfNomP;
    @FXML
    private TextField tfCinP;
    @FXML
    private TextField tfPrenomP;
    @FXML
    private TextField tfSexeP;
    @FXML
    private TextField tfTelP;
    @FXML
    private Button btnRechercherP;
    @FXML
    private Button btnSupprimerP;
    @FXML
    private Button btnSupprimerToutP;
    @FXML
    private Button btnQuitter;
    @FXML
    private Button btnAjouterP;
    @FXML
    private Button btnAnnulerPM;
    @FXML
    private Button btnModifierPM;

    @FXML
    private  Button gererPdfButton;
    @FXML
    private Button btnAnnulerP;
    @FXML
    private TextField tfNomPM;
    @FXML
    private TextField tfCinPM;
    @FXML
    private TextField tfPrenomPM;
    @FXML
    private TextField tfSexePM;
    @FXML
    private TextField tfTelPM;
    @FXML
    private Button btnAddMedicament;
    @FXML

    private ComboBox cbId;
    @FXML
    private TabPane tpMenu;
    ObservableList<Integer> listId;
    boolean refrechCombo = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ajouterColonnes();
        remplirP();
        ecouteurs();
        cbId.setItems(listId);
    }

    private void ajouterColonnes() {
        tabPatients.getColumns().clear();
        TableColumn<Patient, Integer> idCol = new TableColumn<>("Id");
        TableColumn<Patient, String> cinCol = new TableColumn<>("Cin");
        TableColumn<Patient, String> nomCol = new TableColumn<>("Nom");
        TableColumn<Patient, String> prenomCol = new TableColumn<>("Prenom");
        TableColumn<Patient, String> sexeCol = new TableColumn<>("Sexe");
        TableColumn<Patient, String> telCol = new TableColumn<>("Tel");

        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        cinCol.setCellValueFactory(new PropertyValueFactory<>("cin"));
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        sexeCol.setCellValueFactory(new PropertyValueFactory<>("sexe"));
        telCol.setCellValueFactory(new PropertyValueFactory<>("tel"));

        tabPatients.getColumns().addAll(idCol, cinCol, nomCol, prenomCol, sexeCol, telCol);
    }

    private void ecouteurs() {
        btnQuitter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                quitter();
            }
        });
        btnAjouterP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ajouterPatient();
            }
        });
        btnAnnulerPM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                annuler();
            }
        });



        tabPatients.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() == 2)
                    afficherModifier();
            }
        });
        btnAnnulerPM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                annuler();
            }
        });
        btnModifierPM.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                modifierP();
            }
        });
        btnRechercherP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                rechercherP();
            }
        });
        btnAddMedicament.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                openMedicamentView();
            }
        });
        btnSupprimerP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                supprimerP();
            }
        });
        btnSupprimerToutP.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                supprimerToutP();
            }
        });
        cbId.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (refrechCombo  && actionEvent.getEventType() == ActionEvent.ACTION)
                    actualiserPatient();
            }
        });
        gererPdfButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                gererPdf();
            }
        });
    }
    // Method to open the MedicamentView
    private void openMedicamentView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("medicamentView.fxml"));
            Parent root = loader.load();

            // Get the controller for MedicamentView
            MedicamentController medicamentController = loader.getController();

            // Get the selected patient's CIN
            Patient selectedPatient = tabPatients.getSelectionModel().getSelectedItem();
            if (selectedPatient != null) {
                String selectedPatientCin = selectedPatient.getCin();

                // Pass the CIN to MedicamentController
                medicamentController.setPatientCin(selectedPatientCin);

                Stage stage = new Stage();
                stage.setTitle("Add Medicament");
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                System.out.println("Please select a patient first.");
                // Optionally, show an alert dialog to the user
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("No Patient Selected");
                alert.setHeaderText(null);
                alert.setContentText("Please select a patient first.");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to load Medicament view.");
            // Optionally, show an alert dialog to the user
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Loading Error");
            alert.setContentText("Unable to load Medicament view.");
            alert.showAndWait();
        }
    }

    private void supprimerToutP() {
        Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);
        dialog.setTitle("Supprimer Tout?");
        dialog.setHeaderText("Etes vous sûr de supprimer tous les livres?");
        dialog.showAndWait();
        if (dialog.getResult() == ButtonType.OK) {
            if (PatientUtil.supprimerToutP()) {
                remplirP();
                tfNomP.setText("");
                tfCinP.setText("");
                tfNomP.setText("");
                tfPrenomP.setText("");
                tfTelP.setText("");
            }
            else
                afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
        }
    }

    private void supprimerP() {
        TablePosition position = tabPatients.getSelectionModel().getSelectedCells().get(0);
        if (position.getRow() >= 0) {
            Patient pationSelectionne = tabPatients.getItems().get(position.getRow());
            int id = pationSelectionne.getId();
            if (PatientUtil.supprimerP(id))
                remplirP();
            else
                afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
        }
    }

    private void rechercherP() {
        ObservableList<Patient> list = PatientUtil.rechercherPatients(rnomP.getText());
        tabPatients.setItems(list);
    }

    private void quitter() {
        Platform.exit();
    }

    private void afficherModifier() {
        TablePosition position = tabPatients.getSelectionModel().getSelectedCells().get(0);
        System.out.println("Double Click sur la ligne: " + position.getRow());
        if (position.getRow() >= 0) {
            Patient patientSelectionne = tabPatients.getItems().get(position.getRow());
            cbId.getSelectionModel().select(patientSelectionne.getId());
            tfCinP.setText(patientSelectionne.getCin());
            tfNomP.setText(patientSelectionne.getNom());
            tfPrenomP.setText(patientSelectionne.getPrenom());
            tfSexeP.setText(patientSelectionne.getSexe());
            tfTelP.setText(patientSelectionne.getTel());
            tpMenu.getSelectionModel().select(2);
        }
    }


   // private void actualiserNbPages() { lbNbPages.setText(String.format("%.0f", slNbPages.getValue()));}

   // private void actualiserNbPagesM() {lbNbPagesM.setText(String.format("%.0f", slNbPagesM.getValue()));}

    private void actualiserPatient() {
        Integer id = (Integer) cbId.getValue();
        Patient patient = PatientUtil.getPatientById(id);
        if (patient != null) {
            tfCinPM.setText(patient.getCin());
            tfNomPM.setText(patient.getNom());
            tfPrenomPM.setText(patient.getPrenom());
            tfSexePM.setText(patient.getSexe());
            tfTelPM.setText(patient.getTel());
            tpMenu.getSelectionModel().select(2);
        }
    }

    private void ajouterPatient() {
        if (PatientUtil.ajouterPatient(
                tfCinP.getText(),
                tfNomP.getText(),
                tfPrenomP.getText(),
                tfTelP.getText(),
                tfSexeP.getText()
        )) {
            remplirP();
            tfNomP.setText("");
            tpMenu.getSelectionModel().select(0);
        } else
            afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
    }

    private void remplirP() {
        ObservableList<Patient> listesPatients = PatientUtil.getPatients();
        tabPatients.setItems(listesPatients);
        listId = listesPatients.stream()
                .map(Patient::getId)
                .collect(Collectors.toCollection(FXCollections::observableArrayList));

        refrechCombo = false;
        cbId.setItems(listId);
        refrechCombo = true;
    }

    public static void afficherErreur(String titre, String message) {
        Alert dialog = new Alert(Alert.AlertType.ERROR);
        dialog.setTitle(titre);
        dialog.setHeaderText(message);
        dialog.showAndWait();
    }

    private void annuler() {
        tpMenu.getSelectionModel().select(0);
    }

    @FXML
    private void gererPdf() {
        TablePosition position = tabPatients.getSelectionModel().getSelectedCells().get(0);
        if (position != null) {
            int rowIndex = position.getRow();
            if (rowIndex >= 0) {
                Patient selectedPatient = tabPatients.getItems().get(rowIndex);
                if (selectedPatient != null) {
                    try (PDDocument document = new PDDocument()) {
                        PDPage page = new PDPage();
                        document.addPage(page);

                        try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 16);
                            contentStream.beginText();
                            contentStream.newLineAtOffset(100, 750);
                            contentStream.showText("Patient Information");
                            contentStream.endText();

                            contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                            contentStream.beginText();
                            contentStream.newLineAtOffset(100, 700);
                            contentStream.showText("Nom: " + selectedPatient.getNom());
                            contentStream.newLineAtOffset(0, -20);
                            contentStream.showText("Prenom: " + selectedPatient.getPrenom());
                            contentStream.newLineAtOffset(0, -20);
                            contentStream.showText("CIN: " + selectedPatient.getCin());
                            contentStream.newLineAtOffset(0, -20);
                            contentStream.showText("Telephone: " + selectedPatient.getTel());
                            contentStream.endText();
                        }

                        // Define the file path and save the document
                        String filePath = "C:\\PdfPatient\\" + selectedPatient.getNom() + "_" + selectedPatient.getPrenom() + ".pdf";
                        document.save(filePath);

                        // Optionally, show a success message to the user
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("PDF Generated");
                        alert.setHeaderText(null);
                        alert.setContentText("PDF has been generated and saved to: " + filePath);
                        alert.showAndWait();
                    } catch (IOException e) {
                        e.printStackTrace();
                        // Optionally, show an error message to the user
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("PDF Generation Error");
                        alert.setContentText("An error occurred while generating the PDF.");
                        alert.showAndWait();
                    }
                }
            }
        } else {
            // Optionally, show an alert dialog to the user if no patient is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Patient Selected");
            alert.setHeaderText(null);
            alert.setContentText("Please select a patient first.");
            alert.showAndWait();
        }
    }

    private void modifierP() {
        int id = Integer.parseInt(cbId.getValue().toString());
        if (PatientUtil.modifierPatient(id ,
                tfNomPM.getText().toString(),
                tfCinPM.getText().toString(),
                tfPrenomPM.getText().toString(),
                tfSexePM.getText().toString(),
                tfTelPM.getText().toString() ))
        {
            remplirP();
            tpMenu.getSelectionModel().select(0);
        } else
            afficherErreur(PatientUtil.getDernierTitreErreur(), PatientUtil.getDernierMessageErreur());
    }
}