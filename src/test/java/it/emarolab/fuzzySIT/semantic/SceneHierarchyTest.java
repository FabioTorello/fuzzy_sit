package it.emarolab.fuzzySIT.semantic;

import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

import java.util.HashSet;
import java.util.Set;

/**
 * The testing class for {@link SITTBox} and {@link SITABox}.
 * <p>
 *     This tester shows how it is possible to load an fuzzy SIT T-Box,
 *     define a static scene by specifying its objects and spatial relations.
 *     As well as show how it is possible to recognise and learn
 *     such a configuration.
 *     <br>
 *     This implementation is based on the example T-Box for
 *     the fuzzy SIT implementation available at:
 *     {@code src/main/resources/example_SIT_kb.fuzzydl}.
 *     <br>
 *     It format 6 different scenes (where their objects and relations
 *     have been set just for showing purposes). Then, it performs SIT:
 *     recognition, learning and recognition again, for all of such a scenes.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.SceneHierarchyTest <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see it.emarolab.fuzzySIT.monteCarlo.CarloInterface
 */
public class SceneHierarchyTest {

    public static Set<SpatialObject> objects = new HashSet<>(); // the set of objects in the SIT scene to test
    public static Set<SpatialRelation> relations = new HashSet<>(); // the set of relations in the SIT scene to test
    public static int objectcount; // the counter of the amount of objects in the SIT scene to test

    // the name of the types of objects in this example (π)
    public static final String BOOK = "Book";
    public static final String CUP = "Cup";
    public static final String MOUSE = "Mouse";
    // the name of the spatial relations used in this example (ζ)
    public static final String RIGHT = "isRightOf";
    public static final String LEFT = "isLeftOf";
    public static final String FRONT = "isFrontOf";
    public static final String BEHIND = "isBehindOf";
    // the name of individuals indicating objects in the scene
    public static final String B = "b";
    public static final String B1 = "b1";
    public static final String B2 = "b2";
    public static final String C = "c";
    public static final String C1 = "c1";
    public static final String C2 = "c2";
    public static final String M = "m";

    // it is called before to format the scene. The input is the number of objects in the scene
    private static void clear( int cnt){
        objects.clear();
        relations.clear();
        objectcount = cnt;
    }
    // manipulates the objects and relations set to represents a scene S1
    private static void formatS1(){
        clear(2);

        objects.add( new SpatialObject( CUP, C, 1));
        objects.add( new SpatialObject( BOOK, C, .3));

        objects.add( new SpatialObject( CUP, B, .1));
        objects.add( new SpatialObject( BOOK, B, 1));

        relations.add( new SpatialRelation( C, LEFT, B, 1));
        relations.add( new SpatialRelation( B, RIGHT, C, 1));

        relations.add( new SpatialRelation( B, LEFT, C, .2));
        relations.add( new SpatialRelation( C, RIGHT, B, .1));
    }
    // manipulates the objects and relations set to represents a scene S2
    private static void formatS2(){
        clear(2);

        objects.add( new SpatialObject( CUP, C, 1));
        objects.add( new SpatialObject( MOUSE, C,.05));

        objects.add( new SpatialObject( CUP, M,.01));
        objects.add( new SpatialObject( MOUSE, M, 1));

        relations.add( new SpatialRelation( M, LEFT, C, .5));
        relations.add( new SpatialRelation( M, FRONT, C, .5));
        relations.add( new SpatialRelation( C, RIGHT, M, .5));
        relations.add( new SpatialRelation( C, BEHIND, M, .5));

      /*  relations.add( new SpatialRelation( M, RIGHT, C, .5));
        relations.add( new SpatialRelation( M, BEHIND, C, .5));
        relations.add( new SpatialRelation( C, LEFT, M, .5));
        relations.add( new SpatialRelation( C, FRONT, M, .5));*/
    }
    // manipulates the objects and relations set to represents a scene S3
    private static void formatS3(){
        clear(2);

        objects.add( new SpatialObject( CUP, B, .2));
        objects.add( new SpatialObject( BOOK, B, 1));

        objects.add( new SpatialObject( CUP, C, 1));
        objects.add( new SpatialObject( BOOK, C, .1));

        relations.add( new SpatialRelation( B, LEFT, C, 1));
        relations.add( new SpatialRelation( C, RIGHT, B, 1));

        //relations.add( new SpatialRelation( B, RIGHT, C, .1));
        //relations.add( new SpatialRelation( C, LEFT, B, .05));
    }
    // manipulates the objects and relations set to represents a scene S4
    private static void formatS4(){
        clear(3);

        objects.add( new SpatialObject( CUP, C, 1));
        objects.add( new SpatialObject( BOOK, C, .1));

        objects.add( new SpatialObject( CUP, B2, .3));
        objects.add( new SpatialObject( BOOK, B2, 1));

        objects.add( new SpatialObject( CUP, B1, .2));
        objects.add( new SpatialObject( BOOK,B1, 1));

        relations.add( new SpatialRelation( C, LEFT, B1, 1));
        relations.add( new SpatialRelation( C, LEFT, B2, 1));
        relations.add( new SpatialRelation( B1, RIGHT, C, 1));
        relations.add( new SpatialRelation( B1, LEFT, B2, 1));
        relations.add( new SpatialRelation( B2, RIGHT, C, 1));
        relations.add( new SpatialRelation( B2, RIGHT, B1, 1));

       /* relations.add( new SpatialRelation( C, RIGHT, B1, .1));
        relations.add( new SpatialRelation( C, RIGHT, B2, .05));
        relations.add( new SpatialRelation( B1, LEFT, C, .2));
        relations.add( new SpatialRelation( B1, RIGHT, B2, .3));
        relations.add( new SpatialRelation( B2, LEFT, C, .1 ));
        relations.add( new SpatialRelation( B2, LEFT, B1, .05));*/
    }
    // manipulates the objects and relations set to represents a scene S5
    private static void formatS5(){
        clear(3);

        objects.add( new SpatialObject( CUP, C,1));
        objects.add( new SpatialObject( BOOK, C, .1));
        objects.add( new SpatialObject( MOUSE, C, .2));

        objects.add( new SpatialObject( CUP, M, .3));
        objects.add( new SpatialObject( BOOK, M, .2));
        objects.add( new SpatialObject( MOUSE, M, 1));

        objects.add( new SpatialObject( CUP, B, .1));
        objects.add( new SpatialObject( BOOK, B, 1));
        objects.add( new SpatialObject( MOUSE, B, .2));


        relations.add( new SpatialRelation( M, FRONT, C, 1));
        relations.add( new SpatialRelation( M, LEFT, B, 1));
        relations.add( new SpatialRelation( M, LEFT, C, 1));

        relations.add( new SpatialRelation( C, BEHIND, M, 1));
        relations.add( new SpatialRelation( C, RIGHT, M, 1));
        relations.add( new SpatialRelation( C, LEFT, B, 1));

        relations.add( new SpatialRelation( B, RIGHT, M, 1));
        relations.add( new SpatialRelation( B, RIGHT, C, 1));
        relations.add( new SpatialRelation( B, BEHIND, M, 1));

        /*relations.add( new SpatialRelation( M, BEHIND, C, .01));
        relations.add( new SpatialRelation( M, RIGHT, C, .05));
        relations.add( new SpatialRelation( M, RIGHT, C, .01));

        relations.add( new SpatialRelation( C, FRONT, M, .01));
        relations.add( new SpatialRelation( C, LEFT, M, .03));
        relations.add( new SpatialRelation( C, LEFT, B, .01));

        relations.add( new SpatialRelation( B, LEFT, M, .03));
        relations.add( new SpatialRelation( B, LEFT, C, .1));
        relations.add( new SpatialRelation( B, FRONT, C, .1));*/
    }
    // manipulates the objects and relations set to represents a scene S6
    private static void formatS6(){
        clear(3);

        objects.add( new SpatialObject( CUP, C1, 1));
        objects.add( new SpatialObject( BOOK, C1, .2));

        objects.add( new SpatialObject( CUP, C2,1));
        objects.add( new SpatialObject( BOOK, C1, .1));

        objects.add( new SpatialObject( CUP, B, .2));
        objects.add( new SpatialObject( BOOK, B, 1));

        // todo no feasible configurations for the placer
        relations.add( new SpatialRelation( C1, LEFT, B, 1));
        relations.add( new SpatialRelation( C1, FRONT, C2,1 ));
        relations.add( new SpatialRelation( C1, LEFT, C2, 1));

        relations.add( new SpatialRelation( C2, BEHIND, C1, 1));
        relations.add( new SpatialRelation( C2, LEFT, B, 1));
        relations.add( new SpatialRelation( C2, RIGHT, C1, 1));

        relations.add( new SpatialRelation( B, RIGHT, C1, 1));
        relations.add( new SpatialRelation( B, RIGHT, C1, 1));

        // hp: error on perception but not on spatial relation assertions
        /*relations.add( new SpatialRelation( C1, RIGHT, B, .01));
        relations.add( new SpatialRelation( C1, BEHIND, B, .01));
        relations.add( new SpatialRelation( C1, RIGHT, C1, .3));

        relations.add( new SpatialRelation( C2, FRONT, B, .4));
        relations.add( new SpatialRelation( C2, BEHIND, B, .01));
        relations.add( new SpatialRelation( C2, FRONT,C1, .01 ));

        relations.add( new SpatialRelation( B, BEHIND, C1, .2));
        relations.add( new SpatialRelation( B, BEHIND, C2, .02));*/
    }


    /**
     * The main function to show how to use fuzzy SIT recognition and learning API.
     * To log the operation made by those call manipulate the logging flags
     * {@link it.emarolab.fuzzySIT.FuzzySITBase#FLAG_LOG_SHOW} and
     * {@link it.emarolab.fuzzySIT.FuzzySITBase#FLAG_LOG_VERBOSE}
     * @param args not used!
     */
    public static void main(String[] args) {

        // instanciate a T-Box with the default T-Box ontology and reasoner configuration file
        SITABox r;
        SITTBox h = new SITTBox();

        format();
        r = new SITABox(h, objects, relations);
        h.learn( "Scene1", r);

        /*
        System.out.println("-------------------------  S1   ------------------------");
        // create S1 and recognise it
        formatS1();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S1
        h.learn( "Scene1", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S1 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");



        System.out.println("-------------------------  S2   ------------------------");
        // create S2 and recognise it
        formatS2();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S2
        h.learn( "Scene2", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S2 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");



        System.out.println("-------------------------  S3   ------------------------");
        // create S3 and recognise it
        formatS3();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S3
        h.learn( "Scene3", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S3 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");



        System.out.println("-------------------------  S4   ------------------------");
        // create S4 and recognise it
        formatS4();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S4
        h.learn( "Scene4", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S4 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");


        System.out.println("-------------------------  S5   ------------------------");
        // create S5 and recognise it
        formatS5();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S5
        h.learn( "Scene5", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S5 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");



        System.out.println("-------------------------  S6   ------------------------");
        // create S6 and recognise it
        formatS6();
        r = new SITABox(h, objects, relations);
        System.out.println("------------------------- learn ------------------------");
        // learn S6
        h.learn( "Scene6", r);
        System.out.println("---------------------- recognition ---------------------");
        // recognise again S6 (hp: full recognition since learned)
        new SITABox(h, objects, relations);
        System.out.println("--------------------------------------------------------");


        System.out.println("-------------------------  show ------------------------");
        // shows the inferred and learned SIT scene hierarchy
        h.show();
        // saved the augmented ontology
        h.saveTbox("src/test/resources/learnedTest.fuzzydl");
        */
    }


    private static void format(){
        clear(2);

        objects.add( new SpatialObject( BOOK, B, .7));

        objects.add( new SpatialObject( CUP, C, .9));
        objects.add( new SpatialObject( BOOK, C, .1));

        relations.add( new SpatialRelation( B, LEFT, C, .6));
        relations.add( new SpatialRelation( C, RIGHT, B, .6));
    }

}
