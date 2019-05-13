package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

// TODO reset ids when overflow

class MemoryExample{


    private List<Timing> timings = new ArrayList<>();
    private Timing timing;

    // initialize ontology and memory
    private SimpleMemory memory;

    public MemoryExample(String tboxPath){
        memory = new SimpleMemory(new SITTBox( tboxPath));
    }

    public void storeExperience(PerceptionBase scene){
        experience( scene, true);
    }
    public void retrieveExperience(PerceptionBase scene){
        experience( scene, false);
    }
    private void experience(PerceptionBase scene, boolean storeOrRetrieve){ // true: from store, false: from retrieve

        timing = new Timing();

        // must always be done before to store or retrieve
        long initialTime = System.nanoTime();
        memory.encode( scene);
        timing.encodingTime = System.nanoTime() - initialTime;
        System.out.println( "[ ENCODE ]\texperience: " + scene);
        // set store or retrieve cases

        String logs;
        SceneHierarchyVertex learnedOrRetrievedScene;
        initialTime = System.nanoTime();
        if ( storeOrRetrieve) {
            if ( scene.getSceneName().isEmpty())
                learnedOrRetrievedScene = memory.store();
            else learnedOrRetrievedScene = memory.store( scene.getSceneName());
            timing.storingTime = System.nanoTime() - initialTime;
            logs = "storing";
            if( learnedOrRetrievedScene != null)
                System.out.println( "[  LEARN ]\texperience: " + learnedOrRetrievedScene);
        } else {
            learnedOrRetrievedScene = memory.retrieve();
            timing.retrievingTime = System.nanoTime() - initialTime;
            logs = "retrieving";
            System.out.println( "[RETRIEVE]\texperience: " + learnedOrRetrievedScene);
        }
        // synchronous consolidation and forgetting
        if ( learnedOrRetrievedScene != null) {
            consolidateAndForget(scene, logs);
        }
        System.out.println( "[ RECOGN.]\texperience: " + memory.recognize());
        System.out.println( "     Time spent " + timing);
        timings.add( timing);
        System.out.println( "----------------------------------------------");
    }

    public void consolidateAndForget(){
        consolidateAndForget( null, "");
    }
    public void consolidateAndForget( PerceptionBase scene, String logs){
        long initialTime = System.nanoTime();
        memory.consolidate();
        timing.consolidateTime = System.nanoTime() - initialTime;
        System.out.println( "[ CONSOL.]\tnew experience from " + logs + " " + scene + " -> ");

        initialTime = System.nanoTime();
        Set<SceneHierarchyVertex> forgotten = memory.forget();
        timing.forgetTime = System.nanoTime() - initialTime;
        System.out.println( "[ FORGET ]\tfreeze nodes: " + forgotten);
    }

    public SimpleMemory accessMemory(){
        return memory;
    }

    public Measure getTimings(){
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
}

public class MemoryTest {

    public static ConnectObjectScene scene0(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addScrewDriver( 0, .02, .9);
        return scene;
    }

    public static ConnectObjectScene scene0b(){
        ConnectObjectScene scene = new ConnectObjectScene( "Scene0b");
        scene.addTable(0,0, .9);
        scene.addPen( 0, .02, .9);
        return scene;
    }

    public static ConnectObjectScene sceneTable(){
        ConnectObjectScene scene = new ConnectObjectScene( "SceneTable");
        scene.addTable(0,0, .9);
        scene.addPen( 0, .1, .9);
        scene.addLeg( -.5, .2, .9);
        scene.addLeg( -.5, .21, .9);
        return scene;
    }

    private static ConnectObjectScene sceneLeg1(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0, .07, .9);
        scene.addLeg( -.7, .02, .9);
        scene.setSceneName( "SceneLeg1");
        return scene;
    }

    private static ConnectObjectScene sceneLeg2(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.setSceneName( "SceneLeg2");
        return scene;
    }

    private static ConnectObjectScene scene1(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.addPen( .5, .5, .9);
        scene.addScrewDriver( -.5, .02, .9);
        scene.addScrewDriver( -.5, .1, .9);
        return scene;
    }

    private static ConnectObjectScene scene2(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.addScrewDriver( .2, .3, .9);
        return scene;
    }

    private static ConnectObjectScene scene3(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addTable( .0, .05, .9);
        scene.addLeg( 0, .08, .9);
        scene.addScrewDriver( .0, .03, .9);
        return scene;
    }

    private static ConnectObjectScene scene4(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.addPen( -.1, .32, .9);
        scene.addPen( -.1, .3, .9);
        return scene;
    }

    public static ConnectObjectScene scene5(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.addScrewDriver(2.1, 0, .8);
        scene.addPen(2.08, 0, .8);
        scene.addLeg(2.0, 0, .8);
        return scene;
    }

    // TODO to finish implementation
    public static Thread sceneShow(ConnectObjectScene scene) {
        Thread shower = new Thread(() -> ScenePlotter.setSITScene(scene));
        shower.start();
        return shower;
    }


    public static void main(String[] args) {

        MemoryExample memory = new MemoryExample(FuzzySITBase.PATH_BASE + "table_assembling_memory_example.fuzzydl");

        //Thread shower1 = sceneShow( scene1());// TODO it can be shown only one at run

        // memory evolution
        memory.storeExperience( scene0());
        memory.storeExperience( scene0b());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene1());
        memory.storeExperience( scene2());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene3());
        memory.storeExperience( scene4());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene5());
        memory.storeExperience( scene4());
        memory.storeExperience( scene2());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene1());
        memory.storeExperience( scene4());
        memory.storeExperience( scene5());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene3());

        memory.consolidateAndForget();
        System.out.println( "----------------------------------------------");

        System.out.println( memory.getTimings());

        // TODO Retrieving template code implemented but not tested
        // It might learn as well (i.e., a requested scene gives information as an observed one)
        // Than, it might become very similar to store()
        // memory.retrieveExperience( scene1());

        // show hierarchy GUI
        memory.accessMemory().getTbox().show();
        // saved the evolved memory // TODO check scores storing
        //memory.accessMemory().getTbox().saveTbox("src/test/resources/learnedTest.fuzzydl");

        //shower1.interrupt();
    }
}
