package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.util.Map;
import java.util.Set;

public abstract class MemoryInterface {

    //                 FIELDS
    private SITTBox tbox;
    // internal variables set though encode()
    private SITABox abox;
    private PerceptionBase<?> scene;

    //                 CONSTRUCTION
    public MemoryInterface(SITTBox tbox){
        this.tbox = tbox;
    }

    //                 GETTERS
    public SITTBox getTbox(){
        return tbox;
    }
    protected SITABox getAbox(){
        return abox;
    }
    protected PerceptionBase<?> getScene(){
        return scene;
    }

    //                 FUNCTIONALITY
    // LEARN: generate and STRUCTURES a new memory item from encoded data
    protected SceneHierarchyVertex learn(String sceneName, double initialScore){
        if (getScene().getObjects().isEmpty()){
            System.err.println( "WARNING: Scene cannot be learned with a scene having an empty object set");
            return null;
        }
        if( getScene().getRelations().isEmpty()){
            System.err.println( "WARNING: Scene cannot be learned with a scene having an empty relation set");
            return null;
        }
        SceneHierarchyVertex learnedScene = tbox.learn(sceneName, abox);
        learnedScene.setMemoryScore( initialScore);
        return learnedScene;
    }
    // RECOGNIZE: QUERY classified memory items from encoded data
    protected Map<SceneHierarchyVertex, Double> recognize(){
        return abox.getRecognitions();
    }


    //                 INTERFACE
    // ENCODE: recognize, to be called before than learn or recognize
    public Map<SceneHierarchyVertex, Double> encode(PerceptionBase<?> scene) {
        this.scene = scene;
        abox = new SITABox( tbox, scene.getObjects(), scene.getRelations());
        return abox.getRecognitions();
    }

    // TODO update function documentation
    // STORE: lear or update the scores (returns TRUE:learned, FALSE:recognized)
    protected abstract SceneHierarchyVertex store(String sceneName);
    // RETRIEVE: query the BEST scene and update score (returns TRUE: at least one score is updated, FALSE: no recognition)
    public abstract SceneHierarchyVertex retrieve();
    // CONSOLIDATE: update score items
    protected abstract void consolidate();
    // FORGET: remove weak scored items
    public abstract Set<SceneHierarchyVertex> forget();
}
