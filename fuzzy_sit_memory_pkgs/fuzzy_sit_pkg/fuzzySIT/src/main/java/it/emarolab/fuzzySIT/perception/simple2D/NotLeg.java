package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class NotLeg extends FeaturedSpatialObject<Point2> {

    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public NotLeg(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////

    public NotLeg(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public NotLeg(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}