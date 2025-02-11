package entities;

import java.time.LocalTime;

public class Course {
    private int idCours;
    private String nomCours, description, jour;
    private LocalTime horaire;
    private int duree, idCoach;

    // Constructor without ID
    public Course(String nomCours, String description, LocalTime horaire, 
                 String jour, int duree, int idCoach) {
        this.nomCours = nomCours;
        this.description = description;
        this.horaire = horaire;
        this.jour = jour;
        this.duree = duree;
        this.idCoach = idCoach;
    }

    // Constructor with ID
    public Course(int idCours, String nomCours, String description, 
                 LocalTime horaire, String jour, int duree, int idCoach) {
        this.idCours = idCours;
        this.nomCours = nomCours;
        this.description = description;
        this.horaire = horaire;
        this.jour = jour;
        this.duree = duree;
        this.idCoach = idCoach;
    }

    // Getters and Setters
    public int getIdCours() { return idCours; }
    public void setIdCours(int idCours) { this.idCours = idCours; }
    
    public String getNomCours() { return nomCours; }
    public void setNomCours(String nomCours) { this.nomCours = nomCours; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public LocalTime getHoraire() { return horaire; }
    public void setHoraire(LocalTime horaire) { this.horaire = horaire; }
    
    public String getJour() { return jour; }
    public void setJour(String jour) { this.jour = jour; }
    
    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }
    
    public int getIdCoach() { return idCoach; }
    public void setIdCoach(int idCoach) { this.idCoach = idCoach; }

    @Override
    public String toString() {
        return "Course{" +
                "idCours=" + idCours +
                ", nomCours='" + nomCours + '\'' +
                ", description='" + description + '\'' +
                ", horaire=" + horaire +
                ", jour='" + jour + '\'' +
                ", duree=" + duree +
                ", idCoach=" + idCoach +
                '}';
    }
} 