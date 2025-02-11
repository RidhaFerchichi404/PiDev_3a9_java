package services;

import entities.Course;
import utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CourseService implements IService<Course> {
    private Connection cnx;

    public CourseService() {
        cnx = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void create(Course course) throws SQLException {
        String query = "INSERT INTO cours (nom_cours, description, horaire, jour, duree, id_coach) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, course.getNomCours());
            ps.setString(2, course.getDescription());
            ps.setTime(3, Time.valueOf(course.getHoraire()));
            ps.setString(4, course.getJour());
            ps.setInt(5, course.getDuree());
            ps.setInt(6, course.getIdCoach());
            ps.executeUpdate();
        }
    }

    @Override
    public void update(Course course) throws SQLException {
        String query = "UPDATE cours SET nom_cours=?, description=?, horaire=?, jour=?, duree=?, id_coach=? WHERE id_cours=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setString(1, course.getNomCours());
            ps.setString(2, course.getDescription());
            ps.setTime(3, Time.valueOf(course.getHoraire()));
            ps.setString(4, course.getJour());
            ps.setInt(5, course.getDuree());
            ps.setInt(6, course.getIdCoach());
            ps.setInt(7, course.getIdCours());
            ps.executeUpdate();
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM cours WHERE id_cours=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public List<Course> readAll() throws SQLException {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT * FROM cours";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("id_cours"),
                    rs.getString("nom_cours"),
                    rs.getString("description"),
                    rs.getTime("horaire").toLocalTime(),
                    rs.getString("jour"),
                    rs.getInt("duree"),
                    rs.getInt("id_coach")
                ));
            }
        }
        return courses;
    }

    @Override
    public Course readById(int id) throws SQLException {
        String query = "SELECT * FROM cours WHERE id_cours=?";
        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Course(
                        rs.getInt("id_cours"),
                        rs.getString("nom_cours"),
                        rs.getString("description"),
                        rs.getTime("horaire").toLocalTime(),
                        rs.getString("jour"),
                        rs.getInt("duree"),
                        rs.getInt("id_coach")
                    );
                }
            }
        }
        return null;
    }
} 