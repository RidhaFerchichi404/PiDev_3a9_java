package org.example.entities;

public class SalleDeSport {

    private int id;
    private String nom;
    private String zone;
    private String image;
    private int user_id; // Ajoutez cette propriété si elle n'existe pas
    private int rating = 3; // Note par défaut à 3 étoiles

    public SalleDeSport(String nom, String zone, String image, int user_id) {
        this.nom = nom;
        this.zone = zone;
        this.image = image;
        this.user_id = user_id;
    }

    public SalleDeSport(int id, String nom, String zone, String image, int user_id) {
        this.id = id;
        this.nom = nom;
        this.zone = zone;
        this.image = image;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "SalleDeSport{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", zone='" + zone + '\'' +
                ", image='" + image + '\'' +
                ", user_id=" + user_id +
                '}';
    }
}
