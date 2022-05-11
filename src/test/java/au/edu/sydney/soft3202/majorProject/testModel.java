package au.edu.sydney.soft3202.majorProject;

import au.edu.sydney.soft3202.majorProject.controller.mainPageController;
import au.edu.sydney.soft3202.majorProject.model.ModelEngine;
import au.edu.sydney.soft3202.majorProject.model.appSystem;
import au.edu.sydney.soft3202.majorProject.model.sqlModel;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class testModel {
    private ModelEngine modelEngine;
    private appSystem mockAppSystem;
    private sqlModel mockSqlModel;
    private mainPageController controller;
    private String dbURL = "jdbc:sqlite::memory:";
    private Connection connection;

    @BeforeEach
    public void setup(){
        modelEngine = new ModelEngine();
        controller = new mainPageController();
        mockSqlModel = mock(sqlModel.class);
        mockAppSystem = mock(appSystem.class);
    }

    @Test
    public void testDBSetUp(){
        controller.setupDataBase(mockSqlModel);
        verify(mockSqlModel,times(1)).createDB();
        verify(mockSqlModel,times(1)).setupDB();
    }

    @Test
    public void testDBAdd() throws SQLException {
        //test setup and add DB
        setRealDB();
        addWeather("Sydney","02",19.4, 1.34927, "northwest", 100, 4.42038, 53, "r02d", "Moderate rain");
        String[] givingList = new String[]{"Sydney","02","19.4", "1.34927", "northwest", "100", "4.42038", "53", "r02d", "Moderate rain","1"};
        ArrayList<String> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, givingList);
        System.out.println(arrayList);
        mockSqlModel.createDB();
        mockSqlModel.setupDB();
        modelEngine.setSql(mockSqlModel);
        modelEngine.modelAddSql(arrayList);
        verify(mockSqlModel,times(1)).addWeather(anyString(),anyString(),anyDouble(),anyDouble(),anyString(),anyDouble(),anyDouble(),anyInt(),anyString(),anyString(),anyDouble(),anyDouble());
        when(mockSqlModel.queryWeather()).thenReturn(singleExample());
        assertEquals(queryWeather(), mockSqlModel.queryWeather());
        assertEquals(queryWeather(), modelEngine.modelQuerySql());

        //test clear DB
        modelEngine.setSql(mockSqlModel);
        modelEngine.modelClearDB();
        clearDB();
        mockSqlModel.clearDB();
        ArrayList<ArrayList<String>> empty = new ArrayList<>();
        when(mockSqlModel.queryWeather()).thenReturn(empty);
        assertEquals(queryWeather(),modelEngine.modelQuerySql());

        //test delete DB
        String[] givingList2 = new String[]{"Los Angeles","CA","14.2","1.52177","east","14","0.0","74","c02n","Few clouds"};
        arrayList.clear();
        addWeather("Sydney","02",19.4, 1.34927, "northwest", 100, 4.42038, 53, "r02d", "Moderate rain");

        addWeather("Los Angeles","CA",14.2,1.52177,"east",14,0.0,74,"c02n","Few clouds");

        Collections.addAll(arrayList, givingList);
        modelEngine.setSql(mockSqlModel);
        System.out.println(arrayList + "hihihi");
        modelEngine.modelAddSql(arrayList);
        when(mockSqlModel.queryWeather()).thenReturn(severalExample());
        assertEquals(queryWeather(), mockSqlModel.queryWeather());
        assertEquals(queryWeather(), modelEngine.modelQuerySql());

        deleteDB("2");
        when(mockSqlModel.queryWeather()).thenReturn(singleExample());
        assertEquals(queryWeather(), mockSqlModel.queryWeather());
        assertEquals(queryWeather(), modelEngine.modelQuerySql());

    }


    public void setRealDB(){
        createDB();
        setupDB();
    }


    public void createDB() {
        try{
            connection = DriverManager.getConnection(dbURL);
        }catch(SQLException e){
            e.printStackTrace();
        }


//        File dbFile = new File("jdbc:sqlite::memory:real.db");
//        if (dbFile.exists()) {
//            System.out.println("Database already created");
//            return;
//        }
//        try (Connection ignored = DriverManager.getConnection(dbURL)) {
//            System.out.println("A new database has been created successfully.");
//        } catch (SQLException e) {
//            System.out.println(e.getMessage());
//        }
    }

    public void setupDB() {
        String createSavingStatusTableSQL =
                """
                CREATE TABLE IF NOT EXISTS RealWeather (
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
                    description text NOT NULL
                );
                """;

        try (
             Statement statement = connection.createStatement()) {
            statement.execute(createSavingStatusTableSQL);
            System.out.println("Saving Status Table Create Successfully");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void addWeather(String addingCityName, String addingStateName, double addingTemp, double addingSpeed, String addingDirection, double addingClouds, double addingPreci, int addingAqi, String addingIcon, String addingDes) throws SQLException{
        String addStatusWithParametersSQL =
                """
                INSERT INTO RealWeather(city_name, state_code, temperature, wind_speed, wind_direction, clouds, precipitation, air_quality, icon, description) VALUES
                    (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;
//        Connection conn = DriverManager.getConnection(dbURL);
        PreparedStatement preparedStatement = connection.prepareStatement(addStatusWithParametersSQL);
//        try (Connection conn = DriverManager.getConnection(dbURL);
//             PreparedStatement preparedStatement = conn.prepareStatement(addStatusWithParametersSQL)) {
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
        preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            System.out.println("=------------------------------------------------==========");
//            System.out.println(e.getMessage());
//        }
    }


    public ArrayList<ArrayList<String>> singleExample(){
        ArrayList<ArrayList<String>> returnArraylist2D = new ArrayList<>();
        ArrayList<String> returnArrayList = new ArrayList<>();
        returnArrayList.add("Sydney");
        returnArrayList.add("02");
        returnArrayList.add("19.4");
        returnArrayList.add("1.34927");
        returnArrayList.add("northwest");
        returnArrayList.add("100.0");
        returnArrayList.add("4.42038");
        returnArrayList.add("53");
        returnArrayList.add("r02d");
        returnArrayList.add("Moderate rain");
        returnArrayList.add("1");
        returnArraylist2D.add(returnArrayList);
        return returnArraylist2D;
    }

    public ArrayList<ArrayList<String>> queryWeather() {
        String matchingStatusSQL =
                """
                SELECT *
                FROM RealWeather
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(matchingStatusSQL)) {
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

    public void clearDB(){
        String clearDBSQL =
                """
                DELETE FROM RealWeather
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(clearDBSQL)) {
            preparedStatement.executeUpdate();
            System.out.println("CLEAR Real data SUCCESS");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }

    public ArrayList<ArrayList<String>> severalExample(){
        ArrayList<ArrayList<String>> returnedList = singleExample();
        ArrayList<String> firstAdding = new ArrayList<>();
        String[] list = new String[]{"Los Angeles","CA","14.2","1.52177","east","14.0","0.0","74","c02n","Few clouds","2"};
        firstAdding.addAll(List.of(list));
        returnedList.add(firstAdding);
//        ArrayList<String> secondAdding = new ArrayList<>();
//        String[] list2 = new String[]{"Melbourne", "07", "21.1", "2.20657", "north-northeast", "100.0", "0.0", "41", "c04n", "Overcast clouds","3"};
//        secondAdding.addAll(List.of(list2));
//        returnedList.add(secondAdding);
//        System.out.println(returnedList);
        return returnedList;
    }

    public void deleteDB(String id){
        String clearDBSQL =
                """
                DELETE FROM RealWeather
                WHERE id = ?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = connection.prepareStatement(clearDBSQL)) {
            preparedStatement.setString(1, id);
            preparedStatement.executeUpdate();
            System.out.println("CLEAR SUCCESS");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
    }



}
