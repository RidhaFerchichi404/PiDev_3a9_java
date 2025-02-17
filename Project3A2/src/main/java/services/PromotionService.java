package services;

import entities.Promotion;
import utils.MyConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromotionService implements IPromotion<Promotion> {
    private static Connection cnx;
    public PromotionService() {
        cnx = MyConnection.getInstance().getConnection();
    }
    // Create a new promotion
    public void create(Promotion promotion) throws SQLException {
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
    // Update an existing promotion
    public void update(Promotion promotion) throws SQLException {
        String query = "UPDATE promotion SET  Description = ?  WHERE PromotionID = ?";
        try (PreparedStatement stmt = cnx.prepareStatement(query)) {
            stmt.setString(1, promotion.getCodePromo());
            stmt.setString(1, promotion.getDescription());
            stmt.setInt(2, promotion.getPromotionId());
            stmt.setString(3, promotion.getTypeReduction());
            stmt.setBigDecimal(4, promotion.getValeurReduction());
            stmt.setDate(5, promotion.getDateDebut());
            stmt.setDate(6, promotion.getDateFin());
            stmt.setInt(7, promotion.getAbonnementId());
            stmt.setInt(8, promotion.getSalleId());
            stmt.setInt(9, promotion.getPromotionId());
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
}
