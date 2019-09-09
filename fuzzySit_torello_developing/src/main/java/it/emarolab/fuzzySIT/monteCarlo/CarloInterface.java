package it.emarolab.fuzzySIT.monteCarlo;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.PlacedObject;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;


/**
 * The Monte Carlo approach interface.
 * <p>
 *     This interface implements the common pattern to create a monte carlo discovering approach.
 *     It is used in order to find the center of gravity of objects for a specified fuzzy SIT
 *     scene; e.g., recreate the scene.
 *     <br>
 *     It is implemented as a multi thread implementation where each solution
 *     found is streamed to a {@link CarloListener}.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.monteCarlo.CarloInterface <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see it.emarolab.fuzzySIT.monteCarlo.GuidedCarlo
 */
abstract public class CarloInterface
        extends Thread
        implements FuzzySITBase{

    // todo add isalive

    private static long threadIdCnt = 0; // the thread count
    protected long threadId = threadIdCnt++; // the identifier of this thread

    private boolean stop = false; // flag to stop the execution

    private CarloGuesses carloGuesses = new CarloGuesses(); // the data structure to the listener
    private CarloListener listener; // the lister for any-time solution evaluation

    /**
     * The Monte Carlo approach buffer size. This is the number
     * of particles (i.e., scenes) evaluated at each iteration.
     */
    protected int numberOfParticles;
    /**
     * The Monte Carlo approach max state size. This is the maximum number
     * of particles (i.e., scene) which are kept between iterations.
     */
    protected int numberOfRetainedParticles;
    /**
     * The Monte Carlo approach actual state size. This is the actual number
     * of particles (i.e., scene) which are kept between iterations.
     * It can be lower than {@link #numberOfRetainedParticles} if less solution
     * have been found so far.
     */
    protected int retainedParticleCount;

    /**
     * The Monte Carlo approach state. The set of evaluated scene during this iteration.
     * This list is ordered base on the best fuzzy SIT recognition value, where
     * the first elements (at most {@link #numberOfRetainedParticles}) are
     * {@code retained} particles. Namely, the best scenes reconstructions
     * found so far.
     */
    protected List<SceneParticle> particles = new ArrayList<>();
    /**
     * The fuzzy SIT ontology.
     */
    protected SITTBox tbox;
    /**
     * The scene to recreate with this Monte Carlo approach. This should be a node
     * in the scene hierarchy (see {@link SITTBox#getHierarchy()}).
     */
    protected SceneHierarchyVertex toGenerate;

    /**
     * Construct this Monte Carlo approach by specifying all its fields.
     * During this procedure it calls {@link #getListener()} and {@link #getRetainParticlesNumber(int)}.
     * The execution is not automatically started, see {@link #run()} for more.
     * @param tbox the fuzzy SIT ontology to use.
     * @param toGenerateName the name ({@link SceneHierarchyVertex#getScene()}) of the scene to reconstruct, it must be available in
     *                       {@link SITTBox#getHierarchy()} graph described by the given {@code tbox}.
     * @param particlesNumber the number of particles (i.e., scenes) to be evaluated during execution.
     */
    public CarloInterface(SITTBox tbox, String toGenerateName, int particlesNumber) {
        initialise( tbox, toGenerateName, particlesNumber);
    }
    // common constructor
    private void initialise(SITTBox tbox, String toGenerateName, int particlesNumber) {
        // initialise fields
        this.listener = getListener();
        this.tbox = tbox;
        this.numberOfParticles = particlesNumber;
        this.numberOfRetainedParticles = getRetainParticlesNumber( particlesNumber);
        // find the node in the SIT hierarchy to reconstruct (by name)
        for( SceneHierarchyVertex v : tbox.getHierarchy().vertexSet())
            if ( v.getScene().equals( toGenerateName))
                this.toGenerate = v;
        if ( this.toGenerate == null)
            System.err.println( "Scene " + toGenerateName + " not known !!!");
    }
    /**
     * It returns the {@link #numberOfRetainedParticles} based as a percentage
     * ({@link #RETAIN_PARTICLE_PERCENTAGE}) of the {@link #numberOfParticles}, given as input during
     * constructor.
     * @param particlesNumber The Monte Carlo approach buffer size ({@link #numberOfParticles}).
     * @return The Monte Carlo approach max state size ({@link #numberOfRetainedParticles}).
     */
    protected int getRetainParticlesNumber(int particlesNumber){
        return ( RETAIN_PARTICLE_PERCENTAGE * particlesNumber) / 100;
    }

    /**
     * It returns the listener for the any-time Monte Carlo implementation results.
     * This method is called during constructor and the returning value is
     * assigned to the {@link #listener} field.
     * @return an implementation of {@link CarloListener}, which reacts to the
     * solution found by this implementation.
     */
    abstract protected CarloListener getListener();

    /**
     * It implements this Monte Carlo approach as an any-time system (use {@link #start()} to run it).
     * In this method, the {@link #listener} is started in another thread. Than,
     * while {@link #shouldStop()} is {@code false} it:
     * <ul>
     *      <li>resets {@link #retainedParticleCount} to {@code {@link #particles}.size()};</li>
     *      <li>calls {@link #placeParticles()};</li>
     *      <li>calls {@link #validateParticles()};</li>
     *      <li>updates {@link #carloGuesses}, which notifies also the {@link #listener};</li>
     *      <li>calls {@link #clearParticles()}.</li>
     * </ul>
     */
    @Override
    public void run() {

        listener.start();
        //noinspection SynchronizeOnNonFinalField   no setters!!!!
        synchronized ( listener) {
            try {
                listener.wait();
            } catch (InterruptedException e) {}
        }

        while ( ! shouldStop()){

            retainedParticleCount = particles.size();

            // they should manipulate listenedParticles
            //System.out.println( " !!!!! 1 " + particles);
            placeParticles();
            //System.out.println( " !!!!! 2 " + particles);
            validateParticles();
            System.out.println( "[" + toGenerate.getScene() + "] running " + threadId + " :: " + particles);
            carloGuesses.put( particles);
            clearParticles();
            //System.out.println( " !!!!! 4 " + particles);
        }
        synchronized (carloGuesses){
            carloGuesses.notify();
        }
        System.out.println( "[" + toGenerate.getScene() + "] stopping " + threadId + " :: " + particles);
    }

    /**
     * Called at each {@link #run()} iteration, its purpose is to fill the {@link #particles} list
     * with new scene, to be evaluated.
     * <br>
     * In this implementation, it suppose to have a cleared list, with only the {@code retained particles}.
     * It adds element to the particles list until its size is equal to {@link #numberOfParticles})
     * (and keep trying for {@link #MAX_PLACING_TENTATIVE}).
     * For each new particles it call {@link #poseParticle(Collection)} by giving as input the {@link SceneHierarchyVertex#getObjectDistribution()}
     * of the scene to reproduce (i.e., {@link #toGenerate}). Then, the obtained Set of {@link PlacedObject}
     * is added to {@link SceneParticle} (which computes also the spatial relations among them),
     * finally also {@link SceneParticle#addNoisedParticles(Collection, Collection, double)} with
     * {@link #PLACER_NOISE_MEMBERSHIP} degree are introduced.
     */
    protected void placeParticles(){
        // create particle
        int cnt = particles.size();
        while( cnt < numberOfParticles & cnt <= MAX_PLACING_TENTATIVE & !shouldStop()) {
            Set<PlacedObject> spatialObjects = poseParticle( toGenerate.getObjectDistribution());
            SceneParticle particle = new SceneParticle();
            particle.addParticle( spatialObjects);
            particle.addNoisedParticles( spatialObjects, tbox.getObjectType(), PLACER_NOISE_MEMBERSHIP);
            particles.add( particle);
            cnt++;
        }
        if ( cnt >= MAX_PLACING_TENTATIVE)
            System.err.println( "CANNOT INSTANCIATE ALL PARTICLES !!! only " + cnt + " will be used.");
    }

    /**
     * This method is called during each {@link #run()} iterations by {@link #placeParticles()}.
     * Its purposes is to assign a feasible center of gravity to each objects in the scene.
     * Where, those objects are specified with the fuzzy distribution of its type.
     * <br>
     * This implementation for each objects choose a type with respect to the given distribution.
     * Than, it creates a {@link PlacedObject}, which assigns a random center of gravity to such an object.
     * @param distributions a list in which each element consist in the type distribution of an object in the
     *                      scene (se the size of the {@link Collection} is the number of objects, see {@link SceneHierarchyVertex#getObjectDistribution()}).
     *                      So the {@link Map} contains the name of the type of object as key and
     *                      the relative fazzy value of being of that type, as value.
     * @return the set of object representing a scene particle.
     */
    protected Set<PlacedObject> poseParticle(Collection<Map<String, Double>> distributions){
        List<String> types = new ArrayList<>();
        // choose the type of each objects based on the given distribution
        for( Map<String, Double> shapeDistribution : distributions) {
            Map<String, Double> probability = new HashMap<>();
            for ( String key : shapeDistribution.keySet()) {
                Double rnd = getRandom( 0.01, shapeDistribution.get( key) + .01);
                probability.put( key, rnd);
            }
            Double maxProb = .0;
            String maxKey = "";
            for ( String key : probability.keySet()){
                Double prob = probability.get( key);
                if (  prob > maxProb){
                    maxProb = prob;
                    maxKey = key;
                }
            }
            types.add( maxKey);
        }
        Set<PlacedObject> placed = new HashSet<>();
        for ( String t : types)
            // assign center of mass and check for not overlapping objects
            placed.add( new PlacedObject( t, placed, PLACER_ASSERT_MEMBERSHIPS));
        return placed;
    }

    /**
     * This method is called during each {@link #run()}.
     * Its purposes is to {@link SceneParticle#setRecognised(SITTBox)} of all the {@link #particles}.
     * After this computation, it calls {@link #orderParticles()}.
     */
    protected void validateParticles() {
        for (int i = retainedParticleCount; i < particles.size(); i++){ // do not revalidate the retained  listenedParticles
            SceneParticle p = particles.get( i);
            p.setRecognised( tbox);
        }
        orderParticles();
    }

    /**
     * This method is called during each {@link #run()} iterations by {@link #validateParticles()}.
     * Its purpose is to order the recognition of the scene particles to have the particles to retain in
     * the initial positions. This is used during the particles cleaning between two Monte Carlo iterations.
     * <br>
     * In this implementation, it calls {@link #getClass()} and {@link #getRecognised()}.
     * Than it clear {@link #particles} and adds the recognised particles firsts and the classified
     * one later.
     */
    protected void orderParticles(){ // it affects also shouldStop()
        // the order affect the set best degree !!!!!!!!!
        List<SceneParticle> classified = getClassified(); // sets the best degree among other scenes
        List<SceneParticle> recognised = getRecognised(); // sets the best degree among the toGenerate scene
        particles.clear();
        particles.addAll( recognised); // first the one belong toGenerate from high to lower degree
        particles.addAll( classified); // than the one belong to other scenes from high to lower degree
    }
    /**
     * It scans the {@link #particles} list and finds the generated scene recognised as {@link #toGenerate}.
     * Then those recognised class are ordered with a decreasing fuzzy membership. Finally the
     * set of those particles is returned.
     * @return returns the ordered set (highest first) of the generated particles which have been recognised
     * as the {@link #toGenerate} scene.
     */
    protected List<SceneParticle> getRecognised(){ // must be already sort from best to worst
        List<SceneParticle> recognised = new ArrayList<>();
        for ( SceneParticle p :particles) {
            boolean found = false;
            //double bestDegree = 0;
            for (SceneHierarchyVertex v : p.getRecognised().keySet()) {
                if ( toGenerate.equals(v)) {
                    found = true;
                    /*double degree = p.getRecognised().get(v);
                    if (bestDegree < degree)
                        bestDegree = degree;*/
                    break;
                }
            }
            if ( found) {
                //p.setBestDegree(bestDegree);
                recognised.add(p);
            }
        }
        recognised.sort(Comparator.comparing(SceneParticle::getBestDegree).reversed());
        //Collections.reverse( recognised);
        return recognised;
    }
    /**
     * It scans the {@link #particles} list and finds the generated scene recognised in the fuzzy SIT hierarchy,
     * but not as {@link #toGenerate}.
     * Then those recognised class are ordered with a decreasing fuzzy membership. Finally the
     * set of those particles is returned.
     * @return returns the ordered set (highest first) of the generated particles which have been recognised
     * in the fuzzy SIT hierarchy, but not as the {@link #toGenerate} scene.
     */
    protected List<SceneParticle> getClassified(){  // must be already sort from best to worst
        List<SceneParticle> classified = new ArrayList<>();
        for ( SceneParticle p :particles) {
            if (p.getRecognised() != null) {
                boolean recognised = false;
                //double bestDegree = 0;
                for (SceneHierarchyVertex v : p.getRecognised().keySet()) {
                    if (toGenerate.equals(v)) {
                        recognised = true;
                        break;
                    }/* else {
                        double degree = p.getRecognised().get(v);
                        if (bestDegree < degree)
                            bestDegree = degree;
                    }*/
                }
                if (!recognised) {
                    //p.setBestDegree(bestDegree);
                    classified.add(p);
                }
            }
        }
        classified.sort(Comparator.comparing(SceneParticle::getBestDegree).reversed());
        //Collections.reverse( classified);
        return classified;
    }

    // it does not given the results to the lister as soon as their are produced,
    // but when the evaluation of all the listenedParticles is done.
    // it must set retainedParticleCount
    protected void clearParticles() {
        List<SceneParticle> toRemove = new ArrayList<>();
        for (int i = numberOfRetainedParticles; i < particles.size(); i++)
            toRemove.add( particles.get( i));
        particles.removeAll( toRemove);
    }

    protected <T> T getRandomInCollection(Collection<T> coll) {
        int num = (int) (Math.random() * coll.size());
        for(T t: coll) if (--num < 0) return t;
        throw new AssertionError();
    }
    protected <T> T getRandomInCollection(Collection<T> coll, int maxIndex) {
        int num = (int) (Math.random() * maxIndex + 1);
        for(T t: coll) if (--num < 0) return t;
        throw new AssertionError();
    }
    protected double getRandom( double min, double max){
        return ThreadLocalRandom.current().nextDouble( min, max + .01); // todo parametrise
    }

    protected boolean shouldStop(){
        if ( particles.size() > 0) {
            Map<SceneHierarchyVertex, Double> bestRecognition = particles.get(0).getRecognised();
            if ( bestRecognition != null)
                if ( bestRecognition.get( toGenerate) != null)
                    if ( bestRecognition.get( toGenerate) >= PLACER_RECOGNITION_STOP)
                        return true;
        }
        return stop;
    }

    public void kill(){
        stop = true;
    } // set should stop to true

    @Override
    public void interrupt() {
        listener.interrupt();
        super.interrupt();
    }

    abstract class CarloListener
            extends Thread
            implements FuzzySITBase{

        @Override
        public void run() {
            while ( ! shouldStop()) {
                synchronized (this){
                    notify();
                }
                reactToCarlo( carloGuesses.get());
            }
        }

        abstract protected void reactToCarlo(List<SceneParticle> carloGuesses);
    }


    class CsvCarloListener
            extends CarloListener
            implements CsvCarloListenerInterface{

        private List<SceneParticle> writtenParticles = new ArrayList<>();
        private List<SceneParticle> listenedParticles = new ArrayList<>();

        private BufferedWriter bw;
        private File csvFile;

        private int cnt = 0;
        private long iterations = 1;

        public CsvCarloListener(String filePath) {
            csvFile = new File( filePath);
            bw = openFile();
        }

        @Override
        public void run() {
            super.run();
            CsvCarloListenerInterface.super.run();
        }

        @Override
        public void reactToCarlo(List<SceneParticle> carlosParticles) {
            CsvCarloListenerInterface.super.reactToCarlo( carlosParticles);
        }


        public List<SceneParticle> getWrittenParticles() {
            return writtenParticles;
        }

        public List<SceneParticle> getListenedParticles() {
            return listenedParticles;
        }

        @Override
        public void setListenedParticles(List<SceneParticle> particles) {
            listenedParticles = particles;
        }

        @Override
        public BufferedWriter getWriter() {
            return bw;
        }

        @Override
        public void setWriter(BufferedWriter writer) {
            bw = writer;
        }

        public File getCsvFile() {
            return csvFile;
        }

        public int getForceWritingCnt() {
            return cnt;
        }

        @Override
        public void increaseForceWritingCnt() {
            cnt += 1;
        }

        @Override
        public void resetForceWritingCnt() {
            cnt = 0;
        }

        public long getIterations() {
            return iterations;
        }

        @Override
        public void increaseIterations() {
            iterations += 1;
        }

        @Override
        public SITTBox getTbox() {
            return tbox;
        }

        @Override
        public SceneHierarchyVertex getToGenerate() {
            return toGenerate;
        }

        @Override
        public long getThreadId() {
            return threadId;
        }


    }

    interface CsvCarloListenerInterface {

        int FORCE_WRITE_CNT = 5; // todo parameterise

        default BufferedWriter openFile(){
            try {
                FileWriter fw  = new FileWriter( getCsvFile(),true);
                BufferedWriter bw = new BufferedWriter(fw);
                return bw;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        default void appendHeader() { // todo add other infos?
            String header = "#T-box file: " + getTbox().getSyntaxLearnedFile() + NEW_LINE
                    + "#To generate: " + getClass().getSimpleName() + " --> " + getToGenerate() + " = " + getToGenerate().getDefinition() + NEW_LINE
                    + "#Hierarchy: " + getTbox().getHierarchy() + NEW_LINE
                    + "#Starting thread " + getThreadId() + " at time:" + System.currentTimeMillis() + NEW_LINE + NEW_LINE;
            appendToFile( header);
            // force writing
            closeFile( true);
        }

        default void closeFile( boolean reopen){
            try {
                getWriter().close();
                if ( reopen)
                    setWriter( openFile());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        default void appendToFile(String msg){
            try {
                getWriter().write( msg);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        default void run() {
            appendToFile( "#Thread " + getThreadId() + " ends: " + System.currentTimeMillis() + NEW_LINE + NEW_LINE);
            closeFile( false);
        }

        default void reactToCarlo(List<SceneParticle> carlosParticles){
            increaseForceWritingCnt();
            boolean process = true;
            if ( getListenedParticles().isEmpty())
                setListenedParticles( carlosParticles);
            else if ( getListenedParticles().equals( carlosParticles))
                process = false;

            if (process) {
                carlosParticles.removeAll( getWrittenParticles());
                for (SceneParticle s : carlosParticles) {
                    String objectsStr = "";
                    for (PlacedObject o : s.getObjects()){
                        objectsStr += o.getObject() + CSV_SEPARATOR +
                                o.getType() + CSV_SEPARATOR +
                                o.getDegree() + CSV_SEPARATOR +
                                o.getX() + CSV_SEPARATOR +
                                o.getY() + CSV_SEPARATOR;
                    }
                    String csv =  getThreadId() + CSV_SEPARATOR +
                            getToGenerate().getScene() + CSV_SEPARATOR +
                            System.currentTimeMillis() + CSV_SEPARATOR +
                            getIterations() + CSV_SEPARATOR +
                            mapToCsv( s.getRecognised()) + CSV_SEPARATOR +
                            s.getObjects().size() + CSV_SEPARATOR +
                            objectsStr + NEW_LINE;

                    //System.out.print( csv);

                    appendToFile( csv);
                }
                getWrittenParticles().addAll( carlosParticles);
            }

            if( getForceWritingCnt() >= FORCE_WRITE_CNT){
                closeFile( true);
            }

            increaseIterations();
        }

        default String mapToCsv( Map<?,?> map){
            if ( map.isEmpty())
                return 0 + "";
            return map.size() + CSV_SEPARATOR + map.toString().replaceAll("\\{","")
                    .replaceAll("}","")
                    .replaceAll("=", CSV_SEPARATOR);
                    //.replaceAll(", ", CSV_SEPARATOR)
        }




        List<SceneParticle> getWrittenParticles();

        List<SceneParticle> getListenedParticles();
        void setListenedParticles( List<SceneParticle> particles);

        BufferedWriter getWriter();
        void setWriter( BufferedWriter bw);

        File getCsvFile();

        int getForceWritingCnt();
        void increaseForceWritingCnt();
        void resetForceWritingCnt();

        long getIterations();
        void increaseIterations();

        SITTBox getTbox();

        SceneHierarchyVertex getToGenerate();

        long getThreadId();
    }

    private class CarloGuesses {

        private List<SceneParticle> particles = new ArrayList<>();
        private boolean available = false;

        public synchronized List<SceneParticle> get() {
            while ( ! available & ! stop) {
                try {
                    wait();
                } catch (InterruptedException e) {}
            }
            available = false;
            notify();
            List<SceneParticle> copy = new ArrayList<>( particles);
            particles.clear();
            return copy;
        }

        public synchronized void put( List<SceneParticle> particles) {
            while ( available & ! stop) {
                try {
                    System.out.println(" put wait");
                    wait();
                    System.out.println(" put released");
                } catch (InterruptedException e) { }
            }
            for ( SceneParticle s : particles)
                this.particles.add( new SceneParticle( s));
            available = true;
            notify();
        }
    }

}
