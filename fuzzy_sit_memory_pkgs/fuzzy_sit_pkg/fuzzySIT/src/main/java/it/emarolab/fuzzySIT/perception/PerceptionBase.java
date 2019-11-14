package it.emarolab.fuzzySIT.perception;

import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

import java.util.HashSet;
import java.util.Set;

public abstract class PerceptionBase<F> {

    private Set<FeaturedSpatialObject<F>> objects = new HashSet<>(); // the set of objects in the SIT scene to test
    private Set<SpatialRelation> relations = new HashSet<>(); // the set of relations in the SIT scene to test

    public PerceptionBase() {}
    public PerceptionBase(String sceneName) {
        this.sceneName = sceneName;
    }

    private String sceneName = ""; // if it is empty use sequential naming
    public String getSceneName() {
        return sceneName;
    }
    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }
    //TODO This version of addObject is for the property ConnectedObjectScene.java
    /*protected void addObject(String type, String object, double degree, F feature){
        FeaturedSpatialObject<F> newObject = new FeaturedSpatialObject<>(type, object, degree, feature);
        for ( FeaturedSpatialObject<F> obj : getObjects()) {
            SpatialRelation rel = computeRelation(obj, newObject);
            if ( rel != null)
                relations.add(rel);
        }
        objects.add( newObject);
    }*/
//TODO This version of addObject is for the property NearObjectScene.java
    protected void addObject(String type, String object, double degree, F feature){
        FeaturedSpatialObject<F> newObject = new FeaturedSpatialObject<>(type, object, degree, feature);
        for ( FeaturedSpatialObject<F> obj : getObjects()) {
            Set<SpatialRelation>  rel = computeRelation(obj, newObject);
            if ( rel != null)
                for (SpatialRelation rels: rel)
                relations.add(rels);
        }
        objects.add( newObject);
    }
    // add to `relations` all new facts from `object` and `newObject`
    //TODO This version of computeRelation is for the property ConnectedObjectScene.java
      //protected abstract SpatialRelation computeRelation( FeaturedSpatialObject<F> anObject, FeaturedSpatialObject<F> newObject);
    //TODO This version of computeRelation is for the property NearObjectScene.java
     protected abstract Set<SpatialRelation>  computeRelation( FeaturedSpatialObject<F> anObject, FeaturedSpatialObject<F> newObject);

    public Set<FeaturedSpatialObject<F>> getObjects() {
        return objects;
    }
    public Set<SpatialRelation> getRelations() {
        return relations;
    }

    public String toString(){
        return  sceneName + "<<" + objects + ", " + relations + ">>";
    }


    // tutorial: implement feature Axis2
    // - Axis2 extends Point2
    // - Axis2 overrides distance function
    // - Copy ConnectObjectScene and rename as ParallelObjectScene
    // - adjust constants in ParallelObjectScene
}
