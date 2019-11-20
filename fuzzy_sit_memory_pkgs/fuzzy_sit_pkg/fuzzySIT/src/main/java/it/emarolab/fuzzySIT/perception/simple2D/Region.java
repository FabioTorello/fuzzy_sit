package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class Region extends FeaturedSpatialObject <Point2> {



    //TODO consider if the degree makes sense
    //Constructors

    public Region(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);


    }

    public Region(String type, String object, Point2 feature) {
        super(type, object, feature);
    }

}
