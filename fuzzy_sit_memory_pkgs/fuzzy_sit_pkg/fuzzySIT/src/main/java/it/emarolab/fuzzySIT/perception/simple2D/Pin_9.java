package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class Pin_9 extends FeaturedSpatialObject<Point2> {
    //Constructors

    /////////NEW CONSTRUCTOR//////////////////////////////////////
    public Pin_9(String type, String object, double degree) {
        super(type, object, degree);

    }
    /////////////////////////////////////////////////////////////

    public Pin_9(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Pin_9(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}
