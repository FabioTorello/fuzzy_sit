package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javafx.application.Application;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class ScenePlotter extends Application{

    private static final int WINDOW_SIZE = 700;
    // table dimension
    private static final double MIN_TABLE_BOUND = -1; // meters
    private static final double MAX_TABLE_BOUND = 1; // meters

    private static ConnectObjectScene showingScene;
    public static void setSITScene(ConnectObjectScene scene){
        ScenePlotter.showingScene = scene;
        try {
            launch();
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void start(Stage stage) {

        /*mainStage = stage;
        mainStage.setAlwaysOnTop(true);
        // the wumpus doesn't leave when the last stage is hidden.
        Platform.setImplicitExit(false);*/



        String title = "Scene Plotter";
        stage.setTitle(title);
        final NumberAxis xAxis = new NumberAxis(MIN_TABLE_BOUND, MAX_TABLE_BOUND, .1);
        final NumberAxis yAxis = new NumberAxis(MIN_TABLE_BOUND, MAX_TABLE_BOUND, .1);
        final ScatterChart<Number, Number> sc = new ScatterChart<>(xAxis, yAxis);
        xAxis.setLabel("");
        yAxis.setLabel("");
        sc.setTitle(title);

        XYChart.Series seriesLeg = new XYChart.Series();
        seriesLeg.setName(ConnectObjectScene.LEG);
        seriesLeg.getData().addAll(showingScene.getLegsPosition());

        XYChart.Series seriesPen = new XYChart.Series();
        seriesPen.setName(ConnectObjectScene.PEN);
        seriesPen.getData().addAll(showingScene.getPensPosition());

        XYChart.Series seriesTable = new XYChart.Series();
        seriesTable.setName(ConnectObjectScene.TABLE);
        seriesTable.getData().addAll(showingScene.getTablesPosition());

        XYChart.Series seriesScrewdriver = new XYChart.Series();
        seriesScrewdriver.setName(ConnectObjectScene.SCREWDRIVER);
        seriesScrewdriver.getData().addAll(showingScene.getScrewDrivers());

        sc.getData().addAll(seriesLeg, seriesPen, seriesTable, seriesScrewdriver);

        Scene scene = new Scene(sc, WINDOW_SIZE, WINDOW_SIZE);
        stage.setScene(scene);
        stage.show();
    }
}
