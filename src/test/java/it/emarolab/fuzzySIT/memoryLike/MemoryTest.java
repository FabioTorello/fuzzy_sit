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
            if ( scene.getSceneName().isEmpty())
                shouldConsolidate = memory.store();
            else shouldConsolidate = memory.store( scene.getSceneName());
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

    public static ConnectObjectScene scene0(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0, .02, .9);
        scene.setSceneName( "Scene0");
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
        scene.addLeg( -.5, .2, .9);
        scene.addScrewDriver( -.5, .25, .9);
        return scene;
    }

    private static ConnectObjectScene sceneLeg1(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addLeg( 0, 0.03, .9);
        scene.addLeg( -.5, .2, .9);
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
        ConnectObjectScene scene = new ConnectObjectScene( "Scene1");
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
        scene.setSceneName( "Scene2");
        return scene;
    }

    private static ConnectObjectScene scene3(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addTable( .0, .05, .9);
        scene.addPen( 0, .08, .9);
        scene.addScrewDriver( .0, .03, .9);
        scene.setSceneName( "Scene3");
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
        scene.setSceneName( "Scene4");
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
        scene.setSceneName( "Scene5");
        return scene;
    }

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
