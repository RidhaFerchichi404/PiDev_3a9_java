package services;

import entities.Abonnement;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService implements IAbonnement<Abonnement>{
    private static Connection cnx;

    public AbonnementService(){
        cnx = MyConnection.getInstance().getConnection();
    }

    public void create(Abonnement abonnement) throws SQLException {
        if (abonnement.getNom() == null || abonnement.getdescriptiona() == null || abonnement.getSalleNom() == null) {
            throw new IllegalArgumentException("Les champs Nom, Description et SalleName ne peuvent pas être nuls.");
        }
        if (abonnement.getDuree() <= 0 || abonnement.getPrix() <= 0) {
            throw new IllegalArgumentException("La durée et le prix doivent être des valeurs positives.");
        }

        // Récupérer l'ID de la salle de sport à partir de son nom
        int salleId = getIdByName(abonnement.getSalleNom());
        if (salleId == -1) {
            throw new SQLException("La salle de sport '" + abonnement.getSalleNom() + "' n'existe pas.");
        }

        String query = "INSERT INTO Abonnement (Nom, Description, duree, Prix, SalleID, SalleName) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, abonnement.getNom());
            stmt.setString(2, abonnement.getdescriptiona());
            stmt.setInt(3, abonnement.getDuree());
            stmt.setDouble(4, abonnement.getPrix());
            stmt.setInt(5, salleId);
            stmt.setString(6, abonnement.getSalleNom());
            stmt.executeUpdate();
        }
    }




    // Correction ici : Utilisation de 'name' au lieu de 'salleNom'
    public int getIdByName(String name) throws SQLException {
        String query = "SELECT SalleID FROM salledesport WHERE Nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, name); // Utilisation de 'name'
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SalleID");
            }
        }
        return -1;
    }
    /*public int getIdByName(String name) throws SQLException {
        System.out.println("Recherche de la salle avec le nom : " + name); // Log pour débogage
        String query = "SELECT SalleID FROM salledesport WHERE Nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, name.trim());  // Utilisation de trim pour enlever les espaces inutiles
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("SalleID");  // Récupère l'ID de la salle
            } else {
                System.out.println("Aucune salle trouvée avec le nom : " + name);
            }
        }
        return 0; // Retourner 0 si aucune salle n'est trouvée
    }*/



    @Override
    public void update(Abonnement abonnement) throws SQLException {
        String query = "UPDATE Abonnement SET  Description = ? WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
           // stmt.setString(1, abonnement.getNom());               // Nom de l'abonnement
            stmt.setString(1, abonnement.getdescriptiona());        // Description
            //stmt.setInt(3, abonnement.getDuree());                 // Durée
            //stmt.setDouble(4, abonnement.getPrix());               // Prix
            //stmt.setInt(5, abonnement.getSalleDeSportId());        // ID de la salle de sport
            //stmt.setString(6, abonnement.getSalleNom());           // Nom de la salle de sport
            stmt.setInt(2, abonnement.getId());          // ID de l'abonnement
            stmt.executeUpdate();                                  // Exécution de la mise à jour
        }

    }

    @Override
    public void delete(Abonnement abonnement) throws SQLException {
        String query = "DELETE FROM Abonnement WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, abonnement.getId());          // ID de l'abonnement à supprimer
            stmt.executeUpdate();                                  // Exécution de la suppression
        }

    }

    @Override
    public List<Abonnement> readAll() throws SQLException {
        List<Abonnement> abonnements = new ArrayList<>();
        String query = "SELECT * FROM Abonnement";
        try (Statement stmt = cnx.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Abonnement abonnement = new Abonnement();
                abonnement.setId(rs.getInt("AbonnementID"));
                abonnement.setNom(rs.getString("Nom"));
                abonnement.setdescriptiona(rs.getString("Description"));
                abonnement.setDuree(rs.getInt("Duree"));
                abonnement.setPrix(rs.getDouble("Prix"));
                abonnement.setSalleDeSportId(rs.getInt("SalleID"));
                abonnement.setSalleNom(rs.getString("SalleName"));
                abonnements.add(abonnement);
            }
        }
        return abonnements;
    }
}
