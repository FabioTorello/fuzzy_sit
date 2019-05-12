package it.emarolab.fuzzySIT.perception.simple2D;

import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import javafx.scene.chart.XYChart;

import java.util.HashSet;
import java.util.Set;

public class ConnectObjectScene extends PerceptionBase<Point2> {

    // the name of the types of objects in this example (π)
    public static final String LEG = "Leg";
    public static final String TABLE = "Table";
    public static final String SCREWDRIVER = "Screwdriver";
    public static final String PEN = "Pen";
    // the name of the spatial relations used in this example (ζ)
    public static final String CONNECTED = "isConnectedTo";

    private static final double CONNECTED_THRESHOLD = 0.1; // meters (positive number)

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

    public ConnectObjectScene() {}
    public ConnectObjectScene(String sceneName) {
        super(sceneName);
    }

    @Override
    protected SpatialRelation computeRelation(FeaturedSpatialObject<Point2> anObject, FeaturedSpatialObject<Point2> newObject) {
        Point2 aFeature = anObject.getFeature();
        Point2 newFeature = newObject.getFeature();
        double connection = aFeature.distance( newFeature);
        if ( connection <= CONNECTED_THRESHOLD) {
            double degree = 1 - (Math.abs( connection) / CONNECTED_THRESHOLD);
            // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
            if ( degree >= 0.000000000000001 & degree <= .999999999999999)
                return new SpatialRelation(anObject.getObject(), CONNECTED, newObject.getObject(), degree);
            //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
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


    public Set<XYChart.Data> getLegsPosition(){
        return getObjectPosition( LEG);
    }
    public Set<XYChart.Data> getTablesPosition(){
        return getObjectPosition( TABLE);
    }
    public Set<XYChart.Data> getPensPosition(){
        return getObjectPosition( PEN);
    }
    public Set<XYChart.Data> getScrewDrivers(){
        return getObjectPosition( SCREWDRIVER);
    }
    private Set<XYChart.Data> getObjectPosition( String type){
        Set<XYChart.Data> out = new HashSet<>();
        for( FeaturedSpatialObject obj : getObjects())
            if( obj.getType().equals( type)) {
                Point2 feature = (Point2) obj.getFeature();
                out.add( new XYChart.Data( feature.getX(),feature.getY()));
            }
        return out;
    }

}
