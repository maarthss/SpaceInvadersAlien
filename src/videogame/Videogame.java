package videogame;

import java.io.File;
import java.util.ArrayList;
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
import javafx.scene.shape.Line;

public class Videogame extends Application {
    
    private static final int move = 10;
    private int vidas = 5;
    private int score = 0;
    
    private List<ImageView> aliens = new ArrayList<>();
    private List<Line> bullets = new ArrayList<>();
    
    private Group root;
    
    
    
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
        
        alienView.setTranslateY(-70);
        alienView.setTranslateX(alienRandom);

        
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
        
        
        Timeline tl = new Timeline(new KeyFrame(Duration.seconds(2), e ->{
            ImageView alienCopy = new ImageView(alien);
            alienCopy.setFitWidth(70);
            alienCopy.setFitHeight(70);
            int alienRandom2 = (int) (Math.random() * 595);
            alienCopy.setX(alienRandom2);
            alienCopy.setY(-70);
            
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
            aliens.add(alienCopy);
            root.getChildren().add(alienCopy);

        }));

        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
        

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
                
                double xspaceship = spaceshipView.localToScene(spaceshipView.getBoundsInLocal()).getMinX();
                double spaceshipCenter = xspaceship + spaceshipView.getBoundsInLocal().getWidth() / 2;
                Line bullet = new Line();
                bullet.setStartX(spaceshipCenter);
                bullet.setStartY(670);
                bullet.setEndX(spaceshipCenter);
                bullet.setEndY(640);
                bullet.setStroke(Color.MINTCREAM);
                bullet.setStrokeWidth(5);
                
                root.getChildren().add(bullet);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1000), bullet);
                tt.setByY(-670);
                
                tt.play();
                bullets.add(bullet);
                event.consume();
                System.out.println("bullet - X1: " + bullet.getStartX() + ", Y1: " + bullet.getStartY());
            }
        };
        
        System.out.println("alienView - X: " + alienView.getLayoutX() + ", Y: " + alienView.getLayoutY());
        
        
        
        //Codi chatgpt
        /*Timeline collisionCheckTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            for (Line bullet : bullets) {
                for (ImageView alien : aliens) {
                    if (bullet.getBoundsInParent().intersects(alien.getBoundsInParent())) {
                        // Si hay colisión, eliminamos el alien y la bala
                        root.getChildren().remove(bullet);
                        root.getChildren().remove(alien);
                        bullets.remove(bullet);
                        aliens.remove(alien);
                        break; // Detener la búsqueda de más colisiones para esta bala
                    }
                }
            }
        }));

        collisionCheckTimeline.setCycleCount(Timeline.INDEFINITE);
        collisionCheckTimeline.play(); // Iniciar la verificación de colisiones en tiempo real
    }*/
        
        
        /*Rectangle rect = new Rectangle(alienView.getLayoutX(), alienView.getLayoutY(), alienView.getFitWidth(), alienView.getFitHeight());
        boolean intersectionLineRect = rect.intersects(bullet.getBoundsInLocal());
        if(intersectionLineRect){
            System.out.println("Colision");
        }*/
        
        /*if(bullet.getBoundsInParent().intersects(alienView.getBoundsInParent())){
            System.out.println("Hola");
        }*/
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
        
        root = new Group();
        
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        root.getChildren().addAll(btn, backView, spaceshipView, alienView);
        
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
