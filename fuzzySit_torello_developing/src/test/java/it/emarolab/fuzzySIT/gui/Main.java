package it.emarolab.fuzzySIT.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
/*
        FXMLLoader loader = new FXMLLoader( new File("src/test/resources/gui_layout.fxml").toURI().toURL());
        TabPane root = loader.load(); // controller was initialized
        Controller controller = loader.getController();
*/
        FXMLLoader loader = new FXMLLoader(new File("src/test/resources/gui_layout.fxml").toURI().toURL());
        loader.setController( new Controller());
        Controller controller =  loader.getController(); // return the controller
        TabPane root = loader.load();


        controller.addCanvasListeners();
        controller.addLoadBtnListener();
        controller.addSaveBtnListener();
        controller.addRecogniseButtonListener();
        controller.addLearnBtnListener();
       // controller.addTable();


        primaryStage.setTitle("Fuzzy SIT Test");
        primaryStage.setScene(new Scene(root, root.getPrefWidth(), root.getPrefHeight()));
        primaryStage.show();

        /*URL url = new File("src/test/resources/gui_layout.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);

        primaryStage.setTitle("Fuzzy SIT Test");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();


        FXMLLoader loader = new FXMLLoader(url);
        loader.setController( new Controller());
        Controller c =  loader.getController(); // return the controller

        System.out.println( " !!!!!!!!!!!!!!!!!!!!!!!!!!!! " + c + " " + url);

        // Clear away portions as the user drags the mouse
        c.addCanvasListeners();*/

    }

    public static void main(String[] args) {
        launch(args);
    }
}
