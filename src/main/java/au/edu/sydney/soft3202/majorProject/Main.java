package au.edu.sydney.soft3202.majorProject;

import au.edu.sydney.soft3202.majorProject.controller.mainPageController;
import au.edu.sydney.soft3202.majorProject.model.appSystem;
import au.edu.sydney.soft3202.majorProject.model.sqlModel;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class Main extends Application{
    public static appSystem weather;
    public static sqlModel sqlDBModel;
    public static String inputKey;
    public static String outputKey;
    public static String inputState;
    public static String outputState;

    @Override
    public void start(Stage primaryStage) throws Exception {
        fetchEnvironmentVariable();
//        weather = new appSystem();
//        sqlDBModel = new sqlModel();
        System.out.println(inputKey);

        //call the setup database function
//        setupDataBase();
        FXMLLoader mainPage = new FXMLLoader(getClass().getResource("/view/mainPage.fxml"));
        Parent root = mainPage.load();
        mainPageController mainPageController = mainPage.getController();
        mainPageController.setVersion(inputState,outputState);
//        mainPageController.setupDataBase(sqlDBModel);
        Bindings.bindBidirectional(mainPageController.getZoom().valueProperty(), mainPageController.getWorldMapView().zoomFactorProperty());
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("WeatherBit App");
        primaryStage.show();
    }

    public static void main(String[] args) {
        setState(args);
        launch(args);
    }

    public static void fetchEnvironmentVariable(){
        inputKey = System.getenv("INPUT_API_KEY");
        outputKey = System.getenv("PASTEBIN_API_KEY");
    }

    public void setupDataBase(){
        sqlDBModel.removeDB();
        sqlDBModel.createDB();
        sqlDBModel.setupDB();
    }

    public static void setState(String[] args){
        if (args.length == 0){
            inputState = "online";
            outputState = "online";
        }else{
            inputState = args[0];
            outputState = args[1];
        }
    }
}

