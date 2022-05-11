package au.edu.sydney.soft3202.majorProject.model;

import com.google.gson.JsonObject;
import com.sun.javafx.scene.control.LabeledText;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class sqlModel {

    private static final String dbName = "cityWeather.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;


    public void createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            System.out.println("A new database has been created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void setupDB() {
        String createSavingStatusTableSQL =
                """
                CREATE TABLE IF NOT EXISTS cityWeather (
                    id INTEGER PRIMARY KEY,
                    city_name text NOT NULL,
                    state_code text NOT NULL,
                    temperature DOUBLE NOT NULL,
                    wind_speed DOUBLE NOT NULL,
                    wind_direction text NOT NULL,
                    clouds DOUBLE NOT NULL,
                    precipitation DOUBLE NOT NULL,
                    air_quality INTEGER NOT NULL,
                    icon text NOT NULL,
                    description text NOT NULL,
                    lat DOUBLE NOT NULL,
                    lon DOUBLE NOT NULL
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createSavingStatusTableSQL);
            System.out.println("Saving Status Table Create Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void addWeather(String addingCityName, String addingStateName, double addingTemp, double addingSpeed, String addingDirection, double addingClouds, double addingPreci, int addingAqi, String addingIcon, String addingDes, Double lati, Double Lont) throws SQLException{
        String addStatusWithParametersSQL =
                """
                INSERT INTO cityWeather(city_name, state_code, temperature, wind_speed, wind_direction, clouds, precipitation, air_quality, icon, description, lat, lon) VALUES
                    (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
        Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement preparedStatement = conn.prepareStatement(addStatusWithParametersSQL);

            System.out.println("Added questionable data");
            preparedStatement.setString(1, addingCityName);
            preparedStatement.setString(2, addingStateName);
            preparedStatement.setDouble(3, addingTemp);
            preparedStatement.setDouble(4, addingSpeed);
            preparedStatement.setString(5, addingDirection);
            preparedStatement.setDouble(6, addingClouds);
            preparedStatement.setDouble(7, addingPreci);
            preparedStatement.setInt(8, addingAqi);
            preparedStatement.setString(9, addingIcon);
            preparedStatement.setString(10, addingDes);
            preparedStatement.setDouble(11, lati);
            preparedStatement.setDouble(12, Lont);
            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("=------------------------------------------------==========");
//            System.out.println(e.getMessage());
//        }
    }

    public ArrayList<ArrayList<String>> queryWeather() {
        String matchingStatusSQL =
                """
                SELECT *
                FROM cityWeather
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(matchingStatusSQL)) {
            ResultSet results = preparedStatement.executeQuery();

            ArrayList<ArrayList<String>> allCitiesWeather = new ArrayList<>();


            while (results.next()) {
                ArrayList singleCityWeather = new ArrayList();
                singleCityWeather.add(results.getString("city_name"));
                singleCityWeather.add(results.getString("state_code"));
                singleCityWeather.add(results.getString("temperature"));
                singleCityWeather.add(results.getString("wind_speed"));
                singleCityWeather.add(results.getString("wind_direction"));
                singleCityWeather.add(results.getString("clouds"));
                singleCityWeather.add(results.getString("precipitation"));
                singleCityWeather.add(results.getString("air_quality"));
                singleCityWeather.add(results.getString("icon"));
                singleCityWeather.add(results.getString("description"));
                singleCityWeather.add(results.getString("id"));
                singleCityWeather.add(results.getString("lat"));
                singleCityWeather.add(results.getString("lon"));
                allCitiesWeather.add(singleCityWeather);

            }
            System.out.println(allCitiesWeather);
            System.out.println("Query Data Successfully!");

            return allCitiesWeather;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public void deleteDB(String id){
        String clearDBSQL =
                """
                DELETE FROM cityWeather
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(clearDBSQL)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            System.out.println("CLEAR SUCCESS");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public void clearDB(){
        String clearDBSQL =
                """
                DELETE FROM cityWeather
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(clearDBSQL)) {
            preparedStatement.executeUpdate();
            System.out.println("CLEAR SUCCESS");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }



    public void removeDB() {
        File dbFile = new File("cityWeather.db");
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                System.exit(-1);
            } else {
                System.out.println("Removed existing DB file.");
            }
        } else {
            System.out.println("No existing DB file.");
        }
    }





}
