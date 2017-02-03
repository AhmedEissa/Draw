import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * JavaFXDraw
 *
 * Simple drawing application written in JavaFX
 * @author Juneau
 */
public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    private BufferedImage image;

    public static void main(String[] args) {
        Application.launch(Main.class, args);
    }

    @Override
    public void start(Stage primaryStage) {
        int sceneWidth = (int) Screen.getPrimary().getVisualBounds().getWidth() / 3;
        int sceneHeight = (int) Screen.getPrimary().getVisualBounds().getHeight();
        StackPane root = new StackPane();
        primaryStage.getIcons().add(new Image("file:icon.png"));



        Canvas canvas = new Canvas(sceneWidth,
                sceneHeight-55 );


        final GraphicsContext graphicsContext =
                canvas.getGraphicsContext2D();
        graphicsContext.drawImage(copyBackground(),0,0);

//#############################################################################################
        final Button resetButton = new Button("Reset");

        resetButton.setOnAction(actionEvent-> {

            graphicsContext.drawImage(SwingFXUtils.toFXImage(image, null),0,0);

        });

        resetButton.setTranslateX(10);
        resetButton.setMinHeight(50.0);

        //resetButton.setAlignment(Pos.BOTTOM_CENTER);
//#############################################################################################
        // Set up the pen color chooser
        ChoiceBox colorChooser = new ChoiceBox(
                FXCollections.observableArrayList(
                        "Black", "Blue", "Red", "Green", "Brown", "Orange","White"
                ));

        // Select the first option by default
        colorChooser.getSelectionModel().selectFirst();

        colorChooser.getSelectionModel().
                selectedIndexProperty().addListener(
                (ChangeListener)(ov, old, newval) -> {
                    Number idx = (Number)newval;
                    Color newColor;
                    switch(idx.intValue()){
                        case 0: newColor = Color.BLACK;
                            break;
                        case 1: newColor = Color.BLUE;
                            break;
                        case 2: newColor = Color.RED;
                            break;
                        case 3: newColor = Color.GREEN;
                            break;
                        case 4: newColor = Color.BROWN;
                            break;
                        case 5: newColor = Color.ORANGE;
                            break;
                        case 6: newColor = Color.WHITE;
                            break;
                        default: newColor = Color.BLACK;
                            break;
                    }
                    graphicsContext.setStroke(newColor);

                });
        colorChooser.setTranslateX(5);
        colorChooser.setMinHeight(50.0);
        colorChooser.setStyle("-fx-alignment: bottom;");

//#############################################################################################
        ChoiceBox sizeChooser = new ChoiceBox(
                FXCollections.observableArrayList(
                        "1", "2", "3", "4", "5"
                ));
        // Select the first option by default
        sizeChooser.getSelectionModel().selectFirst();

        sizeChooser.getSelectionModel()
                .selectedIndexProperty().addListener(
                (ChangeListener)(ov, old, newval) -> {
                    Number idx = (Number)newval;

                    switch(idx.intValue()){
                        case 0: graphicsContext.setLineWidth(1);
                            break;
                        case 1: graphicsContext.setLineWidth(2);
                            break;
                        case 2: graphicsContext.setLineWidth(3);
                            break;
                        case 3: graphicsContext.setLineWidth(4);
                            break;
                        case 4: graphicsContext.setLineWidth(5);
                            break;
                        default: graphicsContext.setLineWidth(1);
                            break;
                    }
                });
        sizeChooser.setTranslateX(5);
        sizeChooser.setMinHeight(50.0);
        sizeChooser.setStyle("-fx-alignment: bottom;");
//#############################################################################################
        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                (MouseEvent event) -> {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(
                            event.getX(), event.getY()
                    );
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                (MouseEvent event) -> {
                    graphicsContext.lineTo(
                            event.getX(), event.getY()
                    );
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                (MouseEvent event) -> {
                    // DO NOTHING
                });

        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setMinHeight(50.0);
        buttonBox.setStyle("-fx-background-color: black;");
        buttonBox.getChildren().addAll(colorChooser,
                sizeChooser,
                resetButton);

        initDraw(graphicsContext, canvas.getLayoutX(),
                canvas.getLayoutY());

        BorderPane container = new BorderPane();
        container.setTop(buttonBox);


        container.setCenter(canvas);

        root.getChildren().add(container);



        Scene scene = new Scene(root,
                sceneHeight, sceneWidth);

        primaryStage.setTitle("Draw");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(sceneHeight);
        primaryStage.setMaxHeight(sceneHeight);
        primaryStage.setMinWidth(sceneWidth);
        primaryStage.setMaxWidth(sceneWidth);
        primaryStage.setX(0);
        primaryStage.setY(0);

        primaryStage.show();
    }


    private void initDraw(GraphicsContext gc,
                          double x, double y){
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.fill();

        gc.strokeRect(
                x,             //x of the upper left corner
                y,             //y of the upper left corner
                canvasWidth,   //width of the rectangle
                canvasHeight); //height of the rectangle

    }
    private Image copyBackground() {

        File file = new File("NSS_background.png");

        try {
            image = ImageIO.read(file);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return SwingFXUtils.toFXImage(image, null);
    }
}   