package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.SITTBox;

public class MemoryTest {

    public static void main(String[] args) {

        // instanciate a T-Box with the default T-Box ontology and reasoner configuration file
        SITTBox h = new SITTBox(FuzzySITBase.PATH_BASE + "table_assembling_memory_example.fuzzydl");

        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addLeg(.01,50, .977); // L0
        scene.addPen(.0, 50, .977); // P0
        scene.addTable(20, 50, .8); // T0
        scene.addScrewDriver(2.29, 50, .8); // S0
        scene.addLeg(2.0, 50, .8); // L1
        System.out.println( scene);

        SITABox r = new SITABox(h, scene.getObjects(), scene.getRelations());

        System.out.println( "$$$$ " + r.getDefinition());
        h.learn( "Scene1", r);


        /*
        format();
        r = new SITABox(h, objects, relations);
        h.learn( "Scene1", r);

        System.out.println("-------------------------  S1   ------------------------");
        // create S1 and recognise it
        format();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S1
        h.learn( "Scene1", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S1 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");



        System.out.println("-------------------------  show ------------------------");
        // shows the inferred and learned SIT scene hierarchy
        h.show();
        // saved the augmented ontology
        h.saveTbox("src/test/resources/learnedTest.fuzzydl");
        */
    }
}
