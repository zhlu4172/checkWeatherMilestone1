package au.edu.sydney.soft3202.majorProject.model;

import au.edu.sydney.soft3202.majorProject.Main;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class appSystem {

    public ArrayList<ArrayList<String>> readCSVFile() throws FileNotFoundException {
        String line = "";
        String splitBy = ",";
        ArrayList<ArrayList<String>> myList = new ArrayList<ArrayList<String>>();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/csvFile/cities_20000.csv", StandardCharsets.UTF_8));
            while ((line = br.readLine()) != null)   //returns a Boolean value
            {
                String[] element = line.split(splitBy);    // use comma as separator
                ArrayList<String> singleElement  = new ArrayList<>();
                for (int i = 0; i < element.length; i++){
                    singleElement.add(element[i]);
                }
                myList.add(singleElement);
            }
            java.lang.System.out.println(myList.get(2));
            return myList;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public ArrayList<Double> getLocationPair(String stateName, String cityName) throws FileNotFoundException {
        ArrayList<ArrayList<String>> gettingCSV = readCSVFile();
        ArrayList<Double> returnList = new ArrayList<>();
        for (int i = 0; i < gettingCSV.size(); i++){
            ArrayList<String> single = gettingCSV.get(i);
            String csvCityName = single.get(1);
            String csvStateCode = single.get(2);
            if(cityName.equals(csvCityName) && stateName.equals(csvStateCode)){
                System.out.println("inside");
                Double csvLat = Double.parseDouble(single.get(5));
                Double csvLon = Double.parseDouble(single.get(6));
                returnList.add(csvLat);
                returnList.add(csvLon);
            }
        }
        return returnList;
    }

    public ArrayList<String> getAllCities(ArrayList<ArrayList<String>> myList){
        ArrayList<String> cityList = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++){
            if (!cityList.contains(myList.get(i).get(1))){
                cityList.add(myList.get(i).get(1));
            }
        }
        return cityList;
    }

    public ArrayList<String> getCityDetails(ArrayList<String> pair) throws FileNotFoundException {
        ArrayList<ArrayList<String>> myList = readCSVFile();
        ArrayList<String> cityDetail = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++){
            if (myList.get(i).get(1).equals(pair.get(0)) && myList.get(i).get(4).equals(pair.get(1))){
                System.out.println("hi");
                cityDetail.add(myList.get(i).get(5));
                cityDetail.add(myList.get(i).get(6));
            }
        }
        return cityDetail;
    }

    public JsonObject getWeatherAPI(Double Lat, Double Lon){
        try{
            String uri = "https://api.weatherbit.io/v2.0/current?lat=" + Lat + "&lon=" + Lon + "&key=" + Main.inputKey + "&include=V";
            HttpRequest request = HttpRequest.newBuilder(new URI(uri))
                    .GET()
                    .build();

            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            JsonObject userPost = getUserPost(responseBody);
            System.out.println(userPost);
            getWeatherDetails(userPost);
            return userPost;
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }
    }


    public JsonObject getUserPost(String responseBody){
        Gson gson = new Gson();
        JsonObject userPost = gson.fromJson(responseBody, JsonObject.class);
        return userPost;
    }

    public ArrayList<String> getWeatherDetails(JsonObject userPost) throws FileNotFoundException {
        ArrayList<String> returnArrayList = new ArrayList<>();
        JsonArray dataArray = userPost.get("data").getAsJsonArray();
        JsonObject current = dataArray.get(0).getAsJsonObject();
        //add the city name
        String cityName = current.get("city_name").getAsString();
        String stateName = current.get("state_code").getAsString();
        returnArrayList.add(current.get("city_name").getAsString());
        //add the state name
        returnArrayList.add(current.get("state_code").getAsString());
        //add the temperature
        returnArrayList.add(current.get("temp").getAsString());
        //add wind speed
        returnArrayList.add(current.get("wind_spd").getAsString());
        //add wind direction
        returnArrayList.add(current.get("wind_cdir_full").getAsString());
        //add clouds
        returnArrayList.add(current.get("clouds").getAsString());
        //add the precipitation
        returnArrayList.add(current.get("precip").getAsString());
        //add the air quality
        returnArrayList.add(current.get("aqi").getAsString());
        //add icon symbol and weather description
        JsonObject icon = current.get("weather").getAsJsonObject();
        returnArrayList.add(icon.get("icon").getAsString());
        returnArrayList.add(icon.get("description").getAsString());

        ArrayList<Double> gettingPair = getLocationPair(stateName, cityName);
        returnArrayList.add(gettingPair.get(0).toString());
        returnArrayList.add(gettingPair.get(1).toString());
        System.out.println(returnArrayList);
        return returnArrayList;
    }

    public String setPasteWeatherDetails(ArrayList<String> detailList){
         String returnString = "CITY NAME  " + detailList.get(0) + "\nSTATE NAME  " + detailList.get(1) + "\nTEMPERATURE  " + detailList.get(2) + "\nWIND SPEED   " +
                 detailList.get(3) + "\nWIND DIRECTION   " + detailList.get(4) + "\nCLOUDS   " + detailList.get(5) + "\nPRECIPITATION   " + detailList.get(6) +
                "\nAIR QUALITY   " + detailList.get(7) + "\nDESCRIPTION   " + detailList.get(9) + "\n\n";
         return returnString;
    }

    public String postToOutputAPI(String pasteString){
//curl -X POST -d 'api_dev_key=UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6' -d 'api_paste_code=test' -d 'api_option=paste' "https://pastebin.com/api/api_post.php"

        try{
            String key = "api_dev_key=UZ5Tsp_Ex8PC9AZ6EZbqClVbu8GEZyW6";
            String input = "api_paste_code=" + pasteString;
            String user = "api_user_key=" + Main.outputKey;

            //e97fbae096357ae3c0798b707cba6a4a
            System.out.println(pasteString);
            String[] commands = new String[]{"curl", "-X", "POST", "https://pastebin.com/api/api_post.php","-d", key, "-d", user, "-d", input, "-d", "api_option=paste"};
            Process process = Runtime.getRuntime().exec(commands);
            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));
            String line;
            StringBuffer response = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            System.out.println(response);
            return response.toString();

        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }


    public ArrayList<String> addOffline(){
        ArrayList<String> returnArray = new ArrayList<>();
        returnArray.add("Hangzhou");
        returnArray.add("02");
        returnArray.add("19");
        returnArray.add("3");
        returnArray.add("northeast");
        returnArray.add("75");
        returnArray.add("0.151522");
        returnArray.add("187");
        returnArray.add("d02d");
        returnArray.add("Drizzle");
        return returnArray;
    }

    public String mockJsonObject(){
        return "{  \n" +
                "               \"data\":[  \n" +
                "                  {  \n" +
                "                     \"wind_cdir\":\"NE\",\n" +
                "                     \"rh\":59,\n" +
                "                     \"pod\":\"d\",\n" +
                "                     \"lon\":\"-78.63861\",\n" +
                "                     \"pres\":1006.6,\n" +
                "                     \"timezone\":\"America\\/New_York\",\n" +
                "                     \"ob_time\":\"2017-08-28 16:45\",\n" +
                "                     \"country_code\":\"US\",\n" +
                "                     \"clouds\":75,\n" +
                "                     \"vis\":10,\n" +
                "                     \"wind_spd\":6.17,\n" +
                "                     \"wind_cdir_full\":\"northeast\",\n" +
                "                     \"app_temp\":24.25,\n" +
                "                     \"state_code\":\"NC\",\n" +
                "                     \"ts\":1503936000,\n" +
                "                     \"h_angle\":0,\n" +
                "                     \"dewpt\":15.65,\n" +
                "                     \"weather\":{  \n" +
                "                        \"icon\":\"c03d\",\n" +
                "                        \"code\": 803,\n" +
                "                        \"description\":\"Broken clouds\"\n" +
                "                     },\n" +
                "                     \"uv\":2,\n" +
                "                     \"aqi\":45,\n" +
                "                     \"station\":\"CMVN7\",\n" +
                "                     \"wind_dir\":50,\n" +
                "                     \"elev_angle\":63,\n" +
                "                     \"datetime\":\"2017-08-28:17\",\n" +
                "                     \"precip\":0,\n" +
                "                     \"ghi\":444.4,\n" +
                "                     \"dni\":500,\n" +
                "                     \"dhi\":120,\n" +
                "                     \"solar_rad\":350,\n" +
                "                     \"city_name\":\"Raleigh\",\n" +
                "                     \"sunrise\":\"10:44\",\n" +
                "                     \"sunset\":\"23:47\",\n" +
                "                     \"temp\":24.19,\n" +
                "                     \"lat\":\"35.7721\",\n" +
                "                     \"slp\":1022.2\n" +
                "                  }\n" +
                "               ],\n" +
                "               \"minutely\":[ ... ],\n" +
                "               \"count\":1\n" +
                "            }";
    }

    public JsonObject getFakeJsonObject(){
        return getUserPost(mockJsonObject());
    }

}
