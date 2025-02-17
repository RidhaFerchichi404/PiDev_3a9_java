package entities;

import java.util.Objects;

public class Abonnement {
    private int id;
    private String nom;
    private String descriptiona;
    private int duree; // Durée en jours
    private double prix;
    private String salleNom;
    private int salleDeSportId;
    public Abonnement(String nom, String descriptiona, int duree, double prix, String salleNom) {
        this.nom = nom;
        this.descriptiona = descriptiona;
        this.duree = duree;
        this.prix = prix;
        this.salleNom = salleNom;


    }


    public String getDescriptiona() {
        return descriptiona;
    }

    public int getSalleDeSportId() {
        return salleDeSportId;
    }

    public void setSalleDeSportId(int salleDeSportId) {
        this.salleDeSportId = salleDeSportId;
    }

    public String getSalleNom() {
        return salleNom;
    }

    public void setDescriptiona(String descriptiona) {
        this.descriptiona = descriptiona;
    }

    public void setSalleNom(String salleNom) {
        this.salleNom = salleNom;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setdescriptiona(String descriptiona) {
        this.descriptiona = descriptiona;
    }

    public void setDuree(int duree) {
        this.duree = duree;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getdescriptiona() {
        return descriptiona;
    }

    public int getDuree() {
        return duree;
    }

    public double getPrix() {
        return prix;
    }

   /* public int getSalleDeSportId() {
        return salleDeSportId;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Abonnement that = (Abonnement) o;
        return id == that.id && duree == that.duree && Double.compare(prix, that.prix) == 0 && salleDeSportId == that.salleDeSportId && Objects.equals(nom, that.nom) && Objects.equals(descriptiona, that.descriptiona) && Objects.equals(salleNom, that.salleNom);
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", descriptiona='" + descriptiona + '\'' +
                ", duree=" + duree +
                ", prix=" + prix +
                ", salleNom='" + salleNom + '\'' +
                ", salleDeSportId=" + salleDeSportId +
                '}';
    }

}
