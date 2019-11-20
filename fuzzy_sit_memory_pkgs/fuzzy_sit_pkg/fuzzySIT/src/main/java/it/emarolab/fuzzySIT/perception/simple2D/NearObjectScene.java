package it.emarolab.fuzzySIT.perception.simple2D;

/*import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import javafx.scene.chart.XYChart;

import java.util.HashSet;
import java.util.Set;


public class NearObjectScene extends PerceptionBase<Point2> {

    // the name of the types of objects in this example (π)
    public static final String PLATE = "Plate";
    public static final String FORK = "Fork";
    public static final String KNIFE = "Knife";
    public static final String GLASS = "Glass";
    // the name of the spatial relations used in this example (ζ)
    public static final String NEAR = "isNearTo";
    //TODO
    // the name of the spatial relations used in this example (ζ)
    public static final String INREGION = "isInRegion";
    //TODO
    // the name of the individuals indicating regions in the scene
    public static final String REGION1 = "R1";
    public static final String REGION2 = "R2";
    public static final String REGION3 = "R3";
    public static final String REGION4 = "R4";
    public static final String CENTRALREGION = "RC";
    private static final double REGION_THRESHOLD = 0.1; // meters (positive number)

    private static final double NEAR_THRESHOLD = 0.02; // meters (positive number)

    // the name of individuals indicating objects in the scene
    public static final String PLATE_IND_PREFIX = "P";
    public static final String FORK_IND_PREFIX = "F";
    public static final String KNIFE_IND_PREFIX = "K";
    public static final String GLASS_IND_PREFIX = "G";
    // functions to get sequential individual names
    private static int plateCnt = 0, forkCnt = 0, knifeCnt = 0, glassCnt = 0;
    private static String getNewPlateInd(){
        return PLATE_IND_PREFIX + plateCnt++;
    }
    private static String getNewForkInd(){
        return FORK_IND_PREFIX + forkCnt++;
    }
    private static String getNewKnifeInd(){
        return KNIFE_IND_PREFIX + knifeCnt++;
    }
    private static String getNewGlassInd(){
        return GLASS_IND_PREFIX + glassCnt++;
    }

    public NearObjectScene() {}
    public NearObjectScene(String sceneName) {
        super(sceneName);
    }

    @Override
    protected Set<SpatialRelation> computeRelation(FeaturedSpatialObject<Point2> anObject, FeaturedSpatialObject<Point2> newObject) {
        Set<SpatialRelation> relationsSet= new HashSet<SpatialRelation>();
        Point2 aFeature = anObject.getFeature();
        Point2 newFeature = newObject.getFeature();
        double near = aFeature.distance( newFeature);
        //if ((near >= NEAR_THRESHOLD && near >= NEAR_THRESHOLD + 0.1 ) && (aFeature.getX()!=newFeature.getX() || aFeature.getY()!=newFeature.getY())) {
        //double degree = 1 - (Math.abs( near) / NEAR_THRESHOLD) ;
        double degree = 0.2;
        // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
        if ( degree >= 0.000000000000001 & degree <= .999999999999999)
            relationsSet.add(new SpatialRelation(anObject.getObject(), NEAR, newObject.getObject(), degree));
        //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
        //}
        SpatialRelation relationInRegion = computeInRegionProperty(aFeature, anObject);
        if (relationInRegion==null){
            System.err.println("Error: object " + anObject + " is not in any regions on the table");
        }
        else {
            relationsSet.add(relationInRegion);
        }


        relationInRegion = computeInRegionProperty(newFeature, newObject);
        if (relationInRegion==null){
            System.err.println("Error: object " + newObject + " is not in any regions on the table");
        }
        else {
            relationsSet.add(relationInRegion);
        }


        //return null;
        return relationsSet;
    }


    //Function used to compute the spatial property "isInRegion"
    private SpatialRelation computeInRegionProperty(Point2 feature, FeaturedSpatialObject<Point2> object) {
        //For Central Region
        if (feature.getX() >= 0 && feature.getX() <= 0.5) {
            double distanceFromRegionLimit = distanceForRegion(feature, 0.5, 1);
            //if ( distanceFromRegionLimit <= REGION_THRESHOLD) {
            //double degree = 1 - (Math.abs( distanceFromRegionLimit) / REGION_THRESHOLD);
            // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
            //if ( degree >= 0.000000000000001 & degree <= .999999999999999)
            double degree = 0.2;
            return new SpatialRelation(object.getObject(), INREGION, CENTRALREGION, degree);
            //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            //}
        }

        //For Region1
        else if ((feature.getX() >= -0.5 && feature.getX() <= 0) && (feature.getY() >= 0.5 && feature.getY() <= 1)) {
            double distanceFromRegionLimit = distanceForRegion(feature, 0.5, 1);
            if (distanceFromRegionLimit <= REGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / REGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION1, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }

        }

        //For Region2
        else if ((feature.getX() >= 0.5 && feature.getX() <= 1) && (feature.getY() >= 0.5 && feature.getY() <= 1)) {
            double distanceFromRegionLimit = distanceForRegion(feature, 0.5, 1);
            if (distanceFromRegionLimit <= REGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / REGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION2, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }
        }

        //For Region3
        else if ((feature.getX() >= -0.5 && feature.getX() <= 1) && (feature.getY() >= 0 && feature.getY() <= 0.5)) {
            double distanceFromRegionLimit = distanceForRegion(feature, 0.5, 0.5);
            if (distanceFromRegionLimit <= REGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / REGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION3, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }
        }

        //For Region4
        else if ((feature.getX() >= 0.5 && feature.getX() <= 1) && (feature.getY() >= 0 && feature.getY() <= 0.5)) {
            double distanceFromRegionLimit = distanceForRegion(feature, 0.5, 0.5);
            if (distanceFromRegionLimit <= REGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / REGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION4, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }
        }
        else {
            return null;
        }
        return null;
    }



    private double distanceForRegion (Point2 p, double x, double y){

        return Math.sqrt((p.getY() - y) * (p.getY() - y) + (p.getX() - x) * (p.getX() - x));
    }

    public void addPlate( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( NearObjectScene.PLATE, NearObjectScene.getNewPlateInd(), degree, feature);
    }
    public void addFork( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( NearObjectScene.FORK, NearObjectScene.getNewForkInd(), degree, feature);
    }
    public void addKnife( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( NearObjectScene.KNIFE, NearObjectScene.getNewKnifeInd(), degree, feature);
    }
    public void addGlass( double xPose, double yPose, double degree){
        Point2 feature = new Point2(xPose,yPose);
        this.addObject( NearObjectScene.GLASS, NearObjectScene.getNewGlassInd(), degree, feature);
    }


    public Set<XYChart.Data> getPlatesPosition(){
        return getObjectPosition( PLATE);
    }
    public Set<XYChart.Data> getForksPosition(){
        return getObjectPosition( FORK);
    }
    public Set<XYChart.Data> getKnivesPosition(){
        return getObjectPosition( KNIFE);
    }
    public Set<XYChart.Data> getGlassesPosition(){
        return getObjectPosition( GLASS);
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
}*/
