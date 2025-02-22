package services;
import entities.SalleDeSport;

import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDeSportService implements org.example.service.IService<SalleDeSport> {

    private Connection cnx;

    public SalleDeSportService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(SalleDeSport salle) throws SQLException {
        String query = "INSERT INTO salle_de_sport (nom, zone, image, id_user) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, salle.getNom());
        ps.setString(2, salle.getZone());
        ps.setString(3, salle.getImage());
        ps.setLong(4, salle.getUser_id());
        ps.executeUpdate();
    }

    @Override
    public void update(SalleDeSport salle) throws SQLException {
        String req = "UPDATE salle_de_sport SET nom = ?, zone = ?, image = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, salle.getNom());
            ps.setString(2, salle.getZone());
            ps.setString(3, salle.getImage());
            ps.setInt(4, salle.getId());
            
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("La mise à jour de la salle a échoué, aucune ligne modifiée.");
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la mise à jour: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void delete(SalleDeSport salle) throws SQLException {
        String query = "DELETE FROM salle_de_sport WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, salle.getId());
        ps.executeUpdate();
    }

    @Override
    public List<SalleDeSport> readAll() throws SQLException {
        List<SalleDeSport> salles = new ArrayList<>();
        String query = "SELECT * FROM salle_de_sport";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String nom = rs.getString("nom");
            String zone = rs.getString("zone");
            String image = rs.getString("image");
            int idUser = rs.getInt("id_user");
            SalleDeSport salle = new SalleDeSport(id, nom, zone, image, idUser);
            salles.add(salle);
        }

        return salles;
    }

    public SalleDeSport readById(int id) throws SQLException {
        String req = "SELECT * FROM salle_de_sport WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new SalleDeSport(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("zone"),
                        rs.getString("image"),
                        1  // Ajoutez ici la valeur appropriée pour le dernier paramètre
                    );
                }
            }
        }
        return null;
    }
    public List<SalleDeSport> getSallesByEquipement(String equipement) throws SQLException {
        List<SalleDeSport> salles = new ArrayList<>();

        String query = "SELECT DISTINCT s.id, s.nom, s.zone, s.image FROM salle_de_sport s " +
        "JOIN equipement e ON s.id = e.id_salle " +  // ✅ Correction : `salle_id` au lieu de `id`
                "WHERE LOWER(e.nom) LIKE LOWER(?)";  // ✅ Ignorer la casse pour éviter des problèmes de recherche


        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, "%" + equipement + "%");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                SalleDeSport salle = new SalleDeSport(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("zone"),
                        resultSet.getString("image"),
                        1 // Ajoutez ici la valeur appropriée pour le dernier paramètre
                );
                salles.add(salle);
            }
        }

        return salles;
    }
}