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
import javafx.scene.shape.Polygon;

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
        spaceshipView.setY(670);
        //spaceshipView.setX(300);

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
        
        Polygon pAlien = new Polygon(
                alienRandom, 0,
                alienRandom + 70, 0,
                alienRandom + 70, 70,
                alienRandom, 70
        );
        pAlien.setFill(Color.TRANSPARENT);
        pAlien.setStroke(Color.RED);
        
        
        //Fer un mètode genèric que servesqui per s'alien i pes polygon
        TranslateTransition alienTranslate = new TranslateTransition();
        alienTranslate.setByY(630);
        alienTranslate.setDuration(Duration.millis(6000));
        //alienTranslate.setCycleCount(move);
        alienTranslate.setNode(alienView);
        alienTranslate.setNode(pAlien);
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
        Line bullet = new Line();
        
        EventHandler<MouseEvent> getShooting = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                
                double xspaceship = spaceshipView.localToScene(spaceshipView.getBoundsInLocal()).getMinX();
                double spaceshipCenter = xspaceship + spaceshipView.getBoundsInLocal().getWidth() / 2;
                bullet.setStartX(spaceshipCenter);
                bullet.setStartY(670);
                bullet.setEndX(spaceshipCenter);
                bullet.setEndY(640);
                bullet.setStroke(Color.MINTCREAM);
                bullet.setStrokeWidth(5);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1000), bullet);
                tt.setByX(0);
                tt.setByY(-670);
                
                tt.play();
                event.consume();
                
            }
        };
        
        
        
        
        if(bullet.getBoundsInParent().intersects(alienView.getBoundsInParent())){
            System.out.println("Hola");
        }
        //Per fer moltes linies petites que serveixen de dispars
        //int cont = 670;
        
        /*Line line = new Line();
        line.setStartX();
        line.setStartY(670);
        line.setEndX(xsp);
        line.setEndY(100);
        line.setStroke(Color.BLUEVIOLET);
        line.setStrokeWidth(7);*/
        

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
        
        Group root = new Group();
        
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        root.getChildren().addAll(btn, backView, spaceshipView, alienView, bullet, pAlien);
        
        Scene scene = new Scene(root, 600, 850);
    
        
        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    /*private void repeatBullet(ImageView spView){
        Line bullet = new Line();
        
        while (vidas > 0){
            EventHandler<MouseEvent> getShooting = new EventHandler<MouseEvent>(){
            @Override
                public void handle(MouseEvent event){
                
                    double xspaceship = spView.localToScene(spView.getBoundsInLocal()).getMinX();
                    double spaceshipCenter = xspaceship + spView.getBoundsInLocal().getWidth() / 2;
                    bullet.setStartX(spaceshipCenter);
                    bullet.setStartY(670);
                    bullet.setEndX(spaceshipCenter);
                    bullet.setEndY(640);
                    bullet.setStroke(Color.MINTCREAM);
                    bullet.setStrokeWidth(5);
                
                    TranslateTransition tt = new TranslateTransition(Duration.millis(1000), bullet);
                    tt.setByX(0);
                    tt.setByY(-670);
                
                    tt.play();
                    event.consume();
                }
            };
        }
    }*/
    
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
