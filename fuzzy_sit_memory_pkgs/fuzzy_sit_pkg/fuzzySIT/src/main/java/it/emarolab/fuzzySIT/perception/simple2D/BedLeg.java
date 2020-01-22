package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;

public class BedLeg extends FeaturedSpatialObject<Point2> {


        public BedLeg(String type, String object, double degree, Point2 feature) {
            super(type, object, degree, feature);

        }

        public BedLeg(String type, String object, Point2 feature) {
            super(type, object, feature);
        }
}

