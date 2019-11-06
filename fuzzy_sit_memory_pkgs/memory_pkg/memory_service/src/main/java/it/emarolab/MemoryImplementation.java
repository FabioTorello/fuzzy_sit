package it.emarolab;

import it.emarolab.fuzzySIT.memoryLike.MemoryInterface;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.ListenableGraph;
import java.io.*;
import java.util.*;


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
    private double bonus_malus_reinforce=0.5; //TODO it is not "final" because the planner actions determine this value which has to be decided
    //TODO it is not "static" because when this value is modified every instance of this class would see the same value due to the fact it is shared among all the instances of the class

    // reinforce factor for min edge fuzzy degree
    private static final double EXPERIENCE_STRUCTURE = 1;

    //evaluation factor of the planner action //TODO degree of how the action correct or uncorrect is
    private boolean evaluation_factor=false;
    //coefficient considers if an action from planner happened //TODO Should be set from a signal
    private boolean hasHappenedAction=false;
    //Scene Counter
    private static int sceneCnt = 0;
    //The Finite State Machine will decide the syncronitation of the consolidating and forgetting operations
    private boolean synchConsolidateForget=false;
    //File containing the graph information
    private File fileName= new File("memory_service/files/GraphInformation.txt");
    //Time instant
    private static long time_instant = 0;
    //Define variables to take into account the time consumption
    //private List<Timing> timings = new ArrayList<>();
    //private Timing timing;

    //Constructor
    public MemoryImplementation(String tboxPath) {
        super(new SITTBox(tboxPath));
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
    public SceneHierarchyVertex store() {
        return store( SCENE_PREFIX + sceneCnt++);
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
            score += EXPERIENCE_REINFORCE * recognisedValue + bonus_malus_reinforce;
        } // else score freeze (i.e., experience to remove)
        recognisedScene.setMemoryScore( score);
    }
    public void experience(PerceptionBase scene, boolean storeOrRetrieve, boolean synchConsolidateForget){ // true: from store, false: from retrieve

        //timing = new Timing();

        // must always be done before to store or retrieve
        //long initialTime = System.nanoTime();
        encode( scene);
        //timing.encodingTime = System.nanoTime() - initialTime;
        System.out.println( "[ ENCODE ]\texperience: " + scene);
        // set store or retrieve cases

        String logs;
        SceneHierarchyVertex learnedOrRetrievedScene;
        //initialTime = System.nanoTime();
        if ( storeOrRetrieve) {
            if ( scene.getSceneName().isEmpty())
                learnedOrRetrievedScene = store();
            else learnedOrRetrievedScene = store( scene.getSceneName());
            //timing.storingTime = System.nanoTime() - initialTime;
            logs = "storing";
            //If the returned graph is not empty (thus there is only the root node)
            if( learnedOrRetrievedScene != null)
                System.out.println( "[  LEARN ]\texperience: " + learnedOrRetrievedScene);
        } else {
            learnedOrRetrievedScene = retrieve();
            //timing.retrievingTime = System.nanoTime() - initialTime;
            logs = "retrieving";
            System.out.println( "[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
        }
        // synchronous consolidation and forgetting
        if ( learnedOrRetrievedScene != null & synchConsolidateForget) {
            consolidateAndForget(scene, logs);
        }
        System.out.println( "[ RECOGN.]\texperience: " + recognize());
        //System.out.println( "     Time spent " + timing);
        //timings.add( timing);
        System.out.println( "----------------------------------------------");
    }

    public void consolidateAndForget(){
        consolidateAndForget( null);
    }
    public void consolidateAndForget( PerceptionBase scene){
        consolidateAndForget( scene, "external call");
    }
    private void consolidateAndForget( PerceptionBase scene, String logs){
        //long initialTime = System.nanoTime();
        consolidate();
        //timing.consolidateTime = System.nanoTime() - initialTime;
        System.out.println( "[ CONSOL.]\tnew experience from " + logs + " " + scene + " -> ");

        //initialTime = System.nanoTime();
        Set<SceneHierarchyVertex> forgotten = forget();

        //timing.forgetTime = System.nanoTime() - initialTime;
        System.out.println( "[ FORGET ]\tfreeze nodes: " + forgotten);

        //Print the information about the graph in a text file
        MemoryFile();
    }


    @Override
    public SceneHierarchyVertex retrieve() {// TODO very minimal retrieve support, adjust and implement it better!
        //query of classifications of a scene --> it's a graph
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
            //TODO here there is the evaluation of the action, thus the setting of the bonus_malus factor by basing on that
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
            getTbox().removeScene(scene);


        return forgotten;
    }

    //Function used to print the memory graph information in a text file
    private void MemoryFile(){
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> GraphAfterForgettingOperation = getTbox().getHierarchy();
        time_instant++;
        //Open the file
        PrintWriter outpustream = null;
        try{
            if (!fileName.exists()){
                //Create the file
                try {
                    fileName.createNewFile();
                }catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            //Clear the content of the text file //TODO only for testing in the end you have to remove this line
            //new PrintWriter(fileName).close();
            //Now you can write on it
            outpustream = new PrintWriter(new FileOutputStream(fileName,true));
            //Write on the text file

        } catch (FileNotFoundException e){
            System.err.println("Error opening the file" + "GraphInformation.txt");
            System.exit(0);
        }
        outpustream.println("\n" + "/////////////////////// " + "Time Instant: " + time_instant + " /////////////////////// ");
        //outpustream.println("\n");
        outpustream.println("\n" + "NODES IN THE MEMORY: " + GraphAfterForgettingOperation.vertexSet());
        //Loop on all the vertices in the graph
        for( SceneHierarchyVertex sourceVertices : GraphAfterForgettingOperation.vertexSet()){
            //Loop on all the edges touching the specified vertex
            for (SceneHierarchyEdge edges: GraphAfterForgettingOperation.edgesOf(sourceVertices)) {
                if (sourceVertices != GraphAfterForgettingOperation.getEdgeTarget(edges)) {
                    //SOURCE VERTEX INFORMATION
                    outpustream.println("\n" + "---------------------------------------------------------------");
                    //Write on the text file (PRINT CORRECTLY IN THE FILE)
                    outpustream.println(sourceVertices.getScene() + " LINKED TO " +
                            GraphAfterForgettingOperation.getEdgeTarget(edges).getScene() +
                            " With FUZZY DEGREE " + GraphAfterForgettingOperation.getEdgeWeight(edges));
                    //Source vertex Information
                    outpustream.println(" " + sourceVertices.getScene() + " Information:");
                    outpustream.println("   " + "N° OBJECTS: " + sourceVertices.getObjectNumber());
                    outpustream.println("   " + "OBJECT TYPES WITH FUZZY DEGREE: " + sourceVertices.getObjectDistribution());
                    outpustream.println("   " + "SCENE DESCRIPTION: " + sourceVertices.getDefinition());


                    //TARGET VERTEX INFORMATION
                    outpustream.println("\n" + " " + GraphAfterForgettingOperation.getEdgeTarget(edges).getScene() + " Information:");
                    outpustream.println("   " + "N° OBJECTS: " + GraphAfterForgettingOperation.getEdgeTarget(edges).getObjectNumber());
                    outpustream.println("   " + "OBJECT TYPES WITH FUZZY DEGREE: " + GraphAfterForgettingOperation.getEdgeTarget(edges).getObjectDistribution());
                    outpustream.println("   " + "SCENE DESCRIPTION: " + GraphAfterForgettingOperation.getEdgeTarget(edges).getDefinition());
            }/*

                //Weight assigned to a given edge
                GraphAfterForgettingOperation.getEdgeWeight(edges);
                //TODO I have to extract also the Region relate to the scene

                //Number of the objects in the scene category
                int numberObjectsSource = sourceVertices.getObjectNumber();
                //the object types distribution maps (Ψ)
                Collection<Map<String, Double>>pippo=sourceVertices.getObjectDistribution();
                sceneName + "<<" + sceneToPrint.getObjects() + ", " + sceneToPrint.getRelations() + ">>";
                // Target vertex of an edge
                SceneHierarchyVertex targetVertex = GraphAfterForgettingOperation.getEdgeTarget(edges);
                //TARGET VERTEX INFORMATION
                //Number of the objects in the scene category
                int numberObjectsTarget = targetVertex.getObjectNumber();
                //the object types distribution maps (Ψ)
                Collection<Map<String, Double>>pippo2=sourceVertices.getObjectDistribution();
                sceneName + "<<" + sceneToPrint.getObjects() + ", " + sceneToPrint.getRelations() + ">>";
                //TODO extract the information about the relations and objects of the source vertex and target vertex
*/            }

        }
        //Close the file
        outpustream.close();
    }


}
