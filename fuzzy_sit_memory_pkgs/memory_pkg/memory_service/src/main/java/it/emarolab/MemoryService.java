package it.emarolab;

//import it.emarolab.amor.owlDebugger.OFGUI.GuiRunner;
import fuzzy_sit_memory_msgs.*;
import com.google.common.collect.Lists;
import it.emarolab.fuzzySIT.FuzzySITBase;
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
        MemoryImplementation memoryCreation = new MemoryImplementation(FuzzySITBase.PATH_BASE + "table_assembling_memory_example.fuzzydl");



        //Callback for TestServiceDirective.srv calls
        //newServiceServer(GraphName serviceName, java.lang.String serviceType, ServiceResponseBuilder<T,S> serviceResponseBuilder)
        ServiceServer<TestServiceDirectiveRequest, TestServiceDirectiveResponse> MemoryTestCallback =
                connectedNode.newServiceServer("memory_service", TestServiceDirective._TYPE,
                        (request, response) -> {
                    if(request.getTestRequest().getRequest().equals("encode")){
                           response.getTestResponse().setResponse("Server: encoding function");
                    }
                        else if(request.getTestRequest().getRequest().equals("store")){
                            response.getTestResponse().setResponse("Server: storing function");
                        }
                            else if(request.getTestRequest().getRequest().equals("consolidate")){
                                response.getTestResponse().setResponse("Server: consolidating function");
                            }
                                else if (request.getTestRequest().getRequest().equals("forget")){
                                    response.getTestResponse().setResponse("Server: forgetting function");
                                }
                                    else{
                                            response.getTestResponse().setResponse("Server response");
                                    }

                });



        // Callback for ArmorDirective.srv calls (single operation)

        /*ServiceServer<ArmorDirectiveRequest, ArmorDirectiveResponse> armorCallback =
                connectedNode.newServiceServer("armor_interface_srv", ArmorDirective._TYPE,
                        (request, response) -> {
                            ARMORCommandExecutive command = new ARMORCommandExecutive(
                                    request.getArmorRequest(), response.getArmorResponse(),
                                    FULL_ENTITY_IDENTIFIER, connectedNode);
                            if (command.getServiceResponse().getSuccess()) {
                                response.setArmorResponse(command.executeCommand());
                            }else{
                                response.setArmorResponse(command.getServiceResponse());   // catch invalid command
                            }
                        });*/



    }
    //Scene definitions
    public static ConnectObjectScene scene0(){
        ConnectObjectScene scene = new ConnectObjectScene();
        scene.addTable(0,0, .9);
        scene.addScrewDriver( 0, .02, .9);
        return scene;
    }
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

