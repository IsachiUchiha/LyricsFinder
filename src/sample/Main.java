package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    /**
     * Used for creating stage
     * @param primaryStage  Used for creating primary stage
     * @throws Exception    Throws exception
     */

    @Override
    public void start(Stage primaryStage) throws Exception{
//        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("sample.fxml"));
        Parent root=(Parent)loader.load();
        ((Controller)loader.getController()).setPrimaryStage(primaryStage);
        primaryStage.setTitle("Lyrics Finder");
        primaryStage.setScene(new Scene(root, 1015.0, 658.0));
        primaryStage.show();

    }


    /**
     * This is first method that is being called
     * @param args used for providing arguments to main function
     */

    public static void main(String[] args) {
        launch(args);
    }
}
