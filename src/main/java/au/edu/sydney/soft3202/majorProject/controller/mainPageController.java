package au.edu.sydney.soft3202.majorProject.controller;

import au.edu.sydney.soft3202.majorProject.Main;
import au.edu.sydney.soft3202.majorProject.model.ModelEngine;
import au.edu.sydney.soft3202.majorProject.model.appSystem;
import au.edu.sydney.soft3202.majorProject.model.sqlModel;
import com.google.gson.JsonObject;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import java.awt.*;
import java.awt.TextArea;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Optional;

import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;

import javafx.scene.shape.Circle;
import jdk.swing.interop.SwingInterOpUtils;
import org.controlsfx.control.SearchableComboBox;
import org.controlsfx.control.WorldMapView;
import org.controlsfx.control.textfield.AutoCompletionBinding;
import org.controlsfx.control.textfield.TextFields;
import org.controlsfx.glyphfont.FontAwesome;
import org.controlsfx.glyphfont.Glyph;

import javax.swing.*;

public class mainPageController implements Clickable{
    private appSystem weather;
    private sqlModel sql;
    private JsonObject jsonObject;
    private String iconSymbol;
    private WorldMapView.Location currentLocation;
    private ArrayList<String> locationPair;
    private ModelEngine modelEngine = new ModelEngine();

    private String cityValue;
    @FXML
    private TextField fillIn;
    @FXML
    private Button search;
    @FXML
    private Button report;
    @FXML
    private Slider zoom;
    @FXML
    private ChoiceBox history;
    @FXML
    private ComboBox combo;
    @FXML
    private WorldMapView worldMapView;
    @FXML
    private Label list;
    @FXML
    private SearchableComboBox searchableCombo;

    public mainPageController() {
    }

    public void setVersion(String inputVersion, String outputVersion){
        if (inputVersion.equals("online")){
            setupDataBase(modelEngine.getSql());
        }
    }
    @Override
    public void buttonActionController(ActionEvent event) throws Exception {
        String button = event.getSource().toString();
        switch (button) {
            case "Button[id=search, styleClass=button]'Search'":
                    setCityValue();
                    clearChoiceBox();
                setComboBoxChoice(combo.getValue().toString(),modelEngine.modelGetCSV());
                break;

            case "Button[id=confirm, styleClass=button]'Confirm'":
                ArrayList<String> cityPair = modelEngine.getModelCityDetails(getChoiceBoxValue());
                System.out.println("1");
                locationPair = cityPair;
                if(Main.inputState.equals("online")){
                    jsonObject = modelEngine.modelGetWeatherApi(Double.parseDouble(locationPair.get(0)), Double.parseDouble(locationPair.get(1)));
                }
//                setWorldMapView(cityPair);
                ArrayList<String> cityWeather = new ArrayList<>();
                System.out.println("4");
                if(Main.inputState.equals("online")){
                    if (jsonObject != null) {
                        cityWeather = modelEngine.getModelCityWeatherDetails(jsonObject);
                    }
                }else{
                    cityWeather = modelEngine.modelAddOffline();
                }

                System.out.println(cityWeather + "------------");
                if (cityWeather.size() != 0) {
                    try{
                        modelEngine.modelAddSql(cityWeather);
                        ArrayList<ArrayList<String>> gettingArrayList = modelEngine.modelQuerySql();
                        setSelectedCityWeatherDetails(gettingArrayList);
//                        setWorldMapView(cityPair);
                        setWorldMapViewTry();
                        setHistoryChoice(gettingArrayList);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }

                break;
            case "Button[id=clear, styleClass=button]'Clear'":
//                sql.clearDB();
                modelEngine.modelClearDB();
                clearWorldMapView();
                list.setText(null);
                break;
            case "Button[id=delete, styleClass=button]'Delete'":
//                sql.deleteDB(getId());
                modelEngine.modelDeleteDB(getId());
//                ArrayList<ArrayList<String>> gettingArrayList = sql.queryWeather();
                ArrayList<ArrayList<String>> gettingArrayList = modelEngine.modelQuerySql();
                setSelectedCityWeatherDetails(gettingArrayList);
                updateWorldMapView();
                if(Main.inputState.equals("offline")){
                    clearWorldMapView();
                }
                break;
            case "Button[id=report, styleClass=button]'Send Report'":
                String response;
                if(Main.outputState.equals("online")){
//                    response = weather.postToOutputAPI(sendReport(sql.queryWeather()));
                    response = modelEngine.modelPostToOutPutResponse();
                }else{
                    response = "https://pastebin.com/UIFdu235s";
                }
                if(!response.contains("https://pastebin.com/")){
                    setPopUPFail(response);
                }else{
                    setPopUpSuccess();
                }
                break;
        }
    }

    public void setCityValue(){
        cityValue = combo.getValue().toString();
    }

    public void clearChoiceBox(){
        combo.getItems().clear();
    }

    public ArrayList<String> getChoiceBoxValue(){
        ArrayList<String> returnArray = new ArrayList<>();
        if(combo.getValue() != null){
            String city = combo.getValue().toString();
            //add the city name and country name to the array
            returnArray.add(city.split(",")[2]);
            returnArray.add(city.split(",")[0]);

//            return city.split(",")[2];
        }
        return returnArray;
    }

    public void setWorldMapView(ArrayList<String> locationPair){

        if (jsonObject != null){
            if(!Main.inputState.equals("online")){
                locationPair.clear();
                locationPair.add("30.29365");
                locationPair.add("120.16142");
                iconSymbol = "a06d";
            }
            if (locationPair.size() > 0){
                WorldMapView.Location gettingLocation = new WorldMapView.Location(Double.parseDouble(locationPair.get(0)),Double.parseDouble(locationPair.get(1)));
                currentLocation = gettingLocation;

                worldMapView.setLocationViewFactory(location -> {
                    final Tooltip tooltip = new Tooltip();
                    String url = "icon/" + iconSymbol + ".png";
                    System.out.println(iconSymbol);
                    Image image = new Image(url,50,50,true,true);
                    ImageView iv = new ImageView(image);

                    Button button = new Button(locationPair.get(0) + "\n" + locationPair.get(1));
                    button.setTextAlignment(TextAlignment.RIGHT);
                    button.setGraphic(iv);
                    button.setTooltip(tooltip);
                    Tooltip.install(button,tooltip);
                    return button;
                });
                worldMapView.getLocations().add(gettingLocation);
        }
        }
    }

    public void clearWorldMapView(){
        worldMapView.getLocations().clear();
    }

    public void setSelectedCityWeatherDetails(ArrayList<ArrayList<String>> citiesWeatherDetail){
        String totalAdding = "";
        for (int i = 0; i < citiesWeatherDetail.size(); i++){
            ArrayList<String> singleCityWeather = citiesWeatherDetail.get(i);
            String cityName = singleCityWeather.get(0);
            String stateName = singleCityWeather.get(1);
            String temp = singleCityWeather.get(2);
            String windSpeed = singleCityWeather.get(3);
            String windDirection = singleCityWeather.get(4);
            String clouds = singleCityWeather.get(5);
            String precip = singleCityWeather.get(6);
            String airQuality = singleCityWeather.get(7);
            iconSymbol = singleCityWeather.get(8);
            String description = singleCityWeather.get(9);
            String addingString = "CITY NAME  " + cityName + "\nSTATE NAME  " + stateName + "\nTEMPERATURE  " + temp + "\nWIND SPEED   " +
                    windSpeed + "\nWIND DIRECTION   " + windDirection + "\nCLOUDS   " + clouds + "\n PRECIPITATION   " + precip +
                    "\nAIR QUALITY   " + airQuality + "\nDESCRIPTION   " + description + "\n\n";
            totalAdding += addingString;
        }
        list.setText(totalAdding);
    }

    public void setHistoryChoice(ArrayList<ArrayList<String>> citiesWeatherDetail){
        history.getItems().clear();
        ArrayList<String> settingList = new ArrayList<>();

        for (int i = 0; i < citiesWeatherDetail.size(); i++){
            ArrayList<String> singleCityWeather = citiesWeatherDetail.get(i);
            String cityName = singleCityWeather.get(0);
            String stateName = singleCityWeather.get(1);
            String id = singleCityWeather.get(10);
            String adding = id + "," + stateName + "," + cityName;
            System.out.println("addingis:     ");
            settingList.add(adding);

        }
        history.getItems().addAll(settingList);
    }

    public String getId(){
        String historyChoice = history.getValue().toString();
        history.getItems().remove(historyChoice);
        worldMapView.getLocations().remove(currentLocation);
        return historyChoice.split(",")[0];
    }

    public void setComboBoxChoice(String textValue, ArrayList<ArrayList<String>> totalList){
        combo.getItems().clear();

        ArrayList<String> settingList = new ArrayList<>();
        if(Main.inputState.equals("online")){
            for (int i = 0; i < totalList.size(); i++){
                if (totalList.get(i).get(1).contains(textValue)){
                    String addingString = totalList.get(i).get(4) + "," + totalList.get(i).get(2) + "," + totalList.get(i).get(1);
                    settingList.add(addingString);
                }
            }
            combo.setEditable(true);
        }else{
            String addingString = "1" + "," + "Paracel Islands" + "," + "HangZhou";
            String addingString2 = "2,VIC,Melbourne";
            settingList.add(addingString);
            settingList.add(addingString2);
        }

        combo.getItems().addAll(settingList);
    }

    public void setPopUpSuccess(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Paste State");
        alert.setHeaderText("Your Paste is successful.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

        }
    }

    public void setPopUPFail(String input){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Paste State");
        alert.setHeaderText("Your Paste is not successful.");
        alert.setContentText("YOUR ERROR IS " + input);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

        }
    }

    public WorldMapView getWorldMapView() {
        return worldMapView;
    }

    public Slider getZoom(){
        return zoom;
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
//        weather.postToOutputAPI(postWeather);
        return postWeather;
    }

    public void setupDataBase(sqlModel sql){
        if(sql != null){
            sql.removeDB();
        }
        sql.createDB();
        sql.setupDB();
    }

    public ArrayList<String> getHistoryChoiceBoxValue(ChoiceBox choices){

        ArrayList<String> returnArray = new ArrayList<>();
        System.out.println(choices.getValue());
        if(choices.getValue() != null){
            String city = choices.getValue().toString();
            //add the city name and country name to the array
            returnArray.add(city.split(",")[1]);
            returnArray.add(city.split(",")[2]);
        }
//        System.out.println("RETURN ARRAY IS " + returnArray + "\n\n\n");
        return returnArray;
    }

    public void updateWorldMapView() throws FileNotFoundException {
        System.out.println("hihihihi");
        ArrayList<String> stateCity = getHistoryChoiceBoxValue(history);
        System.out.println(stateCity);
        worldMapView.getLocations().clear();
        setWorldMapViewTry();
    }


    public void setWorldMapViewTry(){
        System.out.println("hi");
        ArrayList<ArrayList<String>> gettingArrayList = modelEngine.modelQuerySql();
        if(gettingArrayList.size() != 0){
            System.out.println(gettingArrayList);
            for (int i = 0; i < gettingArrayList.size(); i++){
                ArrayList<String> single = gettingArrayList.get(i);
                String icon = single.get(8);
                Double lat = Double.parseDouble(single.get(11));
                Double lon = Double.parseDouble(single.get(12));
                setWorldMapViewSinglePlace(icon, lat, lon);
            }
        }
    }

    public void setWorldMapViewSinglePlace(String icon, Double lat, Double lon){
        WorldMapView.Location gettingLocation = new WorldMapView.Location(lat,lon);
        worldMapView.setLocationViewFactory(location -> {
            final Tooltip tooltip = new Tooltip();
            String url = "icon/" + icon + ".png";
            Image image = new Image(url,50,50,true,true);
            ImageView iv = new ImageView(image);

            Button button = new Button(lat + "\n" + lat);
            button.setTextAlignment(TextAlignment.RIGHT);
            button.setGraphic(iv);
            button.setTooltip(tooltip);

            Tooltip.install(button,tooltip);
            return button;
        });
        worldMapView.getLocations().add(gettingLocation);
    }

//    public void setSearchableComboBoxChoiceTry(){
//        searchableCombo.getItems().clear();
//
//        ArrayList<ArrayList<String>> totalList = modelEngine.csv
//        ArrayList<String> settingList = new ArrayList<>();
//        if(Main.inputState.equals("online")){
//            for (int i = 0; i < totalList.size(); i++){
//                if (totalList.get(i).get(1).contains(textValue)){
//                    String addingString = totalList.get(i).get(4) + "," + totalList.get(i).get(2) + "," + totalList.get(i).get(1);
//                    settingList.add(addingString);
//                }
//            }
//            combo.setEditable(true);
//        }else{
//            String addingString = "1" + "," + "Paracel Islands" + "," + "HangZhou";
//            String addingString2 = "2,VIC,Melbourne";
//            settingList.add(addingString);
//            settingList.add(addingString2);
//        }
//
//        combo.getItems().addAll(settingList);
//    }


}