package it.emarolab.fuzzySIT.semantic.axioms;

import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.SigmaCounters;

import java.io.Serializable;

/**
 * The container of a fuzzy logic axiom for SIT spatial properties.
 * <p>
 *     Its purposes is to contain an axiom describing the fuzzy membership of
 *     a spatial property to be applied from the {@code Scene Individual}
 *     to an {@code object}.
 *     <br>
 *     Specifically, it contains:
 *     <ul>
 *     <li> spatialType: the name of the fuzzy set indicating the conjunction of the object type and
 *            spatial relations (indicated as {@code Δ ≡ TypeProperty ⊂ SpatialObject} in the
 *            {@code fuzzydl} example syntax). It should follow the CamelCase convention
 *            by indicating, for all combinations, the {@code Type(Δ)} as the first
 *            word and the {@code Property(Δ)} as the others. </li>
 *     <li> spatialProperty: is the SIT mapped property from two object into an object
 *            and the abstract scene representation (indicated as {@code δ ≡ "hasΔ"}) in the
 *            {@code fuzzydl} example syntax).</li>
 *     <li> type: the parsed type (i.e., fuzzy set) from {@code Δ}.
 *           Namely: {@code π ≡ Type(Δ) ⊂ Object}.</li>
 *     <li> property: the parsed property from {@code Δ}.
 *           Namely: {@code ζ ≡ Property(Δ) → spatialProperty}.</li>
 *     </ul>
 *     <br>
 *     Those properties are automatically created by {@link SITTBox}
 *     through a parsing in the {@code fuzzydl} syntax. Which follows a certain convention.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.semantic.axioms.SpatialProperty <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 *
 * @see SigmaCounters
 * @see FuzzySITBase
 */
public class SpatialProperty
            implements FuzzySITBase, Serializable{

    private String spatialType; // Δ ≡ "TypeProperty" ⊂ SpatialObject
    private String spatialProperty; //  δ ≡ "hasΔ"
    // Δ parsed by CamelCase: 1th=Type(Δ), other=Property(Δ)
    private String type; // π ≡ Type(Δ) ⊂ Object
    private String property; // ζ ≡ Property(Δ) → spatialProperty


    /**
     * Construct{@code this} object by providing the
     * {@code spatialType (Δ ≡ "TypeProperty" ⊂ SpatialObject)}
     * @param spatialType the CamelCased spatial property to characterise a scene.
     */
    public SpatialProperty(String spatialType) {
        this.spatialType = spatialType;
        String[] parsed = splitCamelCase(spatialType);
        this.type = parsed[0];
        this.property = parsed[1];
        this.spatialProperty = ROLE_SCENE_PREFIX + spatialType;
    }

    /**
     * @return the type identified by the {@code spatialType}
     * given on constructor (π).
     */
    public String getType() {
        return type;
    }

    /**
     * @return the property identified by the {@code spatialType}
     * given on constructor (ζ).
     */
    public String getProperty() {
        return property;
    }

    /**
     * @return the spatial property used by the SIT to catheterise
     * a relation in the abstract scene (δ).
     */
    public String getSpatialProperty() {
        return spatialProperty;
    }

    /**
     * @return the spatial type (Δ), given during constructor.
     */
    public String getSpatialType() {
        return spatialType;
    }

    /**
     * Two SIT properties are equals if {@link #getSpatialType()}
     * is equals.
     * @param o the {@link SpatialProperty} to check for equality.
     * @return {@code true if this == o}.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpatialProperty)) return false;

        SpatialProperty that = (SpatialProperty) o;

        return getSpatialType() != null ? getSpatialType().equals(that.getSpatialType()) : that.getSpatialType() == null;
    }

    @Override
    public int hashCode() {
        return getSpatialType() != null ? getSpatialType().hashCode() : 0;
    }

    @Override
    public String toString() {
        return "'scene' \"" + ROLE_SCENE_PREFIX + "\" " + type + " 'on the' " + property;
    }

    /**
     * Implements the CamelCase parser for a {@code spatialType (Δ)}.
     * It assume that the first word indicates {@code π ≡ Type(Δ) ⊂ Object},
     * while the ohters {@code ζ ≡ Property(Δ) → spatialProperty}
     * @param s the {@code spatialType (Δ)} to parse.
     * @return the collection {@code {π, ζ}.
     */
    public static String[]splitCamelCase(String s) {
        // Δ is parsed by CamelCase where the first part is the
        // type class (⊂ Object), while the second is a name
        // contained in the related spatial relation (ζ).
        String splited = null;
        try {
            splited = humanReadableCamelCase(s).replace(FuzzySITBase.ROLE_SCENE_PREFIX + " ", "");
            String[] out = new String[2];
            int firstSpace = splited.indexOf(" ");
            out[0] = splited.substring(0, firstSpace);
            out[1] = splited.substring(firstSpace + 1);
            return out;
        } catch (Exception e) {
            System.err.println(" BAD Spatial Class (Δ) definition !! " + s + " [" + splited + "]" );
            e.printStackTrace();
            return null;
        }
    }

    /**
     * It manipulate the CamelCase convention by adding spaces.
     * @param s the CamelCased string to convert for human readability.
     * @return th input formatted with spaces.
     */
    public static String humanReadableCamelCase(String s) {
        return s.replaceAll(
                String.format("%s|%s|%s",
                        "(?<=[A-Z])(?=[A-Z][a-z])",
                        "(?<=[^A-Z])(?=[A-Z])",
                        "(?<=[A-Za-z])(?=[^A-Za-z])"
                ), " "); // changes on " " will effects also splitCamelCase()
    }

}
