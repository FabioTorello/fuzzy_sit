package it.emarolab.fuzzySIT.gui;


import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxEvent;
import com.mxgraph.view.mxGraphSelectionModel;
import it.emarolab.fuzzySIT.FuzzySITBase;
import it.emarolab.fuzzySIT.semantic.SITABox;
import it.emarolab.fuzzySIT.semantic.axioms.SpatialRelation;
import it.emarolab.fuzzySIT.semantic.SITTBox;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyEdge;
import it.emarolab.fuzzySIT.semantic.hierarchy.SceneHierarchyVertex;
import it.emarolab.fuzzySIT.semantic.PlacedObject;
import javafx.application.Platform;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jgrapht.ListenableGraph;
import org.jgrapht.ext.JGraphXAdapter;

import java.io.*;
import java.util.*;

public class Controller
        implements FuzzySITBase{

    // todo make alert dialog

    private static double DOT_SIZE = 10; // todo with respect to EPSILON?

    private static Set<GuiObject> placedObjects = new HashSet<>();

    private static long ID_COUNTER = 0;

    private SITTBox hierarchy; //todo getter
    private SITABox representation;

    @FXML
    private Canvas table;
    @FXML
    private VBox objectTypeLayout;
    @FXML
    private Button loadBtn;
    @FXML
    private SwingNode swingGraphPanel;
    @FXML
    private Button recogniseBtn;
    @FXML
    private Button learnBtn;
    @FXML
    private Button saveBtn;
    @FXML
    private ProgressIndicator progress;

    private void startProgress(){
        progress.setVisible( true);
        recogniseBtn.setDisable( true);
        learnBtn.setDisable( true);
        loadBtn.setDisable( true);
        saveBtn.setDisable( true);
    }
    private void stopProgress(){
        progress.setVisible( false);
        recogniseBtn.setDisable( false);
        learnBtn.setDisable( false);
        loadBtn.setDisable( false);
        saveBtn.setDisable( false);
    }

    protected void addLoadBtnListener(){
        loadBtn.addEventHandler( MouseEvent.MOUSE_RELEASED, e ->{
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extensFilterAll = new FileChooser.ExtensionFilter("all files (*.*)", "*");
            FileChooser.ExtensionFilter extensFilterFuzzy = new FileChooser.ExtensionFilter("t-box (*.fuzzydl)", "*.fuzzydl*");
            fileChooser.getExtensionFilters().addAll(extensFilterFuzzy, extensFilterAll);

            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);

            if( file != null) {
                new Thread( new ExternalTask(){
                    public void perform() {
                        hierarchy = new SITTBox( file.getAbsolutePath());
                        Platform.runLater(() -> {
                            objectTypeLayout.getChildren().clear();
                            typeMemberships.clear();
                            clearTable();
                            addObjectType(hierarchy.getObjectType());// and clear button
                            addGraph(hierarchy.getHierarchy());
                        });
                    }
                }).start();
            }
        });
    }

    protected void addSaveBtnListener(){
        saveBtn.addEventHandler( MouseEvent.MOUSE_RELEASED, e ->{
            if ( hierarchy == null){
                Alert alert = new Alert(Alert.AlertType.ERROR,
                        "You should load a T-Box before to save it !",
                        ButtonType.OK);
                alert.showAndWait();
                return;
            }

            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extensFilterAll = new FileChooser.ExtensionFilter("all files (*.*)", "*");
            FileChooser.ExtensionFilter extensFilterFuzzy = new FileChooser.ExtensionFilter("t-box (*.fuzzydl)", "*.fuzzydl*");
            fileChooser.getExtensionFilters().addAll(extensFilterFuzzy, extensFilterAll);

            //Show open file dialog
            File file = fileChooser.showSaveDialog(null);

            if( file != null) {
                new Thread( new ExternalTask(){
                    public void perform() {
                        Platform.runLater(() -> {
                            hierarchy.saveTbox( file.getAbsolutePath());
                        });
                    }
                }).start();
            }
        });
    }

    protected void addRecogniseButtonListener(){
        // todo check bug not recognised at the first temptative!!!!!!
        recogniseBtn.addEventHandler( MouseEvent.MOUSE_RELEASED, event -> {
            if ( placedObjects.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING,
                        "You should add some object on the scene",
                        ButtonType.OK);
                alert.showAndWait();
                return;
            }

            new Thread( new ExternalTask(){
                public void perform() {
                    representation = recognise();

                    Platform.runLater(() -> {
                        Map<SceneHierarchyVertex, Double> recognitionMap = representation.getRecognitions();
                        String info = "Scene recognised as: " + NEW_LINE ;
                        if ( recognitionMap.isEmpty())
                            info = "Scene not recognised." + NEW_LINE;
                        else {
                            // todo move to SceneRepresentaiotn
                            double individualCardinality = representation.getDefinition().getCardinality();
                            for (SceneHierarchyVertex v : recognitionMap.keySet()) {
                                double classCardinality = v.getDefinition().getCardinality();
                                double recognitionConfidence = classCardinality / individualCardinality;

                                info += " - " + v.getScene() + " (" + recognitionMap.get(v) + ")" +
                                        " with confidence " + DoubleFormatter.roundDegree( classCardinality) + "/"
                                        + DoubleFormatter.roundDegree( individualCardinality) + " = " +
                                        DoubleFormatter.roundDegree( recognitionConfidence) + NEW_LINE;
                            }
                        }

                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
                        alert.setResizable( true);
                        alert.getDialogPane().setContent( new Text(info + "in: " + performingTime + "[ms]"));
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                        alert.show();
                    });
                }
            }).start();
        });
    }

    protected void addLearnBtnListener(){
        learnBtn.addEventHandler( MouseEvent.MOUSE_RELEASED, e ->{
            if ( hierarchy == null){
                Alert alert = new Alert( Alert.AlertType.ERROR, "Load a T-box first !", ButtonType.OK);
                alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                alert.showAndWait();
                return;
            }
            //if ( representation == null)
                representation = recognise();


            TextInputDialog dialog = new TextInputDialog("");
            dialog.setTitle("Learning");
            dialog.setHeaderText("New scene name");
            dialog.setContentText("Please enter a name:"); // to check for no number no strange chars
            Optional<String> result = dialog.showAndWait();
            if ( result.isPresent()){
                new Thread( new ExternalTask() {
                    public void perform() {

                        if ( hierarchy.getScenes().contains( result.get())){
                            Alert alert = new Alert(Alert.AlertType.ERROR, "Scene " + result.get()
                                    + " already defined", ButtonType.OK);
                            alert.setResizable( true);
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                            alert.show();
                            return;
                        }


                        hierarchy.learn( result.get(), representation);//.getDefinition(), placedObjects.size());

                        Platform.runLater(() -> {
                            addGraph( hierarchy.getHierarchy());
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Scene learned in " + performingTime
                                    + "[ms] as: " + NEW_LINE + representation.getDefinition().toGuiString(), ButtonType.OK);
                            alert.setResizable( true);
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                            alert.show();
                        });
                    }
                }).start();
            }
        });
    }

    private SITABox recognise(){
        Set<PlacedObject> spatialObject = new HashSet<>();
        for (  GuiObject g : placedObjects)
            spatialObject.addAll( g.getFuzzyObject( table));
        Set<SpatialRelation> spatialRelation = new HashSet<>();
        for ( PlacedObject p1 : spatialObject)
            for ( PlacedObject p2 : spatialObject)
                if ( ! p1.equalsCoordinates( p2))
                    spatialRelation.addAll( p1.getRelations( p2));

        return new SITABox( hierarchy, spatialObject, spatialRelation);
    }

    protected void addCanvasListeners(){ // requires to add the table first
        table.addEventHandler(MouseEvent.MOUSE_CLICKED,
            e -> {
                if (hierarchy == null){
                    Alert alert = new Alert(Alert.AlertType.WARNING,
                            "You should load a T-Box",
                            ButtonType.OK);
                    alert.showAndWait();
                    return;
                }

                double x = e.getX();
                double y = table.getHeight() - e.getY();
                GuiObject o = new GuiObject( x, y);

                boolean closeToOther = false;
                GuiObject closedObject = null;
                for ( GuiObject p : placedObjects)
                    if ( p.equals( o)) {
                        closeToOther = true;
                        closedObject = p;
                    }


                if ( e.getButton() == MouseButton.PRIMARY){
                    if ( closeToOther){
                        // todo show properties!!!!!
                        /*Alert alert = new Alert(Alert.AlertType.INFORMATION, "Point: " + closedObject, ButtonType.OK);
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                        alert.setResizable( true);
                        alert.show();*/

                        Stage dialog = new Stage();
                        dialog.initStyle(StageStyle.DECORATED);
                        Scene scene = new Scene(new Group(new Text(30, 30,
                                "Point: " + closedObject)));
                        dialog.setScene(scene);
                        dialog.show();


                        return;
                    }

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Add object to scene? " + o, ButtonType.YES, ButtonType.NO);
                    alert.setHeaderText( "Adding object ?");
                    alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                    alert.setResizable( true);
                    Optional<ButtonType> result = alert.showAndWait();
                    if ( result.get() == ButtonType.YES) {
                        placedObjects.add(o);
                        o.addPoint( table);
                    }
                }
                if ( e.getButton() == MouseButton.SECONDARY){
                    if ( closeToOther){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Remove object from scene? " + o, ButtonType.YES, ButtonType.NO);
                        alert.setHeaderText( "Removing object ?");
                        alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label)node).setMinHeight(Region.USE_PREF_SIZE));
                        alert.setResizable( true);
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == ButtonType.YES) {
                            placedObjects.remove( closedObject);
                            closedObject.removePoint( table);
                        }
                    }
                }
            });


        GraphicsContext gc = table.getGraphicsContext2D();
        drawArrow( gc, 5, table.getHeight(), 5, table.getHeight() - 30);
        drawArrow( gc, 0, table.getHeight() - 5, 30, table.getHeight() - 5);
        gc.fillText( "behind (y)", 5, table.getHeight() - 32);
        gc.fillText( "right (x)", 32, table.getHeight() - 5);
    }
    private double ARR_SIZE = 5;
    private void drawArrow(GraphicsContext gc, double x1, double y1, double x2, double y2) {
        //gc.setFill(Color.BLACK);

        double dx = x2 - x1, dy = y2 - y1;
        double angle = Math.atan2(dy, dx);
        int len = (int) Math.sqrt(dx * dx + dy * dy);

        Transform transform = Transform.translate(x1, y1);
        transform = transform.createConcatenation(Transform.rotate(Math.toDegrees(angle), 0, 0));
        gc.setTransform(new Affine(transform));

        gc.strokeLine(0, 0, len, 0);
        gc.fillPolygon(new double[]{len, len - ARR_SIZE, len - ARR_SIZE, len}, new double[]{0, -ARR_SIZE, ARR_SIZE, 0},
                4);

        gc.setTransform( new Affine());
    }


    static Map< String, Slider> typeMemberships = new HashMap<>();
    double DEFAULT_MEMBERSHIP = .5;


    protected void addObjectType( Collection< String> types){

        ToolBar clearLayout = new ToolBar();
        Button clear = new Button( "Clear Scene");
        clearLayout.getItems().addAll( clear);
        clear.addEventHandler( MouseEvent.MOUSE_RELEASED,
                e -> clearTable());
        objectTypeLayout.getChildren().add( clearLayout);



        for (String type : types) {

            if (typeMemberships.containsKey(type)) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Cannot load two equal object types !!! " + type, ButtonType.OK);
                alert.showAndWait();
                return;
            }

            Slider slider = new Slider(0, 1, DEFAULT_MEMBERSHIP);
            typeMemberships.put(type, slider);
            Label label = new Label(type);
            TextField text = new TextField(String.valueOf(DEFAULT_MEMBERSHIP));
            text.setPrefWidth(100);

            slider.addEventHandler(MouseEvent.ANY,
                    e -> text.setText(String.valueOf(slider.getValue())));

            text.addEventHandler(MouseEvent.ANY,
                    e -> checkMembershipInput(text, slider));
            text.setOnKeyPressed(e -> {
                if (e.getCode() == KeyCode.ENTER)
                    checkMembershipInput(text, slider);
            });
            text.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (!newPropertyValue)
                    checkMembershipInput(text, slider); // lost of focus
            });

            ToolBar layout = new ToolBar();
            layout.setPrefHeight(45);
            layout.getItems().addAll(label, slider, text);

            objectTypeLayout.getChildren().add(layout);
        }
    }
    private void checkMembershipInput(TextField text, Slider slider){
        try {
            double number = Double.valueOf(text.getText());
            if ( number < 0 | number > 1)
                membershipError( text, slider);
            slider.setValue( number);
        } catch ( Exception ex){
            membershipError( text, slider);
        }
    }
    private void membershipError( TextField text, Slider slider){
        Alert alert = new Alert(Alert.AlertType.ERROR, "Only number between 0 and 1 are admissible !!! ", ButtonType.OK);
        text.setText( String.valueOf( DEFAULT_MEMBERSHIP));
        slider.setValue( DEFAULT_MEMBERSHIP);
        alert.showAndWait();
    }

    private void clearTable(){
        for (GuiObject g : placedObjects){
            g.removePoint( table);
        }
        placedObjects.clear();
    }

    private void addGraph(ListenableGraph<SceneHierarchyVertex, SceneHierarchyEdge> graph){
        JGraphXAdapter<SceneHierarchyVertex, SceneHierarchyEdge>
                graphAdapter = new JGraphXAdapter<>( graph);
        graphAdapter.setCellsDisconnectable( false);
        graphAdapter.setCellsEditable( false);
        graphAdapter.setCellsDeletable( false);
        graphAdapter.setConnectableEdges( false);
        graphAdapter.setDropEnabled( false);

        mxIGraphLayout layout = new mxHierarchicalLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        mxGraphComponent graphPanel = new mxGraphComponent(graphAdapter);

        graphAdapter.getSelectionModel().addListener(mxEvent.CHANGE, (sender, evt) -> {
            mxGraphSelectionModel sm = (mxGraphSelectionModel) sender;
            mxCell cell = (mxCell) sm.getCell();
            if (cell != null && cell.isVertex())
                for ( SceneHierarchyVertex g : graph.vertexSet())
                    if ( g.toString().equals( graphAdapter.getLabel(cell)))
                        Platform.runLater(() -> {

                            Stage dialog = new Stage();
                            dialog.initStyle(StageStyle.DECORATED);

                            TextArea text = new TextArea("Scene description: " + NEW_LINE +
                                    "Name :" + g.getScene() + NEW_LINE +
                                    "Number of Object: " + g.getObjectDistribution() + NEW_LINE +
                                    "Type of Objects: " + g.getObjectType() + NEW_LINE +
                                    "Definition: " + g.getDefinition().toGuiString());
                            text.setEditable( false);

                            Scene scene = new Scene(new Group(text));
                            dialog.setScene(scene);
                            dialog.show();

                /*
                            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                    "Scene description: " + NEW_LINE +
                                            "Name :" + g.getScene() + NEW_LINE +
                                            "Number of Object: " + g.getObjectDistribution() + NEW_LINE +
                                            "Type of Objects: " + g.getObjectType() + NEW_LINE +
                                            "Definition: " + g.getDefinition().toGuiString(), ButtonType.OK);
                            alert.getDialogPane().getChildren().stream().filter(node -> node instanceof Label).forEach(node -> ((Label) node).setMinHeight(Region.USE_PREF_SIZE));
                            alert.setResizable( true);
                            alert.show();*/
                        });


        });

        swingGraphPanel.setContent( graphPanel);
        swingGraphPanel.autosize();
    }

    private class GuiObject{
        private long id;
        private double x,y;
        private Map<String,Double> membership = new HashMap<>();

        public GuiObject( double x, double y){
            this.x = x;
            this.y = y;
            this.id = ID_COUNTER++;
            setMembership( typeMemberships);
        }

        public long getId() {
            return id;
        }

        // coordinate in the canvas
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        // coordinate in the simulated canvas
        public double getTableX( Canvas canvas){
            return (x * TABLE_SIZE_X) / canvas.getWidth(); // todo get changable table size
        }
        public double getTableY(Canvas canvas){
            return (y * TABLE_SIZE_Y) / canvas.getHeight();
        }

        public Map<String, Double> getMembership() {
            return membership;
        }

        private void setMembership(Map<String,Slider> sliders){
            for ( String t : sliders.keySet())
                membership.put( t, sliders.get( t).getValue());
        }

        private void addPoint( Canvas canvas){
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.fillOval( getX() - DOT_SIZE/2, canvas.getHeight() - getY() - DOT_SIZE/2, DOT_SIZE, DOT_SIZE);
        }

        private void removePoint(Canvas canvas){
            GraphicsContext gc = canvas.getGraphicsContext2D();
            gc.clearRect( getX() - DOT_SIZE/2, canvas.getHeight() - getY() - DOT_SIZE/2, DOT_SIZE, DOT_SIZE);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof GuiObject)) return false;
            GuiObject guiObject = (GuiObject) o;
            if ( Math.abs( getX() - guiObject.getX()) < DOT_SIZE / 2 &
                     Math.abs( getY() - guiObject.getY()) < DOT_SIZE / 2 )
                return true;
            return false;
        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            temp = Double.doubleToLongBits(getX());
            result = (int) (temp ^ (temp >>> 32));
            temp = Double.doubleToLongBits(getY());
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            return result;
        }


        // todo to check canvas table and round
        @Override
        public String toString() {
            return  "id=" + id +
                    ", x=" + getTableX( table) +
                    ", y=" + getTableY( table) + NEW_LINE +
                    "membership=" + membership + NEW_LINE;
        }

        public Collection< PlacedObject> getFuzzyObject( Canvas canvas) {
            Collection<PlacedObject> objects = new HashSet<>();
            for ( String t : membership.keySet())
                objects.add( new PlacedObject( t, "o-" + id, getTableX( canvas), getTableY( canvas), membership.get( t)));
            return objects;
        }
    }

    abstract private class ExternalTask
            implements Runnable {

        protected long performingTime;

        @Override
        public void run() {
            try{
                Platform.runLater(Controller.this::startProgress);
                performingTime = System.currentTimeMillis();
                perform();
                performingTime = System.currentTimeMillis() - performingTime;
                Platform.runLater(Controller.this::stopProgress);
            } catch (Exception e){
                showError( e);
                e.printStackTrace();
            }
        }

        abstract void perform();

        private void showError(Exception e){
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Exception on external task");
                alert.setHeaderText( e.getMessage());
                if ( e.getCause() != null)
                    alert.setContentText( e.getCause().toString());
                alert.setContentText( "No specific cause given.");

                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                String exceptionText = sw.toString();

                Label label = new Label("The exception stacktrace was:");

                TextArea textArea = new TextArea(exceptionText);
                textArea.setEditable(false);
                textArea.setWrapText(true);

                textArea.setMaxWidth(Double.MAX_VALUE);
                textArea.setMaxHeight(Double.MAX_VALUE);
                GridPane.setVgrow(textArea, Priority.ALWAYS);
                GridPane.setHgrow(textArea, Priority.ALWAYS);

                GridPane expContent = new GridPane();
                expContent.setMaxWidth(Double.MAX_VALUE);
                expContent.add(label, 0, 0);
                expContent.add(textArea, 0, 1);

                alert.getDialogPane().setExpandableContent(expContent);

                alert.showAndWait();
                stopProgress();
            });
        }
    }
}
