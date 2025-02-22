package org.example.service;

import org.example.MyConnection;
import org.example.entities.Equipement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipementService implements IService<Equipement> {

    private Connection cnx;

    public EquipementService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Equipement equipement) throws SQLException {
        String query = "INSERT INTO equipement (id_salle, nom, fonctionnement, prochaine_verification, derniere_verification, id_user) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, equipement.getIdSalle());
        ps.setString(2, equipement.getNom());
        ps.setBoolean(3, equipement.isFonctionnement());
        ps.setDate(4, equipement.getProchaineVerification() != null ? new java.sql.Date(equipement.getProchaineVerification().getTime()) : null);
        ps.setDate(5, equipement.getDerniereVerification() != null ? new java.sql.Date(equipement.getDerniereVerification().getTime()) : null);
        ps.setInt(6, equipement.getIdUser());
        ps.executeUpdate();
    }

    @Override
    public void update(Equipement equipement) throws SQLException {
        String query = "UPDATE equipement SET id_salle = ?, nom = ?, fonctionnement = ?, prochaine_verification = ?, derniere_verification = ?, id_user = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, equipement.getIdSalle());
        ps.setString(2, equipement.getNom());
        ps.setBoolean(3, equipement.isFonctionnement());
        ps.setDate(4, equipement.getProchaineVerification() != null ? new java.sql.Date(equipement.getProchaineVerification().getTime()) : null);
        ps.setDate(5, equipement.getDerniereVerification() != null ? new java.sql.Date(equipement.getDerniereVerification().getTime()) : null);
        ps.setInt(6, equipement.getIdUser());
        ps.setInt(7, equipement.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Equipement equipement) throws SQLException {
        String query = "DELETE FROM equipement WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, equipement.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Equipement> readAll() throws SQLException {
        List<Equipement> equipements = new ArrayList<>();
        String query = "SELECT * FROM equipement";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            int idSalle = rs.getInt("id_salle");
            String nom = rs.getString("nom");
            boolean fonctionnement = rs.getBoolean("fonctionnement");
            Date prochaineVerification = rs.getDate("prochaine_verification");
            Date derniereVerification = rs.getDate("derniere_verification");
            int idUser = rs.getInt("id_user");

            Equipement equipement = new Equipement(id, idSalle, nom, fonctionnement, prochaineVerification, derniereVerification, idUser);
            equipements.add(equipement);
        }

        return equipements;
    }

    public List<Equipement> readBySalleId(int salleId) throws SQLException {
        List<Equipement> equipements = new ArrayList<>();
        String query = "SELECT * FROM equipement WHERE id_salle = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, salleId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            boolean fonctionnement = rs.getBoolean("fonctionnement");
            Date prochaineVerification = rs.getDate("prochaine_verification");
            Date derniereVerification = rs.getDate("derniere_verification");
            int idUser = rs.getInt("id_user");

            equipements.add(new Equipement(id, salleId, nom, fonctionnement, prochaineVerification, derniereVerification, idUser));
        }

        return equipements;
    }
}
