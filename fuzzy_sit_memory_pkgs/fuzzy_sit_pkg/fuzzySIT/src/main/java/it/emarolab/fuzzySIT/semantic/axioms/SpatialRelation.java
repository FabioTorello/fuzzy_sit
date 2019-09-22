package fuzzy_sit_pkg.it.emarolab.fuzzySIT.semantic.axioms;

import fuzzydl.Individual;
import fuzzydl.KnowledgeBase;
import fuzzydl.exception.InconsistentOntologyException;
import fuzzy_sit_pkg.it.emarolab.fuzzySIT.semantic.SITABox;

/**
 * The container of a fuzzy logic axiom for property description.
 * <p>
 *     Its purposes it to contain a fuzzy axiom describing a property
 *     between an {@code object} and {@code subject}, with a specific membership value.
 *     <br>
 *     Specifically, it contains: 1) the name of two logic entities (i.e., {@code subject, object}),
 *     2) the name of a property linking the two entities and 3) its fuzzy membership degree.
 *     <br>
 *     As well as {@link SpatialAtom} it also describes
 *     methods to address the entity for a given fuzzy ontology.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SITABox
 */
public class SpatialRelation
        extends SpatialAtom {

    private String relation; // the name of the property between the 'subject' and 'object'
    private String subject; // the subject of this relation
    //the object and degree are provided by SpatialAtom

    /**
     * Construct {@code this} axiom by specifying all its members.
     * @param subject the name of the fuzzy atom to be the subject of {@code this} axiom.
     * @param relation the name of the axiom's property that relates {@code subject} with {@code object}.
     * @param object the name of the fuzzy atom to be the object of {@code this} axiom.
     * @param degree the fuzzy membership value of {@code this} axiom.
     */
    public SpatialRelation(String subject, String relation, String object, double degree) {
        super(object, degree);
        this.relation = relation;
        this.subject = subject;
    }

    /**
     * Construct {@code this} axiom without specify its property and membership degree.
     * @param subject the name of the fuzzy atom to be the subject of {@code this} axiom.
     * @param object the name of the fuzzy atom to be the object of {@code this} axiom.
     */
    public SpatialRelation(String subject, String object) {
        super(object);
        this.subject = subject;
    }

    /**
     * Construct {@code this} axiom without specify its membership degree.
     * @param subject the name of the fuzzy atom to be the subject of {@code this} axiom.
     * @param relation the name of the axiom's property that relates {@code subject} with {@code object}.
     * @param object the name of the fuzzy atom to be the object of {@code this} axiom.
     */
    public SpatialRelation(String subject, String relation, String object) {
        super(object);
        this.relation = relation;
        this.subject = subject;
    }

    /**
     * Construct this axiom by copy another axiom.
     * @param copy the axiom to copy in {@code this object}.
     */
    public SpatialRelation(SpatialRelation copy) {
        super( copy);
        this.relation = copy.relation;
        this.subject = copy.subject;
    }

    /**
     * @return the name of the logical entity of {@code this} fuzzy axiom.
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Search for {@code this {@link #getSubject()} in the given fuzzy knowledge base.
     * @param kb the fuzzy ontology in which search for {@code this} subject.
     * @return the {@link fuzzydl.Individual} representing {@link #getSubject()} in the given ontology.
     * @throws InconsistentOntologyException
     */
    public Individual getFuzzySubject(KnowledgeBase kb)
            throws InconsistentOntologyException {
        return kb.getIndividual(subject);
    }

    /**
     * @return the name of the logical property of {@code this} fuzzy axiom.
     */
    public String getRelation() {
        return relation;
    }

    /**
     * Sets the relation of {@code this} fuzzy axiom.
     * @param relation the name of the relation to set.
     */
    public void setRelation(String relation) {
        this.relation = relation;
    }

    /**
     * Two {@link SpatialRelation} are equal when: {@link super#equals(Object)} is {@code true}
     * and both {@link #getRelation()} and {@link #getSubject()} are equal.
     * @param o the object to be checked against equality with {@code this} axiom.
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialRelation)) return false;
        if (!super.equals(o)) return false;

        SpatialRelation that = (SpatialRelation) o;

        if (getRelation() != null ? !getRelation().equals(that.getRelation()) : that.getRelation() != null)
            return false;
        return getSubject() != null ? getSubject().equals(that.getSubject()) : that.getSubject() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getRelation() != null ? getRelation().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return getSubject() + ' ' + relation + '.' + getObject() + '(' + getDegree() + ')';
    }
}
