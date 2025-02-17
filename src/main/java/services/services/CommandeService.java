package services.services;

import entities.Commande;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommandeService implements IService<Commande> {
    private Connection cnx;

    public CommandeService() {
        cnx = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Commande commande) throws SQLException {
        String query = "INSERT INTO commandes (id_produit, nom_client, adresse_livraison, telephone, quantite, date_commande, statut_commande) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, commande.getIdProduit());
            ps.setString(2, commande.getNomClient());
            ps.setString(3, commande.getAdresseLivraison());
            ps.setString(4, commande.getTelephone());
            ps.setInt(5, commande.getQuantite());
            ps.setTimestamp(6, Timestamp.valueOf(commande.getDateCommande()));
            ps.setString(7, commande.getStatutCommande());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Commande commande) throws SQLException {
        String query = "UPDATE commandes SET id_produit=?, nom_client=?, adresse_livraison=?, telephone=?, quantite=?, date_commande=?, statut_commande=? WHERE id_commande=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, commande.getIdProduit());
            ps.setString(2, commande.getNomClient());
            ps.setString(3, commande.getAdresseLivraison());
            ps.setString(4, commande.getTelephone());
            ps.setInt(5, commande.getQuantite());
            ps.setTimestamp(6, Timestamp.valueOf(commande.getDateCommande()));
            ps.setString(7, commande.getStatutCommande());
            ps.setInt(8, commande.getIdCommande());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM commandes WHERE id_commande=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Commande> readAll() throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commandes";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                commandes.add(new Commande(
                    rs.getInt("id_commande"),
                    rs.getInt("id_produit"),
                    rs.getString("nom_client"),
                    rs.getString("adresse_livraison"),
                    rs.getString("telephone"),
                    rs.getInt("quantite"),
                    rs.getTimestamp("date_commande").toLocalDateTime(),
                    rs.getString("statut_commande")
                ));
            }
        }
        return commandes;
    }

    @Override
    public Commande readById(int id) throws SQLException {
        String query = "SELECT * FROM commandes WHERE id_commande=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Commande(
                        rs.getInt("id_commande"),
                        rs.getInt("id_produit"),
                        rs.getString("nom_client"),
                        rs.getString("adresse_livraison"),
                        rs.getString("telephone"),
                        rs.getInt("quantite"),
                        rs.getTimestamp("date_commande").toLocalDateTime(),
                        rs.getString("statut_commande")
                    );
                }
            }
        }
        return null;
    }
} 