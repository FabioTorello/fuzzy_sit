package it.emarolab;

import it.emarolab.fuzzySIT.memoryLike.MemoryInterface;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.ListenableGraph;
import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MemoryImplementation extends MemoryInterface {

    //Define variables to take into account the time consumption
    private List<Timing> timings = new ArrayList<>();
    private Timing timing;
    //graph of the memory used to find the number of memory items
    ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graphOfMemory;
    private List<ElementsOfMemory> elements = new ArrayList<>();
    private ElementsOfMemory element;

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

    // reinforce factor for min edge fuzzy degree
    private static final double EXPERIENCE_STRUCTURE = 1;
    //Scene Counter
    private static int sceneCnt = 0;

    //File containing the graph information
    private File fileName= new File("memory_service/files/GraphInformation.txt");
    //CSV file for encoding time
    private File fileCSVEncoding= new File("memory_service/files/EncodingTime_MemoryItems.csv");
    //CSV file for storing time
    private File fileCSVStoring= new File("memory_service/files/StoringTime_MemoryItems.csv");
    //CSV file for retrieving time
    private File fileCSVRetrieving= new File("memory_service/files/RetrievingTime_MemoryItems.csv");
    //CSV file for consolidating time
    private File fileCSVConsolidating= new File("memory_service/files/ConsolidatingTime_MemoryItems.csv");
    //CSV file for forgetting time
    private File fileCSVForgetting= new File("memory_service/files/ForgettingTime_MemoryItems.csv");

    //Time instant
    private static long time_instant = 0;

    //private List<Timing> timings = new ArrayList<>();
    //private Timing timing;

    //Constructor
    public MemoryImplementation(String tboxPath, String configPath) {
        super(new SITTBox(tboxPath, configPath));
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
            score += EXPERIENCE_REINFORCE * recognisedValue;
        } // else score freeze (i.e., experience to remove)
        recognisedScene.setMemoryScore( score);
    }
    public void experience(PerceptionBase scene, boolean storeOrRetrieve, boolean synchConsolidateForget){ // true: from store, false: from retrieve



        timing = new Timing();
        element = new ElementsOfMemory();

        // must always be done before to store or retrieve
        long initialTime = System.nanoTime();
        encode( scene);
        timing.encodingTime = System.nanoTime() - initialTime;
        //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
        //graphOfMemory = getTbox().getHierarchy().vertexSet().size();
        element.encodingElements=NumberOfElementInMemory(graphOfMemory);
        System.out.println( "[ ENCODE ]\texperience: " + scene);
        // set store or retrieve cases

        String logs;
        SceneHierarchyVertex learnedOrRetrievedScene;
        initialTime = System.nanoTime();
        if ( storeOrRetrieve) {
            if ( scene.getSceneName().isEmpty())
                learnedOrRetrievedScene = store();
            else learnedOrRetrievedScene = store( scene.getSceneName());
            timing.storingTime = System.nanoTime() - initialTime;
            //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
            graphOfMemory = getTbox().getHierarchy();
            element.storingElements=NumberOfElementInMemory(graphOfMemory);
            logs = "storing";
            //If the returned graph is not empty (thus there is only the root node)
            if( learnedOrRetrievedScene != null)
                System.out.println( "[  LEARN ]\texperience: " + learnedOrRetrievedScene);
        } else {
            learnedOrRetrievedScene = retrieve();
            timing.retrievingTime = System.nanoTime() - initialTime;
            //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
            graphOfMemory = getTbox().getHierarchy();
            element.retrievingElements=NumberOfElementInMemory(graphOfMemory);
            logs = "retrieving";
            System.out.println( "[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
        }
        // synchronous consolidation and forgetting
        if ( learnedOrRetrievedScene != null & synchConsolidateForget) {
            consolidateAndForget(scene, logs);
        }
        System.out.println( "[ RECOGN.]\texperience: " + recognize());
        System.out.println( "     Time spent " + timing);
        System.out.println( "     Elements in memory " + element);
        timings.add( timing);
        elements.add( element);
        //Print the information of the different times and the memory items in different moments
        convertToCSV(timings, elements);

        System.out.println( "----------------------------------------------");
    }

    public void consolidateAndForget(){
        consolidateAndForget( null);
    }
    public void consolidateAndForget( PerceptionBase scene){
        consolidateAndForget( scene, "external call");
    }
    private void consolidateAndForget( PerceptionBase scene, String logs){
        long initialTime = System.nanoTime();
        consolidate();
        timing.consolidateTime = System.nanoTime() - initialTime;
        //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
        graphOfMemory = getTbox().getHierarchy();
        element.consolidateElements=NumberOfElementInMemory(graphOfMemory);
        System.out.println( "[ CONSOL.]\tnew experience from " + logs + " " + scene + " -> ");

        initialTime = System.nanoTime();
        Set<SceneHierarchyVertex> forgotten = forget();

        timing.forgetTime = System.nanoTime() - initialTime;
        //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
        graphOfMemory = getTbox().getHierarchy();
        element.forgetElements=NumberOfElementInMemory(graphOfMemory);
        System.out.println( "[ FORGET ]\tfreeze nodes: " + forgotten);

        //Print the information about the graph in a text file
        //MemoryFile();
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
    public Measure getTimings(){
        return new Measure();
    }

    private class ElementsOfMemory{
        int encodingElements, storingElements, retrievingElements, consolidateElements, forgetElements;

        public long tot(){
            return encodingElements + storingElements + retrievingElements + consolidateElements + forgetElements;
        }



        @Override
        public String toString() {
            return "(encode memory elements:" + encodingElements +
                    ", store memory elements:" + storingElements +
                    ", retrieve memory elements:" + retrievingElements +
                    ", consolidate memory elements:" + consolidateElements +
                    ", forget memory elements:" + forgetElements +
                    ")=" + tot() + " total number of memory elements";
        }


    }

    private class Timing{
        long encodingTime, storingTime, retrievingTime, consolidateTime, forgetTime;

        public long tot(){
            return encodingTime + storingTime + retrievingTime + consolidateTime + forgetTime;
        }

        public Measure getMeasure(){
            return new Measure();
        }

        @Override
        public String toString() {
            return "(encode:" + convert(encodingTime) +
                    ", store:" + convert(storingTime) +
                    ", retrieve:" + convert(retrievingTime) +
                    ", consolidate:" + convert(consolidateTime) +
                    ", forget:" + convert(forgetTime) +
                    ")=" + convert(tot()) + "ms ";
        }

        private double convert(long nanosec){ // returns ms
            return (double) nanosec / 1000000;
        }
    }

    class Measure{
        long encodeAverage, storeAverage, retrieveAverage, consolidateAverage, forgetAverage, allAverage;
        long encodeVariance, storeVariance, retrieveVariance, consolidateVariance, forgetVariance, allVariance;

        List<Timing> time;

        private Measure(){
            time = timings;
            List<Long> encode = new ArrayList<>();
            List<Long> store = new ArrayList<>();
            List<Long> retrieve = new ArrayList<>();
            List<Long> consolidate = new ArrayList<>();
            List<Long> forget = new ArrayList<>();
            List<Long> all = new ArrayList<>();
            for ( Timing t : timings){
                encode.add( t.encodingTime);
                store.add( t.storingTime);
                retrieve.add( t.retrievingTime);
                consolidate.add( t.consolidateTime);
                forget.add( t.forgetTime);
                all.add( t.tot());
            }

            encodeAverage = average( encode);
            storeAverage = average( store);
            retrieveAverage = average( retrieve);
            consolidateAverage = average( consolidate);
            forgetAverage = average( forget);
            allAverage = average( all);

            encodeVariance = variance( encode);
            storeVariance = variance( store);
            retrieveVariance = variance( retrieve);
            consolidateVariance = variance( consolidate);
            forgetVariance = variance( forget);
            allVariance = variance( all);
        }

        private long sum( List<Long> list){
            long sum = 0l;
            for (Long t : list)
                sum += t;
            return sum;
        }
        private long average( List<Long> list) {
            return sum(list) / list.size();
        }
        public long variance( List<Long> list) {
            long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
            for ( Long l : list){
                if( l < min)
                    min = l;
                if (l > max)
                    max = l;
            }
            return max-min;
            /*double z = 0;
            int y = 0;
            double x = 0;
            for (Long word : list) {
                x = (double) list.get(y)* list.get(y);
                z = z + x;
                y++;
            }
            double var = (z - (sum(list) * sum(list)) / list.size()) / (list.size()-1);
            return var;*/
        }

        @Override
        public String toString() {
            return "(encode=" + timing.convert(encodeAverage) + "±" + timing.convert(encodeVariance) + ", " +
                    "store=" + timing.convert(storeAverage) + "±" + timing.convert(storeVariance) + ", " +
                    "retrieve=" + timing.convert(retrieveAverage) + "±" + timing.convert(retrieveVariance) + ", " +
                    "consolidate=" + timing.convert(consolidateAverage) + "±" + timing.convert(consolidateVariance) + ", " +
                    "forget=" + timing.convert(forgetAverage) + "±" + timing.convert(forgetVariance) + ", " +
                    "tot=" + timing.convert(allAverage) + "±" + timing.convert(allVariance) + "ms)";
        }
    }
    private int NumberOfElementInMemory( ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graphOfMemory ){
        int countVertices=1;
        //Loop on all the vertices in the graph
        for( SceneHierarchyVertex sourceVertices : graphOfMemory.vertexSet()) {
           // sourceVertices.getMemoryScore()
            //Loop on all the edges touching the specified vertex
            for (SceneHierarchyEdge edges : graphOfMemory.edgesOf(sourceVertices)) {
                if (sourceVertices != graphOfMemory.getEdgeTarget(edges)) {
                    countVertices++;
                }
            }
        }
                return countVertices;
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

    public void convertToCSV(List<Timing> timings, List<ElementsOfMemory> elements) {
        int i = 0;
        PrintWriter outpustreamCSVEncoding = null;
        PrintWriter outpustreamCSVStoring = null;
        PrintWriter outpustreamCSVRetrieving = null;
        PrintWriter outpustreamCSVConsolidating = null;
        PrintWriter outpustreamCSVForgetting = null;
        //Open the files
        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileCSVEncoding.exists()) {
                //Create the file
                try {
                    fileCSVEncoding.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVEncoding = new PrintWriter(new FileOutputStream(fileCSVEncoding, true));
            if (!fileCSVStoring.exists()) {
                //Create the file
                try {
                    fileCSVStoring.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVStoring = new PrintWriter(new FileOutputStream(fileCSVStoring, true));
            if (!fileCSVRetrieving.exists()) {
                //Create the file
                try {
                    fileCSVRetrieving.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVRetrieving=new PrintWriter(new FileOutputStream(fileCSVRetrieving, true));
            if (!fileCSVConsolidating.exists()) {
                //Create the file
                try {
                    fileCSVConsolidating.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVConsolidating=new PrintWriter(new FileOutputStream(fileCSVConsolidating, true));
            if (!fileCSVForgetting.exists()) {
                //Create the file
                try {
                    fileCSVForgetting.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVForgetting=new PrintWriter(new FileOutputStream(fileCSVForgetting, true));
        }
        catch(FileNotFoundException e){
            System.out.println(e.getMessage());
        }
        //////////////////////////////////////////////
        //If it is the beginning of the experiment or the memory is still empty write the first rows
        //of the CSV files as heading
        if (time_instant == 0){
            //Write in CSV Encoding File
            outpustreamCSVEncoding.println("Encoding Time" + "," + "EncodingMemoryItems");
            outpustreamCSVEncoding.println(timing.convert(timing.encodingTime) + "," + element.encodingElements);
            //Close CSV Encoding File
            outpustreamCSVEncoding.close();

            //Write in CSV Storing File
             outpustreamCSVStoring.println("Storing Time" + "," + "StoringMemoryItems");
             outpustreamCSVStoring.println(timing.convert(timing.storingTime) + "," + element.storingElements);
            //Close CSV Storing File
            outpustreamCSVStoring.close();

            //Write in CSV Retrieving File
             outpustreamCSVRetrieving.println("Retrieving Time" + "," + "RetrievingMemoryItems");
             outpustreamCSVRetrieving.println(timing.convert(timing.retrievingTime)  + "," + element.retrievingElements);
            //Close CSV Retrieving File
            outpustreamCSVRetrieving.close();

            //Write in CSV Consolidating File
             outpustreamCSVConsolidating.println("Consolidating Time" + "," + "ConsolidatingMemoryItems");
             outpustreamCSVConsolidating.println(timing.convert(timing.consolidateTime) + "," + element.consolidateElements);
            //Close CSV Consolidating File
            outpustreamCSVConsolidating.close();

            //Write in CSV Forgetting File
             outpustreamCSVForgetting.println("Forgetting Time" + "," + "ForgettingMemoryItems");
             outpustreamCSVForgetting.println(timing.convert(timing.forgetTime)+ "," + element.forgetElements);
            //Close CSV Forgetting File
            outpustreamCSVForgetting.close();


        }
        for (Timing timingType: timings) {
            for ( ElementsOfMemory elementType: elements) {
                if (i==0){
                    //Write in CSV Encoding File
                    outpustreamCSVEncoding.println(timingType.convert(timing.encodingTime) + "," + element.encodingElements);
                    //Close CSV Encoding File
                    outpustreamCSVEncoding.close();
                }
                else if(i==1){
                    //Write in CSV Storing File
                    outpustreamCSVStoring.println(timingType.convert(timing.storingTime) + "," + element.storingElements);
                    //Close CSV Storing File
                    outpustreamCSVStoring.close();
                }
                else if (i==2){
                    //Write in CSV Retrieving File
                    outpustreamCSVRetrieving.println(timingType.convert(timing.retrievingTime)  + "," + element.retrievingElements);
                    //Close CSV Retrieving File
                    outpustreamCSVRetrieving.close();
                }
                else if(i==3){
                    //Write in CSV Consolidating File
                    outpustreamCSVConsolidating.println(timingType.convert(timing.consolidateTime) + "," + element.consolidateElements);
                    //Close CSV Consolidating File
                    outpustreamCSVConsolidating.close();
                }
                else if(i==4){
                    //Write in CSV Forgetting File
                    outpustreamCSVForgetting.println(timingType.convert(timing.forgetTime)+ "," + element.forgetElements);
                    //Close CSV Forgetting File
                    outpustreamCSVForgetting.close();
                }
            }
            i++;
        }

        time_instant++;
    }
}
