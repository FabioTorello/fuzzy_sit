package it.emarolab;

//import it.emarolab.amor.owlDebugger.OFGUI.GuiRunner;
import fuzzy_sit_memory_msgs.*;
import com.google.common.collect.Lists;
//import it.emarolab.fuzzySIT.FuzzySITBase;
//import it.emarolab.fuzzySIT.perception.FeaturedSpatialObject;
//import it.emarolab.fuzzySIT.perception.PerceptionBase;
import it.emarolab.fuzzySIT.perception.simple2D.*;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import org.jgrapht.*;
import org.ros.internal.loader.CommandLineLoader;
//import org.ros.message.MessageFactory;
import org.ros.namespace.GraphName;
import org.ros.node.*;
import org.ros.node.parameter.ParameterTree;
//import org.ros.node.service.ServiceResponseBuilder;
import org.ros.node.service.ServiceServer;

import org.jgraph.JGraph;


//import java.util.ArrayList;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
//import it.emarolab.fuzzySIT.semantic.SITTBox;
//import java.util.Scanner;

//import it.emarolab.fuzzySIT.semantic.axioms.SpatialObject;
//import java.lang.Object;
//import java.util.Arrays;
//import java.util.stream.Collectors;

import it.emarolab.fuzzySIT.perception.simple2D.Table;
import it.emarolab.fuzzySIT.perception.simple2D.BED_MINUS_X;
import it.emarolab.fuzzySIT.perception.simple2D.BED_MINUS_Y;
import it.emarolab.fuzzySIT.perception.simple2D.BED_X;
import it.emarolab.fuzzySIT.perception.simple2D.BED_Y;
import it.emarolab.fuzzySIT.perception.simple2D.CHAIR_MINUS_X;
import it.emarolab.fuzzySIT.perception.simple2D.CHAIR_MINUS_Y;
import it.emarolab.fuzzySIT.perception.simple2D.CHAIR_X;
import it.emarolab.fuzzySIT.perception.simple2D.CHAIR_Y;
import it.emarolab.fuzzySIT.perception.simple2D.ROOF_MINUS_X;
import it.emarolab.fuzzySIT.perception.simple2D.ROOF_MINUS_Y;
import it.emarolab.fuzzySIT.perception.simple2D.ROOF_X;
import it.emarolab.fuzzySIT.perception.simple2D.ROOF_Y;
import it.emarolab.fuzzySIT.perception.simple2D.NOT_MINUS_X;
import it.emarolab.fuzzySIT.perception.simple2D.NOT_MINUS_Y;
import it.emarolab.fuzzySIT.perception.simple2D.NOT_X;
import it.emarolab.fuzzySIT.perception.simple2D.NOT_Y;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_1;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_2;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_3;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_4;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_5;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_6;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_7;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_8;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_9;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_10;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_11;
import it.emarolab.fuzzySIT.perception.simple2D.Pin_12;


public class MemoryService extends AbstractNodeMain {

    private final static Boolean DEFAULT_FULL_ENTITY_IDENTIFIER = false;
    private final static Boolean DEFAULT_SHOW_GUI = false;
    private static int numberFrame=0;

    private File fileComplexity = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/FileComplexity_ShortestPath.csv");
    private File fileDegrees = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/Degrees_Graph.csv");
    private File fileAdjacencyMatrix = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/AdjacencyMatrix_Graph.csv");
    private File fileEdgeDescription = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/EdgeDescription_Graph.txt");
    private File fileIncidenceMatrix = new File("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/Logfiles/IncidenceMatrix_Graph.csv");

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


        // MemoryImplementation memory = new MemoryImplementation("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/src/main/resources/table_assembling_data_set_memory_example.fuzzydl", "/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/src/main/resources/fuzzyDL_CONFIG" );
        MemoryImplementationVersionUpdated memory = new MemoryImplementationVersionUpdated("/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/src/main/resources/table_assembling_data_set_memory_example.fuzzydl", "/home/fabio/java_workspace/src/fuzzy_sit_memory_pkgs/memory_pkg/memory_service/src/main/resources/fuzzyDL_CONFIG");



        //Create an object representing the memory
        //MemoryImplementation memory = new MemoryImplementation("memory_service/src/main/resources/table_assembling_data_set_memory_example.fuzzydl", "memory_service/src/main/resources/fuzzyDL_CONFIG" );
        // MemoryImplementationVersionUpdated memory = new MemoryImplementationVersionUpdated("memory_service/src/main/resources/table_assembling_data_set_memory_example.fuzzydl", "memory_service/src/main/resources/fuzzyDL_CONFIG" );

        //Callback for TestServiceDirective.srv calls
        //newServiceServer(GraphName serviceName, java.lang.String serviceType, ServiceResponseBuilder<T,S> serviceResponseBuilder)
        ServiceServer<TestServiceDirectiveRequest, TestServiceDirectiveResponse> MemoryTestCallback =
                connectedNode.newServiceServer("memory_service", TestServiceDirective._TYPE,
                        (request, response) -> {
                            /*System.out.print("---------------------------------------------"+"\n");
                            System.out.print("Frame "+request.getTestRequest().getFrame()+"\n");
                            for (SceneItem item: request.getTestRequest().getItems()) {
                                System.out.print(item.getGammaI() + "\n");
                                System.out.print(item.getType() + "\n");
                                System.out.print(item.getDegree() + "\n");
                                System.out.print("\n");
                            }
                            for (Relations relation: request.getTestRequest().getRelations()){
                                System.out.print(relation.getGammaSubject() + "\n");
                                System.out.print(relation.getGammaObject() + "\n");
                                System.out.print(relation.getNameRelation() + "\n");
                                System.out.print(relation.getDegreeRelation() + "\n");
                                System.out.print("\n");
                            }*/
                            System.out.print(request.getTestRequest().getFrame() + "\n");
                            if ((!request.getTestRequest().getItems().isEmpty())&&(!request.getTestRequest().getRelations().isEmpty())&&request.getTestRequest().getFrame()!=-1) {

                                //Subsampling of 3 --> I take one scene every 3
                                if(request.getTestRequest().getFrame() % 4 == 0){
                                    //System.out.print("THE SCENE N° "+request.getTestRequest().getFrame() +" IS GIVEN TO SIT" + "\n");
                                    memory.experience(scene(request.getTestRequest().getItems(), request.getTestRequest().getRelations(), request.getTestRequest().getSceneName()), true, true);
                                }

                            }

                            if (request.getTestRequest().getFrame()==-1) {

                                //dimension of the memory graph
                                ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> GraphMemory=memory.getTbox().getHierarchy();

                                int n_vertices = GraphMemory.vertexSet().size();
                                int m_edges = GraphMemory.edgeSet().size();
                                int [][] adjacency_matrix= new int[n_vertices][n_vertices];
                                int [][] incidence_matrix= new int[n_vertices][m_edges];
                                int number_OfLoops=0;

                                edge_Description(GraphMemory);
                                degrees_OfVerteces(GraphMemory);
                                adjacency_matrix=graph_adjacency_matrix(GraphMemory);
                                System.out.print("\n");
                                /*for (int i=0; i<n_vertices;i++){
                                    for (int j=0; j<n_vertices;j++){
                                        System.out.print(adjacency_matrix[i][j]);
                                    }
                                    System.out.print("\n");
                                }
                                System.out.print("\n");*/

                                incidence_matrix=graph_incidence_matrix(GraphMemory);
                                /*for (int i=0; i<n_vertices;i++){
                                    for (int j=0; j<m_edges;j++){
                                        System.out.print(incidence_matrix[i][j]);
                                    }
                                    System.out.print("\n");
                                }
                                System.out.print("\n");*/

                                number_OfLoops=compute_numberOfLoops(adjacency_matrix,incidence_matrix,n_vertices,m_edges);
                                System.out.print("NUMBER OF LOOPS DETECTED: " + number_OfLoops + "\n");


                                computationalComplexity_ShortestPath(n_vertices,m_edges,number_OfLoops);

                                //print the memory graph
                                memory.getTbox().show();
                                response.getTestResponse().setResponse("Ready");
                            }

                            if(request.getTestRequest().getFrame()!=-1) {
                                response.getTestResponse().setResponse("The scene has been loaded");
                            }
                            /*if ((!request.getTestRequest().getItems().isEmpty())&&(!request.getTestRequest().getRelations().isEmpty())&&request.getTestRequest().getFrame()<51) {
                                memory.experience(scene(request.getTestRequest().getItems(), request.getTestRequest().getRelations(), request.getTestRequest().getSceneName()), true, true);
                            }*/




                            //Show the experience graph

                            //THE SCENE GRAPH SHOULD BE VISUALIZE ONLY IN THE END OF THE BAGFILE
                            // (THIS IS ONLY A TEMPTATIVE)



                            /*if(request.getTestRequest().getFrame()==51 || request.getTestRequest().getFrame()==50) {
                                //response.getTestResponse().setResponse("The scene has been loaded");
                                memory.getTbox().show();
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

    public int compute_numberOfLoops(int [][] adjacency_Matrix, int [][] incidence_Matrix, int numb_vertices, int numb_edges){



        int number_loops=0;
        int [][] minor_Of_matrix= new int [2][2];

        //Loop on adjacency matrix row
        for (int i=0; i<numb_vertices;i++){
            //Loop on adjacency matrix row
            for (int j=i; j<numb_vertices;j++){

                //If two vertices are linked
                if(adjacency_Matrix[i][j]==1){

                    int [] row1 = new int [numb_edges];
                    int [] row2 = new int [numb_edges];
                    int [] row3 = new int [numb_edges];
                    int column_index1=0;
                    int column_index2=0;
                    int count_number2=0;
                    boolean first_number2_found=false;
                    boolean second_number2_found=false;

                    for(int k=0; k<numb_edges;k++) {

                        row1[k]=Math.abs(incidence_Matrix[i][k]);

                        row2[k]=Math.abs(incidence_Matrix[j][k]);

                        row3[k]=row1[k]+row2[k];
                    }

                    for (int q=0; q<numb_edges;q++){

                        if(row3[q]==2){
                            count_number2++;
                        }

                        if((count_number2==1) && (first_number2_found==false)){
                            column_index1=q;
                            first_number2_found=true;
                        }
                        else if(count_number2==2){
                            column_index2=q;
                            second_number2_found=true;
                            break;
                        }
                    }
                    if(second_number2_found==true) {
                        minor_Of_matrix[0][0] = incidence_Matrix[i][column_index1];
                        minor_Of_matrix[0][1] = incidence_Matrix[i][column_index2];
                        minor_Of_matrix[1][0] = incidence_Matrix[j][column_index1];
                        minor_Of_matrix[1][1] = incidence_Matrix[j][column_index2];

                        if ((minor_Of_matrix[0][1] == minor_Of_matrix[1][0]) && (minor_Of_matrix[0][0] == minor_Of_matrix[1][1])){
                            number_loops++;
                        }
                    }

                }

            }
        }

        return number_loops;

    }





    public void edge_Description(ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graph_memory){

        PrintWriter outpustream_edge_description= null;


        int i=0;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileEdgeDescription.exists()) {
                //Create the file
                try {
                    fileEdgeDescription.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            outpustream_edge_description = new PrintWriter(new FileOutputStream(fileEdgeDescription, true));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        for (SceneHierarchyEdge edge : graph_memory.edgeSet()) {

            String first_row = "";
            SceneHierarchyVertex vertex_source;
            SceneHierarchyVertex vertex_target;

            i++;

            first_row = first_row.concat("Edge_" + i + ": ");

            vertex_source = graph_memory.getEdgeSource(edge);

            vertex_target = graph_memory.getEdgeTarget(edge);

            first_row = first_row.concat("[ "+ vertex_source.getScene() + "-->" + vertex_target.getScene());

            first_row = first_row.concat(", "+ "Weight: " + graph_memory.getEdgeWeight(edge) +" ]");

            outpustream_edge_description.println(first_row);
        }

        outpustream_edge_description.println("\n");

        for (SceneHierarchyVertex vertex : graph_memory.vertexSet()) {

            String other_row="";


            other_row = other_row.concat("Vertex " + vertex.getScene() + ": ");

            other_row = other_row.concat("[ " + "Score: " + vertex.getMemoryScore() + " ]");

            outpustream_edge_description.println(other_row);

        }
        outpustream_edge_description.close();
    }


    public void degrees_OfVerteces(ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graph_memory){

        PrintWriter outpustreamdegrees= null;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileDegrees.exists()) {
                //Create the file
                try {
                    fileDegrees.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamdegrees = new PrintWriter(new FileOutputStream(fileDegrees, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        outpustreamdegrees.println("Vertex_Name" + "," +  "inDegree" + "," + "outDegree" );
        //Loop on all the vertices in the graph
        for (SceneHierarchyVertex sourceVertices : graph_memory.vertexSet()) {
            int inDegreeOfVertex=0;
            int outDegreeOfVertex=0;
            //Loop on all the edges touching the specified vertex
            for (SceneHierarchyEdge edges : graph_memory.edgesOf(sourceVertices)) {

                if (sourceVertices==graph_memory.getEdgeSource(edges)){
                    outDegreeOfVertex++;
                }
                else if(sourceVertices==graph_memory.getEdgeTarget(edges)){
                    inDegreeOfVertex++;
                }

            }
            outpustreamdegrees.println(sourceVertices.getScene() + "," +  inDegreeOfVertex + "," + outDegreeOfVertex);

        }
        outpustreamdegrees.close();

    }







    public int [][] graph_adjacency_matrix(ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graph_memory) {

        PrintWriter outpustream_adjacency_matrix= null;

        String first_row="";

        int num_vertices = graph_memory.vertexSet().size();
        int [][] ajacencyMatrix = new int[num_vertices][num_vertices];

        int i=0;
        int k=0;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileAdjacencyMatrix.exists()) {
                //Create the file
                try {
                    fileAdjacencyMatrix.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustream_adjacency_matrix = new PrintWriter(new FileOutputStream(fileAdjacencyMatrix, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        for (SceneHierarchyVertex sourceVertices : graph_memory.vertexSet()) {

            i++;

            first_row = first_row.concat(sourceVertices.getScene());

            if(i!=graph_memory.vertexSet().size()){

                first_row = first_row.concat(",");

            }
        }

        outpustream_adjacency_matrix.println(" " + "," + first_row);

        //Loop on all the vertices in the graph
        for (SceneHierarchyVertex Vertices_external : graph_memory.vertexSet()) {

            int j=0;

            String other_row="";
            other_row = other_row.concat(Vertices_external.getScene());
            other_row = other_row.concat(",");

            //Loop on all the edges touching the specified vertex
            for (SceneHierarchyVertex Vertices_internal : graph_memory.vertexSet()) {

                j++;

                if(Vertices_external.equals(Vertices_internal)){

                    other_row = other_row.concat("0");
                    ajacencyMatrix[k][j-1]=0;

                }

                if((graph_memory.containsEdge(Vertices_external,Vertices_internal)) || (graph_memory.containsEdge(Vertices_internal,Vertices_external))){
                    other_row = other_row.concat("1");
                    ajacencyMatrix[k][j-1]=1;
                }
                else if(((!(graph_memory.containsEdge(Vertices_external,Vertices_internal))) || (!(graph_memory.containsEdge(Vertices_internal,Vertices_external))))&&(!Vertices_external.equals(Vertices_internal))){
                    other_row = other_row.concat("0");
                    ajacencyMatrix[k][j-1]=0;
                }





                if(j!=graph_memory.vertexSet().size()){

                    other_row = other_row.concat(",");

                }

            }

            outpustream_adjacency_matrix.println(other_row);

            k++;
        }
        outpustream_adjacency_matrix.close();

        return ajacencyMatrix;
    }

    public int [][] graph_incidence_matrix(ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graph_memory) {

        PrintWriter outpustream_incidence_matrix= null;

        String first_row="";

        int number_vertices = graph_memory.vertexSet().size();
        int number_edges = graph_memory.edgeSet().size();
        int [][] incidenceMatrix = new int[number_vertices][number_edges];

        int i=0;
        int k=0;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileIncidenceMatrix.exists()) {
                //Create the file
                try {
                    fileIncidenceMatrix.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            outpustream_incidence_matrix = new PrintWriter(new FileOutputStream(fileIncidenceMatrix, true));

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        //Loop on all the edges touching the specified vertex
        for (SceneHierarchyEdge edge : graph_memory.edgeSet()) {

            i++;

            first_row = first_row.concat("Edge_"+i);

            if(i!=graph_memory.edgeSet().size()){

                first_row = first_row.concat(",");

            }
        }

        outpustream_incidence_matrix.println(" " + "," + first_row);

        for (SceneHierarchyVertex vertex : graph_memory.vertexSet()) {

            int j=0;
            String other_row="";

            other_row = other_row.concat(vertex.getScene()+",");

            for (SceneHierarchyEdge edge : graph_memory.edgeSet()) {

                j++;

                //if edge enters in the vertex
                if(vertex==graph_memory.getEdgeSource(edge)){

                    other_row = other_row.concat("1");

                    incidenceMatrix[k][j-1]=1;

                }
                //if edge exits in the vertex
                else if (vertex==graph_memory.getEdgeTarget(edge)){

                    other_row = other_row.concat("-1");

                    incidenceMatrix[k][j-1]=-1;

                }
                else{

                    other_row = other_row.concat("0");

                    incidenceMatrix[k][j-1]=0;

                }

                if(j!=graph_memory.edgeSet().size()){

                    other_row = other_row.concat(",");

                }
            }

            outpustream_incidence_matrix.println(other_row);

            k++;
        }

        outpustream_incidence_matrix.close();

        return incidenceMatrix;
    }




    public void computationalComplexity_ShortestPath(int number_vertices, int number_edges, int numberLoops){

        PrintWriter outpustreamComplexity = null;

        try {
            //CREATES THE CSV FILES IF THEY DO NOT EXIST
            if (!fileComplexity.exists()) {
                //Create the file
                try {
                    fileComplexity.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            outpustreamComplexity = new PrintWriter(new FileOutputStream(fileComplexity, true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        int sum_between_edges_vertices=number_edges+number_vertices;

        String Dijk_1= "O" + "(" + number_vertices*number_vertices + ")";

        String Dijk_2= "O" + "(" + sum_between_edges_vertices+ " log " + number_vertices + ")";

        String Bellman_Ford= "O" + "(" + number_edges*number_vertices + ")" ;

        double graph_density_double=(double)number_edges/(number_vertices*(number_vertices-1));
        System.out.print("DENSITY GRAPH DOUBLE WITHOUT ROUND: " + graph_density_double);
        BigDecimal bd = new BigDecimal(graph_density_double).setScale(2, RoundingMode.HALF_UP);

        double graph_density = bd.doubleValue();
        System.out.print("\nDENSITY GRAPH DOUBLE WITH ROUND: " + graph_density);

        outpustreamComplexity.println("vertices(n)" + "," +  "edges(m)" + "," + "Number_Loops" + "," +"Graph_Density" + "," + "Dijkstra_Algorithm[ O(n^2) ]" + "," + "Dijkstra_Algorithm[ O((m+n)log n) ]"  + "," + "Bellman-Ford_Algorithm[ O(m*n) ]");

        outpustreamComplexity.println(number_vertices + "," + number_edges + "," + numberLoops +","+ graph_density + "," + Dijk_1 + "," + Dijk_2 + "," + Bellman_Ford);


        outpustreamComplexity.close();
    }

    public static ConnectObjectScene scene(List<SceneItem> items, List<Relations> relations, String frame) {
        if ((!items.isEmpty())&&(!relations.isEmpty())) {
            //Define the scene
            ConnectObjectScene scene = new ConnectObjectScene(frame);



            for (SceneItem item : items) {

                /////////////BED LEG////////////////////////////////

                if(item.getType().contains("BED_X")) {
                    //Add an object type BED_X
                    scene.addObject(new BED_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("BED_MINUS_X")) {
                    //Add an object type BED_MINUS_X
                    scene.addObject(new BED_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("BED_Y")) {
                    //Add an object type BED_Y
                    scene.addObject(new BED_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("BED_MINUS_Y")) {
                    //Add an object type BED_X
                    scene.addObject(new BED_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ////////////CHAIR LEG/////////////////////////////////
                else if(item.getType().contains("CHAIR_X")) {
                    //Add an object type CHAIR_X
                    scene.addObject(new CHAIR_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("CHAIR_MINUS_X")) {
                    //Add an object type CHAIR_MINUS_X
                    scene.addObject(new CHAIR_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("CHAIR_Y")) {
                    //Add an object type CHAIR_Y
                    scene.addObject(new CHAIR_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("CHAIR_MINUS_Y")) {
                    //Add an object type CHAIR_MINUS_Y
                    scene.addObject(new CHAIR_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ////////////ROOF LEG/////////////////////////////////
                else if(item.getType().contains("ROOF_X")) {
                    //Add an object type ROOF_X
                    scene.addObject(new ROOF_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("ROOF_MINUS_X")) {
                    //Add an object type ROOF_MINUS_X
                    scene.addObject(new ROOF_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("ROOF_Y")) {
                    //Add an object type ROOF_Y
                    scene.addObject(new ROOF_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("ROOF_MINUS_Y")) {
                    //Add an object type ROOF_MINUS_Y
                    scene.addObject(new ROOF_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ////////////NOT LEG/////////////////////////////////
                else if(item.getType().contains("NOT_X")) {
                    //Add an object type NOT_X
                    scene.addObject(new NOT_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("NOT_MINUS_X")) {
                    //Add an object type NOT_MINUS_X
                    scene.addObject(new NOT_MINUS_X(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("NOT_Y")){
                    //Add an object type NOT_Y
                    scene.addObject(new NOT_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }

                else if(item.getType().contains("NOT_MINUS_Y")) {
                    //Add an object type NOT_MINUS_Y
                    scene.addObject(new NOT_MINUS_Y(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_1//////////////////////////////////////
                else if(item.getType().contains("Pin_1")) {
                    //Add an object type Pin_1
                    scene.addObject(new Pin_1(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_2//////////////////////////////////////
                else if(item.getType().contains("Pin_2")) {
                    //Add an object type Pin_2
                    scene.addObject(new Pin_2(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_3//////////////////////////////////////
                else if(item.getType().contains("Pin_3")) {
                    //Add an object type Pin_3
                    scene.addObject(new Pin_3(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_4//////////////////////////////////////
                else if(item.getType().contains("Pin_4")) {
                    //Add an object type Pin_4
                    scene.addObject(new Pin_4(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_5//////////////////////////////////////
                else if(item.getType().contains("Pin_5")) {
                    //Add an object type Pin_5
                    scene.addObject(new Pin_5(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_6//////////////////////////////////////
                else if(item.getType().contains("Pin_6")) {
                    //Add an object type Pin_6
                    scene.addObject(new Pin_6(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_7//////////////////////////////////////
                else if(item.getType().contains("Pin_7")) {
                    //Add an object type Pin_7
                    scene.addObject(new Pin_7(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_8//////////////////////////////////////
                else if(item.getType().contains("Pin_8")) {
                    //Add an object type Pin_8
                    scene.addObject(new Pin_8(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_9//////////////////////////////////////
                else if(item.getType().contains("Pin_9")) {
                    //Add an object type Pin_9
                    scene.addObject(new Pin_9(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_10//////////////////////////////////////
                else if(item.getType().contains("Pin_10")) {
                    //Add an object type Pin_10
                    scene.addObject(new Pin_10(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_11//////////////////////////////////////
                else if(item.getType().contains("Pin_11")) {
                    //Add an object type Pin_11
                    scene.addObject(new Pin_11(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Pin_12//////////////////////////////////////
                else if(item.getType().contains("Pin_12")) {
                    //Add an object type Pin_12
                    scene.addObject(new Pin_12(item.getType(), item.getGammaI(), item.getDegree()));
                }
                ///////////////////////////////////////////////////////

                ///////////Table//////////////////////////////////////
                else if(item.getType().contains("Table")) {
                    //Add an object type Table
                    scene.addObject(new Table(item.getType(), item.getGammaI(), item.getDegree()));
                }
                //////////////////////////////////////////////////////

            }






            for (Relations relation : relations) {
                scene.getRelations().add(new SpatialRelation(relation.getGammaSubject(), relation.getNameRelation(), relation.getGammaObject(), relation.getDegreeRelation()));
            }

            return scene;
        }

        return new ConnectObjectScene();
    }






   /*
   rosservice call /memory_service "test_request:
  items:
  - gamma_i: 'g_1'
    type: 'BED_X'
    degree: 0.8
  - gamma_i: 'p_1'
    type: 'Pin_1'
    degree: 1
  - gamma_i: 't'
    type: 'Table'
    degree: 1
  relations:
  - gamma_subject: 'g_1'
    gamma_object: 'p_1'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.8
  - gamma_subject: 'g_1'
    gamma_object: 't'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.17
  - gamma_subject: 'p_1'
    gamma_object: 't'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.2
  scene_name: '1.2_3'
  frame: 3"
   rosservice call /memory_service "test_request:
  items:
  - gamma_i: 'g_2'
    type: 'BED_MINUS_X'
    degree: 0.8
  - gamma_i: 'p_2'
    type: 'Pin_2'
    degree: 1
  - gamma_i: 't'
    type: 'Table'
    degree: 1
  relations:
  - gamma_subject: 'g_2'
    gamma_object: 'p_2'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.9
  - gamma_subject: 'g_2'
    gamma_object: 't'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.5
  - gamma_subject: 'p_2'
    gamma_object: 't'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.2
  scene_name: '1.2_5'
  frame: 6"
rosservice call /memory_service "test_request:
  items:
  - gamma_i: 'g_3'
    type: 'BED_Y'
    degree: 0.8
  - gamma_i: 'p_3'
    type: 'Pin_3'
    degree: 1
  - gamma_i: 't'
    type: 'Table'
    degree: 1
  relations:
  - gamma_subject: 'g_3'
    gamma_object: 'p_3'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.8
  - gamma_subject: 'g_3'
    gamma_object: 't'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.7
  - gamma_subject: 'p_3'
    gamma_object: 't'
    nameRelation: 'isConnectedTo'
    degreeRelation: 0.1
  scene_name: '1.2_8'
  frame: 8"
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