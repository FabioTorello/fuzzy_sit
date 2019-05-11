package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

public class ConnectObjectScene extends PerceptionBase<Point2> {

    // the name of the types of objects in this example (π)
    public static final String LEG = "Leg";
    public static final String TABLE = "Table";
    public static final String SCREWDRIVER = "Screwdriver";
    public static final String PEN = "Pen";
    // the name of the spatial relations used in this example (ζ)
    public static final String CONNECTED = "isConnectedTo";
    public static final String NOT_CONNECTED = "isNotConnectedTo";

    private static final double CONNECTED_THRESHOLD = 0.3; // meters (positive number)

    // the name of individuals indicating objects in the scene
    public static final String LEG_IND_PREFIX = "L";
    public static final String TABLE_IND_PREFIX = "T";
    public static final String SCREWDRIVER_IND_PREFIX = "S";
    public static final String PEN_IND_PREFXI = "P";
    // functions to get sequential individual names
    private static int legCnt = 0, tableCnt = 0, screwDriverCnt = 0, penCnt = 0;
    private static String getNewLegInd(){
        return LEG_IND_PREFIX + legCnt++;
    }
    private static String getNewTableInd(){
        return TABLE_IND_PREFIX + tableCnt++;
    }
    private static String getNewScrewDriverInd(){
        return SCREWDRIVER_IND_PREFIX + screwDriverCnt++;
    }
    private static String getNewPenInd(){
        return PEN_IND_PREFXI + penCnt++;
    }

    @Override
    protected SpatialRelation computeRelation(FeaturedSpatialObject<Point2> anObject, FeaturedSpatialObject<Point2> newObject) {
        Point2 aFeature = anObject.getFeature();
        Point2 newFeature = newObject.getFeature();
        double connection = aFeature.distance( newFeature);
        if ( connection <= CONNECTED_THRESHOLD) {
            double degree = 1 - (Math.abs( connection) / CONNECTED_THRESHOLD);
            if ( degree >= 0 & degree <= 1)
                return new SpatialRelation(anObject.getObject(), CONNECTED, newObject.getObject(), degree);
            else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
        }
        return null;
    }

    public void addLeg( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( ConnectObjectScene.LEG, ConnectObjectScene.getNewLegInd(), degree, feature);
    }
    public void addTable( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( ConnectObjectScene.TABLE, ConnectObjectScene.getNewTableInd(), degree, feature);
    }
    public void addScrewDriver( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( ConnectObjectScene.SCREWDRIVER, ConnectObjectScene.getNewScrewDriverInd(), degree, feature);
    }
    public void addPen( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( ConnectObjectScene.PEN, ConnectObjectScene.getNewPenInd(), degree, feature);
    }
}
