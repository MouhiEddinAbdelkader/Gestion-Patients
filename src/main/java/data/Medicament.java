package data;

public class Medicament {

    private int id;
    private String ref;
    private String libelle;
    private String prix;

    // Constructor
    public Medicament(int id, String ref, String libelle, String prix) {
        this.id = id;
        this.ref = ref;
        this.libelle = libelle;
        this.prix = prix;
       // this.patient = patient;
    }

    // Getters
    public int getId() {
        return id;
    }
   // public int getPatient() {return patient      ;}


    public String getRef() {
        return ref;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getPrix() {
        return prix;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }
    // public void setPatient(int id) {this.patient = patient;}

    public void setRef(String ref) {
        this.ref = ref;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setPrix(String prix) {
        this.prix = prix;
    }

    // toString method
    @Override
    public String toString() {
        return "Medicament{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                ", libelle='" + libelle + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }
}
