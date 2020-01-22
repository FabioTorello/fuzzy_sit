package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class CHAIR_MINUS_Y  extends FeaturedSpatialObject<Point2> {


    public CHAIR_MINUS_Y (String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public CHAIR_MINUS_Y (String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}