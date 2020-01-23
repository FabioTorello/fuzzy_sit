package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class NOT_MINUS_X  extends FeaturedSpatialObject<Point2> {

    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public NOT_MINUS_X(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////

    public NOT_MINUS_X (String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public NOT_MINUS_X (String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}