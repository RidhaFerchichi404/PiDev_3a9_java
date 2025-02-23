package entities;

import java.time.LocalDateTime;

public class Commande {
    private int idCommande;
    private int idProduit;
    private long idUser;
    private String nomClient;
    private String adresseLivraison;
    private String telephone;
    private int quantite;
    private LocalDateTime dateCommande;
    private String statutCommande; // "En attente", "En cours", "Livrée", "Annulée"

    // Constructor without ID (for creation)
    public Commande(int idProduit, long idUser, String nomClient, String adresseLivraison, 
                   String telephone, int quantite, LocalDateTime dateCommande, 
                   String statutCommande) {
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.nomClient = nomClient;
        this.adresseLivraison = adresseLivraison;
        this.telephone = telephone;
        this.quantite = quantite;
        this.dateCommande = dateCommande;
        this.statutCommande = statutCommande;
    }

    // Constructor with ID (for retrieval)
    public Commande(int idCommande, int idProduit, long idUser, String nomClient, 
                   String adresseLivraison, String telephone, int quantite, 
                   LocalDateTime dateCommande, String statutCommande) {
        this.idCommande = idCommande;
        this.idProduit = idProduit;
        this.idUser = idUser;
        this.nomClient = nomClient;
        this.adresseLivraison = adresseLivraison;
        this.telephone = telephone;
        this.quantite = quantite;
        this.dateCommande = dateCommande;
        this.statutCommande = statutCommande;
    }

    // Getters and Setters
    public int getIdCommande() { return idCommande; }
    public void setIdCommande(int idCommande) { this.idCommande = idCommande; }

    public int getIdProduit() { return idProduit; }
    public void setIdProduit(int idProduit) { this.idProduit = idProduit; }

    public long getIdUser() { return idUser; }
    public void setIdUser(long idUser) { this.idUser = idUser; }

    public String getNomClient() { return nomClient; }
    public void setNomClient(String nomClient) { this.nomClient = nomClient; }

    public String getAdresseLivraison() { return adresseLivraison; }
    public void setAdresseLivraison(String adresseLivraison) { this.adresseLivraison = adresseLivraison; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public LocalDateTime getDateCommande() { return dateCommande; }
    public void setDateCommande(LocalDateTime dateCommande) { this.dateCommande = dateCommande; }

    public String getStatutCommande() { return statutCommande; }
    public void setStatutCommande(String statutCommande) { this.statutCommande = statutCommande; }

    @Override
    public String toString() {
        return "Commande{" +
                "idCommande=" + idCommande +
                ", idProduit=" + idProduit +
                ", idUser=" + idUser +
                ", nomClient='" + nomClient + '\'' +
                ", adresseLivraison='" + adresseLivraison + '\'' +
                ", telephone='" + telephone + '\'' +
                ", quantite=" + quantite +
                ", dateCommande=" + dateCommande +
                ", statutCommande='" + statutCommande + '\'' +
                '}';
    }
} 