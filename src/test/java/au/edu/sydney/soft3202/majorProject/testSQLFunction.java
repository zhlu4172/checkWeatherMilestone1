//package au.edu.sydney.soft3202.majorProject;
//
//import au.edu.sydney.soft3202.majorProject.model.sqlModel;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.io.File;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
////import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//public class testSQLFunction {
//
//    private sqlModel sql;
//    private File dbFile;
//
//    @BeforeEach
//    public void setUp(){
//        sql = new sqlModel();
//        dbFile = new File("cityWeather.db");
//        if(dbFile.exists()){
//            dbFile.delete();
//        }
////        dbFile.delete();
//    }
//
////    @Test
////    public void testCreateDB(){
////        assertFalse(dbFile.exists());
////        sql.createDB();
////        assertTrue(dbFile.exists());
////
////    }
//
//    @Test
//    public void testAddAndQuery(){
////        assertThrows(SQLException.class, () -> sql.addWeather("Sydney","02",19.4, 1.34927, "northwest", 100, 4.42038, 53, "r02d", "Moderate rain"));
//        sql.createDB();
//        sql.setupDB();
//        try{
//            sql.addWeather("Sydney","02",19.4, 1.34927, "northwest", 100, 4.42038, 53, "r02d", "Moderate rain");
//            assertEquals(singleExample(),sql.queryWeather());
//        }catch (SQLException e){
//            assertEquals(SQLException.class, e);
//        }
//    }
//
//    @Test
//    public void testMixSituation(){
//        //test setUpDB
//        assertThrows(SQLException.class, () -> sql.addWeather("Sydney","02",19.4, 1.34927, "northwest", 100, 4.42038, 53, "r02d", "Moderate rain"));
//
//        sql.createDB();
//        sql.setupDB();
//        try{
//            //test add
//            sql.addWeather("Sydney","02",19.4, 1.34927, "northwest", 100, 4.42038, 53, "r02d", "Moderate rain");
//            sql.addWeather("Los Angeles","CA",14.2,1.52177,"east",14,0.0,74,"c02n","Few clouds");
//            sql.addWeather("Melbourne","07",21.1,2.20657,"north-northeast",100,0.0,41,"c04n","Overcast clouds");
//            ArrayList<ArrayList<String>> gettingQueryResults = sql.queryWeather();
//            System.out.println("query is" + gettingQueryResults);
//            ArrayList<ArrayList<String>> expectedResults = severalExample();
//            System.out.println("expected is " + expectedResults);
//            assertTrue(expectedResults.size() == gettingQueryResults.size() && expectedResults.containsAll(gettingQueryResults) && gettingQueryResults.containsAll(expectedResults));
//
//            //test delete
//            sql.deleteDB("2");
//            ArrayList<String> firstAdding = new ArrayList<>();
//            String[] list = new String[]{"Los Angeles","CA","14.2","1.52177","east","14.0","0.0","74","c02n","Few clouds","2"};
//            firstAdding.addAll(List.of(list));
//            expectedResults.remove(firstAdding);
//            gettingQueryResults = sql.queryWeather();
//            assertTrue(expectedResults.size() == gettingQueryResults.size() && expectedResults.containsAll(gettingQueryResults) && gettingQueryResults.containsAll(expectedResults));
//
//            //test clear;
//            sql.clearDB();
//            expectedResults.clear();
//            gettingQueryResults = sql.queryWeather();
//            assertTrue(expectedResults.size() == gettingQueryResults.size() && expectedResults.containsAll(gettingQueryResults) && gettingQueryResults.containsAll(expectedResults));
//
//        }catch (SQLException e){
//            assertEquals(SQLException.class, e);
//        }
////        dbFile.delete();
//    }
//
//
//    public ArrayList<ArrayList<String>> singleExample(){
//        ArrayList<ArrayList<String>> returnArraylist2D = new ArrayList<>();
//        ArrayList<String> returnArrayList = new ArrayList<>();
//        returnArrayList.add("Sydney");
//        returnArrayList.add("02");
//        returnArrayList.add("19.4");
//        returnArrayList.add("1.34927");
//        returnArrayList.add("northwest");
//        returnArrayList.add("100.0");
//        returnArrayList.add("4.42038");
//        returnArrayList.add("53");
//        returnArrayList.add("r02d");
//        returnArrayList.add("Moderate rain");
//        returnArrayList.add("1");
//        returnArraylist2D.add(returnArrayList);
//        return returnArraylist2D;
//    }
//
//    public ArrayList<ArrayList<String>> severalExample(){
//        ArrayList<ArrayList<String>> returnedList = singleExample();
//        ArrayList<String> firstAdding = new ArrayList<>();
//        String[] list = new String[]{"Los Angeles","CA","14.2","1.52177","east","14.0","0.0","74","c02n","Few clouds","2"};
//        firstAdding.addAll(List.of(list));
//        returnedList.add(firstAdding);
//        ArrayList<String> secondAdding = new ArrayList<>();
//        String[] list2 = new String[]{"Melbourne", "07", "21.1", "2.20657", "north-northeast", "100.0", "0.0", "41", "c04n", "Overcast clouds","3"};
//        secondAdding.addAll(List.of(list2));
//        returnedList.add(secondAdding);
////        System.out.println(returnedList);
//        return returnedList;
//    }
//}
