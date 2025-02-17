package services.services;

import entities.Produit;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProduitService implements IService<Produit> {
    private Connection cnx;

    public ProduitService() {
        cnx = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Produit produit) throws SQLException {
        String query = "INSERT INTO produits (nom, description, categorie, prix, quantite_stock, disponible) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getDescription());
            ps.setString(3, produit.getCategorie());
            ps.setBigDecimal(4, produit.getPrix());
            ps.setInt(5, produit.getQuantiteStock());
            ps.setBoolean(6, produit.isDisponible());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Produit produit) throws SQLException {
        String query = "UPDATE produits SET nom=?, description=?, categorie=?, prix=?, quantite_stock=?, disponible=? WHERE id_produit=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, produit.getNom());
            ps.setString(2, produit.getDescription());
            ps.setString(3, produit.getCategorie());
            ps.setBigDecimal(4, produit.getPrix());
            ps.setInt(5, produit.getQuantiteStock());
            ps.setBoolean(6, produit.isDisponible());
            ps.setInt(7, produit.getIdProduit());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM produits WHERE id_produit=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Produit> readAll() throws SQLException {
        List<Produit> produits = new ArrayList<>();
        String query = "SELECT * FROM produits";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                produits.add(new Produit(
                    rs.getInt("id_produit"),
                    rs.getString("nom"),
                    rs.getString("description"),
                    rs.getString("categorie"),
                    rs.getBigDecimal("prix"),
                    rs.getInt("quantite_stock"),
                    rs.getBoolean("disponible")
                ));
            }
        }
        return produits;
    }

    @Override
    public Produit readById(int id) throws SQLException {
        String query = "SELECT * FROM produits WHERE id_produit=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Produit(
                        rs.getInt("id_produit"),
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("categorie"),
                        rs.getBigDecimal("prix"),
                        rs.getInt("quantite_stock"),
                        rs.getBoolean("disponible")
                    );
                }
            }
        }
        return null;
    }
} 