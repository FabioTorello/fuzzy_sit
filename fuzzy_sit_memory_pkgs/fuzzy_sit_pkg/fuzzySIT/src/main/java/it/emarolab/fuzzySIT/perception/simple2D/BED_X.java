package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class BED_X extends FeaturedSpatialObject<Point2> {


    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public BED_X(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////

    public BED_X(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public BED_X(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}