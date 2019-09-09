package it.emarolab.fuzzySIT.semantic.hierarchy;

import org.jgrapht.graph.DefaultWeightedEdge;

/**
 * The implementation of a weighted graph edge.
 * <p>
 *     It represents an edge in the inferred Scene Hierarchy by this fuzzy SIT implementation.
 *     Each of those is described by a weighted (∈[0,1]) which represents the fuzzy
 *     membership value indicated by such an edge.
 *     <br>
 *     The graphs implementation is based in the http://jgrapht.org/javadoc/org/jgrapht/graph/DefaultWeightedEdge.html
 *     <a href="http://jgrapht.org/">jgrapht</a> library.
 *
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SceneHierarchyVertex
 * @see it.emarolab.fuzzySIT.semantic.SITTBox
 */
public class SceneHierarchyEdge
        extends DefaultWeightedEdge {

    /**
     * Super constructor based on {@link DefaultWeightedEdge}.
     */
    public SceneHierarchyEdge() {
        super();
    }

    /**
     * @return return the value of the weight for graph visualisation.
     */
    @Override
    public String toString() {
        return String.valueOf(getWeight());
    }
}
