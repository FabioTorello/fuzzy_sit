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

    protected void addObject(String type, String object, double degree, F feature){
        FeaturedSpatialObject<F> newObject = new FeaturedSpatialObject<>(type, object, degree, feature);
        for ( FeaturedSpatialObject<F> obj : getObjects()) {
            SpatialRelation rel = computeRelation(obj, newObject);
            if ( rel != null)
                relations.add(rel);
        }
        objects.add( newObject);
    }
    // add to `relations` all new facts from `object` and `newObject`
    protected abstract SpatialRelation computeRelation( FeaturedSpatialObject<F> anObject, FeaturedSpatialObject<F> newObject);

    public Set<FeaturedSpatialObject<F>> getObjects() {
        return objects;
    }
    public Set<SpatialRelation> getRelations() {
        return relations;
    }

    public String toString(){
        return "<<" + objects + ", " + relations + ">>";
    }


    // tutorial: implement feature Axis2
    // - Axis2 extends Point2
    // - Axis2 overrides distance function
    // - Copy ConnectObjectScene and rename as ParallelObjectScene
    // - adjust constants in ParallelObjectScene
}
