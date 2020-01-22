package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class RoofLeg extends FeaturedSpatialObject<Point2> {


    public RoofLeg(String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public RoofLeg(String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}