package repository;

import domain.entity.CarAdvert;

import java.sql.SQLException;
import java.util.List;

public interface CarAdvertRepository {
    void insertAdvert(CarAdvert carAdvert) throws SQLException, ClassNotFoundException;
    void deleteAdvert(String id) throws SQLException, ClassNotFoundException;
    CarAdvert getAdvertById(String id) throws SQLException, ClassNotFoundException;
    List<CarAdvert> getAdvertPage(int limit, int offset) throws SQLException, ClassNotFoundException;
    String getPhotoById(int id) throws SQLException, ClassNotFoundException;
    void addPhotoToAdvert(int id, String fileName) throws SQLException, ClassNotFoundException;
}
