package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import it.emarolab.fuzzySIT.semantic.SITTBox;


class MemoryExample{
    // initialize ontology and memory
    SimpleMemory memory;

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
        // must always be done before to store or retrieve
        memory.encode( scene);
        // set store or retrieve cases
        boolean shouldConsolidate;
        String logs;
        if ( storeOrRetrieve) {
            shouldConsolidate = memory.store();
            logs = "storing";
        } else {
            shouldConsolidate = memory.retrieve();
            logs = "retrieving";
        }
        // synchronous consolidation and forgetting
        if ( shouldConsolidate) {
            System.out.print( " Consolidating new experience from " + logs + "(Scene:" + scene + " -> ");
            memory.consolidate();
            memory.forget();
        }
        System.out.println( "Recognized: " + memory.recognize() + ")");
    }

    public SimpleMemory accessMemory(){
        return memory;
    }
}

public class MemoryTest {

    private static ConnectObjectScene scene1(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(.01,50, .977); // L0
        scene.addPen(.25, 50, .977); // P0
        //System.out.println( scene);
        return  scene;
    }

    private static ConnectObjectScene scene2(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(.01,50, .977); // L0
        scene.addPen(.0, 50, .977); // P0
        scene.addLeg(20, 50, .8); // T0
        scene.addScrewDriver(2.29, 50, .8); // S0
        scene.addLeg(2.0, 50, .8); // L1
        //System.out.println( scene);
        return scene;
    }

    private static ConnectObjectScene scene3(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addLeg(.01,50, .977); // L0
        scene.addPen(.0, 50, .977); // P0
        scene.addTable(20, 50, .8); // T0
        scene.addScrewDriver(2.29, 50, .8); // S0
        scene.addLeg(2.0, 50, .8); // L1
        //System.out.println( scene);
        return scene;
    }


    public static void main(String[] args) {

        MemoryExample memory = new MemoryExample(FuzzySITBase.PATH_BASE + "table_assembling_memory_example.fuzzydl");

        // memory evolution
        memory.storeExperience( scene1());
        memory.storeExperience( scene3());
        memory.storeExperience( scene2());

        // TODO Retrieving template code implemented but not tested
        // It might learn as well (i.e., a requested scene gives information as an observed one)
        // memory.retrieveExperience( scene1());

        // show hierarchy GUI
        memory.accessMemory().getTbox().show();
        // saved the evolved memory // TODO check scores storing
        //memory.accessMemory().getTbox().saveTbox("src/test/resources/learnedTest.fuzzydl");
    }
}
