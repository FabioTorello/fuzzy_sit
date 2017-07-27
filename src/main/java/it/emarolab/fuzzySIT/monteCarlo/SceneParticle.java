package it.emarolab.fuzzySIT.monteCarlo;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.PlacedObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;

import java.util.*;

/**
 * The Monte Carlo particle representation for a fuzzy SIT scene.
 * <p>
 *     It represents a fuzzy SIT scene as a particle to be used during placing
 *     by a Monte Carlo implementation.
 *     In particular, each particles contains {@code objects} and
 *     spatial {@code relations}. The first are implemented as a {@link Set}
 *     of {@link PlacedObject}, where each element has a {@code name}, a {@code type}
 *     and a fuzzy membership value to belong to that type. By adding objects
 *     to the set, this class updates all the {@link SpatialRelation} between them.
 *     <br>
 *     Note that, if a representation of a scene does not have any fuzziness,
 *     we are able to recognise it also with respect to a theoretical scene.
 *     But, since scenes are learned from real samples, we need to add some noise,
 *     in order to recognise a created scene.
 *     In this implementation we consider uncertainties only on the {@code type}
 *     of object, which affect also the number of spatial relations.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.monteCarlo.SceneParticle <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see it.emarolab.fuzzySIT.monteCarlo.CarloInterface
 */
public class SceneParticle
        implements FuzzySITBase{

    // the set of logic object (with x,y coordinates) of this scene
    private Collection<PlacedObject> objects = new HashSet<>();
    // the set of spatial relations between the objects of this scene
    private Collection<SpatialRelation> relations = new HashSet<>();
    // the recognitions of this scene in the fuzzy ontology
    private Map<SceneHierarchyVertex, Double> recognised = null;
    // the best recognition degree in the recognised map
    private Double bestDegree = .0;

    /**
     * Construct a copy of the given scene.
     * @param copy the scene to copy in {@code this} instance.
     */
    public SceneParticle(SceneParticle copy){
        for (PlacedObject o : copy.getObjects())
            objects.add( new PlacedObject( o)); // not noised
        for (SpatialRelation r : copy.relations)
            relations.add( new SpatialRelation( r));
        this.recognised = new LinkedHashMap<>( copy.recognised); // not copied !!!!
        this.bestDegree = copy.bestDegree;
    }

    /**
     * Construct an empty scene.
     */
    public SceneParticle(){
    }

    /**
     * Add a set of objects and compute their spatial relations
     * by using {@link PlacedObject#getRelations(PlacedObject)}.
     * @param spatialObjects the objects to add to this scene.
     */
    public void addParticle(Collection<PlacedObject> spatialObjects) {
        objects.addAll( spatialObjects);
        addRelation( spatialObjects);
    }

    /**
     * Given a theoretical object, with objects of a crisp type, and a set of possible types,
     * it adds to this scene also all the representation of noised {@link PlacedObject} for
     * the other given types, with a given fuzzy degree.
     * @param spatialObjects the set of object to add to this scene.
     * @param allTypes the set of all the types of objects allowed in this scene.
     * @param degree the fuzzy membership value for the noised object added in this scene
     *               for all the {@code spatialObjects} for all the other ({@code ≠ {@link SpatialObject#getType()}} types.
     */
    public void addNoisedParticles(Collection<PlacedObject> spatialObjects, Collection<String> allTypes, double degree){
        long time = System.currentTimeMillis();
        Collection<PlacedObject> noisedObjects = new HashSet<>();

        for ( PlacedObject o : spatialObjects)
            for ( String t : allTypes)
                if ( ! o.getType().equals( t))
                    noisedObjects.add( new PlacedObject( o, t, degree));
        objects.addAll( noisedObjects);
        log(FLAG_LOG_VERBOSE, time, "initialise noised objects: " + noisedObjects);

        addRelation( noisedObjects);
    }

    // updates the relations for all pair of objects in the scenes
    private void addRelation(Collection<PlacedObject> placed){
        long time = System.currentTimeMillis();
        Collection<SpatialRelation> rel = new HashSet<>();
        for ( PlacedObject p1 : placed)
            for ( PlacedObject p2 : placed)
                if ( ! p1.equalsCoordinates( p2))
                    rel.addAll( p1.getRelations( p2));
        relations.addAll( rel);
        log(FLAG_LOG_VERBOSE, time, "initialise relations: " + rel);
    }


    /**
     * @return the complete object set of this scene.
     */
    public Collection<PlacedObject> getNoisedObjects() {
        return objects;
    }

    /**
     * @return only the objects with a crisp {@code type} value ({@code ! {@link PlacedObject#isNoise()}}).
     */
    public Collection<PlacedObject> getObjects() {
        Collection<PlacedObject> out = new HashSet<>();
        for ( PlacedObject o : objects)
            if ( ! o.isNoise())
                out.add( o);
        return out;
    }

    /**
     * @return all the spatial relations of the scene, based on {@link #getNoisedObjects()}.
     */
    public Collection<SpatialRelation> getRelations() {
        return relations;
    }


    /**
     * Given a fuzzy SIT ontology, it ask the reasoner for the classification
     * and store the results. It creates a new {@code new {@link SITABox}(tbox, this)}
     * and ask for {@link SITABox#getRecognitions()}.
     * This calls also updates {@link #getBestDegree()}.
     * @param tbox the fuzzy SIT ontology in which perform classification.
     */
    public void setRecognised( SITTBox tbox) {
        SITABox representation = new SITABox(tbox, this);
        this.recognised = representation.getRecognitions();
        this.setBestDegree();
    }
    private void setBestDegree() {
        for( Double d : recognised.values())
            if ( d > bestDegree)
                bestDegree = d;
    }

    /**
     * @return the recognition of this scene in the SIT class hierarchy.
     * {@code null} if {@link #setRecognised(SITTBox)} is never called.
     */
    public Map<SceneHierarchyVertex, Double>  getRecognised() {
        return recognised; // is null (i.e.: not recognised) if degree is 0
    }

    /**
     * @return the highest value in the {@link #getRecognised()} map.
     * 0 if {@link #setRecognised(SITTBox)} is never called.
     */
    public Double getBestDegree() {
        return bestDegree;
    }

    /**
     * Two {@link SceneParticle} are considered to be equal if
     * both their {@link #getObjects()} and {@link #getRelations()} are equals.
     * @param o the object to check for equality with {@code this}
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SceneParticle)) return false;

        SceneParticle that = (SceneParticle) o;

        if (getObjects() != null ? !getObjects().equals(that.getObjects()) : that.getObjects() != null) return false;
        if (getRelations() != null ? !getRelations().equals(that.getRelations()) : that.getRelations() != null)
            return false;
        /*if (getRecognised() != null ? !getRecognised().equals(that.getRecognised()) : that.getRecognised() != null)
            return false;*/
        return true;
        //return getDegree() != null ? getDegree().equals(that.getDegree()) : that.getDegree() == null;
    }

    @Override
    public int hashCode() {
        int result = getObjects() != null ? getObjects().hashCode() : 0;
        result = 31 * result + (getRelations() != null ? getRelations().hashCode() : 0);
        //result = 31 * result + (getRecognised() != null ? getRecognised().hashCode() : 0);
        //result = 31 * result + (getDegree() != null ? getDegree().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if ( recognised != null)
            return recognised.toString();//.getScene() + "(" + degree + ")";
        else return "??" + "()";
    }

}
