package it.emarolab.fuzzySIT.monteCarlo;

import it.emarolab.fuzzySIT.semantic.SITTBox;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Queue;

public class GuidedCarloTest {

    public static final String BASE_PATH = "src/test/resources/";
    public static final String ONTOLOGY_LOAD_PATH = BASE_PATH + "composedTableSetup.fuzzydl";
    public static final int NUMBER_PARTICLES = 15;

    public static void main(String[] args) {

        Queue<String> toGenerates = new LinkedList<>();
        toGenerates.add("FK");
        toGenerates.add("FGK");
        toGenerates.add("FPGK");
        SITTBox h = new SITTBox(ONTOLOGY_LOAD_PATH);

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");


        for ( int i = 0; i < 10; i++) {

            Calendar cal = Calendar.getInstance();
            String date = sdf.format(cal.getTime());
            GuidedCarlo.csvFilePath = "src/test/resources/log/" + date + ".carlolog";

            CarloInterface carlo = new GuidedCarlo(h, toGenerates, NUMBER_PARTICLES, null, new ArrayList<>());
            carlo.start();

            try {
                Thread.sleep(600000); // 10 min
                carlo.kill();
                carlo = null;
                Runtime.getRuntime().freeMemory();
                Thread.sleep( 18000); // 3 min
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}