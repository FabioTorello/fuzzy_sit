package it.emarolab.fuzzySIT.perception.simple2D;

/*import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import javafx.scene.chart.XYChart;
import java.util.HashSet;
import java.util.Set;

public class RelatedRegionOnScene extends PerceptionBase <Point2>{
    // the name of the types of objects in this example (π)
    public final String REGION1 = "R1";
    public final String REGION2 = "R2";
    public final String REGION3 = "R3";
    public final String REGION4 = "R4";
    public final String CENTRALREGION = "RC";

    //Set of regions on scene
    public Set<RegionOnScene> regions;
    //Number of regions in the scene
    public static int nRegions;

    // the name of the spatial relations used in this example (ζ)
    public static final String INREGION = "isInRegionTo";

    private static final double INREGION_THRESHOLD = 0.1; // meters (positive number)

    public RelatedRegionOnScene() {}
    public RelatedRegionOnScene(String sceneName, Set<RegionOnScene> regions, int nRegions) {
        super(sceneName);
        this.regions=regions;
        this.nRegions=nRegions;
    }

    public void addObjectsToScene(Set<ObjectOnScene> objects) {
        for (ObjectOnScene single_object : objects) {
            this.addObject(single_object.getType(), single_object.getObject(), single_object.getDegree(), single_object.getFeature());
        }
    }
    public void addElementsToScene(Set<RegionOnScene> regions, Set <ObjectOnScene> objects) {
        addObjectsToScene(objects);
        for (RegionOnScene single_region : regions) {
            this.addObject(single_region.getType(), single_region.getObject(), single_region.getDegree(), single_region.getFeature());
        }
    }

    @Override
    protected SpatialRelation computeRelation(FeaturedSpatialObject<Point2> object, FeaturedSpatialObject<Point2>  region) {
        double distanceFromRegionLimit = distanceForRegion(object.getFeature(),region.getFeature());
        //For Central Region
        if(object.getFeature().getX()>= 0 && (object.getFeature().getX() <= 0.5)){
            //if ( distanceFromRegionLimit <= INREGION_THRESHOLD) {
                //double degree = 1 - (Math.abs( distanceFromRegionLimit) / INREGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                //if ( degree >= 0.000000000000001 & degree <= .999999999999999)
            double degree=0.9;
                return new SpatialRelation(object.getObject(), INREGION, CENTRALREGION, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            //}
        }
        //For Region1
        else if ((object.getFeature().getX() >= -0.5 && object.getFeature().getX() <= 0) && (object.getFeature().getY() >= 0.5 && object.getFeature().getY() <= 1)) {
            if (distanceFromRegionLimit <= INREGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / INREGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION1, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }

        }
        //For Region2
        else if ((object.getFeature().getX() >= 0.5 && object.getFeature().getX() <= 1) && (object.getFeature().getY() >= 0.5 && object.getFeature().getY() <= 1)) {
            if (distanceFromRegionLimit <= INREGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / INREGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION2, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }
        }

        //For Region3
        else if ((object.getFeature().getX() >= -0.5 && object.getFeature().getX() <= 1) && (object.getFeature().getY() >= 0 && object.getFeature().getY() <= 0.5)) {
            if (distanceFromRegionLimit <= INREGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / INREGION_THRESHOLD);
                // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
                if (degree >= 0.000000000000001 & degree <= .999999999999999)
                    return new SpatialRelation(object.getObject(), INREGION, REGION3, degree);
                //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
            }
        }

        //For Region4
        else if ((object.getFeature().getX() >= 0.5 && object.getFeature().getX() <= 1) && (object.getFeature().getY() >= 0 && object.getFeature().getY() <= 0.5)) {
            if (distanceFromRegionLimit <= INREGION_THRESHOLD) {
                double degree = 1 - (Math.abs(distanceFromRegionLimit) / INREGION_THRESHOLD);
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

       public void addObjectsInRegions (Set <ObjectOnScene> objects, Set <RegionOnScene> regions){
        for (ObjectOnScene object: objects)
        {
            for (RegionOnScene region: regions) {
                computeRelation(object, region);
            }
       }
       }

    private double distanceForRegion (Point2 object, Point2 region){

        return Math.sqrt((object.getY() - region.getY()) * (object.getY() - region.getY()) + (object.getX() - region.getX()) * (object.getX() - region.getX()));
    }
}*/