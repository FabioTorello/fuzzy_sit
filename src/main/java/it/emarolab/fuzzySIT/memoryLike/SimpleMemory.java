package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.ListenableGraph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SimpleMemory extends MemoryInterface{

    private static String SCENE_PREFIX = "Scene";
    private static final double LEARNED_SCORE = .5;
    private static final double ENCODE_TH = .5; // threshold above which it consolidates
    private static final double LEARN_TH = 1; // threshold under which it learns
    private static final double SCORE_WEAK = .2; // threshold under which it forgets (0,inf] (after consolidation (0,1])

    private static final double EXPERIENCE_REINFORCE = 1.5; // reinforce factor for re-stored or re-retrieved experience
    private static final double EXPERIENCE_STRUCTURE = 1; // reinforce factor for min edge fuzzy degree

    private static int sceneCnt = 0;

    public SimpleMemory(SITTBox tbox) {
        super(tbox);
    }

    public SceneHierarchyVertex store() {
        return store( SCENE_PREFIX + sceneCnt++);
    }

    @Override
    public SceneHierarchyVertex store(String sceneName){
        Map<SceneHierarchyVertex, Double> rec = recognize();
        // if memory is empty, learn new encoded scene
        if( rec.keySet().isEmpty())
            return learn(sceneName, LEARNED_SCORE);

        // if encoded scene can be recognized, update score
        boolean shouldLearn = true;
        for ( SceneHierarchyVertex recognisedScene : rec.keySet()){
            double recognisedValue = rec.get( recognisedScene);
            if( recognisedValue >= ENCODE_TH) // update score
                updateScoreStoring(recognisedScene, recognisedValue);
            if( similarityValue(recognisedScene) >= LEARN_TH)
                shouldLearn = false;
            /*if( recognizedValue >= LEARN_TH)
                shouldLearn = false;*/
        }
        // if encoded scene can be recognized, learn new scene
        if ( shouldLearn)
            return learn(sceneName, LEARNED_SCORE);
        return null;
    }
    protected void updateScoreStoring(SceneHierarchyVertex recognisedScene, double recognisedValue) {
        updateScorePolicy( recognisedScene, recognisedValue);
    }
    private double similarityValue(SceneHierarchyVertex recognisedScene){
        //double fuzzyness = FuzzySITBase.ROLE_SHOULDER_BOTTOM_PERCENT * recognisedScene.getDefinition().getCardinality() /100;
        double actualCardinality = getAbox().getDefinition().getCardinality();
        double memoryCardinality = recognisedScene.getDefinition().getCardinality();
        double out = memoryCardinality / actualCardinality;
        if ( out > 1)
            System.err.println("WARNING: similarity value " + out + " for: " + getAbox().getDefinition() +"="+ actualCardinality + ", and " + recognisedScene.getDefinition() +"=" + memoryCardinality);
        return  out;
    }

    @Override
    public SceneHierarchyVertex retrieve() { // TODO very minimal retrieve support, adjust and implement it better!
        Map<SceneHierarchyVertex, Double> rec = recognize();
        SceneHierarchyVertex out = null;
        double bestOut = 0;
        // if memory is empty, do not do nothing
        if( ! rec.keySet().isEmpty()) {
            // update score of retrieved
            for (SceneHierarchyVertex recognisedScene : rec.keySet()) {
                double recognisedValue = rec.get( recognisedScene);
                if ( recognisedValue > bestOut){
                    out = recognisedScene;
                    bestOut = recognisedValue;
                }
                updateScoreRetrieve(recognisedScene, recognisedValue);
            }
            return out; // true if at least one score is updated
        }
        return out;
    }
    private void updateScoreRetrieve(SceneHierarchyVertex recognisedScene, double recognisedValue) {
        updateScorePolicy( recognisedScene, recognisedValue);
    }

    private void updateScorePolicy(SceneHierarchyVertex recognisedScene, double recognisedValue) {
        // TODO adjust and validate
        double score = recognisedScene.getMemoryScore();
        if ( recognisedScene.getMemoryScore() > 0) { // not froze node
            // reinforce for re-stored or re-retrieved experiences
            score += EXPERIENCE_REINFORCE * recognisedValue;
        } // else score freeze (i.e., experience to remove)
        recognisedScene.setMemoryScore( score);
    }

    @Override
    public void consolidate() {
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        // TODO adjust and validate
        // reinforce based on graph edges
        for( SceneHierarchyVertex vertex : h.vertexSet()) {
            if ( vertex.getMemoryScore() > 0) { // not froze node
                double edgeMin = Double.POSITIVE_INFINITY;//edgeMean = 0; int cnt = 0;
                for (SceneHierarchyEdge edge : h.edgesOf( vertex)) {
                    if (h.getEdgeTarget( edge).equals( vertex)) {
                        double wight = h.getEdgeWeight( edge);
                        if ( edgeMin < wight)
                            edgeMin = wight;
                        //edgeMean += h.getEdgeWeight(edge); cnt++;
                    }
                }
                if (edgeMin != Double.POSITIVE_INFINITY)//(edgeMean > 0 & cnt > 0)
                    //vertex.setMemoryScore(vertex.getMemoryScore() * EXPERIENCE_STRUCTURE * edgeMean / cnt);
                    vertex.setMemoryScore(vertex.getMemoryScore() + EXPERIENCE_STRUCTURE * edgeMin);
            }
        }

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
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        for ( SceneHierarchyVertex experience : h.vertexSet()){
            if( experience.getMemoryScore() >= 0)
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
                //scene.setMemoryScore(-1); // score getter will be always 0 and is not consider on consolidation
                forgotten.add( scene);
            }
        }

        for( SceneHierarchyVertex scene : forgotten)
            getTbox().removeScene( scene);

        return forgotten;
    }
}
