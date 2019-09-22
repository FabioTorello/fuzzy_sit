package fuzzy_sit_pkg.it.emarolab.fuzzySIT.semantic.axioms;

import fuzzydl.Degree;
import fuzzydl.Individual;
import fuzzydl.KnowledgeBase;
import fuzzydl.exception.InconsistentOntologyException;


/**
 * The container of a fuzzy logic atom.
 * <p>
 *     Its purpose is to be a container for a fuzzy atom, composed by
 *     an entity with a give membership value.
 *     <br>
 *     Specifically, it contains: 1) the name of a logic entity and 2) its fuzzy membership degree.
 *     As well as describing methods to address the entity for a given fuzzy ontology.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.axioms.SpatialAtom <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SpatialObject
 * @see SpatialRelation
 */
public class SpatialAtom {

    private String object; // the name of the fuzzy entity
    private double degree; // the membership value

    /**
     * Initialise a {@code copy} of the spatial atom.
     * @param copy the atom to copy in {@code this} container.
     */
    public SpatialAtom( SpatialAtom copy){
        this.object = copy.object;
        this.degree = copy.degree;
    }

    /**
     * Initialise {@code this} atom by specifying all its members.
     * @param object the name of the logical entity to describe.
     * @param degree the fuzzy membership degree.
     */
    public SpatialAtom(String object, double degree) {
        this.object = object;
        this.degree = degree;
    }

    /**
     * Initialise {@code this} atom without assign any fuzzy membership degree.
     * @param object the name of the logical entity to describe.
     */
    public SpatialAtom(String object) {
        this.object = object;
    }

    /**
     * @return the name of the logical entity described in {@code this} atom.
     */
    public String getObject() {
        return object;
    }

    /**
     * Search for {@code this {@link #getObject()}} in the given fuzzy knowledge base.
     * @param kb the fuzzy ontology in which search for {@code this} object.
     * @return the {@link fuzzydl.Individual} representing {@link #getObject()} in the given ontology.
     * @throws InconsistentOntologyException
     */
    public Individual getFuzzyObject(KnowledgeBase kb)
            throws InconsistentOntologyException {
        return kb.getIndividual(object);
    }

    /**
     * @return the fuzzy membership value of {@code this} atom.
     */
    public double getDegree() {
        return degree;
    }

    /**
     * @return the fuzzy membership {@link fuzzydl.Degree} of {@code this} atom.
     */
    public Degree getFuzzyDegree() {
        return Degree.getDegree(degree);
    }

    /**
     * Sets {@code this} fuzzy membership value.
     * @param degree the value to set.
     */
    public void setDegree(double degree) {
        this.degree = degree;
    }

    /**
     * Two atoms are equal when both {@link #getObject()} and {@link #getDegree()} are equal.
     * @param o the object to be checked against equality with {@code this} atom.
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialAtom)) return false;

        SpatialAtom that = (SpatialAtom) o;

        if (Double.compare(that.getDegree(), getDegree()) != 0) return false;
        return getObject() != null ? getObject().equals(that.getObject()) : that.getObject() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getObject() != null ? getObject().hashCode() : 0;
        temp = Double.doubleToLongBits(getDegree());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return object + '(' + degree + ')';
    }
}
