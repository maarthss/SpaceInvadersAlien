package videogame;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import static javafx.scene.input.KeyCode.A;
import static javafx.scene.input.KeyCode.D;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Videogame extends Application {
    
    private static final int move = 10;
    private int vidas = 5;
    private int score = 0;
    
    private List<ImageView> aliens = new ArrayList<>();
    private List<Rectangle> bullets = new ArrayList<>();
    
    private Group root;
    
    Rectangle bullet = new Rectangle();
    ImageView alienView;
    Circle c;
    ImageView alienCopy;
    Circle c2;
    
    
    
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
        alienView = new ImageView(alien);
        alienView.setFitHeight(70);
        alienView.setFitWidth(70);
        

        
        //Generar un num random entre 0 y 600, que es l'amplada de la finestra,
        //perque els aliens es generin a un punt aleatori de l'eix X. L'eix Y sempre es 0
        int alienRandom = (int)(Math.random()*595);
        System.out.println(alienRandom);
        
        alienView.setTranslateY(-70);
        alienView.setTranslateX(alienRandom);
        
        
        //Circle de colision per alien
        c = new Circle();
        c.setFill(Color.GREENYELLOW);
        c.setCenterX(alienRandom + alienView.getFitWidth() / 2);
        c.setCenterY(-25);  
        c.setRadius(25);
        
        /*L'imageView i el circle tenen diferents coordenades de referència, a posta s'han
        d'ajustar el centerX y el centerY */

        
        //Background picture
        Image background = new Image(new File(".\\src\\resources\\images\\estrellas2.jpeg").toURI().toString());        
        ImageView backView = new ImageView(background);
        backView.setFitHeight(850);
        backView.setFitWidth(600);
        
        TranslateTransition alienTranslate = new TranslateTransition();
        alienTranslate.setByY(630);
        alienTranslate.setDuration(Duration.millis(6000));
        //alienTranslate.setCycleCount(move);
        alienTranslate.setNode(alienView);
        alienTranslate.play();
        alienTranslate.setOnFinished((event) -> {
        alienView.setVisible(false);      
        });
        
        TranslateTransition ct = new TranslateTransition();
        ct.setByY(630);
        ct.setDuration(Duration.millis(6000));
        ct.setNode(c);
        ct.play();
        
        
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(2), e ->{
            alienCopy = new ImageView(alien);
            alienCopy.setFitWidth(70);
            alienCopy.setFitHeight(70);
            int alienRandom2 = (int) (Math.random() * 595);
            alienCopy.setX(alienRandom2);
            alienCopy.setY(-70);
            
            c2 = new Circle();
            c2.setFill(Color.GREENYELLOW);
            c2.setCenterX(alienRandom2 + alienCopy.getFitWidth() / 2);
            c2.setCenterY(-25);
            c2.setRadius(25);
            
            TranslateTransition copyTranslate = new TranslateTransition();
            copyTranslate.setByY(630);
            copyTranslate.setDuration(Duration.millis(6000));
            copyTranslate.setCycleCount(1);
            copyTranslate.setNode(alienCopy);
            copyTranslate.play();
            copyTranslate.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ev) {
                    alienCopy.setVisible(false);
                }
            });
            
            TranslateTransition ct2 = new TranslateTransition();
            ct2.setByY(630);
            ct2.setDuration(Duration.millis(6000));
            ct2.setCycleCount(1);
            ct2.setNode(c2);
            ct2.play();
            
            aliens.add(alienCopy);
            root.getChildren().addAll(c2, alienCopy);

        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
        

        EventHandler<KeyEvent> getMovement = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(0.5), spaceshipView);
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
                
                double xspaceship = spaceshipView.localToScene(spaceshipView.getBoundsInLocal()).getMinX();
                double spaceshipCenter = xspaceship + spaceshipView.getBoundsInLocal().getWidth() / 2;
                bullet = new Rectangle();
                bullet.setX(spaceshipCenter);
                bullet.setY(670);
                bullet.setWidth(2);
                bullet.setHeight(25);
                bullet.setStroke(Color.MINTCREAM);
                bullet.setStrokeWidth(5);
                
                root.getChildren().add(bullet);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1000), bullet);
                tt.setByY(-670);
                
                tt.play();
                bullets.add(bullet);
                event.consume();
                //System.out.println("bullet - X1: " + bullet.getStartX() + ", Y1: " + bullet.getStartY());
            }
        };
        
        System.out.println("alienView - X: " + alienView.getLayoutX() + ", Y: " + alienView.getLayoutY());
        
        System.out.println("Line bounds in parent: " + bullet.getBoundsInParent());
        System.out.println("Circle bounds in parent: " + c.getBoundsInParent());
        
        
        Timeline bulletMovement = new Timeline(new KeyFrame(Duration.millis(16), event ->{
            Iterator<Rectangle> bulletIterator = bullets.iterator();
            while(bulletIterator.hasNext()){
                bullet = bulletIterator.next();
                //bullet.setX(bullet.getX() + 5);
                
                if(bullet.getBoundsInParent().intersects(c.getBoundsInParent())){
                    System.out.println("Col");
                    root.getChildren().remove(bullet);
                    bulletIterator.remove();
                }
                
                if(bullet.getX() > 800){
                    root.getChildren().remove(bullet);
                    bulletIterator.remove();
                }
            }
        }));
        
        bulletMovement.setCycleCount(Timeline.INDEFINITE);
        bulletMovement.play();
      
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement);  
        
        root = new Group();
        
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        root.getChildren().addAll(btn, backView, spaceshipView, c, alienView);
        
        Scene scene = new Scene(root, 600, 850);

        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
