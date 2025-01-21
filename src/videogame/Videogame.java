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
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;

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
        spaceshipView.setTranslateY(670);

        //Alien png
        Image alien = new Image(new File(".\\src\\resources\\images\\alien.png").toURI().toString());
        ImageView alienView = new ImageView(alien);
        alienView.setFitHeight(70);
        alienView.setFitWidth(70);
        
        //Generar un num random entre 0 y 600, que es l'amplada de la finestra,
        //perque els aliens es generin a un punt aleatori de l'eix X. L'eix Y sempre es 0
        int alienRandom = (int)(Math.random()*595);
        System.out.println(alienRandom);
        
        alienView.setTranslateY(0);
        alienView.setTranslateX(alienRandom);

        
        //Background picture
        Image background = new Image(new File(".\\src\\resources\\images\\estrellas2.jpeg").toURI().toString());        
        ImageView backView = new ImageView(background);
        backView.setFitHeight(850);
        backView.setFitWidth(600);
        
        TranslateTransition alienTranslate = new TranslateTransition();
        alienTranslate.setByY(630);
        alienTranslate.setDuration(Duration.millis(6000));
        alienTranslate.setNode(alienView);
        alienTranslate.play();
        alienTranslate.setOnFinished((event) -> {
            alienView.setVisible(false);
        });
        

        //Make aliens move and go down
        /*while(vidas > 5){
            alienTranslate.play();
        
            alienTranslate.setOnFinished((event) -> {
                alienView.setVisible(false);
            });
        }*/

        

        EventHandler<KeyEvent> getMovement = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1), spaceshipView);
                switch(event.getCode()){
                    case D, RIGHT -> tt.setByX(move);
                    case A, LEFT -> tt.setByX(-move);
                }
                System.out.println(spaceshipView.getTranslateX());
                tt.play();
                event.consume();
            }
        };
        
        
        Button btn1 = new Button();
        
        
        
        EventHandler<MouseEvent> getShooting = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                
                double xspaceship = spaceshipView.getTranslateX();
                Line bullet = new Line();
                bullet.setStartX(xspaceship);
                bullet.setStartY(670);
                /*bullet.setEndX(xspaceship);
                bullet.setEndY(0);*/
                bullet.setStroke(Color.MINTCREAM);
                bullet.setStrokeWidth(5);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1000), bullet);
                tt.setByX(xspaceship);
                tt.setByY(0);
                
                tt.play();
                event.consume();
               
            }
        };
        
        //Per fer moltes linies petites que serveixen de dispars
        //int cont = 670;

        /*for(int i = 0; i < 10; i++){
            bullet.setStartX(xspaceship);
            System.out.println(cont);
            bullet.setStartY(cont);
            cont = cont - 50;
            System.out.println(cont);
            bullet.setEndX(xspaceship);
            bullet.setEndY(cont);
            System.out.println(cont);
            bullet.setStroke(Color.MINTCREAM);
            bullet.setStrokeWidth(5);
        }*/
        
                
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement);  
        btn1.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        
        Group root = new Group();
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        root.getChildren().addAll(btn, backView, spaceshipView, alienView, btn1);
        
        Scene scene = new Scene(root, 600, 850);
    
        
        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    /*private void checkBounds(Shape block){
        boolean collisionDetected = false;
        for(Shape static_bloc : nodes){
            if(static_bloc != block){
                static_bloc.setFill(Color.GREEN);
                
                if(block.getBoundsInParen().intersects(static_bloc.getBoundsInParent())){
                    collisionDetected = true;
                }
            }
        }
    }*/

    public static void main(String[] args) {
        launch(args);
    }
    
}
