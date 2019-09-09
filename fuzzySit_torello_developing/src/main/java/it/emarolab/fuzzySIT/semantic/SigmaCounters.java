package it.emarolab.fuzzySIT.semantic;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialProperty;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * The fuzzy representation of cardinality restrictions.
 * <p>
 *     It represents the fuzzy counters of the number of {@link SpatialProperty#getProperty()} (ζ)
 *     applied to a specific {@link SpatialProperty#getType()} (π).
 *     Specifically this class represents just a {@link Set} of {@link Sigma}, where elements with the same ζ or π
 *     are not duplicated. Instead, their fuzzy membership values are summed.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.axioms.SigmaCounters <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see it.emarolab.fuzzySIT.semantic.axioms.SpatialProperty
 * @see it.emarolab.fuzzySIT.semantic
 */
public class SigmaCounters
        implements Serializable, FuzzySITBase {

    // the collection of counters for each π
    private Set< Sigma> counters = new HashSet<>();

    /**
     * Construct and empty {@link Set} manager of {@link Sigma}.
     */
    public SigmaCounters(){}

    /**
     * If {@code this} {@link Set} already contains the specified {@link SpatialProperty},
     * the given fuzzy value is summed to the available one.
     * Otherwise, a new {@link Sigma#Sigma(SpatialProperty, double)} is
     * added to {@code this} {@link Set}.
     * @param property the spatial property to count, its must specify ζ and π.
     * @param value the fuzzy membership value.
     * @return {@code true} if this set did not already contain the specified element.
     */
    public boolean add( SpatialProperty  property, double value){
        Sigma sigma = get(property);
        if ( sigma != null) {
            sigma.sum(value);
            return false;
        }
        return counters.add( new Sigma( property, value));
    }
    /**
     * If {@code this} {@link Set} already contains the specified ζ and π,
     * the given fuzzy value is summed to the available one.
     * Otherwise, a new {@link Sigma#Sigma(String, String, double)} is
     * added to {@code this} {@link Set}.
     * @param type the name of the spatial type π to count.
     * @param relation the name of the spatial property ζ to count.
     * @param value the fuzzy membership value.
     * @return {@code true} if this set did not already contain the specified element.
     */
    public boolean add( String type, String relation, double value){
        Sigma sigma = get( type, relation);
        if ( sigma != null) {
            sigma.sum(value);
            return false;
        }
        return counters.add( new Sigma( type, relation, value));
    }

    /**
     * It adds a counter with the specified {@link SpatialProperty}
     * if {@code this} {@link Set} does not already contains it.
     * In this case, the related fuzzy membership value is set to 0.
     * @param property the spatial property to count, its must specify ζ and π.
     * @return {@code true} if this set did not already contain the specified element.
     */
    public boolean add( SpatialProperty  property){
        return counters.add( new Sigma( property));
    }
    /**
     * It adds a counter with the specified ζ and π
     * if {@code this} {@link Set} does not already contains them.
     * In this case, the related fuzzy membership value is set to 0.
     * @param type the name of the spatial type π to count.
     * @param relation the name of the spatial property ζ to count.
     * @return {@code true} if this set did not already contain the specified element.
     */
    public boolean add( String type, String relation){
        return counters.add( new Sigma( type, relation));
    }

    /**
     * It removes from {@code this} {@link Set} a counter with the specified {@link SpatialProperty}.
     * @param property the spatial property to remove, its must specify ζ and π.
     * @return {@code true} if this set contained the specified element.
     */
    public boolean remove( SpatialProperty property){
        return counters.remove( new Sigma( property));
    }
    /**
     * It removes a counter {@code this} {@link Set} with the specified ζ and π.
     * @param type the name of the spatial type π to remove.
     * @param relation the name of the spatial property ζ to remove.
     * @return {@code true} if this set contained the specified element.
     */
    public boolean remove( String type, String relation){
        return counters.remove( new Sigma( type, relation));
    }
    /**
     * It removes a counter {@code this} {@link Set}.
     * @param sigma the counter to removes, it must specify ζ and π.
     * @return {@code true} if this set contained the specified element.
     */
    public boolean remove( Sigma sigma){
        return counters.remove( sigma);
    }

    /**
     * It checks if {@code this} {@link Set} contains the specified {@link SpatialProperty}.
     * @param property the spatial property to check, its must specify ζ and π.
     * @return {@code true} if this set contains the specified {@link Sigma#Sigma(SpatialProperty)}
     */
    public boolean contains( SpatialProperty property){
        return counters.contains( new Sigma( property));
    }
    /**
     * It checks if {@code this} {@link Set} contains the specified ζ and π.
     * @param type the name of the spatial type π to check.
     * @param relation the name of the spatial property ζ to check.
     * @return {@code true} if this set contains the specified {@link Sigma#Sigma(String, String)}
     */
    public boolean contains( String type, String relation){
        return counters.contains( new Sigma( type, relation));
    }
    /**
     * It checks if {@code this} {@link Set} contains the specified {@link Sigma}.
     * @param sigma the counter to check, it must specify ζ and π.
     * @return {@code true} if this set contains the specified {@link Sigma}
     */
    public boolean contains( Sigma sigma){
        return counters.contains( sigma);
    }

    /**
     * It finds in {@code this} {@link Set} a counter that is {@link Sigma#equals(Object)} to the specified one.
     * Note, that membership value in not considered during equality check.
     * @param sigma the counter to retrieve
     * @return the specified element in {@code this} {@link Set},
     * {@code null} if it does not exists.
     */
    public Sigma get( Sigma sigma){
        for ( Sigma s : counters)
            if ( s.equals( sigma))
                return s;
        return null;
    }
    /**
     * It finds in {@code this} {@link Set} a counter that has the specified {@link SpatialProperty}.
     * Note, that membership value in not considered during equality check.
     * @param property the spatial property to retrieve, its must specify ζ and π.
     * @return the specified element in {@code this} {@link Set},
     * {@code null} if it does not exists.
     */
    public Sigma get( SpatialProperty property){
        for ( Sigma s : counters)
            if ( s.equals( property))
                return s;
        return null;
    }
    /**
     * It finds in {@code this} {@link Set} a counter that has the specified ζ and π.
     * Note, that membership value in not considered during equality check.
     * @param type the name of the spatial type π to retrieve.
     * @param relation the name of the spatial property ζ to retrieve.
     * @return the specified element in {@code this} {@link Set},
     * {@code null} if it does not exists.
     */
    public Sigma get( String type, String relation){
        for ( Sigma s : counters)
            if ( s.getType().equals( type) & s.getRelation().equals( relation))
                return s;
        return null;
    }

    /**
     * It returns all the counters in {@code this} {@link Set} that
     * have the specified {@link Sigma#getType()}.
     * @param type the name of the spatial type π to retrieve.
     * @return a new {@link Set} of not copied counters.
     */
    public Set<Sigma> getByType(String type){
        Set< Sigma> out = new HashSet<>();
        for ( Sigma s : counters)
            if ( s.getType().equals( type))
                out.add( s);
        return out;
    }
    /**
     * It returns all the counters in {@code this} {@link Set} that
     * have the specified {@link Sigma#getRelation()}.
     * @param relation the name of the spatial property ζ to retrieve.
     * @return a new {@link Set} of not copied counters.
     */
    public Set<Sigma> getByRelation(String relation){
        Set< Sigma> out = new HashSet<>();
        for ( Sigma s : counters)
            if ( s.getRelation().equals( relation))
                out.add( s);
        return out;
    }

    /**
     * It returns the names of {@link Sigma#getType()} for all counters in {@code this} {@link Set}.
     * @return a new {@link Set} of copied names.
     */
    public Set<String> getAllTypes(){
        Set<String> out = new HashSet<>();
        for (Sigma s : counters)
            out.add( s.getType());
        return out;
    }
    /**
     * It returns the names of {@link Sigma#getRelation()} for all counters in {@code this} {@link Set}.
     * @return a new {@link Set} of copied names.
     */
    public Set<String> getAllRelations(){
        Set<String> out = new HashSet<>();
        for (Sigma s : counters)
            out.add( s.getRelation());
        return out;
    }

    /**
     * @return {@code this} {@link Set}.
     */
    public final Set<Sigma> getCounters() {
        return counters;
    }

    /**
     * @return the total sum of the fuzzy memberships of all the counters in {@code this} {@link Set}.
     */
    public double getCardinality() {
        double cardinality = 0;
        for ( Sigma s : counters)
            cardinality += s.getCount();
        return cardinality;
    }

    /**
     * It uses {@link #get(SpatialProperty)} to retrieve a counter
     * and adds the specified membership value to it, if it exists.
     * @param property the spatial property to retrieve, its must specify ζ and π.
     * @param addendum the membership value to add.
     */
    public void sum( SpatialProperty property, double addendum){
        Sigma sigma = get( property);
        if( sigma != null)
            sigma.sum( addendum);
    }
    /**
     * It uses {@link #get(String, String)} to retrieve a counter
     * and adds the specified membership value to it, if it exists.
     * @param type the name of the spatial type π to retrieve.
     * @param relation the name of the spatial property ζ to retrieve.
     * @param addendum the membership value to add.
     */
    public void sum( String type, String relation, double addendum){
        Sigma sigma = get( type, relation);
        if ( sigma != null)
            sigma.sum( addendum);
    }

    /**
     * To {@link SigmaCounters} are equals if they have two equal
     * {@link Set} ({@link #getCounters()}).
     * @param o the object ot check for equality.
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SigmaCounters)) return false;
        SigmaCounters that = (SigmaCounters) o;
        return counters != null ? counters.equals(that.counters) : that.counters == null;
    }

    @Override
    public int hashCode() {
        return counters != null ? counters.hashCode() : 0;
    }

    /**
     * It returns a textual description of this object formatted for the simulation GUI
     * ({@link it.emarolab.fuzzySIT.gui})
     * @return a description of {@code this} {@link Set}.
     */
    public String toGuiString(){
        String out = "";
        for ( String s : getAllTypes()) {
            out += NEW_LINE + "\t" + s + ":{";
            Set<Sigma> sigmas = getByType(s);
            int cnt = sigmas.size();
            for (Sigma g : sigmas) {
                out += g.getRelation() + "(" + g.getRoundedCount() + ")";
                if ( --cnt > 0)
                    out += ", ";
                else out += "}";
            }
            out += NEW_LINE;
        }
        return  "Σ → [" + out + " ]";
    }

    @Override
    public String toString() {
        String out = "";
        for ( String s : getAllTypes()) {
            out += "\t" + s + ":{";
            Set<Sigma> sigmas = getByType(s);
            int cnt = sigmas.size();
            for (Sigma g : sigmas) {
                out += g.getRelation() + "(" + g.getRoundedCount() + ")";
                if ( --cnt > 0)
                    out += ", ";
                else out += "}";
            }
        }
        return  "Σ → [" + out + " ]";
    }

    /**
     * The Sigma Counter for a specific {@link SpatialProperty}.
     * <p>
     *     It represents a sigma counter for a {@link SpatialProperty#getProperty()} (ζ)
     *     applied to a specific {@link SpatialProperty#getType()} (π).
     *
     *
     * <div style="text-align:center;"><small>
     * <b>File</b>:        it.emarolab.fuzzySIT.semantic.axioms.Sigma <br>
     * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
     * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
     * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
     * <b>date</b>:        27/06/17 <br>
     * </small></div>
     *
     * @see SigmaCounters
     */
    public class Sigma
            implements Serializable{

        private SpatialProperty property; // the container of ζ and π
        private double count = 0; // the sigma counter for this specific property

        /**
         * Create a new counter (set to 0) with a {@link SpatialProperty#SpatialProperty(String)},
         * where the input is: {@code type+relation}.
         * @param type the name of the spatial type π to count.
         * @param relation the name of the spatial property ζ to count.
         */
        public Sigma(String type, String relation) {
            this.property = new SpatialProperty( type + relation);
        }
        /**
         * Create a new counter (set to 0) with a {@link SpatialProperty},
         * @param property the spatial property to check, its must specify ζ and π.
         */
        public Sigma(SpatialProperty property) {
            this.property = property;
        }

        /**
         * Create a new counter with a {@link SpatialProperty#SpatialProperty(String)},
         * where the input is: {@code type+relation}.
         * @param type the name of the spatial type π to count.
         * @param relation the name of the spatial property ζ to count.
         * @param count the initial value.
         */
        public Sigma(String type, String relation, double count) {
            this.count = count;
            this.property = new SpatialProperty( type + relation);
        }
        /**
         * Create a new counter with a {@link SpatialProperty},
         * @param property the spatial property to check, its must specify ζ and π.
         * @param count the initial value.
         */
        public Sigma( SpatialProperty property, double count) {
            this.count = count;
            this.property = property;
        }

        /**
         * The logic {@code type} ( π ≡ Type(Δ) ⊂ Object) of the spatial relation counted by {@code this} class.
         * @return {@link SpatialProperty#getType()}, based on the constructing parameters.
         */
        public String getType() {
            return property.getType();
        }
        // contains ζ spatialProperty
        /**
         * The logic {@code property} (ζ ≡ Property(Δ) → spatialProperty) of the spatial relation counted by {@code this} class.
         * @return {@link SpatialProperty#getProperty()}, based on the constructing parameters.
         */
        public String getRelation() {
            return property.getProperty();
        }
        /**
         * The logic {@code property} of sit spatial relations (δ ≡ "hasΔ") of the spatial relation counted by {@code this} class.
         * @return {@link SpatialProperty#getSpatialType()}, based on the constructing parameters.
         */
        public String getRole() {
            return property.getSpatialProperty();
        }
        /**
         * The logic {@code type} (π ≡ Type(Δ) ⊂ Object) of the spatial relation counted by {@code this} class.
         * @return {@link SpatialProperty#getType()}, based on the constructing parameters.
         */
        public String getSpatialType() {
            return property.getSpatialType();
        }

        /**
         * @return the fuzzy counting value.
         */
        public double getCount() {
            return count;
        }

        /**
         * @return the fuzzy counting value rounded with {@link it.emarolab.fuzzySIT.FuzzySITBase.DoubleFormatter#roundDegree(double)}.
         */
        public double getRoundedCount(){
            return DoubleFormatter.roundDegree( getCount());
        }

        /**
         * It sums the given value to this {@link #getCount()} value.
         * @param addendum the fuzzy value to add to {@code this} counter.
         */
        public void sum(double addendum){
            this.count += addendum;
        }

        /**
         * It sets the {@link #getCount()} value.
         * @param count the fuzzy counting value to set.
         */
        public void setCount(double count) {
            this.count = count;
        }

        /**
         * A {@link Sigma} is equal to a {@link SpatialProperty} if the latter
         * is equivalent the property given on constructor.<br>
         * Two {@link Sigma}s are equals if both have the same {@link SpatialProperty}
         * given on constructor.
         * @param o the object to check for equality with {@code this}.
         * @return {@code true if this == o}.
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if ((o instanceof SpatialProperty))
                return o.equals( property);
            if (!(o instanceof Sigma)) return false;
            Sigma sigma = (Sigma) o;
            return property != null ? property.equals(sigma.property) : sigma.property == null;
        }

        @Override
        public int hashCode() {
            return property != null ? property.hashCode() : 0;
        }

        @Override
        public String toString() {
            //return property + " " + count;
            return Double.toString(count);
        }
    }
}