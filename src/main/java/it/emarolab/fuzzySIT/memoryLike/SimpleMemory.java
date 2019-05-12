package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.ListenableGraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleMemory extends MemoryInterface{

    private static String SCENE_PREFIX = "Scene";
    private static final double LEARNED_SCORE = .5;
    private static final double ENCODE_TH = .5; // threshold above which it consolidates
    private static final double LEARN_TH = 1; // threshold under which it learns
    private static final double SCORE_WEAK = 1;

    private static int sceneCnt = 0;

    public SimpleMemory(SITTBox tbox) {
        super(tbox);
    }

    public boolean store() {
        return store( SCENE_PREFIX + sceneCnt++);
    }

    @Override
    public boolean store(String sceneName){
        Map<SceneHierarchyVertex, Double> rec = recognize();
        // if memory is empty, learn new encoded scene
        if( rec.keySet().isEmpty()){
            learn(sceneName, LEARNED_SCORE);
            return true;
        }
        // if encoded scene can be recognized, update score
        boolean shouldLearn = true;
        for ( SceneHierarchyVertex recognisedScene : rec.keySet()){
            double recognisedValue = rec.get( recognisedScene);
            if( recognisedValue >= ENCODE_TH) // update score
                updateScoreStoring(recognisedScene);
            if( similarityValue(recognisedScene, recognisedValue) >= LEARN_TH)
                shouldLearn = false;
            /*if( recognizedValue >= LEARN_TH)
                shouldLearn = false;*/
        }
        // if encoded scene can be recognized, learn new scene
        if ( shouldLearn)
            learn(sceneName, LEARNED_SCORE);
        return shouldLearn;
    }
    protected void updateScoreStoring(SceneHierarchyVertex recognisedScene) {
        updateScorePolicy( recognisedScene);
    }
    private double similarityValue(SceneHierarchyVertex recognisedScene, double recognisedValue){
        //double fuzzyness = FuzzySITBase.ROLE_SHOULDER_BOTTOM_PERCENT * recognisedScene.getDefinition().getCardinality() /100;
        double actualCardinality = getAbox().getDefinition().getCardinality();
        double memoryCardinality = recognisedScene.getDefinition().getCardinality();
        double out = memoryCardinality / actualCardinality;
        if ( out > 1)
            System.err.println("WARNING: similarity value " + out + " for: " + getAbox().getDefinition() +"="+ actualCardinality + ", and " + recognisedScene.getDefinition() +"=" + memoryCardinality);
        return  out;
    }

    @Override
    public boolean retrieve() { // TODO very minimal retrieve support, adjust and implement it better!
        Map<SceneHierarchyVertex, Double> rec = recognize();
        // if memory is empty, do not do nothing
        if( ! rec.keySet().isEmpty()) {
            // update score of retrieved
            boolean updated = false;
            for (SceneHierarchyVertex recognisedScene : rec.keySet()) {
                updateScoreRetrieve(recognisedScene);
                if( ! updated)
                    updated = true;
            }
            return updated; // true if at least one score is updated
        }
        return false;
    }
    private void updateScoreRetrieve(SceneHierarchyVertex recognisedScene) {
        updateScorePolicy( recognisedScene);
    }

    private void updateScorePolicy(SceneHierarchyVertex recognisedScene) {
        recognisedScene.setMemoryScore( recognisedScene.getMemoryScore() + 1); // TODO adjust
    }

    @Override
    public void consolidate() {
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        // find max score in the memory graph
        double maxScore = 0;
        for( SceneHierarchyVertex scene : h.vertexSet()){
            double score = scene.getMemoryScore();
            if ( score > maxScore)
                maxScore = score;
        }
        if ( maxScore >= 0)
            normalizeScoreConsolidating( maxScore);
    }
    public void normalizeScoreConsolidating(double maxScore){
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> rec = getTbox().getHierarchy();
        for ( SceneHierarchyVertex experience : rec.vertexSet()){
            experience.setMemoryScore( experience.getMemoryScore() / maxScore);
        }
    }

    @Override
    public Set<SceneHierarchyVertex> forget(){
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        Set<SceneHierarchyVertex> forgotten = new HashSet<>();
        // find weak score in the memory graph
        for( SceneHierarchyVertex scene : h.vertexSet()){
            if( scene.getMemoryScore() < SCORE_WEAK) {
                scene.setMemoryScore(-1); // score getter will be always 0
                forgotten.add( scene);
            }
        }
        return forgotten;
    }
}
