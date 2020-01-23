package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class ChairLeg extends FeaturedSpatialObject<Point2> {

    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public ChairLeg(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////


    public ChairLeg(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public ChairLeg(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}

