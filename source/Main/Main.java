package source.Main;

import javafx.application.Application;
import javafx.stage.Stage;
import source.View.FirstWindow;


public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
    
        FirstWindow firstWindow = new FirstWindow();
        firstWindow.showFirstWindow();

      
    }

  
    public static void showMainScene(Stage primaryStage) {
  
        FirstWindow firstWindow = new FirstWindow();
        firstWindow.showFirstWindow();

      
    }
}
