package au.edu.sydney.soft3202.majorProject.model;

import au.edu.sydney.soft3202.majorProject.Main;
import com.google.gson.JsonObject;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;

public class ModelEngine {
    private appSystem weather = new appSystem();
    private sqlModel sql = new sqlModel();
//    private witAPI witAPIModel = new witAPI();

    public ArrayList<ArrayList<String>> modelGetCSV() throws FileNotFoundException {
        return weather.readCSVFile();
    }

    public ArrayList<String> getModelCityDetails(ArrayList<String> pair) throws FileNotFoundException {
        return weather.getCityDetails(pair);
    }

    public ArrayList<String> getModelCityWeatherDetails(JsonObject jsonObject) throws FileNotFoundException {
        return weather.getWeatherDetails(jsonObject);
    }

    public ArrayList<String> modelAddOffline(){
        return weather.addOffline();
    }

    public void modelAddSql(ArrayList<String> cityWeather) throws SQLException {
        sql.addWeather(cityWeather.get(0), cityWeather.get(1), Double.parseDouble(cityWeather.get(2)), Double.parseDouble(cityWeather.get(3)), cityWeather.get(4), Double.parseDouble(cityWeather.get(5)), Double.parseDouble(cityWeather.get(6)), Integer.parseInt(cityWeather.get(7)), cityWeather.get(8), cityWeather.get(9), Double.parseDouble(cityWeather.get(10)), Double.parseDouble(cityWeather.get(11)));
    }

    public ArrayList<ArrayList<String>> modelQuerySql(){
        return sql.queryWeather();
    }

    public void modelClearDB(){
        sql.clearDB();
    }

    public void modelDeleteDB(String id){
        sql.deleteDB(id);
    }

    public String modelPostToOutPutResponse(){
        return weather.postToOutputAPI(sendReport(sql.queryWeather()));
    }

    public String sendReport(ArrayList<ArrayList<String>> eachWeather){
        String postWeather = "";
        for (int i = 0; i < eachWeather.size(); i++){
            ArrayList<String> current = eachWeather.get(i);
            String currentAdding = "CITY NAME  " + current.get(0) + "\nSTATE NAME  " + current.get(1) + "\nTEMPERATURE  " + current.get(2) + "\nWIND SPEED   " +
                    current.get(3) + "\nWIND DIRECTION   " + current.get(4) + "\nCLOUDS   " + current.get(5) + "\nPRECIPITATION   " + current.get(6) +
                    "\nAIR QUALITY   " + current.get(7) + "\nDESCRIPTION   " + current.get(9) + "\n\n";
            postWeather += currentAdding;
        }
        return postWeather;
    }

    public JsonObject modelGetWeatherApi(Double lat, Double lon){
        return weather.getWeatherAPI(lat, lon);
    }

    public void setSql(sqlModel newSQL){
        sql = newSQL;
    }

    public ArrayList<Double> getLocationPair(String stateName, String cityName) throws FileNotFoundException {
        return weather.getLocationPair(stateName, cityName);
    }

    public sqlModel getSql(){
        return this.sql;
    }

    public appSystem getAppSystem(){
        return this.weather;
    }


    public void modelSetupDB(sqlModel sql){
        if(sql != null){
            sql.removeDB();
        }
        sql.createDB();
        sql.setupDB();
    }



}
