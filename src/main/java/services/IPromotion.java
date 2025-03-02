package services;

import entities.Promotion;

import java.util.List;
import java.util.ArrayList;
import java.sql.SQLException;

public interface IPromotion<A>{
    void create(A a) throws SQLException;

    void update(Promotion promotion) throws SQLException;
    void delete(int promotionId) throws SQLException;
    //List<A> readAll() throws SQLException;
}
