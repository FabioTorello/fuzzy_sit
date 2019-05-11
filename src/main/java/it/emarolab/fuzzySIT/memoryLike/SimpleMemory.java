package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.ListenableGraph;

import java.util.Map;

public class SimpleMemory extends MemoryInterface{

    private static final double ENCODE_TH = .5;

    public SimpleMemory(SITTBox tbox) {
        super(tbox);
    }

    @Override
    public void store(String sceneName) {
        Map<SceneHierarchyVertex, Double> rec = recognize();
        for ( SceneHierarchyVertex recognisedScene : rec.keySet()){
            double recognizedValue = rec.get( recognisedScene);
            if( recognizedValue > ENCODE_TH){ // update score

            } else { // learn
                learn( sceneName);
            }
        }
    }

    @Override
    public void forget() {
        // TODO to implement !!!!!
    }

    @Override
    public void consolidate() {
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        for( SceneHierarchyVertex scene : h.vertexSet()){
            scene.getMemoryScore();
        }
    }

    // TO DO retrieve() missing in interface
}
