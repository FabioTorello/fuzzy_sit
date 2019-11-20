package it.emarolab.fuzzySIT.perception.simple2D;

/*import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import javafx.scene.chart.XYChart;

import java.util.HashSet;
import java.util.Set;

public class RelatedObjectOnScene extends PerceptionBase<Point2> {

    //Set of objects on scene
    public Set<ObjectOnScene> objects;



    public RelatedObjectOnScene(){}
    public RelatedObjectOnScene(String sceneName,Set<ObjectOnScene> objects) {
        super(sceneName);
        this.objects=objects;
    }



    // the name of the spatial relations used in this example (ζ)
    public static final String NEAR = "isNearTo";

    private static final double NEAR_THRESHOLD = 0.1; // meters (positive number)



    @Override
    protected SpatialRelation computeRelation(FeaturedSpatialObject<Point2> anObject, FeaturedSpatialObject<Point2>  newObject) {
        Point2 aFeature = anObject.getFeature();
        Point2 newFeature = newObject.getFeature();
        double connection = aFeature.distance( newFeature);
        if ( connection <= NEAR_THRESHOLD) {
            //double degree = 1 - (Math.abs( connection) / NEAR_THRESHOLD);
            double degree = 0.9;
            // [0.000000000000001,0.999999999999999] set with resolution ROLE_SHOULDER_RESOLUTION = "#.####"; and ROLE_SHOULDER_RESOLUTION = "#.####";
            //if ( degree >= 0.000000000000001 & degree <= .999999999999999)
                return new SpatialRelation(anObject.getObject(), NEAR, newObject.getObject(), degree);
            //else System.err.println("Error on computing fuzzy degree: 1-" + connection + "/" + CONNECTED_THRESHOLD + "=" + degree);
        }
        return null;
    }

    //AddObject calls computeRelation
    public void addObjectsToScene(Set<ObjectOnScene> objects) {
        for (ObjectOnScene single_object : objects) {
            //this.addObject(single_object.getType(), single_object.getObject(),single_object.getDegree(), single_object.getFeature());
        }
        /*if (single_object.getType()=="Plate") {
                this.addObject(single_object.getType(), single_object.getNewPlateInd() ,single_object.getDegree(), single_object.getFeature());
            }
            else if (single_object.getType()=="Glass"){
                this.addObject(single_object.getType(), single_object.getNewGlassInd() ,single_object.getDegree(), single_object.getFeature());
            }
            else if(single_object.getType()=="Fork"){
                this.addObject(single_object.getType(), single_object.getNewForkInd() ,single_object.getDegree(), single_object.getFeature());
            }
            else if(single_object.getType()=="Knife"){
                this.addObject(single_object.getType(), single_object.getNewKnifeInd() ,single_object.getDegree(), single_object.getFeature());
            }

    }
    }
}*/
