package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class ROOF_X  extends FeaturedSpatialObject<Point2> {


    public ROOF_X (String type, String object, double degree, Point2 feature) {
        super(type, object, degree, feature);

    }

    public ROOF_X (String type, String object, Point2 feature) {
        super(type, object, feature);
    }
}