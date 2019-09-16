package repository;

import domain.entity.Car;
import domain.entity.CarAdvert;
import domain.entity.Owner;
import domain.enums.Condition;
import utils.DatabaseConnectionManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CarAdvertRepositoryImpl implements CarAdvertRepository {

    @Override
    public void insertAdvert(CarAdvert carAdvert) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseConnectionManager.initializeDatabase()) {
            connection.setAutoCommit(false);
            int owner_id = insertOwnerQuery(connection, carAdvert);
            if (owner_id == 0) {
                throw new IllegalArgumentException("Cant create advert without owner id");
            }
            insertAdvertQuery(connection, owner_id, carAdvert);
            insertTelephoneQuery(connection, owner_id, carAdvert);
            connection.commit();
        }
    }

    @Override
    public CarAdvert getAdvertById(String id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseConnectionManager.initializeDatabase()) {
            String sql = String.format("SELECT * from car_advert\n" +
                    "INNER JOIN owner on car_advert.owner_id = owner.id\n" +
                    "INNER JOIN (SELECT owner_id, string_agg(number, ',') AS numbers " +
                    "FROM telephone_number GROUP BY owner_id) AS n ON n.owner_id =  car_advert.owner_id\n" +
                    "WHERE car_advert.id = '%s';", id);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            return carAdvertResponseBuilder(rs).get(0);
        }
    }

    @Override
    public List<CarAdvert> getAdvertPage(int limit, int offset) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseConnectionManager.initializeDatabase()) {
            String sql = String.format("SELECT * from car_advert\n" +
                    "INNER JOIN owner on car_advert.owner_id = owner.id\n" +
                    "INNER JOIN (SELECT owner_id, string_agg(number, ',') AS numbers " +
                    "FROM telephone_number GROUP BY owner_id) AS n ON n.owner_id =  car_advert.owner_id\n" +
                    "LIMIT %s OFFSET %s;", limit, offset);
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            return carAdvertResponseBuilder(rs);
        }
    }

    @Override
    public String getPhotoById(int id) throws SQLException, ClassNotFoundException {
        String photo = "";
        try (Connection connection = DatabaseConnectionManager.initializeDatabase()) {
            PreparedStatement statement = connection.prepareStatement("SELECT photo FROM car_advert WHERE id = ?");
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            while(rs.next()) {
                photo = rs.getString("photo");
            }
            System.out.println(photo);
            return photo;
        }
    }

    @Override
    public void addPhotoToAdvert(int id, String fileName) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseConnectionManager.initializeDatabase()) {
            PreparedStatement statement = connection.prepareStatement("UPDATE car_advert SET photo = ? WHERE id = ?");
            statement.setString(1, fileName);
            statement.setInt(2, id);
            statement.executeUpdate();
        }
    }

    @Override
    public void deleteAdvert(String id) throws SQLException, ClassNotFoundException {
        try (Connection connection = DatabaseConnectionManager.initializeDatabase()) {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM car_advert WHERE id = ?");
            statement.setInt(1,Integer.parseInt(id));
            statement.executeUpdate();
        }
    }

    private int insertOwnerQuery(Connection connection, CarAdvert carAdvert) {
        int owner_id = 0;
        try {
            PreparedStatement ownerStatement = connection.prepareStatement("INSERT INTO owner (name) VALUES(?)",
                Statement.RETURN_GENERATED_KEYS);
            ownerStatement.setString(1, carAdvert.getOwner().getOwnerName());
            ownerStatement.executeUpdate();
            ResultSet rs = ownerStatement.getGeneratedKeys();
            while (rs.next()) {
                owner_id = rs.getInt(1);
            }
            return owner_id;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return owner_id;
    }

    private void insertAdvertQuery(Connection connection, int owner_id, CarAdvert carAdvert ) {
        try {
            PreparedStatement advertStatement = connection.prepareStatement("insert into car_advert (owner_id, make, model, year, " +
                "horse_power, engine_capacity, condition) values(?, ?, ?, ?, ?, ?, ?)");
            advertStatement.setInt(1, owner_id);
            advertStatement.setString(2, carAdvert.getCar().getMake());
            advertStatement.setString(3, carAdvert.getCar().getModel());
            advertStatement.setInt(4, carAdvert.getCar().getYear());
            advertStatement.setInt(5, carAdvert.getCar().getHorsePower());
            advertStatement.setInt(6, carAdvert.getCar().getEngineCapacity());
            advertStatement.setString(7, carAdvert.getCar().getCondition().name());
            advertStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertTelephoneQuery(Connection connection, int owner_id, CarAdvert carAdvert) {
        try {
            for (String number : carAdvert.getOwner().getNumbers()) {
                PreparedStatement telephoneStatement = connection.prepareStatement("insert into telephone_number (owner_id, number) values(?, ?)");
                telephoneStatement.setInt(1, owner_id);
                telephoneStatement.setString(2, number);
                telephoneStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<String> getListOfOwnerNumbers(String numbers) {
        String[] partNumbers = numbers.split("\\,");
        return new ArrayList<>(Arrays.asList(partNumbers));
    }

    private List<CarAdvert> carAdvertResponseBuilder(ResultSet rs) {
        List<CarAdvert> adverts = new ArrayList<>();
        try {
            while (rs.next()) {
                Car responseCar = new Car();
                Owner responseOwner = new Owner();
                CarAdvert responseCarAdvert = new CarAdvert();
                responseCar.setMake(rs.getString("make"));
                responseCar.setModel(rs.getString("model"));
                responseCar.setEngineCapacity(rs.getInt("engine_capacity"));
                responseCar.setHorsePower(rs.getInt("horse_power"));
                responseCar.setYear(rs.getInt("year"));
                responseCar.setCondition(Condition.valueOf(rs.getString("condition")));
                responseOwner.setId(rs.getInt("owner_id"));
                responseOwner.setOwnerName(rs.getString("name"));
                String numbersString = rs.getString("numbers");
                responseOwner.setNumbers(getListOfOwnerNumbers(numbersString));
                responseCarAdvert.setId(rs.getInt("id"));
                responseCarAdvert.setCar(responseCar);
                responseCarAdvert.setOwner(responseOwner);
                adverts.add(responseCarAdvert);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adverts;
    }
}
