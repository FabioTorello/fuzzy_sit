package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

public class DefineRelationsOnScene extends ConnectObjectScene {

    // the name of the spatial relations used in this example (ζ)
    public static final String NEAR = "isNearTo";
    public static final String INREGION = "isInRegion";

    private static final double NEAR_THRESHOLD = 0.53; // meters (positive number)
    private static final double INREGION_THRESHOLD = 0.25; // meters (positive number)

    //Constructor
    public DefineRelationsOnScene() {}
    public DefineRelationsOnScene(String sceneName) {
        super(sceneName);
    }

    @Override
    protected SpatialRelation computeRelation(FeaturedSpatialObject<Point2> anObject, FeaturedSpatialObject<Point2> newObject) {
        Point2 aFeature = anObject.getFeature();
        Point2 newFeature = newObject.getFeature();
        double computeDistance = aFeature.distance( newFeature);
        //Evaluate the type of the object
        if ((anObject instanceof Object) && (newObject instanceof Object) ){
            //Consider only the relation isInNear
            if ( computeDistance <= NEAR_THRESHOLD) {
                double degree = 1 - (Math.abs( computeDistance) / NEAR_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if ( degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(anObject.getObject(), NEAR, newObject.getObject(), degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }

        }
        else if ((anObject instanceof Object) && (newObject instanceof Region) ){
            //Consider only the relation isInRegion
            if ( computeDistance <= INREGION_THRESHOLD) {
                double degree = 1 - (Math.abs( computeDistance) / INREGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if ( degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(anObject.getObject(), INREGION, newObject.getObject(), degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }

        }
        else if ((anObject instanceof Region) && (newObject instanceof Object) ){
            if ( computeDistance <= NEAR_THRESHOLD) {
                //Consider only the relation isInRegion
                double degree = 1 - (Math.abs( computeDistance) / INREGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if ( degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(anObject.getObject(), INREGION, newObject.getObject(), degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }

        }
        else if ((anObject instanceof Region) && (newObject instanceof Region))
        {
            //Consider only the relation isInNear
            if ( computeDistance <= NEAR_THRESHOLD) {
                double degree = 1 - (Math.abs( computeDistance) / NEAR_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if ( degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(anObject.getObject(), NEAR, newObject.getObject(), degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }
        }
        else{
            return null;
        }

        return null;
    }
}
