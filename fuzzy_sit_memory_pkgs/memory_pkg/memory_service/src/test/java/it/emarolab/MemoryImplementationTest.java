package it.emarolab;
//import com.google.common.collect.Lists;
import it.emarolab.fuzzySIT.FuzzySITBase;
//import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import it.emarolab.fuzzySIT.perception.simple2D.Point2;
import it.emarolab.fuzzySIT.perception.simple2D.DefineRelationsOnScene;
import it.emarolab.fuzzySIT.perception.simple2D.Object;
import it.emarolab.fuzzySIT.perception.simple2D.Region;
//import org.ros.internal.loader.CommandLineLoader;
//import org.ros.node.DefaultNodeMainExecutor;
//import org.ros.node.NodeConfiguration;
//import org.ros.node.NodeMainExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;



public class MemoryImplementationTest {



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

public static DefineRelationsOnScene scene6(){
    DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene6");
    Object plate = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
    Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
    Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
    Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
    Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
    Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
    scene.addObject(plate);
    scene.addObject(region1);
    scene.addObject(region2);
    scene.addObject(region4);
    scene.addObject(region3);
    scene.addObject(centralRegion);
    return scene;
}

    public static DefineRelationsOnScene scene7(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene7");
        Object plate = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object fork = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate);
        scene.addObject(fork);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region4);
        scene.addObject(region3);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene8(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene8");
        Object plate = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object fork = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object glass = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate);
        scene.addObject(fork);
        scene.addObject(glass);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }


    public static DefineRelationsOnScene scene9(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene9");
        Object plate = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object fork = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object glass = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object knife = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate);
        scene.addObject(fork);
        scene.addObject(glass);
        scene.addObject(knife);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene15(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene15");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.2, 0.6 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene21(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene21");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(-0.4, 0.4 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.2, 0.6 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene14(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene14");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.8, 0.7 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.4, 0.4 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene1(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene1");
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene2(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene2");
        Object knife = new Object("Knife","K1", .9, new Point2(0.25, 0.3 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(knife);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene3(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene3");
        Object knife = new Object("Knife","K1", .9, new Point2(0.25, 0.3 ));
        Object glass = new Object("Glass","G1", .9, new Point2(0.3, 0.4 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(knife);
        scene.addObject(glass);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene4(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene4");
        Object knife = new Object("Knife","K1", .9, new Point2(0.25, 0.3 ));
        Object glass = new Object("Glass","G1", .9, new Point2(0.3, 0.4 ));
        Object fork = new Object("Fork","F1", .9, new Point2(0.2, 0.5 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(knife);
        scene.addObject(glass);
        scene.addObject(fork);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene5(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene5");
        Object knife = new Object("Knife","K1", .9, new Point2(0.25, 0.3 ));
        Object glass = new Object("Glass","G1", .9, new Point2(0.3, 0.4 ));
        Object fork = new Object("Fork","F1", .9, new Point2(0.2, 0.5 ));
        Object plate = new Object("Plate","P1", .9, new Point2(0.4, 0.6 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(knife);
        scene.addObject(glass);
        scene.addObject(fork);
        scene.addObject(plate);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene10(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene10");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(glass1);
        scene.addObject(knife1);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene11(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene11");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(glass1);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene12(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene12");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(-0.4, 0.4 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene13(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene13");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(-0.4, 0.4 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(0.6, 0.8 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene16(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene16");
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(-0.15, 0.75 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.1, 0.35 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.3, 0.3 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene17(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene17");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(-0.4, 0.4 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.75, 0.15 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(0.6, 0.8 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene18(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene18");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(-0.4, 0.4 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.75, 0.15 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(0.6, 0.8 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene19(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene19");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(-0.4, 0.4 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.75, 0.15 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(0.6, 0.8 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }
    public static DefineRelationsOnScene scene20(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene20");
        Object fork1 = new Object("Fork","F1", .9, new Point2(-0.4, 0.4 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.75, 0.15 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(0.6, 0.8 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene22(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene22");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(-0.4, 0.4 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.3, 0.3 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene23(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene23");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.8, 0.7 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.3, 0.3 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.9, 0.9 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene24(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene24");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.8, 0.7 ));
        Object glass1 = new Object("Glass","G1", .9, new Point2(-0.3, 0.3 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(0.75, 0.15 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass1);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static DefineRelationsOnScene scene25(){
        DefineRelationsOnScene scene = new DefineRelationsOnScene ("Scene25");
        Object plate1 = new Object("Plate","P1", .9, new Point2(-0.15, 0.75 ));
        Object plate2 = new Object("Plate","P2", .9, new Point2(-0.3, 0.6 ));
        Object fork1 = new Object("Fork","F1", .9, new Point2(0.6, 0.6 ));
        Object fork2 = new Object("Fork","F2", .9, new Point2(0.8, 0.7 ));
        Object glass2 = new Object("Glass","G2", .9, new Point2(-0.1, 0.35 ));
        Object knife1 = new Object("Knife","K1", .9, new Point2(0.55, 0.3 ));
        Object knife2 = new Object("Knife","K2", .9, new Point2(0.75, 0.15 ));
        Region region1 = new Region("R1","Region1", .9, new Point2(-0.25, 0.75));
        Region region2 = new Region("R2","Region2", .9, new Point2(0.75, 0.75));
        Region region3 = new Region("R3","Region3", .9, new Point2(-0.25, 0.25));
        Region region4 = new Region("R4","Region4", .9, new Point2(0.75, 0.25));
        Region centralRegion = new Region("RC","CentralRegion", .9, new Point2(0.25, 0.5));
        scene.addObject(plate1);
        scene.addObject(plate2);
        scene.addObject(fork1);
        scene.addObject(fork2);
        scene.addObject(glass2);
        scene.addObject(knife1);
        scene.addObject(knife2);
        scene.addObject(region1);
        scene.addObject(region2);
        scene.addObject(region3);
        scene.addObject(region4);
        scene.addObject(centralRegion);
        return scene;
    }

    public static void main(String[] args) {

        MemoryImplementation memory = new MemoryImplementation("memory_service/src/main/resources/table_classification_memory_example.fuzzydl", "memory_service/src/main/resources/fuzzyDL_CONFIG" );

         memory.experience( scene1(),true,true);
         /*memory.experience( scene2(),true,true);
         memory.experience( scene3(),true,true);
         memory.experience( scene4(),true,true);
         memory.experience( scene5(),true,true);
         memory.experience( scene6(),true,true);
         memory.experience( scene7(),true,true);
         memory.experience( scene8(),true,true);*/

         //memory.experience( scene9(),true,true);

         /*memory.experience( scene10(),true,true);
         memory.experience( scene11(),true,true);
         memory.experience( scene12(),true,true);
         memory.experience( scene13(),true,true);
         memory.experience( scene14(),true,true);
        //Retrieve a scene
         memory.experience( scene9(),false,true);
        //Retrieve a scene
         memory.experience( scene11(),false,true);
         memory.experience( scene15(),true,true);
        //Retrieve a scene
         memory.experience( scene14(),false,true);
         memory.experience( scene16(),true,true);
         memory.experience( scene17(),true,true);
         memory.experience( scene18(),true,true);
         memory.experience( scene19(),true,true);
        //Retrieve a scene
         memory.experience( scene16(),false,true);
        //Retrieve a scene
         memory.experience( scene14(),false,true);
         memory.experience( scene20(),true,true);
         memory.experience( scene21(),true,true);
        //Retrieve a scene
         memory.experience( scene15(),false,true);
        //Retrieve a scene
         memory.experience( scene14(),false,true);
         memory.experience( scene22(),true,true);
        //Retrieve a scene
         memory.experience( scene21(),false,true);
         memory.experience( scene23(),true,true);
         memory.experience( scene24(),true,true);
         memory.experience( scene25(),true,true);*/





















        //Show the graph
        memory.getTbox().show();

         //TODO This code below is for the ConnectedObjectScene
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
