package it.emarolab;

//import it.emarolab.amor.owlDebugger.OFGUI.GuiRunner;
import fuzzy_sit_memory_msgs.*;
import com.google.common.collect.Lists;
import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.*;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.message.MessageFactory;
import org.ros.namespace.GraphName;
import org.ros.node.*;
import org.ros.node.parameter.ParameterTree;
import org.ros.node.service.ServiceResponseBuilder;
import org.ros.node.service.ServiceServer;
import java.util.ArrayList;
import java.util.List;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import java.util.Scanner;

import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
import java.lang.Object;
import java.util.Arrays;

public class MemoryService extends AbstractNodeMain {

    private final static Boolean DEFAULT_FULL_ENTITY_IDENTIFIER = false;
    private final static Boolean DEFAULT_SHOW_GUI = false;
    private static int numberFrame=0;

    //The getDefaultNodeName method returns the default name of the node.
    @Override
    public GraphName getDefaultNodeName() {
        return GraphName.of("rosjava/perception2owl");
    }

    //The onStart method is the entry point for your program (or node). The ConnectedNode parameter is the factory we use to build things like 	     Publishers and Subscribers.
    @Override
    public void onStart(final ConnectedNode connectedNode) {
        //rosjava offers full access to the ROS Parameter Server. The Parameter Server is a shared dictionary of configuration parameters 		  accessible to all the nodes at runtime. It is meant to store configuration parameters that are easy to inspect and modify.Parameters 		  are accessible via ParameterTrees
        ParameterTree params = connectedNode.getParameterTree();

        //Create an object representing the memory
       // MemoryImplementation memoryCreation = new MemoryImplementation("memory_service/src/main/resources/table_assembling_memory_example.fuzzydl");
        MemoryImplementation memory = new MemoryImplementation("memory_service/src/main/resources/table_assembling_data_set_memory_example.fuzzydl", "memory_service/src/main/resources/fuzzyDL_CONFIG" );


        //Callback for TestServiceDirective.srv calls
        //newServiceServer(GraphName serviceName, java.lang.String serviceType, ServiceResponseBuilder<T,S> serviceResponseBuilder)
        ServiceServer<TestServiceDirectiveRequest, TestServiceDirectiveResponse> MemoryTestCallback =
                connectedNode.newServiceServer("memory_service", TestServiceDirective._TYPE,
                        (request, response) -> {
                           /* System.out.print("---------------------------------------------"+"\n");
                            System.out.print("Frame "+request.getTestRequest().getFrame()+"\n");
                            for (SceneItem item: request.getTestRequest().getItems()) {
                                System.out.print(item.getGammaI() + "\n");
                                System.out.print(item.getDegreeBed() + "\n");
                                System.out.print(item.getDegreeRoof() + "\n");
                                System.out.print(item.getDegreeChair() + "\n");
                                System.out.print(item.getDegreeNot() + "\n");
                                System.out.print(item.getDegreePin1() + "\n");
                                System.out.print(item.getDegreePin2() + "\n");
                                System.out.print(item.getDegreePin3() + "\n");
                                System.out.print(item.getDegreePin4() + "\n");
                                System.out.print(item.getDegreePin5() + "\n");
                                System.out.print(item.getDegreePin6() + "\n");
                                System.out.print(item.getDegreePin7() + "\n");
                                System.out.print(item.getDegreePin8() + "\n");
                                System.out.print(item.getDegreePin9() + "\n");
                                System.out.print(item.getDegreePin10() + "\n");
                                System.out.print(item.getDegreePin11() + "\n");
                                System.out.print(item.getDegreePin12() + "\n");
                                System.out.print(item.getDegreeTable() + "\n");
                                System.out.print("\n");

                            }

                            for (Relations relation: request.getTestRequest().getRelations()){
                                System.out.print(relation.getGammaSubject() + "\n");
                                System.out.print(relation.getGammaObject() + "\n");
                                System.out.print(relation.getNameRelation() + "\n");
                                System.out.print(relation.getDegreeRelation() + "\n");
                                System.out.print("\n");
                            }*/
                           // memory.experience( scene(request.getTestRequest().getItems(), request.getTestRequest().getRelations(), request.getTestRequest().getFrame()), true,true);
                            //Show the experience graph

                            //THE SCENE GRAPH SHOULD BE VISUALIZE ONLY IN THE END OF THE BAGFILE
                            // (THIS IS ONLY A TEMPTATIVE)

                           /* if( request.getTestRequest().getFrame()==64) {
                                memory.getTbox().show();
                            }*/

                            response.getTestResponse().setResponse("The scene " + " has been loaded" );

                            /*if(request.getTestRequest().getRequest().equals("scene")){
                                memoryCreation.experience( scene0(),true,true);
                                response.getTestResponse().setResponse("There is scene to load");
                            }
                            else {
                                response.getTestResponse().setResponse("There is no scene to load");
                            }*/


                });







    }
 //THIS IS THE VERSION OF "ADD" FUNCTION I HAVE TO USE
 /*public void addObject( FeaturedSpatialObject<F> newObject){
     for ( FeaturedSpatialObject<F> obj : getObjects()) {
         SpatialRelation rel = computeRelation(obj, newObject);
         if ( rel != null)
             relations.add(rel);
     }
     objects.add( newObject);
 }*/

    /*private static final List<String> pins = Arrays.asList("1", "2","3","4","5","6","7","8","9","10","11","12");
    private static final List<Double> pinsX = Arrays.asList(-0.30, -0.05, 0.03, 0.27, 0.24, 0.24, 0.27, 0.04, -0.04, -0.28, -0.24, -0.28);
    private static final List<Double> pinsY = Arrays.asList(-0.16, -0.17, -0.15, -0.17, -0.02, 0.05, 0.19, 0.17, 0.17, 0.20, 0.05, -0.05);
    private static final List<String> legsType = Arrays.asList("BED", "CHAIR", "ROOF", "NOT");
    private static int pin;*/

   public static ConnectObjectScene scene(List<SceneItem> items, List<Relations> relations, long frame) {
        if ((!items.isEmpty())&&(!relations.isEmpty())) {
            //Define the scene
            ConnectObjectScene scene = new ConnectObjectScene("frame" + frame);



            for (SceneItem item : items) {
                /////////////BED LEG////////////////////////////////
                if(item.getType()=="BED_X") {
                    //Add an object type BED_X
                    scene.addObject(new BED_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="BED_MINUS_X") {
                    //Add an object type BED_MINUS_X
                    scene.addObject(new BED_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="BED_Y") {
                    //Add an object type BED_Y
                    scene.addObject(new BED_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="BED_MINUS_Y") {
                    //Add an object type BED_X
                    scene.addObject(new BED_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ////////////CHAIR LEG/////////////////////////////////
                else if(item.getType()=="CHAIR_X") {
                    //Add an object type CHAIR_X
                    scene.addObject(new CHAIR_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="CHAIR_MINUS_X") {
                    //Add an object type CHAIR_MINUS_X
                    scene.addObject(new CHAIR_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="CHAIR_Y") {
                    //Add an object type CHAIR_Y
                    scene.addObject(new CHAIR_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="CHAIR_MINUS_Y") {
                    //Add an object type CHAIR_MINUS_Y
                    scene.addObject(new CHAIR_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ////////////ROOF LEG/////////////////////////////////
                else if(item.getType()=="ROOF_X") {
                    //Add an object type ROOF_X
                    scene.addObject(new ROOF_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="ROOF_MINUS_X") {
                    //Add an object type ROOF_MINUS_X
                    scene.addObject(new ROOF_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="ROOF_Y") {
                    //Add an object type ROOF_Y
                    scene.addObject(new ROOF_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="ROOF_MINUS_Y") {
                    //Add an object type ROOF_MINUS_Y
                    scene.addObject(new ROOF_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ////////////NOT LEG/////////////////////////////////
                else if(item.getType()=="NOT_X") {
                    //Add an object type NOT_X
                    scene.addObject(new NOT_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="NOT_MINUS_X") {
                    //Add an object type NOT_MINUS_X
                    scene.addObject(new NOT_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="NOT_Y") {
                    //Add an object type NOT_Y
                    scene.addObject(new NOT_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType()=="NOT_MINUS_Y") {
                    //Add an object type NOT_MINUS_Y
                    scene.addObject(new NOT_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_1//////////////////////////////////////
                else if(item.getType()=="Pin_1") {
                    //Add an object type Pin_1
                    scene.addObject(new Pin_1(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_2//////////////////////////////////////
                else if(item.getType()=="Pin_2") {
                    //Add an object type Pin_2
                    scene.addObject(new Pin_2(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_3//////////////////////////////////////
                else if(item.getType()=="Pin_3") {
                    //Add an object type Pin_3
                    scene.addObject(new Pin_3(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_4//////////////////////////////////////
                else if(item.getType()=="Pin_4") {
                    //Add an object type Pin_4
                    scene.addObject(new Pin_4(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_5//////////////////////////////////////
                else if(item.getType()=="Pin_5") {
                    //Add an object type Pin_5
                    scene.addObject(new Pin_5(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_6//////////////////////////////////////
                else if(item.getType()=="Pin_6") {
                    //Add an object type Pin_6
                    scene.addObject(new Pin_6(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_7//////////////////////////////////////
                else if(item.getType()=="Pin_7") {
                    //Add an object type Pin_7
                    scene.addObject(new Pin_7(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_8//////////////////////////////////////
                else if(item.getType()=="Pin_8") {
                    //Add an object type Pin_8
                    scene.addObject(new Pin_8(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_9//////////////////////////////////////
                else if(item.getType()=="Pin_9") {
                    //Add an object type Pin_9
                    scene.addObject(new Pin_9(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_10//////////////////////////////////////
                else if(item.getType()=="Pin_10") {
                    //Add an object type Pin_10
                    scene.addObject(new Pin_10(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_11//////////////////////////////////////
                else if(item.getType()=="Pin_11") {
                    //Add an object type Pin_11
                    scene.addObject(new Pin_11(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_12//////////////////////////////////////
                else if(item.getType()=="Pin_12") {
                    //Add an object type Pin_12
                    scene.addObject(new Pin_12(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Table//////////////////////////////////////
                else if(item.getType()=="Table") {
                    //Add an object type Table
                    scene.addObject(new Table(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

            }






            for (Relations relation : relations) {
                scene.getRelations().add(new SpatialRelation(relation.getGammaSubject(), relation.getNameRelation(), relation.getGammaObject(), relation.getDegreeRelation()));
            }

            return scene;
            }
        return null;
   }

   /* public static ConnectObjectScene scene(List<SceneItem> items, int frame) {
        ConnectObjectScene scene = new ConnectObjectScene("frame"+frame);
        //There is always the type Table object and it is the origin of my system so x=0 and y=0
        scene.addObject(new Table("Table","T0",0.9, new Point2(0.0,0.0)));
        int nItems=0;
        for (SceneItem item: items){
            for (FuzzyDegree degrees:item.getDegrees())
            {
                StringBuilder obj = new StringBuilder();
                obj.append(degrees.getValueChair());
                obj.append(nItems);
                String object = obj.toString();
                //if (pins.contains(degrees.getValuePin())){
                    pin = Integer.parseInt(degrees.getValuePin());
                    scene.addObject(new Pin("Pin", "Pin" + degrees.getValuePin(), degrees.getDegreePin(), new Point2(pinsX.get(Integer.parseInt(degrees.getValuePin())-1), pinsY.get(Integer.parseInt(degrees.getValuePin())-1))));

                    //scene.getRelations().add( new SpatialRelation( "Leg1", "CONNECTED", "Pin2", .8));

                //}
                //else{

                    //for (String pin: pins) {
                        //for (FuzzyDegree deg:item.getDegrees()) {
                            //if (degrees.getValue().contains(pin)) {
                                for (int i = 0; i < legsType.size(); i++) {
                                    if (degrees.getValueChair().contains(legsType.get(i))) {
                                        String legType = legsType.get(i).toLowerCase();
                                        scene.addObject(new Leg(legType.replace(legType.charAt(0), Character.toUpperCase(legType.charAt(0))) + "Leg", object, degrees.getDegreeChair(), new Point2(pinsX.get(pin - 1), pinsY.get(pin - 1))));
                                        break;
                                    }
                                }
                              //  break;
                           //}
                       // }
                    //}


               // }

            }

            nItems++;
        }


        return scene;

    }*/

/*rosservice call /memory_service "test_request:
  items:
  - gamma_i: 'g1'
    degrees:
    - value: 'R1'
      degree: 0.9
    x: -0.25
    y: 0.75
  - gamma_i: 'g2'
    degrees:
    - value: 'R2'
      degree: 0.9
    x: 0.75
    y: 0.75
  - gamma_i: 'g3'
    degrees:
    - value: 'R3'
      degree: 0.9
    x: -0.25
    y: 0.25
  - gamma_i: 'g4'
    degrees:
    - value: 'R4'
      degree: 0.9
    x: 0.75
    y: 0.25
  - gamma_i: 'g5'
    degrees:
    - value: 'RC'
      degree: 0.9
    x: 0.25
    y: 0.5"





    rosservice call /memory_service "test_request:
  items:
  - gamma_i: 'g1'
    degrees:
    - value: '1'
      degree: 0.9
  - gamma_i: 'g2'
    degrees:
    - value: 'BED_MINUS_X'
      degree: 0.9"
*/


//     For testing and debugging purposes only
//     You can use this main as entry point in an IDE (e.g., IDEA) to run a debugger

        public static void main (String argv[]) throws java.io.IOException {

            java.lang.String[] args = {"it.emarolab.MemoryService"};
            CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(args));
            NodeConfiguration nodeConfiguration = loader.build();
            MemoryService service = new MemoryService();
            
            NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
            nodeMainExecutor.execute(service, nodeConfiguration);

            //CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(args));
            // NodeConfiguration nodeConfiguration = loader.build();
            //ARMORMainService service = new ARMORMainService();

            //NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
            //nodeMainExecutor.execute(service, nodeConfiguration);
        }
}

