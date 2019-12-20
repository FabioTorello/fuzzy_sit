package it.emarolab;

//import it.emarolab.amor.owlDebugger.OFGUI.GuiRunner;
import fuzzy_sit_memory_msgs.*;
import com.google.common.collect.Lists;
import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.ConnectObjectScene;
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
import it.emarolab.fuzzySIT.perception.simple2D.Point2;
import it.emarolab.fuzzySIT.perception.simple2D.Leg;
import it.emarolab.fuzzySIT.perception.simple2D.Pin;
import it.emarolab.fuzzySIT.perception.simple2D.Table;
import java.lang.Object;
import java.util.Arrays;

public class MemoryService extends AbstractNodeMain {

    private final static Boolean DEFAULT_FULL_ENTITY_IDENTIFIER = false;
    private final static Boolean DEFAULT_SHOW_GUI = false;

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


                            memory.experience( scene(request.getTestRequest().getItems()), true,true);
                            //Show the experience graph
                            //memory.getTbox().show();
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


    private static final List<String> pins = Arrays.asList("1", "2","3","4","5","6","7","8","9","10","11","12");
    private static final List<Double> pinsX = Arrays.asList(-0.30, -0.05, 0.03, 0.27, 0.24, 0.24, 0.27, 0.04, -0.04, -0.28, -0.24, -0.28);
    private static final List<Double> pinsY = Arrays.asList(-0.16, -0.17, -0.15, -0.17, -0.02, 0.05, 0.19, 0.17, 0.17, 0.20, 0.05, -0.05);
    private static final List<String> legsType = Arrays.asList("BED", "CHAIR", "ROOF", "NOT");
    private static int pin;
    public static ConnectObjectScene scene(List<SceneItem> items) {
        ConnectObjectScene scene = new ConnectObjectScene();
        //There is always the type Table object and it is the origin of my system so x=0 and y=0
        scene.addObject(new Table("Table","T0",0.9, new Point2(0.0,0.0)));
        int nItems=0;
        for (SceneItem item: items){
            for (FuzzyDegree degrees:item.getDegrees())
            {
                StringBuilder obj = new StringBuilder();
                obj.append(degrees.getValue());
                obj.append(nItems);
                String object = obj.toString();
                if (pins.contains(degrees.getValue())){
                    pin = Integer.parseInt(degrees.getValue());
                    scene.addObject(new Pin("Pin", "Pin" + degrees.getValue(), degrees.getDegree(), new Point2(pinsX.get(Integer.parseInt(degrees.getValue())-1), pinsY.get(Integer.parseInt(degrees.getValue())-1))));

                }
                else{

                    //for (String pin: pins) {
                        //for (FuzzyDegree deg:item.getDegrees()) {
                            //if (degrees.getValue().contains(pin)) {
                                for (int i = 0; i < legsType.size(); i++) {
                                    if (degrees.getValue().contains(legsType.get(i))) {
                                        String legType = legsType.get(i).toLowerCase();
                                        scene.addObject(new Leg(legType.replace(legType.charAt(0), Character.toUpperCase(legType.charAt(0))) + "Leg", object, degrees.getDegree(), new Point2(pinsX.get(pin - 1), pinsY.get(pin - 1))));
                                        break;
                                    }
                                }
                              //  break;
                           //}
                       // }
                    //}


                }

            }

            nItems++;
        }


        return scene;

    }


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

