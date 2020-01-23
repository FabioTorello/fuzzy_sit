package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class NOT_Y  extends FeaturedSpatialObject<Point2> {

    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public NOT_Y(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////


    public NOT_Y (String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public NOT_Y (String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}