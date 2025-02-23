package services;

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
        String query = "INSERT INTO commandes (id_produit, id_user, nom_client, adresse_livraison, telephone, quantite, date_commande, statut_commande) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, commande.getIdProduit());
            ps.setLong(2, commande.getIdUser());
            ps.setString(3, commande.getNomClient());
            ps.setString(4, commande.getAdresseLivraison());
            ps.setString(5, commande.getTelephone());
            ps.setInt(6, commande.getQuantite());
            ps.setTimestamp(7, Timestamp.valueOf(commande.getDateCommande()));
            ps.setString(8, commande.getStatutCommande());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Commande commande) throws SQLException {
        String query = "UPDATE commandes SET id_produit=?, id_user=?, nom_client=?, adresse_livraison=?, telephone=?, quantite=?, date_commande=?, statut_commande=? WHERE id_commande=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, commande.getIdProduit());
            ps.setLong(2, commande.getIdUser());
            ps.setString(3, commande.getNomClient());
            ps.setString(4, commande.getAdresseLivraison());
            ps.setString(5, commande.getTelephone());
            ps.setInt(6, commande.getQuantite());
            ps.setTimestamp(7, Timestamp.valueOf(commande.getDateCommande()));
            ps.setString(8, commande.getStatutCommande());
            ps.setInt(9, commande.getIdCommande());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(Commande commande) throws SQLException {
        String query = "DELETE FROM commandes WHERE id_commande=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, commande.getIdCommande());
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
                    rs.getLong("id_user"),
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

    public List<Commande> readAllByUser(long userId) throws SQLException {
        List<Commande> commandes = new ArrayList<>();
        String query = "SELECT * FROM commandes WHERE id_user = ?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setLong(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    commandes.add(new Commande(
                        rs.getInt("id_commande"),
                        rs.getInt("id_produit"),
                        rs.getLong("id_user"),
                        rs.getString("nom_client"),
                        rs.getString("adresse_livraison"),
                        rs.getString("telephone"),
                        rs.getInt("quantite"),
                        rs.getTimestamp("date_commande").toLocalDateTime(),
                        rs.getString("statut_commande")
                    ));
                }
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
                        rs.getLong("id_user"),
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