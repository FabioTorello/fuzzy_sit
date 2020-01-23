package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class Pin_10 extends FeaturedSpatialObject<Point2> {
    //Constructors

    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public Pin_10(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////

    public Pin_10(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Pin_10(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}
