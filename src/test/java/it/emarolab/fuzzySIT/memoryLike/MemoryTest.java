package it.emarolab.fuzzySIT.memoryLike;

import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;

public class MemoryTest {

    public static void main(String[] args) {

        // instanciate a T-Box with the default T-Box ontology and reasoner configuration file
 /*       SITABox r;
        SITTBox h = new SITTBox();

*/


        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addLeg(.01,50,.8); // L0
        scene.addPen(.0, 50, .8); // P0

        System.out.println( scene);
        scene.addTable(20, 50, .8); // T0

        scene.addScrewDriver(2.29, 50, .8); // S0
        System.out.println( scene);
        scene.addLeg(2.0, 50, .8); // L1
        System.out.println( scene);



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
