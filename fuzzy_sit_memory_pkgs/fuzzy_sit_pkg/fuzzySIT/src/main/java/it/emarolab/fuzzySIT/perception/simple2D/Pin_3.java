package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class Pin_3 extends FeaturedSpatialObject<Point2> {
    //Constructors

    public Pin_3(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Pin_3(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}
