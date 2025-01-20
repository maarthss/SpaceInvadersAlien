package videogame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import static javafx.scene.input.KeyCode.S;
import static javafx.scene.input.KeyCode.UP;
import static javafx.scene.input.KeyCode.W;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

public class Videogame extends Application {
    
    private static final int move = 10;
    private int vidas = 5;
    private int score = 0;
    
    @Override
    public void start(Stage stage) {

        //ACCÉS ALS RESOURCES
        Button btn = new Button();
        
        //Spaceship png
        Image spaceship = new Image(new File(".\\src\\resources\\images\\spaceship.png").toURI().toString());
        ImageView spaceshipView = new ImageView(spaceship);
        spaceshipView.setFitHeight(130);
        spaceshipView.setFitWidth(130);
        spaceshipView.setY(700);

        //Alien png
        Image alien = new Image(new File(".\\src\\resources\\images\\alien.png").toURI().toString());
        ImageView alienView = new ImageView(alien);
        alienView.setFitHeight(70);
        alienView.setFitWidth(70);
        
        //Background picture
        Image background = new Image(new File(".\\src\\resources\\images\\estrellas2.jpeg").toURI().toString());
        BackgroundImage backImage = new BackgroundImage(background, null, null, null, null);
        Background back = new Background(backImage);
        
        ImageView backView = new ImageView(background);
        backView.setFitHeight(850);
        backView.setFitWidth(600);
        

        EventHandler<KeyEvent> getMovement = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1), spaceshipView);
                switch(event.getCode()){
                    case D, RIGHT -> tt.setByX(move);
                    case A, LEFT -> tt.setByX(-move);
                    /*case W, UP -> tt.setByY(-move);
                    case S, DOWN -> tt.setByY(move);*/
                }
                tt.play();
                event.consume();
            }
        };
        
        /*HBox spaceshipBox = new HBox();
        spaceshipBox.getChildren().addAll(spaceshipView);*/
                
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement);        
        
        StackPane root = new StackPane();
        root.setBackground(back);
        root.getChildren().addAll(btn, spaceshipView, alienView);
        
        Scene scene = new Scene(root, 600, 850);
    
        
        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.setMinHeight(850);
        stage.setMinWidth(600);
        stage.setMaxHeight(850);
        stage.setMaxWidth(600);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
