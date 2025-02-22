package services;

import entities.Abonnement;
import entities.Promotion;
import utils.MyConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AbonnementService implements IAbonnement<Abonnement>{
    private static Connection cnx;

    public AbonnementService(){
        cnx = MyConnection.getInstance().getConnection();
    }

    public void create(Abonnement abonnement) throws SQLException {
        if (abonnement.getNom() == null || abonnement.getdescriptiona() == null /*|| abonnement.getSalleNom() == null*/) {
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
    public boolean checkIfAbonnementExists(int abonnementId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Abonnement WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, abonnementId);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        }
        return false;
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

    public Abonnement getAbonnementByNom(String nom) throws SQLException {
        String query = "SELECT * FROM Abonnement WHERE Nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, nom);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Abonnement abonnement = new Abonnement();
                    abonnement.setNom(rs.getString("Nom"));
                    abonnement.setDescriptiona(rs.getString("Description"));
                    abonnement.setDuree(rs.getInt("Duree"));
                    abonnement.setPrix(rs.getDouble("Prix"));
                    abonnement.setSalleDeSportId(rs.getInt("SalleID"));
                    abonnement.setSalleNom(rs.getString("SalleName"));
                    return abonnement;
                }
            }
        }
        return null; // Aucun abonnement trouvé
    }

    /*public void update(Abonnement abonnement) throws SQLException {
        String query = "UPDATE Abonnement SET Description = ?, duree = ?, Prix = ?, SalleName = ? , SalleID = ? WHERE Nom = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, abonnement.getdescriptiona());
            stmt.setInt(2, abonnement.getDuree());
            stmt.setDouble(3, abonnement.getPrix());
            stmt.setString(4, abonnement.getSalleNom());
            stmt.setString(5, abonnement.getNom());
            stmt.setInt(6, abonnement.getSalleDeSportId());
            stmt.executeUpdate();
        }
    }*/
    @Override
    public void update(Abonnement abonnement) throws SQLException {
        if (abonnement == null) {
            throw new IllegalArgumentException("L'objet Abonnement ne peut pas être null.");
        }

        // Utiliser l'ID pour cibler l'enregistrement
        String query = "UPDATE Abonnement SET Nom = ?, Description = ?, duree = ?, Prix = ?, SalleName = ? WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, abonnement.getNom());
            stmt.setString(2, abonnement.getdescriptiona());
            stmt.setInt(3, abonnement.getDuree());
            stmt.setDouble(4, abonnement.getPrix());
            stmt.setString(5, abonnement.getSalleNom());
            stmt.setInt(6, abonnement.getId()); // Utiliser l'ID pour cibler l'enregistrement
            stmt.executeUpdate();
        }
    }


    /*@Override
    public void delete(Abonnement abonnement) throws SQLException {
        String query = "DELETE FROM Abonnement WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, abonnement.getId());          // ID de l'abonnement à supprimer
            stmt.executeUpdate();                                  // Exécution de la suppression
        }
    }*/
   /* @Override
    public void delete(int id) throws SQLException {
        System.out.println("Tentative de suppression de l'abonnement avec ID: " + id);
        String query = "DELETE FROM Abonnement WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            System.out.println("Nombre de lignes supprimées : " + rowsDeleted); // Vérifier combien de lignes sont affectées
        }
    }*/
    public void delete(int abonnementID) throws SQLException { // Ajouter throws SQLException
        String deleteAbonnementQuery = "DELETE FROM abonnement WHERE AbonnementID = ?";
        try (PreparedStatement abonnementStatement = cnx.prepareStatement(deleteAbonnementQuery)) {
            abonnementStatement.setInt(1, abonnementID);
            abonnementStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la suppression de l'abonnement", e);
        }
    }


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

                // Charger les promotions associées à cet abonnement
                List<Promotion> promotions = getPromotionsForAbonnement(abonnement.getId());
                abonnement.setPromotions(promotions);

                abonnements.add(abonnement);
            }
        }
        return abonnements;
    }

    private List<Promotion> getPromotionsForAbonnement(int abonnementId) throws SQLException {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotion WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, abonnementId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Promotion promotion = new Promotion();
                    promotion.setPromotionId(rs.getInt("PromotionID"));
                    promotion.setCodePromo(rs.getString("CodePromo"));
                    promotion.setDescription(rs.getString("Description"));
                    promotion.setTypeReduction(rs.getString("TypeReduction"));
                    promotion.setValeurReduction(BigDecimal.valueOf(rs.getDouble("ValeurReduction")));
                    promotion.setDateDebut(rs.getDate("DateDebut"));
                    promotion.setDateFin(rs.getDate("DateFin"));
                    promotion.setAbonnementId(rs.getInt("AbonnementID"));
                    promotions.add(promotion);
                }
            }
        }
        return promotions;
    }
}
