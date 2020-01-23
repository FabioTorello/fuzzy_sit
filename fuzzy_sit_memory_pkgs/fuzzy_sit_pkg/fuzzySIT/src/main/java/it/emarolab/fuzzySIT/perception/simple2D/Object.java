package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
//import javafx.scene.chart.XYChart;
import it.emarolab.fuzzySIT.perception.PerceptionBase;

import java.util.HashSet;
import java.util.Set;


public class Object extends FeaturedSpatialObject <Point2> {
    
    //Name of the object
    /*public static String name;
    // the name of individuals indicating objects in the scene
    //public static final String OBJECTONTABLE_IND_PREFIX = String.valueOf(name.charAt(0));
   // public static int objectOnTableCnt=0;
    public static double degree;*/
   // public static double xPose;
    //public static double yPose;


    // the name of the types of objects in this example (π)
    public static final String PLATE = "Plate";
    public static final String GLASS = "Glass";
    public static final String FORK = "Fork";
    public static final String KNIFE = "Knife";



    // the name of individuals indicating objects in the scene
    public static final String PLATE_IND_PREFIX = "P";
    public static final String GLASS_IND_PREFIX = "G";
    public static final String FORK_IND_PREFIX = "F";
    public static final String KNIFE_IND_PREFIX = "K";


    //Constructors

    public Object(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Object(String type, String object, Point2 feature) {
        super(type, object, feature);
    }


    // functions to get sequential individual names
    public static int plateCnt = 0, glassCnt = 0, forkCnt = 0, knifeCnt = 0;
    public static String getNewPlateInd(){
        return PLATE_IND_PREFIX + plateCnt++;
    }
    public static String getNewGlassInd(){
        return GLASS_IND_PREFIX + glassCnt++;
    }
    public static String getNewForkInd(){
        return FORK_IND_PREFIX + forkCnt++;
    }
    public static String getNewKnifeInd(){
        return KNIFE_IND_PREFIX + knifeCnt++;
    }







    
}
