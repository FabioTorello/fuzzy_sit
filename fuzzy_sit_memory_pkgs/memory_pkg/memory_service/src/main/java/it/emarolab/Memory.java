package it.emarolab;

import fuzzy_sit_memory_msgs.TestServiceDirectiveRequest;
import fuzzy_sit_memory_msgs.TestServiceDirectiveResponse;
import it.emarolab.fuzzySIT.memoryLike.*;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;


//import java.util.ArrayList;
//import java.util.List;
import java.util.Set;

class Memory {

    //private List<Timing> timings = new ArrayList<>();
    //private Timing timing;

    // initialize ontology and memory
    private SimpleMemory memory;
    private boolean synchConsolidateForget = true;
    //private List<String> listStrings = new ArrayList<String>();

    public Memory(String tboxPath){
        memory = new SimpleMemory(new SITTBox( tboxPath));
    }
    public Memory(String tboxPath, boolean synchConsolidateForget){
        memory = new SimpleMemory(new SITTBox( tboxPath));
        this.synchConsolidateForget = synchConsolidateForget;
    }

    public void storeExperience(PerceptionBase scene){
        experience( scene,  true);
    }
    public void retrieveExperience(PerceptionBase scene){
        experience( scene, false);
    }
    private void experience(PerceptionBase scene, boolean storeOrRetrieve){ // true: from store, false: from retrieve

        //timing = new Timing();

        // must always be done before to store or retrieve
        //long initialTime = System.nanoTime();
        memory.encode( scene);
        //timing.encodingTime = System.nanoTime() - initialTime;
        System.out.println( "[ ENCODE ]\texperience: " + scene);
        //listStrings.add("[ ENCODE ]\texperience: " + scene);
        // set store or retrieve cases

        String logs;
        SceneHierarchyVertex learnedOrRetrievedScene;
        //initialTime = System.nanoTime();
        if ( storeOrRetrieve) {
            if ( scene.getSceneName().isEmpty())
                learnedOrRetrievedScene = memory.store();
            else learnedOrRetrievedScene = memory.store( scene.getSceneName());
            //timing.storingTime = System.nanoTime() - initialTime;
            logs = "storing";
            if( learnedOrRetrievedScene != null)
                System.out.println( "[  LEARN ]\texperience: " + learnedOrRetrievedScene);
                //listStrings.add("[ LEARN ]\texperience: " + learnedOrRetrievedScene);
        } else {
            learnedOrRetrievedScene = memory.retrieve();
            //timing.retrievingTime = System.nanoTime() - initialTime;
            logs = "retrieving";
            System.out.println( "[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
            //listStrings.add("[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
        }
        // synchronous consolidation and forgetting
        if ( learnedOrRetrievedScene != null & synchConsolidateForget) {
            consolidateAndForget(scene, logs);
        }
        System.out.println( "[ RECOGN.]\texperience: " + memory.recognize());
        //listStrings.add("[ RECOGN.]\texperience: " + memory.recognize());
        //System.out.println( "     Time spent " + timing);
        //timings.add( timing);
        System.out.println( "----------------------------------------------");
        //listStrings.add("----------------------------------------------");
        //response.getTestResponse().setResponse(listStrings);
    }

    public void consolidateAndForget(){
        consolidateAndForget( null);
    }
    public void consolidateAndForget( PerceptionBase scene){
        consolidateAndForget( scene, "external call");
    }
    private void consolidateAndForget( PerceptionBase scene, String logs){
        //long initialTime = System.nanoTime();
        memory.consolidate();
        //timing.consolidateTime = System.nanoTime() - initialTime;
        System.out.println( "[ CONSOL.]\tnew experience from " + logs + " " + scene + " -> ");

        //initialTime = System.nanoTime();
        Set<SceneHierarchyVertex> forgotten = memory.forget();
        //timing.forgetTime = System.nanoTime() - initialTime;
        System.out.println( "[ FORGET ]\tfreeze nodes: " + forgotten);
    }

    public SimpleMemory accessMemory(){
        return memory;
    }

    /*public Measure getTimings(){
        return new Measure();
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
            return var
        }

       @Override
        /*public String toString() {
            return "(encode=" + timing.convert(encodeAverage) + "±" + timing.convert(encodeVariance) + ", " +
                    "store=" + timing.convert(storeAverage) + "±" + timing.convert(storeVariance) + ", " +
                    "retrieve=" + timing.convert(retrieveAverage) + "±" + timing.convert(retrieveVariance) + ", " +
                    "consolidate=" + timing.convert(consolidateAverage) + "±" + timing.convert(consolidateVariance) + ", " +
                    "forget=" + timing.convert(forgetAverage) + "±" + timing.convert(forgetVariance) + ", " +
                    "tot=" + timing.convert(allAverage) + "±" + timing.convert(allVariance) + "ms)";
        }
    }*/

    //Scene definitions
    public static ConnectObjectScene scene0(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addScrewDriver( 0, .02, .9);
        return scene;
    }

    public static void main (String argv[]) throws java.io.IOException {

      /*Memory memoryTest = new Memory("memory_service/src/main/resources/table_assembling_memory_example.fuzzydl");
      memoryTest.storeExperience( scene0());
        memoryTest.consolidateAndForget();
        System.out.println( "----------------------------------------------");
        memoryTest.accessMemory().getTbox().show();*/
    }
}
