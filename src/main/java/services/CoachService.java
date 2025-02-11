package services;

import entities.Coach;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CoachService implements IService<Coach> {
    private Connection cnx;

    public CoachService() {
        cnx = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Coach coach) throws SQLException {
        String query = "INSERT INTO coachs (nom, prenom, email, telephone, specialite, salaire, date_embauche) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, coach.getNom());
            ps.setString(2, coach.getPrenom());
            ps.setString(3, coach.getEmail());
            ps.setString(4, coach.getTelephone());
            ps.setString(5, coach.getSpecialite());
            ps.setBigDecimal(6, coach.getSalaire());
            ps.setDate(7, Date.valueOf(coach.getDateEmbauche()));
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Coach coach) throws SQLException {
        String query = "UPDATE coachs SET nom=?, prenom=?, email=?, telephone=?, specialite=?, salaire=?, date_embauche=? WHERE id_coach=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, coach.getNom());
            ps.setString(2, coach.getPrenom());
            ps.setString(3, coach.getEmail());
            ps.setString(4, coach.getTelephone());
            ps.setString(5, coach.getSpecialite());
            ps.setBigDecimal(6, coach.getSalaire());
            ps.setDate(7, Date.valueOf(coach.getDateEmbauche()));
            ps.setInt(8, coach.getIdCoach());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM coachs WHERE id_coach=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Coach> readAll() throws SQLException {
        List<Coach> coaches = new ArrayList<>();
        String query = "SELECT * FROM coachs";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                coaches.add(new Coach(
                    rs.getInt("id_coach"),
                    rs.getString("nom"),
                    rs.getString("prenom"),
                    rs.getString("email"),
                    rs.getString("telephone"),
                    rs.getString("specialite"),
                    rs.getBigDecimal("salaire"),
                    rs.getDate("date_embauche").toLocalDate()
                ));
            }
        }
        return coaches;
    }

    @Override
    public Coach readById(int id) throws SQLException {
        String query = "SELECT * FROM coachs WHERE id_coach=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coach(
                        rs.getInt("id_coach"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("email"),
                        rs.getString("telephone"),
                        rs.getString("specialite"),
                        rs.getBigDecimal("salaire"),
                        rs.getDate("date_embauche").toLocalDate()
                    );
                }
            }
        }
        return null;
    }
} 