package entities;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Coach {
    private int idCoach;
    private String nom, prenom, email, telephone, specialite;
    private BigDecimal salaire;
    private LocalDate dateEmbauche;

    // Constructor without ID (for creation)
    public Coach(String nom, String prenom, String email, String telephone, 
                String specialite, BigDecimal salaire, LocalDate dateEmbauche) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.specialite = specialite;
        this.salaire = salaire;
        this.dateEmbauche = dateEmbauche;
    }

    // Constructor with ID (for retrieval)
    public Coach(int idCoach, String nom, String prenom, String email, 
                String telephone, String specialite, BigDecimal salaire, 
                LocalDate dateEmbauche) {
        this.idCoach = idCoach;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.telephone = telephone;
        this.specialite = specialite;
        this.salaire = salaire;
        this.dateEmbauche = dateEmbauche;
    }

    // Getters and Setters
    public int getIdCoach() { return idCoach; }
    public void setIdCoach(int idCoach) { this.idCoach = idCoach; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
    
    public BigDecimal getSalaire() { return salaire; }
    public void setSalaire(BigDecimal salaire) { this.salaire = salaire; }
    
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }

    @Override
    public String toString() {
        return "Coach{" +
                "idCoach=" + idCoach +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", email='" + email + '\'' +
                ", telephone='" + telephone + '\'' +
                ", specialite='" + specialite + '\'' +
                ", salaire=" + salaire +
                ", dateEmbauche=" + dateEmbauche +
                '}';
    }
} 