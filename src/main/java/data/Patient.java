package data;

public class Patient {
    private int id;
    private String cin;
    private String nom;
    private String prenom;
    private String sexe;
    private String tel;

    // Constructeur
    public Patient(int id, String cin, String nom, String prenom, String sexe, String tel) {
        this.id = id;
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.tel = tel;
    }



    // Getter pour id
    public int getId() {
        return id;
    }

    // Getter pour cin
    public String getCin() {
        return cin;
    }

    // Getter pour nom
    public String getNom() {
        return nom;
    }

    // Getter pour prenom
    public String getPrenom() {
        return prenom;
    }

    // Getter pour sexe
    public String getSexe() {
        return sexe;
    }

    // Getter pour tel
    public String getTel() {
        return tel;
    }



    // Méthode toString
    @Override
    public String toString() {
        return "Personne{" +
                "id=" + id +
                ", cin=" + cin +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", sexe='" + sexe + '\'' +
                ", tel=" + tel +
                '}';
    }
}
