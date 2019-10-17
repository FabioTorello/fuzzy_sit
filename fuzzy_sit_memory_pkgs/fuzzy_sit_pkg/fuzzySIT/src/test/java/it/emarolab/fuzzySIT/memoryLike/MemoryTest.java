package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;

// TODO reset ids when overflow

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

    public static void main(String[] args) {



        MemoryExample memory = new MemoryExample(FuzzySITBase.PATH_BASE + "table_assembling_memory_example.fuzzydl");

        //Thread shower1 = sceneShow( scene1());// TODO it can be shown only one at run

        // memory evolution
        // scenes set random... many of them subsume sceneTable(), sceneLeg1() or sceneLeg2()
        memory.storeExperience( scene0());
        memory.storeExperience( scene0b());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene1());
        memory.storeExperience( scene2());
        memory.storeExperience( scene3());
        memory.storeExperience( scene4());
        memory.storeExperience( scene5());
        memory.storeExperience( scene4());
        memory.storeExperience( scene2());
        memory.storeExperience( scene1());
        memory.storeExperience( scene4());
        memory.storeExperience( scene5());
        memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());
        memory.storeExperience( scene3());
        memory.storeExperience( scene1());
        memory.storeExperience( scene5());
        memory.storeExperience( scene0());
        memory.storeExperience( scene0b());
        // uncomment to see changes in the experience graph
        // recurrent (sub)scenes are remembered and weaker experiences are removed
        /*memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());*/
        /*memory.storeExperience( sceneTable());
        memory.storeExperience( sceneLeg1());
        memory.storeExperience( sceneLeg2());*/

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
