package it.emarolab.fuzzySIT.semantic;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.function.DoubleBinaryOperator;

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
@SuppressWarnings("Duplicates")
public class SceneHierarchyTestPaper {

    public static Set<SpatialObject> objects = new HashSet<>(); // the set of objects in the SIT scene to test
    public static Set<SpatialRelation> relations = new HashSet<>(); // the set of relations in the SIT scene to test
    public static int objectcount; // the counter of the amount of objects in the SIT scene to test

    // the name of the types of objects in this example (π)
    public static final String SPHERE = "Sphere";
    public static final String CONE = "Cone";
    // the name of the spatial relations used in this example (ζ)
    public static final String RIGHT = "isRightOf";
    public static final String LEFT = "isLeftOf";
    public static final String FRONT = "isFrontOf";
    public static final String BEHIND = "isBehindOf";
    // the name of individuals indicating objects in the scene
    public static final String S1 = "s1";
    public static final String S2 = "s1";
    public static final String C = "c";

    // it is called before to format the scene. The input is the number of objects in the scene
    private static void clear( int cnt){
        objects.clear();
        relations.clear();
        objectcount = cnt;
    }
    private static void formatObject() {
        clear(3);
        objects.add(new SpatialObject(SPHERE, S1, fuzzyNoise(.9)));
        objects.add(new SpatialObject(SPHERE, S2, fuzzyNoise(.9)));
        objects.add(new SpatialObject(CONE, C, fuzzyNoise(.9)));

        objects.add(new SpatialObject(SPHERE, C, fuzzyNoise(.1)));
        objects.add(new SpatialObject(CONE, S1, fuzzyNoise(.1)));
        objects.add(new SpatialObject(CONE, S2, fuzzyNoise(.1)));
    }
    private static void formatStaticRelation(){
        relations.add(new SpatialRelation(S1, RIGHT, S2, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(S2, LEFT, S1, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(S1, FRONT, S2, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(S2, BEHIND, S1, fuzzyNoise(.5)));
    }
    // manipulates the objects and relations set to represents a scene S1
    private static String sceneId = "-1";
    private static void formatBaseScene() { // sphere cone sphere (in diagonal)
        sceneId = "A";
        formatObject();
        formatStaticRelation();

        relations.add(new SpatialRelation(S1, FRONT, C, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(S1, RIGHT, C, fuzzyNoise(.5)));

        relations.add(new SpatialRelation(S2, LEFT, C, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(S2, BEHIND, C, fuzzyNoise(.5)));

        relations.add(new SpatialRelation(C, BEHIND, S1, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(C, LEFT, S1, fuzzyNoise(.5)));

        relations.add(new SpatialRelation(C, RIGHT, S2, fuzzyNoise(.5)));
        relations.add(new SpatialRelation(C, FRONT, S2, fuzzyNoise(.5)));
    }
    private static void formatConeLeftScene() { // spheres in diagonal, cone 10cm on the right  han A
        sceneId = "B";
        clear(3);
        formatObject();
        formatStaticRelation();

        relations.add(new SpatialRelation(S1, FRONT, C, .60));//fuzzyNoise(.22)));
        relations.add(new SpatialRelation(S1, RIGHT, C, .35));//fuzzyNoise(.78)));

        relations.add(new SpatialRelation(C, BEHIND, S1, .60));//fuzzyNoise(.22)));
        relations.add(new SpatialRelation(C, LEFT, S1, .35));//fuzzyNoise(.78)));

        relations.add(new SpatialRelation(S2, BEHIND, C, .28));//fuzzyNoise(.7)));
        relations.add(new SpatialRelation(S2, LEFT, C, .68));//fuzzyNoise(.3)));

        relations.add(new SpatialRelation(C, FRONT, S2, .28));//fuzzyNoise(.7)));
        relations.add(new SpatialRelation(C, RIGHT, S2, .68));//fuzzyNoise(.3)));
    }

    //private static String noiseLog = "";
    private static final double MIN_NOISE = 0.001;//.01;
    private static final double MAX_NOISE = 0.2;//.2;
    private static double noise(double rangeMin, double rangeMax){
        Random rand = new Random();
        double r = rangeMin + (rangeMax - rangeMin) * rand.nextDouble();
        return r;
    }
    private static double fuzzyNoise( double degree) {
        double n = noise();
       // noiseLog += n + ";\n";
        double noised = degree + n;
        if (noised <= 0)
            return 0.001;
        if (noised >= 1)
            return 1;
        else return noised;

       //return degree;
    }
    private static double noise(){
        double rangeMax = noise(MIN_NOISE,MAX_NOISE);
        double rangeMin = -1*noise(MIN_NOISE,MAX_NOISE);
        return  noise( rangeMin, rangeMax);
    }

    private static void formatSceneConeMoving(SITTBox h, double min, double max, double step) {

        String logPath = "src/test/resources/log/scenepaper/testscene" + System.currentTimeMillis() + ".log";

        try(FileWriter fw = new FileWriter(logPath, true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw))
        {
            String logHeader = "cnt,duration[ms],xCone,yCone,degree"
                    + ",CrightS2,CfrontS2,CleftS1,CbhehindS1"
                    + "; (a="+ (FuzzySITBase.ROLE_SHOULDER_BOTTOM_PERCENT/100)
                    +". minSpace=" + min + ". maxSpace=" + max + ". step=" + step + ". minNoise=" + MIN_NOISE + ". maxNoise=" + MAX_NOISE + ". sceneId=" + sceneId + ")";
            out.println(logHeader);
            System.out.println(logHeader + "\nwritng in " + logPath);

            long initial = System.currentTimeMillis();

            double spatialNoiseRate = (max-min)/5;// /8;

            int cnt = 1;
            int todo = (int) ((2+(max-min)/step) * ((max-min)/step)) + 1;
            double mmx = min;
            while (mmx <= max){
                double mmy = min;
                while (mmy <= max){

                    long t = System.currentTimeMillis();
                    double mx = mmx + noise(-spatialNoiseRate,spatialNoiseRate);
                    double my = mmy + noise(-spatialNoiseRate,spatialNoiseRate);

                    if(mx == 0 || mx == max){
                    }else {
                        // hp: S1 right C, s1 front C, C front S2, c right S2
                        formatObject();
                        formatStaticRelation();

                        double xs1 = mx;//s1 right-left
                        double ys1 = my;//s1 front-behind
                        double p11 = Math.max(0, 1 - (2 / Math.PI) * (Math.atan(ys1 / xs1)));
                        double p12 = Math.max(0, 1 - (2 / Math.PI) * (-Math.atan(ys1 / xs1) + Math.PI / 2));
//c left s1 - s1 right c
//c bheind s1 - s1 front c
                        relations.add(new SpatialRelation(S1, RIGHT, C, p11));//fuzzyNoise(p11)));
                        relations.add(new SpatialRelation(S1, FRONT, C, p12));//fuzzyNoise(p12)));

                        double cleftS1 = Math.abs(1-p11);//fuzzyNoise(Math.abs(1-p11));
                        relations.add(new SpatialRelation(C, LEFT, S1, cleftS1));
                        double cbhehindS1 = Math.abs(1-p12);//fuzzyNoise(Math.abs(1-p12));
                        relations.add(new SpatialRelation(C, BEHIND, S1, cbhehindS1));


                        double xs2 = max - mx;//s2 right-left
                        double ys2 = max - my;//s2 front-behind
                        double p21 = Math.max(0, 1 - (2 / Math.PI) * Math.atan(ys2 / xs2));
                        double p22 = Math.max(0, 1 - (2 / Math.PI) * (-Math.atan(ys2 / xs2) + Math.PI / 2));

                        relations.add(new SpatialRelation(S2, LEFT, C, p21));//fuzzyNoise(p21)));
                        relations.add(new SpatialRelation(S2, BEHIND, C, p22));//fuzzyNoise(p22)));

                        double cRights2 = Math.abs(1-p21);//fuzzyNoise(Math.abs(1-p21));
                        relations.add(new SpatialRelation(C, RIGHT, S2, cRights2));
                        double cfrontS2 = Math.abs(1-p22);//fuzzyNoise(Math.abs(1-p22));
                        relations.add(new SpatialRelation(C, FRONT, S2, cfrontS2));


                        SITABox r = new SITABox(h, objects, relations);
                        double degree = 0;
                        for(Double d : r.getRecognitions().values()) {
                            degree = d;
                            break; // only one
                        }

                        String log = cnt
                                + "," + (System.currentTimeMillis() - t) +"," + mx +"," + my + "," + degree
                                + ","+cRights2+","+cfrontS2+","+cleftS1+","+cbhehindS1
                                //noiseLog;
                                + ";";

                        //noiseLog = "";
                        out.println( log);

                        if(cnt % 50 == 0)
                            System.out.println( "[" + ((System.currentTimeMillis() - initial)/1000) + "s]todo:" + todo + "-->" + log);
                    }

                    cnt++;
                    mmy += step;
                }
                mmx += step;
            }
            out.println("done");
            System.out.println( "log written at " + logPath);
        } catch (IOException e) {
            //exception handling left as an exercise for the reader
        }
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
        SITTBox h = new SITTBox("src/main/resources/simpleSITscenes.fuzzydl");

        formatConeLeftScene();
        r = new SITABox(h, objects, relations);
        h.learn("SceneBase", r);

        formatSceneConeMoving(h, 0, .6, .01);//reasonable 0,.6,0.01 (o 0.03) // not sure to work across 0
    }
}
