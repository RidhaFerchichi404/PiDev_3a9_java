package services;

import entities.Promotion;
import utils.MyConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PromotionService implements IPromotion<Promotion> {
    private static Connection cnx;
    public PromotionService() {
        cnx = MyConnection.getInstance().getConnection();
    }
    // Create a new promotion
    public void create(Promotion promotion) throws SQLException {
        // Récupérer la date actuelle
        LocalDate currentDate = LocalDate.now();

        // Convertir les dates de la promotion en LocalDate pour faciliter la comparaison
        LocalDate dateDebut = promotion.getDateDebut().toLocalDate();
        LocalDate dateFin = promotion.getDateFin().toLocalDate();

        // Vérifier que la date de début n'est pas antérieure à la date actuelle
        if (dateDebut.isBefore(currentDate)) {
            throw new IllegalArgumentException("La date de début ne peut pas être antérieure à la date actuelle.");
        }

        // Vérifier que la date de fin n'est pas antérieure à la date de début
        if (dateFin.isBefore(dateDebut)) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début.");
        }

        // Si les vérifications sont passées, procéder à l'insertion dans la base de données
        String query = "INSERT INTO Promotion (CodePromo, Description, TypeReduction, ValeurReduction, DateDebut, DateFin, AbonnementID) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, promotion.getCodePromo());
            stmt.setString(2, promotion.getDescription());
            stmt.setString(3, promotion.getTypeReduction());
            stmt.setBigDecimal(4, promotion.getValeurReduction());
            stmt.setDate(5, promotion.getDateDebut());
            stmt.setDate(6, promotion.getDateFin());
            stmt.setInt(7, promotion.getAbonnementId());

            stmt.executeUpdate();
        }
    }

    // Read all promotions
    /*public List<Promotion> readAll() throws SQLException {
        ArrayList<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM promotion";
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Promotion promotion = new Promotion();
                promotion.setPromotionId(rs.getInt("PromotionID"));
                promotion.setCodePromo(rs.getString("CodePromo"));
                promotion.setDescription(rs.getString("Description"));
                promotion.setTypeReduction(rs.getString("TypeReduction"));
                promotion.setValeurReduction(rs.getBigDecimal("ValeurReduction"));
                promotion.setDateDebut(String.valueOf(rs.getDate("DateDebut")));  // Supposant que c'est un java.sql.Date
                promotion.setDateFin(String.valueOf(rs.getDate("DateFin")));
                promotion.setAbonnementId(rs.getInt("AbonnementID"));
                promotion.setSalleId(rs.getInt("SalleID"));
                promotions.add(promotion);
            }
        }
        return promotions;
    }*/
    public boolean checkIfAbonnementExists(int abonnementId) throws SQLException {
        String query = "SELECT COUNT(*) FROM abonnement WHERE AbonnementID = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, abonnementId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // Retourne true si l'abonnement existe
                }
            }
        }
        return false; // Retourne false si l'abonnement n'existe pas
    }

    // Update an existing promotion
    public void update(Promotion promotion) throws SQLException {
        String query = "UPDATE promotion SET CodePromo = ?, ValeurReduction = ?, DateDebut = ?, DateFin = ? WHERE PromotionID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, promotion.getCodePromo());
            stmt.setBigDecimal(2, promotion.getValeurReduction());
            stmt.setDate(3, promotion.getDateDebut());
            stmt.setDate(4, promotion.getDateFin());
            stmt.setInt(5, promotion.getPromotionId()); // Utiliser l'ID pour cibler l'enregistrement
            stmt.executeUpdate();
        }
    }

    // Delete a promotion
    public void delete(int promotionId) throws SQLException {
        String query = "DELETE FROM promotion WHERE PromotionID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, promotionId);
            stmt.executeUpdate();
        }
    }
    // Méthode pour récupérer les promotions par ID d'abonnement
    public List<Promotion> getPromotionsByAbonnementId(int abonnementId) throws SQLException {
        List<Promotion> promotions = new ArrayList<>();
        String query = "SELECT * FROM Promotion WHERE AbonnementID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setInt(1, abonnementId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Promotion promotion = new Promotion();
                    promotion.setPromotionId(rs.getInt("PromotionID"));
                    promotion.setCodePromo(rs.getString("CodePromo"));
                    promotion.setDescription(rs.getString("Description"));
                    promotion.setTypeReduction(rs.getString("TypeReduction"));
                    promotion.setValeurReduction(rs.getBigDecimal("ValeurReduction"));
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
