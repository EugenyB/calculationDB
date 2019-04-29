package dao;

import beans.Calculation;

import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CalculationDAO implements Serializable {

    private final Connection connection;

    public CalculationDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Calculation> findAll() {
        try {
            PreparedStatement ps = connection.prepareStatement("select * from calculation");
            ResultSet rs = ps.executeQuery();
            List<Calculation> calculations = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id");
                String date = rs.getString("creationdate");
                String data = rs.getString("data");
                calculations.add(new Calculation(id, data, date));
            }
            return calculations;
        } catch (SQLException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public void save(String creationDate, String data) {
        try (PreparedStatement ps = connection.prepareStatement("insert into calculation (creationdate, data) VALUES (?,?)");){
            ps.setString(1, creationDate);
            ps.setString(2, data);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Calculation find(int id) {
        try (PreparedStatement ps = connection.prepareStatement("select * from calculation where id=?")) {
            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();
            if (resultSet.next()) {
                return new Calculation(id, resultSet.getString("data"), resultSet.getString("creationdate"));
            } else return null;
        } catch (SQLException e) {
            return null;
        }
    }

    public void delete(int selectedId) {
        try (PreparedStatement ps = connection.prepareStatement("delete from calculation where id = ?")) {
            ps.setInt(1, selectedId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
