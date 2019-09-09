package it.emarolab.fuzzySIT.monteCarlo;

import it.emarolab.fuzzySIT.semantic.PlacedObject;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.io.BufferedWriter;
import java.io.File;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class GuidedCarlo
        extends CarloInterface{

    private Queue<String> toGenerateNames;
    public static String csvFilePath;
    private boolean killed = false;

    private SceneHierarchyVertex achievedConfiguration;
    private Collection<PlacedObject> fixedObject;

    public GuidedCarlo(SITTBox tbox, Queue<String> toGenerateNames, int particlesNumber,
                       SceneHierarchyVertex achievedConfiguration, Collection<PlacedObject> fixedObject) {
        super(tbox, toGenerateNames.peek(), particlesNumber);
        this.toGenerateNames = toGenerateNames;

        this.fixedObject = fixedObject;
        this.achievedConfiguration = achievedConfiguration;
    }

    @Override
    protected CarloListener getListener() {
        return new CsvGuidedCarloListener( csvFilePath + "-" + threadId);
    }

    boolean print = true;

    @Override
    protected Set<PlacedObject> poseParticle(Collection<Map<String, Double>> desiredDistribution) {
        // get what is changed
        // hp: desired is always bigger than achieved todo check in the class semantic
        // hp: those are equal  todo make check for similar distribution
        // todo move on constructor??
        if ( achievedConfiguration != null) {
            desiredDistribution = new ArrayList<>( desiredDistribution);
            desiredDistribution.removeAll(achievedConfiguration.getObjectDistribution());

            if ( print) {
                //System.err.println("!!!!! " + achievedConfiguration.getObjectDistribution());
                //System.err.println("!!!!! " + desiredDistribution);
                print = false;
            }
        }

        HashSet<PlacedObject> translatedObject = new HashSet<>();
        for ( PlacedObject p : fixedObject)
            translatedObject.add( new PlacedObject( p));

        // todo better parameter, no out if the table, add scaling factor
        // todo make some particles created from scratch?????
        int choose = ThreadLocalRandom.current().nextInt(0,3); //[0,2]
        if (choose > 0) {
            if (choose == 1) {
                double translateX = ThreadLocalRandom.current().nextDouble(-EPSILON * 3, EPSILON * 3);
                double translateY = ThreadLocalRandom.current().nextDouble(-EPSILON * 3, EPSILON * 3);
                for (PlacedObject p : translatedObject) {
                    p.setX(p.getX() + translateX);
                    p.setY(p.getY() + translateY);
                }
            } else {
                double scale = ThreadLocalRandom.current().nextDouble(0.4, 2.5);
                for (PlacedObject p : translatedObject) {
                    p.setX(p.getX() * scale);
                    p.setY(p.getY() * scale);
                }
            }
        }

        Set<PlacedObject> placed = new HashSet<>( translatedObject);
        placed.addAll( super.poseParticle( desiredDistribution));

        // not allow unfeasible particles (overlapping)
        // todo docheck
        /*List<String> types = new ArrayList<>();
        for( Map<String, Double> shapeDistribution : desiredDistribution) {
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
        Set<PlacedObject> placed = new HashSet<>( translatedObject);
        for ( String t : types)
            // check for not overlapping objects???
            placed.add( new PlacedObject( t, placed, PLACER_ASSERT_MEMBERSHIPS));
        */
        return placed;
    }

    @Override
    public void run() {
        super.run();
        if ( toGenerateNames.isEmpty() | killed){
            for (GuidedCarlo c : subCarlos)
                c.kill();
        } else subCarlos.remove( this);

        synchronized ( this) {
            toGenerateNames.poll();
        }
    }

    @Override
    public void kill() {
        killed = true;
        super.kill();
    }

    private List<GuidedCarlo> subCarlos = new ArrayList<>();

    class GuidedCarloListener
            extends CarloListener{

        private List<SceneParticle> propagatedParticles = new ArrayList<>();


        @Override
        public void run() {
            super.run();
        }

        @Override
        protected void reactToCarlo(List<SceneParticle> carloGuesses) {
            if ( ! carloGuesses.isEmpty()) {
                if ( toGenerateNames.size() > 1){
                    SceneParticle bestParticle = null;
                    for ( int i = 0; i < carloGuesses.size(); i++) {
                        if (!propagatedParticles.contains( carloGuesses.get( i))) {
                            bestParticle = carloGuesses.get( i);
                            break;
                        }
                    }
                    if ( bestParticle != null) {
                        for (SceneHierarchyVertex recognised : bestParticle.getRecognised().keySet()) {
                            if (recognised.equals( toGenerate)) {
                                if ( bestParticle.getRecognised().get( recognised) > .6) { // todo parametrise
                                    Queue<String> toGenerateNameCopy;
                                    synchronized ( GuidedCarlo.this){
                                        toGenerateNameCopy = new LinkedList<>(toGenerateNames);
                                        toGenerateNameCopy.poll();
                                        GuidedCarlo subCarlo = new GuidedCarlo(tbox, toGenerateNameCopy, numberOfParticles,
                                                recognised, bestParticle.getObjects());
                                        subCarlo.start();
                                        subCarlos.add( subCarlo);
                                    }
                                    propagatedParticles.add(bestParticle);
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    class CsvGuidedCarloListener
            extends GuidedCarloListener
            implements CsvCarloListenerInterface{

        private List<SceneParticle> writtenParticles = new ArrayList<>();
        private List<SceneParticle> listenedParticles = new ArrayList<>();

        private BufferedWriter bw = null;

        private File csvFile;

        private int cnt = 0;
        private long iterations = 1;

        public CsvGuidedCarloListener(String filePath) {
            csvFile = new File( filePath);
            bw = openFile();
        }

        @Override
        public void run() {
            appendHeader();
            super.run();
            CsvCarloListenerInterface.super.run();
        }

        @Override
        public void reactToCarlo(List<SceneParticle> carlosParticles) {
            super.reactToCarlo( carlosParticles);
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
        public BufferedWriter openFile() {
            if ( bw == null)
                return CsvCarloListenerInterface.super.openFile();
            else
                synchronized ( bw) {
                    return CsvCarloListenerInterface.super.openFile();
                }
        }

        @Override
        public void appendHeader() {
            synchronized ( bw) {
                CsvCarloListenerInterface.super.appendHeader();
            }
        }

        @Override
        public void closeFile(boolean reopen) {
            synchronized ( bw) {
                CsvCarloListenerInterface.super.closeFile( reopen);
            }
        }

        @Override
        public void appendToFile(String msg) {
            synchronized ( bw) {
                CsvCarloListenerInterface.super.appendToFile(msg);
            }
        }

        @Override
        public BufferedWriter getWriter() {
            synchronized ( bw) {
                return bw;
            }
        }

        @Override
        public void setWriter(BufferedWriter writer) {
            synchronized ( bw) {
                bw = writer;
            }
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

        @Override
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
}
