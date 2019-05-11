package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.util.Map;

public abstract class MemoryInterface<P> {

    //                 INTERFACE
    // ENCODE: find the BEST recognition
    //         to be called before than learn or recognize
    public Map<SceneHierarchyVertex, Double> encode(PerceptionBase<P> scene) {
        abox = new SITABox( tbox, scene.getObjects(), scene.getRelations());
        return abox.getRecognitions();
    }
    public abstract void store(String sceneName);          // STORE: lear or update the scores
    public abstract void forget();         // FORGET: remove weak scored items
    // public abstract void retrieve();    // RETRIEVE: query the BEST scene and update score // TODO to implement
    public abstract void consolidate();    // CONSOLIDATE: update score items

    //                 FIELDS AND CONSTRUCTION
    private SITTBox tbox;
    private SITABox abox;
    public MemoryInterface(SITTBox tbox){
        this.tbox = tbox;
    }
    public SITTBox getTbox(){
        return tbox;
    }


    //                 FUNCTIONALITY
    // LEARN: add and STRUCTURES a new memory item
    public void learn(String sceneName){
        tbox.learn( sceneName, abox);
    }
    // RECOGNIZE: QUERY sub-graph of memory items
    public Map<SceneHierarchyVertex, Double> recognize(){
        return abox.getRecognitions();
    }
}
