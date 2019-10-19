package it.emarolab;

import it.emarolab.fuzzySIT.memoryLike.MemoryInterface;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.ListenableGraph;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MemoryImplementation extends MemoryInterface {

    //Static variables and coefficients related to the memory functionalities
    private static String SCENE_PREFIX = "Scene";
    private static final double LEARNED_SCORE = .5;
    // threshold above which it consolidates
    private static final double ENCODE_TH = .5;
    // threshold under which it learns
    private static final double LEARN_TH = 1;
    // threshold under which it forgets (0,inf] (after consolidation (0,1])
    private static final double SCORE_WEAK = .1;

    // reinforce factor for re-stored or re-retrieved experience
    private static final double EXPERIENCE_REINFORCE = 1.5;
    //reinforce factor for bonus/malus from planner actions
    //TODO this value should be added to the final score of the scene used by planner (action's results correct-->bonus, otherwise -->malus) and it should not be added to the score updating during the retrieving or storing phases but in consolidating phase
    private double bonus_malus_reinforce=0;//TODO it is not "final" because the planner actions determine this value which has to be decided
    //TODO it is not "static" because when this value is modified every instance of this class would see the same value due to the fact it is shared among all the instances of the class

    // reinforce factor for min edge fuzzy degree
    private static final double EXPERIENCE_STRUCTURE = 1;

    //evaluation factor of the planner action //TODO degree of how the action correct or uncorrect is
    private boolean evaluation_factor=false;
    //coefficient considers if an action from planner happened //TODO Should be set from a signal
    private boolean hasHappenedAction=false;


    public MemoryImplementation(SITTBox tbox) {
        super(tbox);
    }

    @Override
    public SceneHierarchyVertex store(String sceneName){
        //Try to understand if the current scene has already been seen and recognized
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
    private double similarityValue(SceneHierarchyVertex recognisedScene){
        //double fuzzyness = FuzzySITBase.ROLE_SHOULDER_BOTTOM_PERCENT * recognisedScene.getDefinition().getCardinality() /100;
        //getDefinition: return the SIT scene individual definition formalised as SigmaCounters
        //getCardinality: the total sum of the fuzzy memberships of all the counters
        double actualCardinality = getAbox().getDefinition().getCardinality();
        double memoryCardinality = recognisedScene.getDefinition().getCardinality();
        double out = memoryCardinality / actualCardinality;
        if ( out > 1)
            System.err.println("WARNING: similarity value " + out + " for: " + getAbox().getDefinition() +"="+ actualCardinality + ", and " + recognisedScene.getDefinition() +"=" + memoryCardinality);
        return  out;
    }
    protected void updateScoreStoring(SceneHierarchyVertex recognisedScene, double recognisedValue) {
        updateScorePolicy(recognisedScene, recognisedValue,0,false);
    }
    private void updateScorePolicy(SceneHierarchyVertex recognisedScene, double recognisedValue, double bonus_malus_reinforce,boolean hasHappenedAction) {
        // TODO adjust and validate
        double score = recognisedScene.getMemoryScore();
        if ( recognisedScene.getMemoryScore() > 0) { // not froze node
            // reinforce for re-stored or re-retrieved experiences
            bonus_malus_reinforce=evaluationAction(hasHappenedAction);
            score += EXPERIENCE_REINFORCE * recognisedValue+bonus_malus_reinforce;
        } // else score freeze (i.e., experience to remove)
        recognisedScene.setMemoryScore( score);
    }


    @Override
    public SceneHierarchyVertex retrieve() {// TODO very minimal retrieve support, adjust and implement it better!
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
        updateScorePolicy( recognisedScene, recognisedValue,0, false);
    }

    @Override
    protected void consolidate() {
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        // TODO adjust and validate
        // reinforce based on graph edges
        for( SceneHierarchyVertex vertex : h.vertexSet()) {
            if ( vertex.getMemoryScore() > 0) { // not froze node
                double edgeMin = Double.POSITIVE_INFINITY;//edgeMean = 0; int cnt = 0;
                for (SceneHierarchyEdge edge : h.edgesOf( vertex)) {
                    if (h.getEdgeTarget( edge).equals( vertex)) {//if the target vertex of the current edge is equal to the current vertex considered
                        double wight = h.getEdgeWeight( edge);//Returns the weight assigned to a given edge. Unweighted graphs return 1.0
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
    public double evaluationAction (boolean hasHappenedAction){
        //if an action happened, it is evaluated otherwise there is not any bonus or malus
        if (hasHappenedAction) {
            if (evaluation_factor) {
                bonus_malus_reinforce = 1;
            }
        }
        else{
            return bonus_malus_reinforce=0;
        }
        return bonus_malus_reinforce;
    }

    @Override
    public Set<SceneHierarchyVertex> forget() {
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
