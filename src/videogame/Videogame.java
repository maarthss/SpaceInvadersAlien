package videogame;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Videogame extends Application {
     
    private int posX = 300;
    private int posY = 670;
    
    private static final int MOVE = 10;
    private int vidas = 5;
    private int score = 0;
    
    
    private List<ImageView> aliens = new ArrayList<>();
    private List<Rectangle> bullets = new ArrayList<>();
    private List<Circle> circles = new ArrayList<>();
    
    private Group root;
    
    private Timeline alienTimeline;
    private Timeline bulletTimeline;
    
    private double alienDuration = 6000;
    private double durationDecrement = 1000;
    private double minDuration = 2000; 
    
    private double alienInterval = 2.0;
    private double intervalDecrement = 0.2;
    private double minInterval = 0.5;
    
    private int cont = 0;
    
    private Label labelScore;
    
    
    @Override
    public void start(Stage stage) {

        //ACCÉS ALS RESOURCES
        Button btn = new Button();
        
        //Spaceship png
        Image spaceship = new Image(new File(".\\src\\resources\\images\\spaceship.png").toURI().toString());
        ImageView spaceshipView = new ImageView(spaceship);
        spaceshipView.setFitHeight(130);
        spaceshipView.setFitWidth(130);
        spaceshipView.setY(posY);
        //spaceshipView.setX(300);

        //Alien png
        Image alien = new Image(new File(".\\src\\resources\\images\\alien.png").toURI().toString());

        
        //Background picture
        Image background = new Image(new File(".\\src\\resources\\images\\estrellas2.jpeg").toURI().toString());        
        ImageView backView = new ImageView(background);
        backView.setFitHeight(850);
        backView.setFitWidth(600);
        

        alienTimeline = new Timeline(new KeyFrame(Duration.seconds(alienInterval), e -> spawnAlien(alien, spaceshipView)));
        alienTimeline.setCycleCount(Timeline.INDEFINITE);
        alienTimeline.play();


        alienTimeline.setCycleCount(Timeline.INDEFINITE);
        alienTimeline.play();
        

        EventHandler<KeyEvent> getMovement = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                double currentX = spaceshipView.getTranslateX();
                double newX = currentX;
                
                switch(event.getCode()){
                    case D, RIGHT -> newX += MOVE;
                    case A, LEFT -> newX -= MOVE;
                }
                
                double minX = 0;
                double maxX = 600 - spaceshipView.getFitWidth();

                if(newX >= minX && newX <= maxX){
                    TranslateTransition tt = new TranslateTransition(Duration.millis(0.5), spaceshipView);
                    
                    tt.setByX(newX - currentX);
                    tt.play();
                }
                event.consume();
            }
        };
        
        
        //Button btn1 = new Button();
        
        
        EventHandler<MouseEvent> getShooting = event -> {
            double xspaceship = spaceshipView.localToScene(spaceshipView.getBoundsInLocal()).getMinX();
            double spaceshipCenter = xspaceship + spaceshipView.getBoundsInLocal().getWidth() / 2;

            Rectangle bullet = new Rectangle();
            bullet.setX(spaceshipCenter);
            bullet.setY(posY);
            bullet.setWidth(2);
            bullet.setHeight(25);
            bullet.setStroke(Color.MINTCREAM);
            bullet.setStrokeWidth(5);

            root.getChildren().add(bullet);

            TranslateTransition bulletTransition = new TranslateTransition(Duration.millis(1000), bullet);
            bulletTransition.setByY(-670);
                
            bulletTransition.play();
            bullets.add(bullet);
            
            event.consume();
        };
        
       
        bulletTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> detectCollisions()));
        bulletTimeline.setCycleCount(Timeline.INDEFINITE);
        bulletTimeline.play();
        
        InputStream fontStream = getClass().getResourceAsStream("/resources/PressStart2P-Regular.ttf");
            if (fontStream == null) {
                System.out.println("¡No se encontró la fuente!");
            } else {
                Font font = Font.loadFont(fontStream, 32);
                if (font == null) {
                    System.out.println("Error al cargar la fuente.");
                } else {
                    labelScore = new Label("SCORE: 0");
                    labelScore.setFont(font);
                    labelScore.setTranslateX(30);
                    labelScore.setTranslateY(30);
                }
}
        
        /*Font font = Font.loadFont(getClass().getResourceAsStream(".\\src\\resources\\PressStart2P-Regular.ttf"), 32);
        labelScore = new Label("SCORE: 0");
        labelScore.setFont(font);
        labelScore.setTranslateX(30);
        labelScore.setTranslateY(30);*/
        
        
        root = new Group();
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement);  
        
        
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        root.getChildren().addAll(btn, backView, spaceshipView, labelScore);
        
        Scene scene = new Scene(root, 600, 850);
        System.out.println("Scene: " + scene.getWidth());

        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    
    private void spawnAlien(Image alienImage, ImageView spaceshipView) {
        Platform.runLater(() -> {
            ImageView alienCopy = new ImageView(alienImage);
            alienCopy.setFitWidth(70);
            alienCopy.setFitHeight(70);

            double minSpawn = spaceshipView.getFitWidth() / 2;
            double maxSpawn = 600 - spaceshipView.getFitWidth() / 2 - alienCopy.getFitWidth();
            int alienRandom2 = (int) (minSpawn + Math.random() * (maxSpawn - minSpawn));
            alienCopy.setX(alienRandom2);
            alienCopy.setY(-70);

            Circle c2 = new Circle();
            c2.setFill(Color.TRANSPARENT);
            c2.setCenterX(alienRandom2 + alienCopy.getFitWidth() / 2);
            c2.setCenterY(-25);
            c2.setRadius(25);

            TranslateTransition aTransition = new TranslateTransition(Duration.millis(alienDuration), alienCopy);
            aTransition.setByY(630);
            aTransition.setOnFinished(ev -> Platform.runLater(() -> {
                aliens.remove(alienCopy);
                circles.remove(c2);
                root.getChildren().removeAll(alienCopy, c2);
            }));

            TranslateTransition cTransition = new TranslateTransition(Duration.millis(alienDuration), c2);
            cTransition.setByY(630);

            aliens.add(alienCopy);
            circles.add(c2);
            root.getChildren().addAll(c2, alienCopy);

            aTransition.play();
            cTransition.play();

            cont++;
            if (cont == 5) {
                if (alienDuration > minDuration) {
                    alienDuration -= durationDecrement;
                }

                if (alienInterval > minInterval) {
                    alienInterval -= intervalDecrement;
                    alienTimeline.stop();
                    alienTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(alienInterval), e -> spawnAlien(alienImage, spaceshipView)));
                    alienTimeline.play();
                }
                cont = 0;
            }
        });
    }
    
    
    private void detectCollisions() {
        List<Rectangle> bulletsToRemove = new ArrayList<>();
        List<ImageView> aliensToRemove = new ArrayList<>();
        List<Circle> circlesToRemove = new ArrayList<>();

    // Detectar colisiones
        for (Rectangle bullet : bullets) {
            boolean bulletRemoved = false;

            for (int i = 0; i < aliens.size(); i++) {
                ImageView alien = aliens.get(i);
                Circle circle = circles.get(i);

                if (bullet.getBoundsInParent().intersects(circle.getBoundsInParent())) {
                    
                    score += 30;
                    labelScore.setText("SCORE: " + Integer.toString(score));
                    
                    aliensToRemove.add(alien);
                    circlesToRemove.add(circle);
                    bulletsToRemove.add(bullet);
                    bulletRemoved = true;
                    break;
                }
            }

            // Si la bala sale de la pantalla, marcarla para eliminar
            if (!bulletRemoved && bullet.getTranslateY() <= -670) {
                bulletsToRemove.add(bullet);
            }
        }

        // Eliminar los elementos marcados
        Platform.runLater(() -> {
            bullets.removeAll(bulletsToRemove);
            aliens.removeAll(aliensToRemove);
            circles.removeAll(circlesToRemove);

            root.getChildren().removeAll(bulletsToRemove);
            root.getChildren().removeAll(aliensToRemove);
            root.getChildren().removeAll(circlesToRemove);
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
