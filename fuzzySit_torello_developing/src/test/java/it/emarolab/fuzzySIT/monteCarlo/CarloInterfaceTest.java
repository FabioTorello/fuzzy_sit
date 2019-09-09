package it.emarolab.fuzzySIT.monteCarlo;

import it.emarolab.fuzzySIT.semantic.SITTBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarloInterfaceTest {

    public static final String BASE_PATH = "src/test/resources/";
    public static final String ONTOLOGY_LOAD_PATH = BASE_PATH + "easyTableSetup.fuzzydl";
    public static final String SCENE_TOGENERATE = "TableSetUp";
    public static final int NUMBER_PARTICLES = 15;

    public static void main(String[] args) {

        String  scene = "FPGK";
        String  path = BASE_PATH + "composedTableSetup.fuzzydl";

        for (int i = 7; i < 10; i++){

            if( i > 5){
                scene = "TableSetUp";
                path = BASE_PATH + "easyTableSetup.fuzzydl";
            }

            try {
                SITTBox h = new SITTBox(path);
                CarloInterface carlo = new CarloImpl(h, scene, NUMBER_PARTICLES);
                carlo.start();

                try {
                    Thread.sleep( 15*60*1000); // 15 min =
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                carlo.kill();
                try {
                    Thread.sleep(140000); // 1.5 min
                    Runtime.getRuntime().freeMemory();
                    carlo.interrupt(); // todo to review !!!!!!!!!
                    h = null;
                    carlo = null;
                    Thread.sleep(140000); // 1.5 min
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
                i--;
            }
        }


        //CarloInterface c1 = new CsvCarloListener( csvFilePath)

        /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c1.kill(); // todo killall
        */
    }
}

class CarloImpl
        extends CarloInterface{

    private ListenerImpl listener;

    public CarloImpl(SITTBox tbox, String toGenerateName, int particlesNumber) {
        super(tbox, toGenerateName, particlesNumber);
        listener.appendHeader();
    }

    @Override
    protected CarloListener getListener() { // called in super constructor
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");
        String date = sdf.format(cal.getTime());
        String csvFilePath = CarloInterfaceTest.BASE_PATH + "log/" + date + ".carlolog";
        listener = new ListenerImpl( csvFilePath);
        return listener;
    }


    class ListenerImpl
            extends CarloInterface.CsvCarloListener{

        public ListenerImpl(String filePath) {
            super(filePath);
        }
    }
}