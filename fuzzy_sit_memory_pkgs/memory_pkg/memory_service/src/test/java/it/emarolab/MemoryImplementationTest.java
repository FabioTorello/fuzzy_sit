package it.emarolab;
import com.google.common.collect.Lists;
import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;
import java.util.List;


public class MemoryImplementationTest {


    public static ConnectObjectScene scene0(){
        ConnectObjectScene scene = new ConnectObjectScene("Scene0");
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
        ConnectObjectScene scene = new ConnectObjectScene("Scene1");
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.addPen( .5, .5, .9);
        scene.addScrewDriver( -.5, .02, .9);
        scene.addScrewDriver( -.5, .1, .9);
        return scene;
    }








    public static void main(String[] args) {


        MemoryImplementation memory = new MemoryImplementation(FuzzySITBase.PATH_BASE + "table_assembling_memory_example.fuzzydl");

        //I want to store the scene1 and I want the consolidating function compute the score
            //memory.experience(scene1(),true,true);

        //I want to store the scene0 and I want the consolidating function compute the score
            //memory.experience( scene0(),true,true);

        //I want to store the scene0b and I want the consolidating function compute the score
            //memory.experience( scene0b(),true,true);

        //I want to store the sceneLeg1 and I want the consolidating function compute the score
            //memory.experience( sceneLeg1(),true,true);

       /* //I want to retrieve the scene0 and I want the consolidating function compute the score
            memory.experience( scene0(),false,true);

        //I want to retrieve the scene1 and I want the consolidating function compute the score
            memory.experience(scene1(),false,true);*/

        //I want to store the sceneTable and I want the consolidating function compute the score
            memory.experience( sceneTable(),true,true);

        /*//I want to retrieve the sceneTable and I want the consolidating function compute the score
            memory.experience( sceneTable(),false,true);

        //I want to retrieve the scene0b and I want the consolidating function compute the score
            memory.experience( scene0b(),false,true);
            //TODO here the scene0b is not in the graph so the retrieve experience is null because it cannot be found it
            //TODO but if the request happens many times that scene has to be store because maybe it is important!
        //I want to retrieve the sceneLeg1 and I want the consolidating function compute the score
            memory.experience( sceneLeg1(),false,true);*/

        //I want to store the sceneLeg2 and I want the consolidating function compute the score
            //memory.experience( sceneLeg2(),true,true);

       /* //I want to retrieve the sceneLeg2 and I want the consolidating function compute the score
            memory.experience( sceneLeg2(),false,true);

        //I want to retrieve the scene1 and I want the consolidating function compute the score
            memory.experience(scene1(),false,true);
            //TODO here scene1 is no more in the graph but if I try to retrieve it the software told me it is equal
            //to SceneLeg1 but it is not true because scene1 has more objects in its definition
            //maybe the retrieving has to take into account also the number of the objects besides the types of them*/




    }
}
