package it.emarolab;
import com.google.common.collect.Lists;
import it.emarolab.fuzzySIT.FuzzySITBase;
//import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
//import it.emarolab.fuzzySIT.perception.simple2D.ObjectOnScene;
//import it.emarolab.fuzzySIT.perception.simple2D.NearObjectScene;
import it.emarolab.fuzzySIT.perception.simple2D.Point2;
//import it.emarolab.fuzzySIT.perception.simple2D.RelatedObjectOnScene;
//import it.emarolab.fuzzySIT.perception.simple2D.RelatedRegionOnScene;
//import it.emarolab.fuzzySIT.perception.simple2D.RegionOnScene;
import it.emarolab.fuzzySIT.perception.simple2D.DefineRelationsOnScene;
import it.emarolab.fuzzySIT.perception.simple2D.Object;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.Region;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;



public class MemoryImplementationTest {


    /*public static ConnectObjectScene scene0(){
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
    }*/

  /*  private static ConnectObjectScene scene1(){
        ConnectObjectScene scene = new ConnectObjectScene("Scene1");
        scene.addTable(0,0, .9);
        scene.addLeg( 0.01, 0, .9);
        scene.addLeg( 0.03, 0, .9);
        scene.addPen( .5, .5, .9);
        scene.addScrewDriver( -.5, .02, .9);
        scene.addScrewDriver( -.5, .1, .9);
        return scene;
    } */

    /*  private static NearObjectScene scene1N(){
        NearObjectScene scene = new NearObjectScene();
        scene.addFork(0, 0, 0.9);
        scene.addGlass(0, .02, 0.9);
        scene.addPlate(0.1, 0.2, 0.9);
        scene.addKnife(0.01, 0.001, 0.9);
        return scene;
    }*/

  /* private static RelatedRegionOnScene scene1N() {
       ObjectOnScene fork = new ObjectOnScene("Fork","F",0.9, new Point2(0.1,0.02));
       ObjectOnScene plate = new ObjectOnScene("Plate","P",0.9, new Point2(0.2,0.02));
       Set<ObjectOnScene> objects= new HashSet<ObjectOnScene>();
       //RelatedObjectOnScene scene = new RelatedObjectOnScene("Scene0", objects);
       objects.add(fork);
       objects.add(plate);
       RegionOnScene centralregion = new RegionOnScene("Region", "RC", 0.9,new Point2(0, 0.5));
       RegionOnScene region1 = new RegionOnScene("Region", "R1", 0.9,new Point2(-0.5,0.5));
       RegionOnScene region2 = new RegionOnScene("Region", "R2", 0.9,new Point2(0.5, 0.5));
       RegionOnScene region3 = new RegionOnScene("Region", "R3", 0.9,new Point2(-0.5, 0));
       RegionOnScene region4 = new RegionOnScene("Region", "R4", 0.9,new Point2(0.5, 0));
       Set<RegionOnScene> regions= new HashSet<RegionOnScene>();
       regions.add(centralregion);
       regions.add(region1);
       regions.add(region2);
       regions.add(region3);
       regions.add(region4);
       RelatedRegionOnScene scene = new RelatedRegionOnScene("scene0", regions, 5);
       scene.addElementsToScene(regions,objects);
       //scene.addRegionsToScene(regions);
       scene.addObjectsInRegions (objects, regions);
       return scene;
   }*/

public static DefineRelationsOnScene scene1(){
    DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene1");
    Object plate = new Object("Plate","P1", .9, new Point2(0.03, 0));
    Region region1 = new Region("R1","Region1", .9, new Point2(0.01, 0));
    scene.addObject(plate);
    scene.addObject(region1);
    return scene;
}



    public static void main(String[] args) {

        MemoryImplementation memory = new MemoryImplementation("memory_service/src/main/resources/table_classification_memory_example.fuzzydl", "memory_service/src/main/resources/fuzzyDL_CONFIG" );
         memory.experience( scene1(),true,true);
        //I want to store the scene1 and I want the consolidating function compute the score
          // memory.experience(scene1(),true,true);

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
            //memory.experience( sceneTable(),true,true);

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
