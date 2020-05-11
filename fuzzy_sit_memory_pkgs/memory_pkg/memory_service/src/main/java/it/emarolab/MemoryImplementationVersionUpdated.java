package it.emarolab;


import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import it.emarolab.fuzzySIT.memoryLike.MemoryInterface;
//import javafx.util.Pair;
import org.jgrapht.ListenableGraph;
import it.emarolab.fuzzySIT.semantic.SITABox;
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintStream;

import java.io.*;
import java.time.LocalTime;
import java.util.*;

public class MemoryImplementationVersionUpdated extends MemoryInterface {


    //Define variables to take into account the time consumption
    private List<Timing> timings = new ArrayList<>();
    private Timing timing;

    List<Timing> scoreForgottenScene;


    private static String SCENE_PREFIX = "Scene";

    // recognition = 1, similarity = 0 => it always consolidates on recognition
    private static final double ENCODE_RECOGNITION_TH = .9; // recognition threshold above which it consolidates [0,1]
    private static final double ENCODE_SIMILARITY_TH = .2; // similarity threshold above which it consolidates [0,1]

    // recognition = 1, similarity = 1 => it always learns on recognition
    private static final double LEARN_RECOGNITION_TH = .9; // recognition threshold below which it learns [0,1]
    private static final double LEARN_SIMILARITY_TH = .8; // similarity threshold below which it learns [0,>~1]

    private static final double SCORE_WEAK = .1; // threshold under which it forgets [0,1]
    private static final double LEARNED_SCORE = .5; // initial score, percentage of max score [0,1]

    private static final double ENCODE_REINFORCE = 1.5;//original was 10 10; // reinforce factor for re-stored or re-retrieved experience [1,inf)

    private static int sceneCnt = 0;

    private static long id = 0;

    // actually remove node if true.
    // Otherwise the node remains but it is not consolidated (i.e., frozen).
    // REMARK: with false, SIT performances do not benefit from forgetting,
    //         with true the Graph would not show forgotten nodes.
    private static boolean REMOVE_FORGET = true;

    private String storingName = "";

    /////////////////////////////////////////////////////////////////////////
    ////////////////////For Command line execution///////////////////////////
    //File containing the graph information

    private File fileConsoleOut= new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/FileConsoleOut.txt");

    private File fileName = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/GraphInformation.txt");
    //CSV file for encoding time
    private File fileCSVEncoding = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/EncodingTime_MemoryItems.csv");
    //CSV file for storing time
    private File fileCSVStoring = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/StoringTime_MemoryItems.csv");
    //CSV file for retrieving time
    private File fileCSVRetrieving = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/RetrievingTime_MemoryItems.csv");
    //CSV file for consolidating time
    private File fileCSVConsolidating = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/ConsolidatingTime_MemoryItems.csv");
    //CSV file for forgetting time
    private File fileCSVForgetting = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/ForgettingTime_MemoryItems.csv");
    //CSV file for the table to save the variation of the consolidating time respect to the type of consolidating function
    private File fileTimeToConsolidatingFunction = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/ConsolidatingTime_MemoryItems_Function_One.csv");
    //CSV file for the table to save the variation of the number of memory items forgotten with a type of consolidating function
    private File fileItemsForgottenConsolidatingFunction = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/ItemsForgotten_ConsolidatingFunction_One.csv");
    //CSV file for the table to save the forgotten scenes with a type of consolidating function
    private File fileScoreItemsForgottenConsolidatingFunction = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/Score_ItemsForgotten_ConsolidatingFunction_One.csv");
    //CSV file for the table to save the forgotten scenes with a type of consolidating function
    private File fileSceneScoreVariation = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/Score_Variation_ConsolidatingFunction_One.csv");



     //   PrintStream stream = new PrintStream(fileConsoleOut);
      //  System.setOut(stream);


    /////////////////////////////////////////////////////////////////////////
    /*//File containing the graph information
    private File fileName = new File("memory_service/Logfiles/GraphInformation.txt");
    //CSV file for encoding time
    private File fileCSVEncoding = new File("memory_service/Logfiles/EncodingTime_MemoryItems.csv");
    //CSV file for storing time
    private File fileCSVStoring = new File("memory_service/Logfiles/StoringTime_MemoryItems.csv");
    //CSV file for retrieving time
    private File fileCSVRetrieving = new File("memory_service/Logfiles/RetrievingTime_MemoryItems.csv");
    //CSV file for consolidating time
    private File fileCSVConsolidating = new File("memory_service/Logfiles/ConsolidatingTime_MemoryItems.csv");
    //CSV file for forgetting time
    private File fileCSVForgetting = new File("memory_service/Logfiles/ForgettingTime_MemoryItems.csv");
    //CSV file for the table to save the variation of the consolidating time respect to the type of consolidating function
    private File fileTimeToConsolidatingFunction = new File("memory_service/Logfiles/ConsolidatingTime_MemoryItems_Function_One.csv");
    //CSV file for the table to save the variation of the number of memory items forgotten with a type of consolidating function
    private File fileItemsForgottenConsolidatingFunction = new File("memory_service/Logfiles/ItemsForgotten_ConsolidatingFunction_One.csv");
    //CSV file for the table to save the forgotten scenes with a type of consolidating function
    private File fileScoreItemsForgottenConsolidatingFunction = new File("memory_service/Logfiles/Score_ItemsForgotten_ConsolidatingFunction_One.csv");
    //CSV file for the table to save the forgotten scenes with a type of consolidating function
    private File fileSceneScoreVariation = new File("memory_service/Logfiles/Score_Variation_ConsolidatingFunction_One.csv");*/

    //Time instant
    private static long time_instant = 0;

    //Constructor
    public MemoryImplementationVersionUpdated(String tboxPath, String configPath) {
        super(new SITTBox(tboxPath, configPath));
    }


    public SceneHierarchyVertex store() {
        return store(SCENE_PREFIX + sceneCnt++);
    }

    @Override
    public SceneHierarchyVertex store(String sceneName){
        Map<SceneHierarchyVertex, Double> rec = recognize();
        // if memory is empty, learn new encoded scene
        if( rec.keySet().isEmpty()) {
            timing.sceneName = sceneName;
            return learn(sceneName, LEARNED_SCORE);
        }

        // if encoded scene can be recognized, update score
        boolean shouldLearn = true;
        double maxScore = 0;
        for ( SceneHierarchyVertex recognisedScene : rec.keySet()){
            double recognisedValue = rec.get( recognisedScene);
            double similarityValue = getAbox().getSimilarity(recognisedScene);
            if( recognisedValue >= ENCODE_RECOGNITION_TH & similarityValue >= ENCODE_SIMILARITY_TH)  // update score
                updateScoreStoring(recognisedScene, recognisedValue);
            if( similarityValue >= LEARN_SIMILARITY_TH & recognisedValue >= LEARN_RECOGNITION_TH) // do not learn
                shouldLearn = false;
            double score = recognisedScene.getMemoryScore();
            if ( score > maxScore)
                maxScore = score;
        }
        // if encoded scene can not be recognized, learn new scene
        if ( shouldLearn) {
            timing.sceneName = sceneName;
            return learn(sceneName, LEARNED_SCORE * maxScore);
        }
        return null;
    }

    protected void updateScoreStoring(SceneHierarchyVertex recognisedScene, double recognisedValue) {
        updateScorePolicy( recognisedScene, recognisedValue);
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
        if ( recognisedScene.getMemoryScore() > 0) // not froze node
            score += ENCODE_REINFORCE * recognisedValue; // reinforce for re-stored or re-retrieved experiences
        // else score freeze (i.e., experience to remove)
        recognisedScene.setMemoryScore( score);
    }


    @Override
    public void consolidate() {
        // TODO reinforce score based on graph edges and nodes

        //Set<Pair<SceneHierarchyVertex, SceneHierarchyVertex>> removed = getTbox().simplify();
        //System.out.println( "\tsimplifying " + removed);

        normalizeScoreConsolidating();
    }


    public void normalizeScoreConsolidating(){
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();
        double maxScore = 0;
        for( SceneHierarchyVertex scene : h.vertexSet()){
            double score = scene.getMemoryScore();
            if ( score > maxScore)
                maxScore = score;
        }
        if ( maxScore > 0)
            for (SceneHierarchyVertex experience : h.vertexSet())
                if (experience.getMemoryScore() >= 0)
                    experience.setMemoryScore(experience.getMemoryScore() / maxScore);
    }




    @Override
    public Set<SceneHierarchyVertex> forget(){

        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> h = getTbox().getHierarchy();

        Set<SceneHierarchyVertex> forgotten = new HashSet<>();

        scoreForgottenScene= new ArrayList<>();

        /*for (SceneHierarchyVertex itemForgotten : forgotten) {
            timing.sceneName = itemForgotten.getScene();
            timing.sceneScore = itemForgotten.getMemoryScore();
            scoreForgottenScene.add(timing);
        }*/
        // find weak score in the memory graph
        for( SceneHierarchyVertex scene : h.vertexSet()){
            if( scene.getMemoryScore() < SCORE_WEAK) {

                //For .csv file/////////
                Timing  timing_forget = new Timing();
                timing_forget.sceneName = scene.getScene();
                timing_forget.sceneScore = scene.getMemoryScore();
                ///////////////////////////////

                scene.setMemoryScore(-1); // score getter will be always 0 and is not consider on consolidation
                forgotten.add( scene);

                //For .csv file/////////
                scoreForgottenScene.add(timing_forget);
                timing.forgetDone = true;
                //////////////////////////

            }
        }

        timing.numberOfItemsForgotten=forgotten.size();

        if( REMOVE_FORGET)
            for( SceneHierarchyVertex scene : forgotten)
                getTbox().removeScene( scene);
        return forgotten;
    }


    public void experience(PerceptionBase scene, boolean storeOrRetrieve, boolean synchConsolidateForget) { // true: from store, false: from retrieve


        timing = new Timing();

        timing.learnDone = false;
        timing.forgetDone = false;

        PrintWriter outpustreamConsoleOut = null;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileConsoleOut.exists()) {
                //Create the file
                try {
                    fileConsoleOut.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamConsoleOut = new PrintWriter(new FileOutputStream(fileConsoleOut, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }




        //element = new ElementsOfMemory();

        // must always be done before to store or retrieve
        long initialTime = System.nanoTime();
        encode(scene);
        timing.encodingTime = System.nanoTime() - initialTime;

        //element.encodingElements=NumberOfElementInMemory(graphOfMemory);
        System.out.println("[ ENCODE ]\texperience: " + scene);
        outpustreamConsoleOut.println("[ ENCODE ]\texperience: " + scene);
        // set store or retrieve cases

        String logs;
        SceneHierarchyVertex learnedOrRetrievedScene;
        initialTime = System.nanoTime();
        if ( storeOrRetrieve) {
            if ( scene.getSceneName().isEmpty())
                if ( storingName.isEmpty())
                    learnedOrRetrievedScene = store();
                else learnedOrRetrievedScene = store(storingName);
            else learnedOrRetrievedScene = store( scene.getSceneName());
            timing.storingTime = System.nanoTime() - initialTime;


            //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
            //graphOfMemory = getTbox().getHierarchy();
            // element.storingElements=NumberOfElementInMemory(graphOfMemory);
            logs = "storing";
            //If the returned graph is not empty (thus there is only the root node)
            if (learnedOrRetrievedScene != null) {
                System.out.println("[  LEARN ]\texperience: " + learnedOrRetrievedScene);
                outpustreamConsoleOut.println("[  LEARN ]\texperience: " + learnedOrRetrievedScene);
                timing.learnDone = true;
            }
        } else {
            learnedOrRetrievedScene = retrieve();
            timing.retrievingTime = System.nanoTime() - initialTime;
            //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
            // graphOfMemory = getTbox().getHierarchy();
            //element.retrievingElements=NumberOfElementInMemory(graphOfMemory);
            logs = "retrieving";
            System.out.println("[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
            outpustreamConsoleOut.println("[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
        }
        // synchronous consolidation and forgetting
        if (learnedOrRetrievedScene != null & synchConsolidateForget) {
            consolidateAndForget(scene, logs, outpustreamConsoleOut);
        }

        System.out.println("[ RECOGN.]\texperience: " + recognize());
        outpustreamConsoleOut.println("[ RECOGN.]\texperience: " + recognize());

        //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
        timing.elements = getTbox().getHierarchy().vertexSet().size();


        System.out.println("    Total time spent " + timing.tot());
        outpustreamConsoleOut.println("    Total time spent " + timing.tot());
        System.out.println("    Elements in memory " + timing.elements);
        outpustreamConsoleOut.println("    Elements in memory " + timing.elements);
        System.out.println("    Memory has learnt in this loop: " + timing.learnDone);
        outpustreamConsoleOut.println("    Memory has learnt in this loop: " + timing.learnDone);
        System.out.println("    Memory has forgot something in this loop: " + timing.forgetDone);
        outpustreamConsoleOut.println("    Memory has forgot something in this loop: " + timing.forgetDone);
        //timings.add(timing);


        //elements.add( element);
        System.out.println("----------------------------------------------");
        outpustreamConsoleOut.println("----------------------------------------------");
        outpustreamConsoleOut.close();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //CODE FOR THE LOG FILES

        id++;
        LocalTime timeStamp = LocalTime.now();


        //Print the information of the different times and the memory items in different moments
        convertToCSV(id, timeStamp);


        //Table which save the variation of the consolidating time respect to the number of memory items and
        // the type of the consolidating function
        // TODO when you change the consolidating fucntion remember to change the string which represents
        //  the name of the file at the beginning of this page
        consolidatingTimeWithDifferentConsolidating(id, timeStamp, "ConsolidatingFunction1");

        // TODO when you change the consolidating fucntion remember to change the string which represents
        //  the name of the file at the beginning of this page
        itemsForgottenWithDifferentConsolidating(id,timeStamp,"ConsolidatingFunction1");

        // TODO when you change the consolidating fucntion remember to change the string which represents
        //  the name of the file at the beginning of this page
        sceneForgottenFunctionWithDifferentConsolidating(id,timeStamp,"ConsolidatingFunction1");

        sceneScoreVariationFunction(id,timeStamp,"ConsolidatingFunction1");
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    }



    public void consolidateAndForget() {
        consolidateAndForget(null);
    }

    public void consolidateAndForget(PerceptionBase scene) {
        consolidateAndForget(scene, "external call", null);
    }

    private void consolidateAndForget(PerceptionBase scene, String logs, PrintWriter outpustreamConsoleOut) {
        long initialTime = System.nanoTime();
        consolidate();
        timing.consolidateTime = System.nanoTime() - initialTime;
        //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
        //graphOfMemory = getTbox().getHierarchy();
        //element.consolidateElements=NumberOfElementInMemory(graphOfMemory);
        System.out.println("[ CONSOL.]\tnew experience from " + logs + " " + scene + " -> ");
        outpustreamConsoleOut.println("[ CONSOL.]\tnew experience from " + logs + " " + scene + " -> ");

        initialTime = System.nanoTime();
        Set<SceneHierarchyVertex> forgotten = forget();




        timing.forgetTime = System.nanoTime() - initialTime;

       /* scoreForgottenScene= new ArrayList<>();

        for (SceneHierarchyVertex itemForgotten : forgotten) {
            timing.sceneName = itemForgotten.getScene();
            timing.sceneScore = itemForgotten.getMemoryScore();
            scoreForgottenScene.add(timing);
        }*/
        //CHECKS THE MEMORY IN ORDER TO FIND THE NUMBER OF ELEMENTS CONTAINED
        //graphOfMemory = getTbox().getHierarchy();
        //element.forgetElements=NumberOfElementInMemory(graphOfMemory);

        System.out.println("[ FORGET ]\tfreeze nodes: " + forgotten);
        outpustreamConsoleOut.println("[ FORGET ]\tfreeze nodes: " + forgotten);

        //Print the information about the graph in a text file
        //MemoryFile();
    }


    private class Timing {
        long encodingTime, storingTime, retrievingTime, consolidateTime, forgetTime;
        //Number of nodes in the memory after a loop of SIT algorithm
        long elements;
        boolean learnDone;
        boolean forgetDone;
        String sceneName;
        int numberOfItemsForgotten;
        double sceneScore;


        public long tot() {
            return encodingTime + storingTime + retrievingTime + consolidateTime + forgetTime;
        }

        public MemoryImplementationVersionUpdated.Measure getMeasure() {
            return new MemoryImplementationVersionUpdated.Measure();
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

        private double convert(long nanosec) { // returns ms
            return (double) nanosec / 1000000;
        }
    }

    class Measure {
        long encodeAverage, storeAverage, retrieveAverage, consolidateAverage, forgetAverage, allAverage;
        long encodeVariance, storeVariance, retrieveVariance, consolidateVariance, forgetVariance, allVariance;

        List<MemoryImplementationVersionUpdated.Timing> time;

        private Measure() {
            time = timings;
            List<Long> encode = new ArrayList<>();
            List<Long> store = new ArrayList<>();
            List<Long> retrieve = new ArrayList<>();
            List<Long> consolidate = new ArrayList<>();
            List<Long> forget = new ArrayList<>();
            List<Long> all = new ArrayList<>();
            for (MemoryImplementationVersionUpdated.Timing t : timings) {
                encode.add(t.encodingTime);
                store.add(t.storingTime);
                retrieve.add(t.retrievingTime);
                consolidate.add(t.consolidateTime);
                forget.add(t.forgetTime);
                all.add(t.tot());
            }

            encodeAverage = average(encode);
            storeAverage = average(store);
            retrieveAverage = average(retrieve);
            consolidateAverage = average(consolidate);
            forgetAverage = average(forget);
            allAverage = average(all);

            encodeVariance = variance(encode);
            storeVariance = variance(store);
            retrieveVariance = variance(retrieve);
            consolidateVariance = variance(consolidate);
            forgetVariance = variance(forget);
            allVariance = variance(all);
        }

        private long sum(List<Long> list) {
            long sum = 0l;
            for (Long t : list)
                sum += t;
            return sum;
        }

        private long average(List<Long> list) {
            return sum(list) / list.size();
        }

        public long variance(List<Long> list) {
            long min = Long.MAX_VALUE, max = Long.MIN_VALUE;
            for (Long l : list) {
                if (l < min)
                    min = l;
                if (l > max)
                    max = l;
            }
            return max - min;
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

    public void convertToCSV(long id, LocalTime timeStamp) {
        //int i = 0;
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
            outpustreamCSVRetrieving = new PrintWriter(new FileOutputStream(fileCSVRetrieving, true));
            if (!fileCSVConsolidating.exists()) {
                //Create the file
                try {
                    fileCSVConsolidating.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVConsolidating = new PrintWriter(new FileOutputStream(fileCSVConsolidating, true));
            if (!fileCSVForgetting.exists()) {
                //Create the file
                try {
                    fileCSVForgetting.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamCSVForgetting = new PrintWriter(new FileOutputStream(fileCSVForgetting, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        //////////////////////////////////////////////
        //If it is the beginning of the experiment or the memory is still empty write the first rows
        //of the CSV files as heading
        if (id == 1) {
            //Write in CSV Encoding File
            outpustreamCSVEncoding.println("ID" + "," + "Time Stamp" + "," + "Encoding Time(ms)" + "," + "Memory Items" + "," + "Learning Loop?" + "," + "Forgetting Loop?");
            outpustreamCSVEncoding.println(id + "," + timeStamp + "," + timing.convert(timing.encodingTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
            //Close CSV Encoding File
            outpustreamCSVEncoding.close();

            //Write in CSV Storing File
            outpustreamCSVStoring.println("ID" + "," + "Time Stamp" + "," + "Storing Time(ms)" + "," + "Memory Items" + "," + "Item_Learnt" + "," + "Learning Loop?" + "," + "Forgetting Loop?");
            outpustreamCSVStoring.println(id + "," + timeStamp + "," + timing.convert(timing.storingTime) + "," + timing.elements + "," + timing.sceneName + "," + timing.learnDone + "," + timing.forgetDone);
            //Close CSV Storing File
            outpustreamCSVStoring.close();

            //Write in CSV Retrieving File
            outpustreamCSVRetrieving.println("ID" + "," + "Time Stamp" + "," + "Retrieving Time(ms)" + "," + "Memory Items" + "," + "Learning Loop?" + "," + "Forgetting Loop?");
            outpustreamCSVRetrieving.println(id + "," + timeStamp + "," + timing.convert(timing.retrievingTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
            //Close CSV Retrieving File
            outpustreamCSVRetrieving.close();

            //Write in CSV Consolidating File
            outpustreamCSVConsolidating.println("ID" + "," + "Time Stamp" + "," + "Consolidating Time(ms)" + "," + "Memory Items" + "," + "Learning Loop?" + "," + "Forgetting Loop?");
            outpustreamCSVConsolidating.println(id + "," + timeStamp + "," + timing.convert(timing.consolidateTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
            //Close CSV Consolidating File
            outpustreamCSVConsolidating.close();

            //Write in CSV Forgetting File
            outpustreamCSVForgetting.println("ID" + "," + "Time Stamp" + "," + "Forgetting Time(ms)" + "," + "Memory Items" + "," + "Learning Loop?" + "," + "Forgetting Loop?");
            outpustreamCSVForgetting.println(id + "," + timeStamp + "," + timing.convert(timing.forgetTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
            //Close CSV Forgetting File
            outpustreamCSVForgetting.close();


        }
        for (int i=0;i<5;i++) {
            if (i == 0) {
                //Write in CSV Encoding File
                outpustreamCSVEncoding.println(id + "," + timeStamp + "," + timing.convert(timing.encodingTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
                //Close CSV Encoding File
                outpustreamCSVEncoding.close();
            } else if (i == 1) {
                //Write in CSV Storing File
                outpustreamCSVStoring.println(id + "," + timeStamp + "," + timing.convert(timing.storingTime) + "," + timing.elements + "," + timing.sceneName + "," + timing.learnDone + "," + timing.forgetDone);
                //Close CSV Storing File
                outpustreamCSVStoring.close();
            } else if (i == 2) {
                //Write in CSV Retrieving File
                outpustreamCSVRetrieving.println(id + "," + timeStamp + "," + timing.convert(timing.retrievingTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
                //Close CSV Retrieving File
                outpustreamCSVRetrieving.close();
            } else if (i == 3) {
                //Write in CSV Consolidating File
                outpustreamCSVConsolidating.println(id + "," + timeStamp + "," + timing.convert(timing.consolidateTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
                //Close CSV Consolidating File
                outpustreamCSVConsolidating.close();
            } else if (i == 4) {
                //Write in CSV Forgetting File
                outpustreamCSVForgetting.println(id + "," + timeStamp + "," + timing.convert(timing.forgetTime) + "," + timing.elements + "," + timing.learnDone + "," + timing.forgetDone);
                //Close CSV Forgetting File
                outpustreamCSVForgetting.close();
            }
        }
        //i++;
        //time_instant++;
    }


    public void consolidatingTimeWithDifferentConsolidating(long id, LocalTime timeStamp, String typeFunction){

        PrintWriter outpustreamDifferentConsolidating = null;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileTimeToConsolidatingFunction.exists()) {
                //Create the file
                try {
                    fileTimeToConsolidatingFunction.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamDifferentConsolidating = new PrintWriter(new FileOutputStream(fileTimeToConsolidatingFunction, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (id==1) {
            outpustreamDifferentConsolidating.println("ID" + "," + "Time Stamp" + "," + "Consolidating Function Type" + "," + "Consolidating Time(ms)"  + "," + "Memory Items");
            outpustreamDifferentConsolidating.println(id + "," + timeStamp + "," + typeFunction + "," + timing.convert(timing.consolidateTime) + "," + timing.elements);
            outpustreamDifferentConsolidating.close();
        }
        outpustreamDifferentConsolidating.println(id + "," + timeStamp + "," + typeFunction + "," + timing.convert(timing.consolidateTime) + "," + timing.elements);
        outpustreamDifferentConsolidating.close();
    }







    public void itemsForgottenWithDifferentConsolidating(long id, LocalTime timeStamp, String typeFunction){

        PrintWriter outpustreamItemsForgotten = null;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileItemsForgottenConsolidatingFunction.exists()) {
                //Create the file
                try {
                    fileItemsForgottenConsolidatingFunction.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamItemsForgotten = new PrintWriter(new FileOutputStream(fileItemsForgottenConsolidatingFunction, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (id==1) {
            outpustreamItemsForgotten.println("ID" + "," + "Time Stamp" + "," + "Items_Forgotten"  + "," + "Consolidating_Function");
            outpustreamItemsForgotten.println(id + "," + timeStamp + "," + timing.numberOfItemsForgotten + "," + typeFunction);
            outpustreamItemsForgotten.close();
        }
        if (timing.forgetDone==true) {
            outpustreamItemsForgotten.println(id + "," + timeStamp + "," + timing.numberOfItemsForgotten + "," + typeFunction);
            outpustreamItemsForgotten.close();
        }
    }






    public void sceneForgottenFunctionWithDifferentConsolidating(long id, LocalTime timeStamp, String typeFunction){

        PrintWriter outpustreamScoreItemsForgotten = null;
        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileScoreItemsForgottenConsolidatingFunction.exists()) {
                //Create the file
                try {
                    fileScoreItemsForgottenConsolidatingFunction.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamScoreItemsForgotten = new PrintWriter(new FileOutputStream(fileScoreItemsForgottenConsolidatingFunction, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        if (id==1) {
            outpustreamScoreItemsForgotten.println("ID" + "," + "Time Stamp" + "," + "Scene_Name" + "," + "Score"  + "," + "Score_Weak_Th" + "," + "Consolidating_Function");
            outpustreamScoreItemsForgotten.close();
        }
        if (timing.forgetDone==true){
            for (MemoryImplementationVersionUpdated.Timing forgottenItem: scoreForgottenScene ){
                outpustreamScoreItemsForgotten.println(id + "," + timeStamp + "," + forgottenItem.sceneName + "," + forgottenItem.sceneScore  + "," + SCORE_WEAK + "," + typeFunction);
            }
            outpustreamScoreItemsForgotten.close();
        }
    }



    public void sceneScoreVariationFunction( long id, LocalTime timeStamp, String typeFunction){

        PrintWriter outpustreamScoreVariation = null;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileSceneScoreVariation.exists()) {
                //Create the file
                try {
                    fileSceneScoreVariation.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamScoreVariation = new PrintWriter(new FileOutputStream(fileSceneScoreVariation, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }


        //obtain the memory with the aim to use it to write the scenes and scores relation over time
        ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> memoryGraph = getTbox().getHierarchy();
        //System.out.println("NUMBER VERTEX: " + memoryGraph.vertexSet().size());
        for (SceneHierarchyVertex  graphOfMemory: memoryGraph.vertexSet() ){
            if (id==1) {
                outpustreamScoreVariation.println("ID" + "," +  "Time Stamp" + "," + "Scene Name" + "," + "Score"  + "," + "Consolidating_Function");

            }
            outpustreamScoreVariation.println(id + "," + timeStamp + "," +  graphOfMemory.getScene()  + "," + graphOfMemory.getMemoryScore() + "," + typeFunction);

        }
        outpustreamScoreVariation.close();
    }
















}
