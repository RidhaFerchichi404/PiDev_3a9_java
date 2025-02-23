package entities;

import java.math.BigDecimal;

public class Produit {
    private int idProduit;
    private String nom;
    private String description;
    private String categorie;
    private BigDecimal prix;
    private int quantiteStock;
    private boolean disponible;
    private String imagePath;

    // Constructor without ID (for creation)
    public Produit(String nom, String description, String categorie, 
                  BigDecimal prix, int quantiteStock, boolean disponible, String imagePath) {
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.disponible = disponible;
        this.imagePath = imagePath;
    }

    // Constructor with ID (for retrieval)
    public Produit(int idProduit, String nom, String description, String categorie, 
                  BigDecimal prix, int quantiteStock, boolean disponible, String imagePath) {
        this.idProduit = idProduit;
        this.nom = nom;
        this.description = description;
        this.categorie = categorie;
        this.prix = prix;
        this.quantiteStock = quantiteStock;
        this.disponible = disponible;
        this.imagePath = imagePath;
    }

    // Getters and Setters
    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategorie() { return categorie; }
    public void setCategorie(String categorie) { this.categorie = categorie; }

    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }

    public int getQuantiteStock() { return quantiteStock; }
    public void setQuantiteStock(int quantiteStock) { this.quantiteStock = quantiteStock; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return "Produit{" +
                "idProduit=" + idProduit +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", categorie='" + categorie + '\'' +
                ", prix=" + prix +
                ", quantiteStock=" + quantiteStock +
                ", disponible=" + disponible +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
} 