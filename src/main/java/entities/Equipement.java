package entities;

import java.util.Date;

public class Equipement {

    private int id;
    private int idSalle;
    private String nom;
    private boolean fonctionnement;
    private Date prochaineVerification;
    private Date derniereVerification;
    private long idUser; // Utilisateur qui gère l’équipement

    public Equipement(int idSalle, String nom, boolean fonctionnement, Date prochaineVerification, Date derniereVerification, long idUser) {
        this.idSalle = idSalle;
        this.nom = nom;
        this.fonctionnement = fonctionnement;
        this.prochaineVerification = prochaineVerification;
        this.derniereVerification = derniereVerification;
        this.idUser = idUser;
    }

    public Equipement(int id, int idSalle, String nom, boolean fonctionnement, Date prochaineVerification, Date derniereVerification, long idUser) {
        this.id = id;
        this.idSalle = idSalle;
        this.nom = nom;
        this.fonctionnement = fonctionnement;
        this.prochaineVerification = prochaineVerification;
        this.derniereVerification = derniereVerification;
        this.idUser = idUser;
    }

    public Equipement() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public void setIdSalle(int idSalle) {
        this.idSalle = idSalle;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public boolean isFonctionnement() {
        return fonctionnement;
    }

    public void setFonctionnement(boolean fonctionnement) {
        this.fonctionnement = fonctionnement;
    }

    public java.sql.Date getProchaineVerification() {
        return (java.sql.Date) prochaineVerification;
    }

    public void setProchaineVerification(Date prochaineVerification) {
        this.prochaineVerification = prochaineVerification;
    }

    public java.sql.Date getDerniereVerification() {
        return (java.sql.Date) derniereVerification;
    }

    public void setDerniereVerification(Date derniereVerification) {
        this.derniereVerification = derniereVerification;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    @Override
    public String toString() {
        return "Equipement{" +
                "id=" + id +
                ", idSalle=" + idSalle +
                ", nom='" + nom + '\'' +
                ", fonctionnement=" + fonctionnement +
                ", prochaineVerification=" + prochaineVerification +
                ", derniereVerification=" + derniereVerification +
                ", idUser=" + idUser +
                '}';
    }
}
