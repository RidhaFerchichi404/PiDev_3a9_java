package org.example.service;

import org.example.MyConnection;
import org.example.entities.Exercice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciceService implements IService<Exercice> {

    private Connection cnx;

    public ExerciceService() {
        cnx = MyConnection.getInstance().getConnection();
    }

    @Override
    public void create(Exercice exercice) throws SQLException {
        String query = "INSERT INTO exercice (description, image, id_user, id_equipement, nom_exercice) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, exercice.getDescription());
        ps.setString(2, exercice.getImage());
        ps.setInt(3, exercice.getIdUser());
        ps.setInt(4, exercice.getIdEquipement());
        ps.setString(5, exercice.getNomExercice());
        ps.executeUpdate();
    }

    @Override
    public void update(Exercice exercice) throws SQLException {
        String query = "UPDATE exercice SET description = ?, image = ?, id_user = ?, id_equipement = ?, nom_exercice = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setString(1, exercice.getDescription());
        ps.setString(2, exercice.getImage());
        ps.setInt(3, exercice.getIdUser());
        ps.setInt(4, exercice.getIdEquipement());
        ps.setString(5, exercice.getNomExercice());
        ps.setInt(6, exercice.getId());
        ps.executeUpdate();
    }

    @Override
    public void delete(Exercice exercice) throws SQLException {
        String query = "DELETE FROM exercice WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, exercice.getId());
        ps.executeUpdate();
    }

    @Override
    public List<Exercice> readAll() throws SQLException {
        List<Exercice> exercices = new ArrayList<>();
        String query = "SELECT * FROM exercice";
        Statement st = cnx.createStatement();
        ResultSet rs = st.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String description = rs.getString("description");
            String image = rs.getString("image");
            int idUser = rs.getInt("id_user");
            int idEquipement = rs.getInt("id_equipement");
            String nomExercice = rs.getString("nom_exercice");

            Exercice exercice = new Exercice(id, description, image, idUser, idEquipement, nomExercice);
            exercices.add(exercice);
        }

        return exercices;
    }
}
