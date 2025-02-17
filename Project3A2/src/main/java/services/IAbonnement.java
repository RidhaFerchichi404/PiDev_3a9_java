package services;
import entities.Abonnement;

import java.sql.SQLException;
import java.util.List;
public interface IAbonnement<T> {
    void create(T t) throws SQLException;
    int getIdByName(String name) throws SQLException;
    Abonnement getAbonnementByNom(String nom) throws SQLException;
    void update(T t) throws SQLException;
    void delete(T t) throws SQLException;
    List<T> readAll() throws SQLException;
}
