package it.emarolab.fuzzySIT;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;

/**
 * The base class containing common constant and vocabulary.
 * <p>
 *    This interface should be implemented by all the classes of the
 *    fuzzy SIT architecture. It is used to propagate common
 *    constants, vocabulary and static functions.
 *
 * <div style="text-align:center;"><small>
 * <b>File</b>:        it.emarolab.fuzzySIT.FuzzySITBase <br>
 * <b>Licence</b>:     GNU GENERAL PUBLIC LICENSE. Version 3, 29 June 2007 <br>
 * <b>Author</b>:      Buoncompagni Luca (luca.buoncompagni@edu.unige.it) <br>
 * <b>affiliation</b>: EMAROLab, DIBRIS, University of Genoa. <br>
 * <b>date</b>:        27/06/17 <br>
 * </small></div>
 */
public interface FuzzySITBase {


    int RETAIN_PARTICLE_PERCENTAGE = 30; // percentage of particlesNumber

    int MAX_PLACING_TENTATIVE = 10000;//Integer.MAX_VALUE;

    // meters
    double EPSILON = .1; // two object cannot be closer than
    double TABLE_SIZE_X = .8;
    double TABLE_SIZE_Y = .8;

    String NEW_LINE = System.getProperty( "line.separator"); // todo manage also path separator
    String ANNOTATION_PREFIX = "#!# ";
    String ANNOTATION_CARDINALITY_SEPARATOR = " <- ";

    String PATH_BASE = "src/main/resources/";
    String FILE_FUZZYDL_CONFIG = PATH_BASE + "fuzzyDL_CONFIG";
    String FILE_ONTOLOGY_LOAD = PATH_BASE + "example_SIT_kb.fuzzydl";

    boolean FLAG_LOG_SHOW = false;
    boolean FLAG_LOG_VERBOSE = false;

    String INDIVIDUAL_SCENE = "s";

    String CONCEPT_SCENE_TOP = "Scene";
    String CONCEPT_SPATIAL_TOP = "SpatialObject";

    Double LIMIT_LOW = 0.0;
    Double LIMIT_HIGH = 100.0;

    String REPLACE_DOUBLE_POINT = ".";

    String ROLE_SCENE_PREFIX = "has";

    double PLACER_ASSERT_MEMBERSHIPS = 1;
    double PLACER_NOISE_MEMBERSHIP = .1;

    double PLACER_RECOGNITION_STOP = .9;

    String CSV_SEPARATOR = ", ";

    String ROLE_ATLEAST = "atLeast";

    String LEARNER_FILE_AUXILIARY_PATH = ".sitLearned.tmp";

    double ROLE_SHOULDER_MIN = 0;
    double ROLE_SHOULDER_MAX = 20;
    double ROLE_SHOULDER_BOTTOM_PERCENT = 50;// - x%
    String ROLE_SHOULDER_RESOLUTION = "#.####"; // todo adjust

    int LOG_TIME_LENGTH = 20;

    long INITIAL_TIME = System.currentTimeMillis();

    default long getTimeFromStart() {
        return System.currentTimeMillis() - INITIAL_TIME;
    }

    default long log(double refTime, Object msg) {
        return log(FLAG_LOG_SHOW, Math.round(refTime * 1000), msg);
    }

    default long log(long refTime, Object msg) {
        return log(FLAG_LOG_SHOW, refTime, msg);
    }

    default long log(boolean show, double refTime, Object msg) {
        if (show)
            System.out.println(
                    StringPadder.padString(
                            "[" + getTimeFromStart() + "(" + Math.round(refTime * 1000) + ")ms]",
                            LOG_TIME_LENGTH, true)
                            + " : " + msg);
        return System.currentTimeMillis();
    }

    default long log(boolean show, long refTime, Object msg) {
        long t = System.currentTimeMillis();
        if (show)
            System.out.println(
                    StringPadder.padString(
                            "[" + getTimeFromStart() + "(" + (t - refTime) + ")ms]",
                            LOG_TIME_LENGTH, true)
                            + " : " + msg);
        return t;
    }


    // private static class for containing methods to round a double number with a specific number of digits
    class DoubleFormatter {
        // initialise the static decimal formatter
        private static DecimalFormat df, df2;
        static{
            df = new DecimalFormat(ROLE_SHOULDER_RESOLUTION);
            df.setRoundingMode( RoundingMode.CEILING);

            // used only to print string
            df2 = new DecimalFormat( "#.##");
            df2.setRoundingMode( RoundingMode.CEILING);
        }

        /**
         * Format a double number with a number of digits equal to {@link #ROLE_SHOULDER_RESOLUTION}.
         * @param degree the double to format
         * @return the formatted number, with the rounded given number of digits
         */
        public static double roundDegree(double degree) {

            double ll;
            try {
                 ll=df.getNumberInstance().parse(df.format(degree)).doubleValue();
            }
            catch (ParseException e) {
                e.printStackTrace();
                ll = 0;
            }
            return ll;
        }
        /**
         * Format a double number with two digits.
         * @param degree the double to format
         * @return the formatted number, with two rounded digits
         */
        public static double roundDegree2(double degree) {
            return Double.parseDouble( df2.format(degree));
        }
    }

    // this is a private class used to implements the functions for logging strings in an ordered manner
    class StringPadder {

        private StringPadder() {} // not instanciable

        /*
         * It return the {@code message} into a string of the given {@code length}.
         * If the {@code message.length()} is greater than the {@code length} the
         * string '..' is appended where the message is cut. Otherwise, empty space are added.
         * This manipulation is done on the {@code right} or on the left of the message based on the input parameter.
         * @param message the message to pad in a given length
         * @param length  the length of the returning value
         * @param right   set to {@code true} for right flushed message. {@code False} for left.
         * @return the message padded with the given length.
         */
        private static String padString(String message, int length, boolean right) {
            if (message == null)
                return null;
            int size = message.length();
            if (size == length)
                return message;
            if (size < length) {
                if (right)
                    return getOffset(length - size) + message;
                else return message + getOffset(length - size);
            } else {
                String outer = "..";
                size += outer.length();
                if (right)
                    return outer + message.substring(size - length, message.length());
                else return message.substring(0, message.length() - (size - length)) + outer;
            }
        }
        private static String getOffset(int size) { // return an "   " string of given size
            String out = "";
            for (int i = 0; i < size; i++)
                out += " ";
            return out;
        }
    }
}
