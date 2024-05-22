package data;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MedicamentUtil {

    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/gestionpatients?user=root&password=";

    public static ObservableList<Medicament> getMedicamentsList(String patientCin) {
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

    public static boolean supprimerMedicament(int id) {
        String query = "DELETE FROM medicament WHERE id = ?";

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, id);
                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean modifierMedicament(int id, String libelle, String prix) {
        Map<String, String> fieldsToUpdate = new HashMap<>();

        if (libelle != null && !libelle.isEmpty()) {
            fieldsToUpdate.put("libelle", libelle);
        }

        if (prix != null && !prix.isEmpty()) {
            fieldsToUpdate.put("prix", prix);
        }

        if (fieldsToUpdate.isEmpty()) {
            return false; // Nothing to update
        }

        StringBuilder queryBuilder = new StringBuilder("UPDATE medicament SET ");
        fieldsToUpdate.forEach((key, value) -> queryBuilder.append(key).append(" = ?, "));
        queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length()); // Remove the last comma and space
        queryBuilder.append(" WHERE id = ?");

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {

                int index = 1;
                for (String value : fieldsToUpdate.values()) {
                    pstmt.setString(index++, value);
                }
                pstmt.setInt(index, id);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean ajouterMedicament(String patientCin, String libelle, String prix) {
        String query = "INSERT INTO medicament (ref, libelle, prix) VALUES (?, ?, ?)";

        try {
            Class.forName(JDBC_DRIVER);
            try (Connection conn = DriverManager.getConnection(DB_URL);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, patientCin);
                pstmt.setString(2, libelle);
                pstmt.setString(3, prix);

                int affectedRows = pstmt.executeUpdate();
                return affectedRows > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
