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

public class Videogame extends Application {
    
    private static final int move = 10;
    
    @Override
    public void start(Stage stage) {
        
        
        //ACCÉS ALS RESOURCES
        
        Button btn = new Button();
        
        //Spaceship png

        Image spaceship = new Image(new File(".\\src\\resources\\images\\spaceship.png").toURI().toString());
        ImageView spaceshipView = new ImageView(spaceship);
        spaceshipView.setFitHeight(130);
        spaceshipView.setFitWidth(130);
        
        
        
        //Alien png
        Image alien = new Image(new File(".\\src\\resources\\images\\alien.png").toURI().toString());
        ImageView alienView = new ImageView(alien);
        alienView.setFitHeight(70);
        alienView.setFitWidth(70);
        


        
        EventHandler<KeyEvent> getMovement = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1), spaceshipView);
                switch(event.getCode()){
                    case D, RIGHT -> tt.setByX(move);
                    case A, LEFT -> tt.setByX(-move);
                    case W, UP -> tt.setByY(-move);
                    case S, DOWN -> tt.setByY(move);
                }
                tt.play();
                event.consume();
            }
        };
        
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement);        
        
        Group root = new Group();
        root.getChildren().addAll(btn, spaceshipView, alienView);
        
        Scene scene = new Scene(root, 600, 850);
        
        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
