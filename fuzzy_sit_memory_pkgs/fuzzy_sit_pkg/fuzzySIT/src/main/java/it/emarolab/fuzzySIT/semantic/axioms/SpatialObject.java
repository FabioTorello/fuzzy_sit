package it.emarolab.fuzzySIT.semantic.axioms;

import fuzzydl.Concept;
import fuzzydl.KnowledgeBase;
import it.emarolab.fuzzySIT.semantic.SITABox;

/**
 * The container of a fuzzy logic axiom for individual classification.
 * <p>
 *     Its purposes it to be container for fuzzy axiom describing the fuzzy membership of
 *     an element (i.e., {@code object}) to a set (i.e., {@code type}).
 *     <br>
 *     Specifically, it contains: 1) the name of the logic entity,
 *     2) the name of one of its type and 3) its fuzzy membership degree.
 *     <br>
 *     As well as {@link SpatialAtom} it also describes
 *     methods to address the entity for a given fuzzy ontology.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.axioms.SpatialObject <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SITABox
 */
public class SpatialObject
        extends SpatialAtom {

    private String type; // the fuzzy ontological class in which this object belongs

    /**
     * Construct {@code this} axiom as a copy of another.
     * @param copy the axiom to copy.
     */
    public SpatialObject(SpatialObject copy) {
        super(copy);
        this.type = copy.type;
    }

    /**
     * Construct {@code this} axiom full specifying all its members.
     * @param type the name of one of the set in which {@code this object belongs}.
     * @param object the name of the logical entity to describe.
     * @param degree the fuzzy membership degree.
     */
    public SpatialObject(String type, String object, double degree) {
        super(object, degree);
        this.type = type;
    }

    /**
     * Construct {@code this} axiom without specifying its fuzzy membership degree.
     * @param type the name of one of the set in which {@code this object belongs}.
     * @param object the name of the logical entity to describe.
     */
    public SpatialObject(String type, String object) {
        super(object);
        this.type = type;
    }

    /**
     * @return the name of one of the fuzzy set in which {@code this.{@link #getObject()}} belongs to.
     */
    public String getType() {
        return type;
    }

    /**
     * Search for the {@link fuzzydl.Concept} type in which {@code this.{@link #getObject()}} belongs to.
     * @param kb the fuzzy ontology in which retrieve the type of {@code this} object.
     * @return the fuzzy description of the type of this object for the specified ontology.
     */
    public Concept getFuzzyType(KnowledgeBase kb) {
        return kb.getConcept(type);
    }

    /**
     * Two {@link SpatialObject} are equal when: {@link super#equals(Object)} is {@code true}
     * and also {@link #getType()} is equal.
     * @param o the object to be checked against equality with {@code this} axiom.
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialObject)) return false;
        if (!super.equals(o)) return false;

        SpatialObject that = (SpatialObject) o;

        return getType() != null ? getType().equals(that.getType()) : that.getType() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getObject() + " ∈ " + getType() + '(' + getDegree() + ')';
    }
}
