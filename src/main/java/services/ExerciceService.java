package services;


import entities.Exercice;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciceService implements org.example.service.IService<Exercice> {

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
        ps.setLong(3, exercice.getIdUser());
        ps.setInt(4, exercice.getIdEquipement());
        ps.setString(5, exercice.getNomExercice());
        ps.executeUpdate();
    }

    @Override
    public void update(Exercice exercice) throws SQLException {
        String query = "UPDATE exercice SET description = ?, image = ?, id_user = ?, nom_exercice = ? WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);

        ps.setString(1, exercice.getDescription());
        ps.setString(2, exercice.getImage());
        ps.setLong(3, exercice.getIdUser()); // ✅ Utiliser l'ID de l'utilisateur correct
        ps.setString(4, exercice.getNomExercice());
        ps.setInt(5, exercice.getId()); // ✅ Utiliser l'ID de l'exercice pour la mise à jour

        ps.executeUpdate();

        System.out.println("✅ Exercice mis à jour avec succès. ID Utilisateur : " + exercice.getIdUser());
    }



    @Override
    public void delete(Exercice exercice) throws SQLException {
        String query = "DELETE FROM exercice WHERE id = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, exercice.getId());  // Correction : on supprime par `id`
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
    public List<Exercice> readByEquipementId(int idEquipement) throws SQLException {
        List<Exercice> exercices = new ArrayList<>();
        String query = "SELECT * FROM exercice WHERE id_equipement = ?";
        PreparedStatement ps = cnx.prepareStatement(query);
        ps.setInt(1, idEquipement);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Exercice exercice = new Exercice(
                    rs.getInt("id"),
                    rs.getString("description"),
                    rs.getString("image"),
                    rs.getLong("id_user"),    // 🔴 Vérifie bien que cette ligne est présente
                    rs.getInt("id_equipement"),
                    rs.getString("nom_exercice")
            );
            exercices.add(exercice);
        }

        return exercices;
    }

}
