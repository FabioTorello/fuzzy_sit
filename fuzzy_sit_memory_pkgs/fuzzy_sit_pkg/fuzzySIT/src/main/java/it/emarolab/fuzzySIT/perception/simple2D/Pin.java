package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import javafx.scene.chart.XYChart;
import it.emarolab.fuzzySIT.perception.PerceptionBase;

import java.util.HashSet;
import java.util.Set;


public class Pin extends FeaturedSpatialObject <Point2> {
    //Constructors

    public Pin(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Pin(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}
