package it.emarolab.fuzzySIT.semantic;

import fuzzydl.*;
import fuzzydl.exception.FuzzyOntologyException;
import fuzzydl.exception.InconsistentOntologyException;
import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import it.emarolab.fuzzySIT.monteCarlo.SceneParticle;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.util.*;

/**
 * The fuzzy SIT manager for the ontological A-Box
 * <p>
 *     It manages the fuzzy SIT description of a scene and its recognition.
 *     Specifically, during construction it:
 *     <ul>
 *     <li> adds the given objects and spatial relation in the specified {@link SITTBox}. </li>
 *     <li> updates the reasoner and queries the {@link SigmaCounters} of the scene representation to be created.</li>
 *     <li> creates the Scene individual ρ with the queried definition</li>
 *     <li> queries for the classification of ρ in the known scene hierarchy (recognition). </li>
 *     </ul>
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.SITABox <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SITTBox
 * @see it.emarolab.fuzzySIT.monteCarlo.CarloInterface
 */
public class SITABox
        implements FuzzySITBase {

    // todo check for warning
    private SITTBox tbox; // the ontology to be used for scene description and recognition
    private KnowledgeBase abox; // a copy of the fuzzy ontology to be manipulated by this class
    private SigmaCounters definition; // the definition of the scene described by the specified objects and relations
    private Individual scene; // the fuzzy individual describing the scene (ρ)
    private Map<SceneHierarchyVertex, Double> recognitions; // the fuzzy sets classifying the scene individual ρ (recognition).
    private Collection<Map<String, Double>> objectDistribution = new ArrayList<>(); // Ψ: Map<ObjectName, Map< ObjectType, Membership>

    /**
     * Initialise a recogniser by specifying the objects of the scene and its spatial relations.
     * This constructors queries the {@link SigmaCounters} and creates an individual
     * describing the scene individual ({@link #INDIVIDUAL_SCENE}).
     * @param hierarchy the SIT T-Box containing the used ontology.
     * @param objects the set of object of the scene to describe.
     * @param relations the set of spatial relations of the scene to describe.
     */
    public SITABox(SITTBox hierarchy, Set<? extends SpatialObject> objects,
                   Set<SpatialRelation> relations){
        this.tbox = hierarchy;
        this.abox = hierarchy.getTboxCopy(); // always starts with a fresh kb
        try {
            addSpatialRepresentation( objects, relations);
            solveAbox();
            definition = querySigmaCount( objects);
            scene = addSceneIndividual( definition);
            solveAboxAssertions();
            recognitions = recognise( scene, tbox);
        } catch (InconsistentOntologyException |
                FuzzyOntologyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise a recogniser by specifying the {@link SigmaCounters} definition of the scene.
     * This constructors creates an individual
     * describing the scene individual ({@link #INDIVIDUAL_SCENE}).
     * @param hierarchy the SIT T-Box containing the used ontology.
     * @param definition the sigma count definition of a SIT scene individual.
     */
    public SITABox(SITTBox hierarchy, SigmaCounters definition){
        this.tbox = hierarchy;
        this.abox = hierarchy.getTboxCopy(); // always starts with a fresh kb
        try {
            this.definition = definition;
            solveAbox();
            scene = addSceneIndividual( definition);
            solveAboxAssertions();
            recognitions = recognise( scene, tbox);
        } catch (InconsistentOntologyException |
                FuzzyOntologyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialise a recogniser by specifying the scene particle, which contains
     * objects anf their spatial relations.
     * This constructors queries the {@link SigmaCounters} and creates an individual
     * describing the scene individual ({@link #INDIVIDUAL_SCENE}).
     * @param hierarchy the SIT T-Box contained the used ontology.
     * @param particle the scene particle containing objects and their spatial relations.
     */
    public SITABox(SITTBox hierarchy, SceneParticle particle) {
        this.tbox = hierarchy;
        this.abox = hierarchy.getTboxCopy(); // always starts with a fresh kb
        try {
            addSpatialRepresentation( particle.getNoisedObjects(), particle.getRelations());
            solveAbox();
            definition = querySigmaCount( particle.getNoisedObjects());
            scene = addSceneIndividual( definition);
            solveAboxAssertions();
            recognitions = recognise( scene, tbox);
        } catch (InconsistentOntologyException |
                FuzzyOntologyException e) {
            e.printStackTrace();
        }
    }

    // called on constructors, it add fuzzy object individuals and spatial relations (fuzzy properties) between them
    private void addSpatialRepresentation(Collection<? extends SpatialObject> objects, Collection<SpatialRelation> relations)
            throws InconsistentOntologyException, FuzzyOntologyException {

        // Map< ObjectsName, Map< ObjectType, Degree>
        Map<String, Map<String,Double>> objectDistributionMap = new HashMap<>();

        // add individuals belonging to fuzzy class (e.g.: book ∈ Book(.65))
        long time = System.currentTimeMillis();
        for (SpatialObject o : objects) {
            abox.addAssertion(o.getFuzzyObject(abox), o.getFuzzyType(abox), o.getFuzzyDegree());

            if ( objectDistributionMap.containsKey( o.getObject())){
                objectDistributionMap.get( o.getObject()).put( o.getType(), o.getDegree());
            } else {
                Map<String,Double> subMap = new HashMap<>();
                subMap.put( o.getType(), o.getDegree());
                objectDistributionMap.put( o.getObject(), subMap);
            }
        }
        objectDistribution = objectDistributionMap.values();

        time = log(time, " adding " + objects.size() + " individuals : " + objects);

        // add spatial relations between objects (e.g.: cup hasRight.book1(0.7))
        for (SpatialRelation r : relations)
            abox.addRelation(r.getFuzzySubject(abox), r.getRelation(), r.getFuzzyObject(abox), r.getFuzzyDegree());
        log(time, " adding " + relations.size() + " relations : " + relations);
    }
    // called on constructors, it queries the scene relations counters
    private SigmaCounters querySigmaCount(Collection<? extends SpatialObject> objects)
            throws InconsistentOntologyException, FuzzyOntologyException {

            long time = System.currentTimeMillis();
            SigmaCounters sigma = new SigmaCounters();
            for (String s : tbox.getSpatial().keySet()) {
                Set<String> valuatedObjectName = new HashSet<>();
                for (SpatialObject o : objects) {
                    if (!valuatedObjectName.contains(o.getObject())) {
                        try {
                            MinInstanceQuery q = new MinInstanceQuery(abox.getConcept(s), o.getFuzzyObject(abox));
                            double sol = q.solve(abox).getSolution();
                            log(FLAG_LOG_VERBOSE, q.getTotalTime(), "\t?? " + o.getObject() + " ∈ " + s + " (" + sol + ")");
                            if (sol > 0) // comment this for exhaustive sigma count (no open-world assumption)
                                sigma.add(tbox.getSpatial().get(s), sol);
                            valuatedObjectName.add(o.getObject());
                        } catch (NullPointerException e){
                            System.err.println( "\t?? " + o.getObject() + " ∈ " + s + " (" + abox + ")");
                        }
                    }
                }
            }
            log(time, "queried definition " + sigma);
            return sigma;
    }
    // called on constructors, adds the scene individual by using the queries SigmaCounters
    private Individual addSceneIndividual( SigmaCounters sigma)
            throws InconsistentOntologyException, FuzzyOntologyException {
        long initialTime = System.currentTimeMillis();
        Individual i = abox.getIndividual( INDIVIDUAL_SCENE);
        abox.addIndividualToConcept( abox.getConcept(CONCEPT_SCENE_TOP).getType(), i);
        long time = System.currentTimeMillis();
        for ( SigmaCounters.Sigma cnt : sigma.getCounters()) {
            Concept restriction = abox.addDatatypeRestriction( Concept.EXACT_VALUE,
                    cnt.getRoundedCount(), cnt.getRole());
            abox.addAssertion( i, restriction, Degree.ONE);
            time = log(FLAG_LOG_VERBOSE, time, "\t\tscene restriction: " + restriction);
        }
        log( initialTime, "adding scene individual: " + i);
        return i;
    }
    // called on constructors, query for the recognitions of the scene individuals
    private Map<SceneHierarchyVertex, Double> recognise(Individual actualScene, SITTBox tbox)
            throws FuzzyOntologyException {
        long time = System.currentTimeMillis();
        long initialTime = time;
        Set<SceneHierarchyVertex> vertexes = tbox.getHierarchy().vertexSet();
        Map<SceneHierarchyVertex, Double> recognised = new HashMap<>();
        for ( String stored : tbox.getScenes()){
            Query q = new MinInstanceQuery( abox.getConcept( stored), actualScene);
            double degree = q.solve(abox).getSolution();
            if ( degree > 0){
                for ( SceneHierarchyVertex v : vertexes) {
                    if (v.getScene().equals(stored)) {
                        recognised.put( v, degree);
                        break;
                    }
                }
                time = log( time, actualScene + " ∈ " + stored + " (" + degree + ")");
            } else
                log( FLAG_LOG_VERBOSE, q.getTotalTime(),actualScene + " ∉ " + stored);
        }
        log( initialTime, "scene recognition completed, results size " + recognised.size());
        return recognised;
    }

    /**
     * It calls, {@link KnowledgeBase#solveKB()} for a copy of the {@link SITTBox} given on constructor.
     * @throws InconsistentOntologyException
     * @throws FuzzyOntologyException
     */
    public void solveAbox()
            throws InconsistentOntologyException, FuzzyOntologyException {
        long time = System.currentTimeMillis();
        abox.solveKB();
        log( time, "solving A-box");
    }
    /**
     * It calls, {@link KnowledgeBase#solveAssertions()} for a copy of the {@link SITTBox} given on constructor.
     * @throws InconsistentOntologyException
     * @throws FuzzyOntologyException
     */
    public void solveAboxAssertions()
            throws InconsistentOntologyException, FuzzyOntologyException {
        long time = System.currentTimeMillis();
        abox.solveAssertions();
        log( time, "solving A-box assertions");
    }

    /**
     * @return the fuzzy individual describing {@code this} and created during constructors.
     */
    public Individual getScene() {
        return scene;
    }

    /**
     * @return the map of all the recognised class in which the scene individuals belongs to.
     * Those classes are all vertex of the {@link SITTBox#getHierarchy()} graph. Each
     * key of the map as a value representing the fuzzy membership value of such a classification.
     */
    public Map<SceneHierarchyVertex, Double> getRecognitions() {
        return recognitions;
    }

    /**
     * @return the SIT scene individual definition formalised as {@link SigmaCounters}.
     */
    public SigmaCounters getDefinition(){
        return definition;
    }

    /**
     * @return the distribution of the objects given on constructors. It returns a list where each
     * elements represents an object described through a map of possible types and the relative
     * fuzzy membership value.
     */
    public Collection<Map<String, Double>> getObjectDistribution() {
        return objectDistribution;
    }

}
