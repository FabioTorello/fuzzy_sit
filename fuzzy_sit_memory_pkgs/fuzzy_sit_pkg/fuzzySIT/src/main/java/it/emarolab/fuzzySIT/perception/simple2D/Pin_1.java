package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class Pin_1 extends FeaturedSpatialObject<Point2> {
    //Constructors

    public Pin_1(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Pin_1(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}