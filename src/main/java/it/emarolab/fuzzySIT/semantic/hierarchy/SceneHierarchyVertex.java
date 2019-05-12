package it.emarolab.fuzzySIT.semantic.hierarchy;

import it.emarolab.fuzzySIT.semantic.SigmaCounters;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * The implementation of a vertex of the SIT learned scene hierarchy.
 * <p>
 *     It specifies each vertex create by the SIT algorithm and describes the scene hierarchy.
 *     Basically, a vertex describes a Scene class (i.e., a fuzzy set) through:
 *     <ul>
 *     <li> {@code scene} (∅): the name of the logical representation of a Scene class, </li>
 *     <li> {@code definition} (Ω ≡ {..., ∑k, ...}): the fuzzy cardinality restriction ({@link SigmaCounters}), </li>
 *     <li> {@code objectType} (Π = {..., πq, ...}): the set of all types name in this scene.</li>
 *     <li> {@code objectDistribution} (Ψ ≡ {..., Γj, ...} where Γj = < ..., <πi,ni>, ... > ; ni ∈ ℝ):
 *                 is a set where each elements indicate an object in the scene (j). Defined through
 *                 a distribution map that related for each possible types (i)
 *                 a type name (πi ≡ Type(Δi) ⊂ Object) and the fuzzy membership value (ni). </li>
 *     </ul>
 *     Such a vertex are connected through the {@link SceneHierarchyEdge}, which specifies an fuzzy implication
 *     between two scenes (i.e., vertices) and its membership value.
 *     <br>
 *     The graphs implementation is based in the http://jgrapht.org/javadoc/org/jgrapht/graph/DefaultWeightedEdge.html
 *     <a href="http://jgrapht.org/">jgrapht</a> library.
 *
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SceneHierarchyVertex
 * @see it.emarolab.fuzzySIT.semantic.SITTBox
 */
public class SceneHierarchyVertex
        implements Serializable{

    private String scene; // the name of the entity representing this scene class (i.e., fuzzy set)
    private SigmaCounters definition; // the cardinality restriction defining the scene
    private Set<String> objectType; // todo remove????
    private Collection<Map<String, Double>> objectDistribution; //<Type,Degree> for all objects todo make a class and check comments

    // TODO work around of a bad definition
    private double memoryScore; // set memory score to a negative number to freeze it to 0
    public double getMemoryScore() {
        if ( memoryScore < 0)
            return 0;
        return memoryScore;
    }
    public void setMemoryScore(double score) {
        this.memoryScore = score;
    }

    /**
     * Instanciate a new scene hierarchy vertex by specifying all its members.
     * @param scene the unique identifying name of {@code this} scene.
     * @param representation the fuzzy A-Box where the objects of {@code this} scene has been described.
     */
    public SceneHierarchyVertex(String scene, SITABox representation) {
        initialise( scene, representation.getDefinition(), representation.getObjectDistribution());
    }
    /**
     * Instanciate a new scene hierarchy vertex by specifying all its members.
     * @param scene the unique identifying name of {@code this} scene.
     * @param definition the fuzzy restriction computed by the SIT for this scene ({@link SigmaCounters}).
     * @param distribution the fuzzy distribution of Types for all the objects in {@code this} scene.
     */
    public SceneHierarchyVertex(String scene, SigmaCounters definition, Collection<Map<String, Double>> distribution) {
        initialise( scene, definition, distribution);
    }
    // common in all constructors.
    private void initialise(String scene, SigmaCounters definition, Collection<Map<String, Double>> distribution) {
        this.scene = scene;
        this.definition = definition;

        this.objectType = new HashSet<>();
        for ( SigmaCounters.Sigma s : definition.getCounters())
            this.objectType.add( s.getType());
        this.objectDistribution = distribution;
    }

    /**
     * @return the unique name of {@code this} scene (∅).
     */
    public String getScene() {
        return scene;
    }

    /**
     * @return the SIT cardinality restriction ({@link SigmaCounters}) for {@code this} scene (Ω).
     */
    public SigmaCounters getDefinition() {
        return definition;
    }

    /**
     * @return the name of all possible Types of objects in {@code this} scene.
     */
    public Set<String> getObjectType(){
        return objectType; // todo remove
    }

    /**
     * @return the number of objects in {@code this} scene (Π).
     */
    public int getObjectNumber() {
        return objectDistribution.size();
    }

    /**
     * @return the object types distribution maps (Ψ).
     */
    public Collection<Map<String, Double>> getObjectDistribution() {
        return objectDistribution;
    }

    @Override
    public String toString() {
        return getScene() + "[" + memoryScore + "]";
    }

    // todo equal ?????
}
