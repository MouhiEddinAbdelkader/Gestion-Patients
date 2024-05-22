    package data;

    import javafx.collections.FXCollections;
    import javafx.collections.ObservableList;
    import javafx.scene.paint.Color;

    import java.sql.*;

    public class PatientUtil {
        private static String dernierTitreErreur = "";
        private static String dernierMessageErreur = "";

        private static String JDBC_Driver = "com.mysql.cj.jdbc.Driver";
        private static String DB_Url = "jdbc:mysql://localhost:3306/gestionpatients?user=root&password=";

        public static String getDernierTitreErreur() {
            return dernierTitreErreur;
        }

        public static void setDernierTitreErreur(String dernierTitreErreur) {
            PatientUtil.dernierTitreErreur = dernierTitreErreur;
        }

        public static String getDernierMessageErreur() {
            return dernierMessageErreur;
        }

        public static void setDernierMessageErreur(String dernierMessageErreur) {
            PatientUtil.dernierMessageErreur = dernierMessageErreur;
        }

        public static ObservableList<Patient> getPatients() {
            ObservableList<Patient> liste = FXCollections.observableArrayList();
            String query = "SELECT * FROM patient ORDER BY id";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {

                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String cin = rs.getString("cin");
                        String nom = rs.getString("nom");
                        String prenom = rs.getString("prenom");
                        String sexe = rs.getString("sexe");
                        String tel = rs.getString("tel");

                        Patient patient = new Patient(id, cin, nom, prenom, sexe, tel);
                        liste.add(patient);
                    }
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }

            return liste;
        }

        public static Patient getPatientById(int id) {
            Patient patient = null;
            String query = "SELECT * FROM patient WHERE id = ?";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setInt(1, id);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            int patientId = rs.getInt("id");
                            String cin = rs.getString("cin");
                            String nom = rs.getString("nom");
                            String prenom = rs.getString("prenom");
                            String sexe = rs.getString("sexe");
                            String tel = rs.getString("tel");

                            patient = new Patient(patientId, cin, nom, prenom, sexe, tel);
                        }
                    }
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }

            return patient;
        }


        public static boolean ajouterPatient(String cin, String nom, String prenom, String sexe, String tel) {
            String query = "INSERT INTO patient (cin, nom, prenom, sexe, tel) VALUES (?, ?, ?, ?, ?)";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, cin);
                    pstmt.setString(2, nom);
                    pstmt.setString(3, prenom);
                    pstmt.setString(4, sexe);
                    pstmt.setString(5, tel);

                    int affectedRows = pstmt.executeUpdate();
                    return affectedRows > 0;
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }
            return false;
        }

        public static boolean modifierPatient(int id, String cin, String nom, String prenom, String sexe, String tel) {

            String query = "UPDATE patient SET cin=?, nom=?, prenom=?, sexe=?, tel=? WHERE id=?";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, cin);
                    pstmt.setString(2, nom);
                    pstmt.setString(3, prenom);
                    pstmt.setString(4, sexe);
                    pstmt.setString(5, tel);
                    pstmt.setInt(6, id);

                    int affectedRows = pstmt.executeUpdate();
                    return affectedRows > 0;
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        public static ObservableList<Patient> rechercherPatients(String nomp) {
            ObservableList<Patient> liste = FXCollections.observableArrayList();
            String query = "SELECT * FROM patient WHERE nom LIKE ? ORDER BY id";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setString(1, "%" + nomp + "%");

                    try (ResultSet rs = pstmt.executeQuery()) {
                        while (rs.next()) {
                            int id = rs.getInt("id");
                            String cin = rs.getString("cin");
                            String nom = rs.getString("nom");
                            String prenom = rs.getString("prenom");
                            String sexe = rs.getString("sexe");
                            String tel = rs.getString("tel");

                            Patient patient = new Patient(id, cin, nom, prenom, sexe, tel);
                            liste.add(patient);
                        }
                    }
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }

            return liste;
        }


        public static boolean supprimerToutP() {
            String query = "DELETE FROM patient";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     Statement stmt = conn.createStatement()) {

                    int affectedRows = stmt.executeUpdate(query);
                    return affectedRows > 0;
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

        public static boolean supprimerP(int id) {
            String query = "DELETE FROM patient WHERE id = ?";

            try {
                Class.forName(JDBC_Driver);
                try (Connection conn = DriverManager.getConnection(DB_Url);
                     PreparedStatement pstmt = conn.prepareStatement(query)) {

                    pstmt.setInt(1, id);

                    int affectedRows = pstmt.executeUpdate();
                    return affectedRows > 0;
                }
            } catch (Exception e) {
                setDernierTitreErreur("Erreur de Base de Données");
                setDernierMessageErreur(e.getMessage());
                e.printStackTrace();
            }

            return false;
        }

    }

