package it.emarolab.fuzzySIT.semantic;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The fuzzy representation of objects with a real center of gravity.
 * <p>
 *     It extends a {@link SpatialObject} with the 2D center of mass of the object
 *     (i.e., projection in the table).
 *     It hypotheses that the reference frame in a corner of the table and its axes
 *     follow the edge of the table. Namely, no negative coordinates are allowed.
 *     For example the reference frame can be fixed on the left bottom corner of the 2D table
 *     representation with X pointing on the East and Y to the North.
 *     <br>
 *     This class is in charge to define the meaning of the fuzzy T-Box relations.
 *     Specifically for this implementation, in the {@link #getRelations(PlacedObject)}
 *     method, it defines: {@code isLeftOf, isRightOf, isFrontOf, isBehindOf} as the
 *     membership to a fuzzy cone pointing weest, east, south and north, respectively.
 *     Where the fuzzy value is {@code max[0, 1-(2/π θ)]}, where θ is the phase of the
 *     polar coordinates of an object expressed in a frame centered in another object
 *     (input parameter). Namely, {@code θ = 2/π - atan( (y1-y2)/(x1-x2))}.
 *
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.PlacedObject <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see it.emarolab.fuzzySIT.monteCarlo.SceneParticle
 */
public class PlacedObject
        extends SpatialObject
        implements FuzzySITBase{

    /**
     * The counter of objects, used to auto-generate their unique ID.
     */
    static long ID = 0;
    private static String getNewObjectName( String type){
        return type.substring(0,1) + ID++;
        // overflow not managed!
    }

    // the center of gravity in the 2D table representation
    private double x;
    private double y;
    // used during generation and learning.
    // it represents the caches of this instance to be classified with another object type.
    private boolean isNoise = false;

    /**
     * Construct a copy of the given object.
     * @param copy the object to copy in {@code this} instance.
     */
    public PlacedObject(PlacedObject copy){
        super( copy);
        this.x = copy.x;
        this.y = copy.y;
        this.isNoise = copy.isNoise;
    }

    /**
     * Fully construct this object by specifying all it members.
     * @param type the object Type.
     * @param id the object Name.
     * @param x the X coordinate of the center of gravity projected in the table.
     * @param y the Y coordinate of the center of gravity projected in the table.
     * @param degree the fuzzy membership of {@code this} to be an object of the given {@code type}.
     */
    public PlacedObject(String type, String id, double x, double y, double degree) {
        // poses a unique name to each objects
        // poses a 1 membership to the type of object (no fuzzy on placing !!!!!?????)
        super( type, id, degree);
        this.x = x;
        this.y = y;
    }


    /**
     * Creates a new noised logic representation of an object as
     * a copy of a given object but classified with respect to the given type instead.
     * @param o the object to be affected by noise on type recognition.
     * @param type the specified noised type.
     * @param degree the fuzzy degree of {@code this ≡ o}, to be classified as
     *               the new given {@code type}.
     */
    public PlacedObject(PlacedObject o, String type, double degree) {
        super( type, getNewObjectName( type), degree);
        this.x = o.getX();
        this.y = o.getY();
        isNoise = true;
    }


    /**
     * Generates a new object by randomly place it without overlapping
     * in a {@link #EPSILON} interval. This interval is checked also
     * for table edges. If a solution is not found after {@link #MAX_PLACING_TENTATIVE}
     * the initialisation stops with an error. <br>
     * The name of the new object is automatically defined as {@code type.substring(0,1) + {@link #ID}++}.
     * @param type the name of the new object's type
     * @param alreadyPlaced the set of objects already placed in the table
     * @param degree the fuzzy value of the new object to be of the given {@code type}.
     */
    public PlacedObject(String type, Set<PlacedObject> alreadyPlaced, double degree) {
        super( type, getNewObjectName( type), degree);
        randomInitialise( alreadyPlaced);
    }
    private void randomInitialise(Set<PlacedObject> alreadyPlaced){
        // do not put object too close
        int cnt = 0;
        Double randomX = null;
        Double randomY = null;

        boolean validPosition = false;
        while( !validPosition & cnt++ < MAX_PLACING_TENTATIVE) {

            randomX = ThreadLocalRandom.current().nextDouble(EPSILON, TABLE_SIZE_X - EPSILON); // not on the edge
            randomY = ThreadLocalRandom.current().nextDouble(EPSILON, TABLE_SIZE_Y - EPSILON); // not on the edge

            if( alreadyPlaced.isEmpty())
                validPosition = true;
            else {
                for (PlacedObject p : alreadyPlaced) {
                    if (Math.abs(randomX - p.getX()) > EPSILON) {
                        if (Math.abs(randomY - p.getY()) > EPSILON) {
                            validPosition = true;
                        } else {
                            validPosition = false;
                            break;
                        }
                    } else {
                        validPosition = false;
                        break;
                    }
                }
            }
        }

        //if ( cnt >= MAX_PLACING_TENTATIVE)
        //    System.err.println("CANNOT INITIALISE THE PARTICLE !!!! ");

        if (validPosition){
            this.x = randomX;
            this.y = randomY;
        }

    }

    /**
     * Computes all ({@code isLeftOf, isRightOf, isFrontOf, isBehindOf})
     * the 2D spatial relations between {@code this} object
     * and the specified one.
     * @param p the object used to get the spatial relation w.r.t. {@code this}.
     * @return the set of spatial relations between {@code this} and {@code p}.
     */
    public Set<SpatialRelation> getRelations(PlacedObject p){
        Set< SpatialRelation> out = new HashSet<>();
        // todo general relation types
        addNewRelation( out, "isLeftOf", p, isLeftOf( p));
        addNewRelation( out, "isRightOf", p, isRightOf( p));
        addNewRelation( out, "isBehindOf", p, isBehindOf( p));
        addNewRelation( out, "isFrontOf", p, isFrontOf( p));
        return out;
    }
    private void addNewRelation( Set< SpatialRelation> out, String relation, PlacedObject p, double degree){
        if ( degree > 0)
            out.add( new SpatialRelation( this.getObject(), relation, p.getObject(), degree));
    }

    private double isLeftOf( PlacedObject p){
        if( this.getX() > p.getX())
            return 0; // is on the right
        return spatialMembershipX(p);
    }
    private double isRightOf( PlacedObject p){
        if( p.getX() > this.getX())
            return 0; // is on the left
        return spatialMembershipX(p);
    }
    private double spatialMembershipX( PlacedObject p){
        double px = Math.abs( getX() - p.getX()); // px with respect to <this>
        double py = Math.abs( getY() - p.getY()); // py with respect to <this>

        return 1 - (2 / Math.PI) * Math.atan( py / px);
    }

    private double isFrontOf( PlacedObject p){
        if( this.getY() > p.getY())
            return 0; // is behind
        return spatialMembershipY( p);
    }
    private double isBehindOf(PlacedObject p){
        if( p.getY() > this.getY())
            return 0; // is front
        return spatialMembershipY( p);
    }
    private double spatialMembershipY( PlacedObject p){
        double px = Math.abs( this.getX() - p.getX()); // px with respect to <this>
        double py = Math.abs( this.getY() - p.getY()); // py with respect to <this>

        // todo different from spatialMembershipX
        return 1 - (2 / Math.PI) * ( (Math.PI / 2) - Math.atan( py / px));
    }


    /**
     * @return {@code true} if this object has been constructed with {@link #PlacedObject(PlacedObject, String, double)}.
     */
    public boolean isNoise() {
        return isNoise;
    }

    /**
     * @return the X coordinate of the object's center of gravity project in the table
     */
    public double getX() {
        return x;
    }
    /**
     * @return the Y coordinate of the object's center of gravity project in the table
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the X coordinate of the object's center of gravity project in the table.
     * @param x the 2D X coordinate.
     */
    public void setX( double x) {
        this.x = x;
    }
    /**
     * Sets the Y coordinate of the object's center of gravity project in the table.
     * @param y the 2D Y coordinate.
     */
    public void setY( double y) {
        this.y = y;
    }

    /**
     * Check if center of gravity of {@code this} object is within
     * an interval of radius {@link #EPSILON} fixed in the center
     * of gravity of a given object.
     * @param that the object to check for similar coordinates.
     * @return {@code true} if {@code |that.{@link #getX()} - this.getX{@link #getX()}| < ε }
     *   {@code |that.{@link #getY()} - this.{@link #getY()}| < ε }
     */
    public boolean equalsCoordinates(PlacedObject that) {
        if (Math.abs(that.getX() - getX()) < EPSILON
                & Math.abs(that.getY() - getY()) < EPSILON)
            return true;
        return false;
    }

    /**
     * Two {@link PlacedObject} are equals {@link #equalsCoordinates(PlacedObject)} and
     * {@link #isNoise()} are equals.
     * @param o the object to be checked against equality with {@code this} axiom.
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlacedObject)) return false;
        if (!super.equals(o)) return false;

        PlacedObject that = (PlacedObject) o;

        if ( ! equalsCoordinates( that)) return false;
        return isNoise() == that.isNoise();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        temp = Double.doubleToLongBits(getX());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getY());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (isNoise() ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "{x=" + DoubleFormatter.roundDegree2( x) + ", y=" + DoubleFormatter.roundDegree2( y) + "}[m]";
    }

}
