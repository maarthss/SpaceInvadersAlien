package videogame;

import java.io.File;
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

        /*alienTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e ->{
            Platform.runLater(() -> {
                ImageView alienCopy = new ImageView(alien);
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
                if(cont == 5){
                    if(alienDuration > minDuration){
                        alienDuration -= durationDecrement;
                    }
                    
                    if(alienInterval > minInterval){
                        alienInterval -= intervalDecrement;
                        alienTimeline.stop();
                        alienTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(alienInterval), this::start));
                        alienTimeline.play();
                    }
                    cont = 0;
                }
            });*/
            /*ImageView alienCopy = new ImageView(alien);
            alienCopy.setFitWidth(70);
            alienCopy.setFitHeight(70);
            
            double minSpawn = spaceshipView.getFitWidth() / 2;
            double maxSpawn = 600 - spaceshipView.getFitWidth() / 2 - alienCopy.getFitWidth();
            
            int alienRandom2 = (int) (minSpawn + Math.random() * (maxSpawn - minSpawn));
            alienCopy.setX(alienRandom2);
            alienCopy.setY(-70);*/
            
             /*L'imageView i el circle tenen diferents coordenades de referència, a posta s'han
            d'ajustar el centerX y el centerY */
            
            /*Circle c2 = new Circle();
            c2.setFill(Color.TRANSPARENT);
            c2.setCenterX(alienRandom2 + alienCopy.getFitWidth() / 2);
            c2.setCenterY(-25);
            c2.setRadius(25);*/
            
            /*TranslateTransition alienTransition = new TranslateTransition();
            alienTransition.setByY(630);
            alienTransition.setDuration(Duration.millis(alienDuration));
            //alienTransition.setCycleCount(1);
            alienTransition.setNode(alienCopy);
     
            
            TranslateTransition circleTransition = new TranslateTransition();
            circleTransition.setByY(630);
            circleTransition.setDuration(Duration.millis(alienDuration));
            //circleTransition.setCycleCount(1);
            circleTransition.setNode(c2);
            
            aliens.add(alienCopy);
            circles.add(c2);
            
            Platform.runLater(() -> root.getChildren().addAll(c2, alienCopy));
            //root.getChildren().addAll(c2, alienCopy);
            
            alienTransition.setOnFinished(event -> Platform.runLater(() ->{
                root.getChildren().removeAll(alienCopy, c2);
                aliens.remove(alienCopy);
                circles.remove(c2);
            }));
            
            /*alienTransition.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ev) {
                    root.getChildren().removeAll(c2, alienCopy);
                    aliens.remove(alienCopy);
                    circles.remove(c2);
                }
            });
            
            
            alienTransition.play();
            circleTransition.play();
            cont++;
            
            if(cont == 5){
                if(alienDuration > minDuration){
                    alienDuration -= durationDecrement;
                }
                if(alienInterval > minInterval){
                    alienInterval -= intervalDecrement;
                    
                    alienTimeline.stop();
                    alienTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(alienInterval), e2 -> alienTimeline.play()));
                    alienTimeline.play();
                }
                cont = 0;
            }*/

        //}));


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
                /*bulletTransition.setOnFinished(ev -> Platform.runLater(() -> {
                    bullets.remove(bullet);
                    root.getChildren().remove(bullet);
                }));*/
            bulletTransition.play();
            bullets.add(bullet);
            
            event.consume();
        };
        
        
        /*EventHandler<MouseEvent> getShooting = new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                
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
                bullets.add(bullet);
                
                //root.getChildren().add(bullet);
                
                TranslateTransition tt = new TranslateTransition(Duration.millis(1000), bullet);
                tt.setByY(-670);
                tt.setOnFinished(ev -> Platform.runLater(() -> {
                    bullets.remove(bullet);
                    root.getChildren().remove(bullet);
                }));
                tt.play();
                
                
                bullets.add(bullet);
                event.consume();
            }
        };*/
        
        
       /*bulletTimeline = new Timeline(new KeyFrame(Duration.millis(16), event ->{
            
            /*List<Rectangle> bulletsToRemove = new ArrayList<>();
            List<ImageView> aliensToRemove = new ArrayList<>();
            List<Circle> circlesToRemove = new ArrayList<>();
            
            for(Rectangle bullet : bullets){
                boolean bulletRemoved = false;
                
                Iterator<ImageView> alienIterator = aliens.iterator();
                Iterator<Circle> circleIterator = circles.iterator();
                
                while(alienIterator.hasNext() && circleIterator.hasNext()){
                    ImageView aliena = alienIterator.next();
                    Circle circle = circleIterator.next();
                    
                    if(bullet.getBoundsInParent().intersects(circle.getBoundsInParent())){
                        System.out.println("COLLISION");
                        
                        aliensToRemove.add(aliena);
                        circlesToRemove.add(circle);
                        bulletsToRemove.add(bullet);
                        
                        bulletRemoved = true;
                        break;
                    }
                }
                
                if(!bulletRemoved && bullet.getTranslateY()<= -670){
                    bulletsToRemove.add(bullet);
                }
            }
            
            for(Rectangle bullet: bulletsToRemove){
                root.getChildren().remove(bullet);
                bullets.remove(bullet);
            }
            
            for(ImageView aliena : aliensToRemove){
                root.getChildren().remove(aliena);
                aliens.remove(aliena);
            }
            
            for(Circle circle : circlesToRemove){
                root.getChildren().remove(circle);
                circles.remove(circle);
            }
            
            Iterator<Rectangle> bulletIterator = bullets.iterator();
            
            while(bulletIterator.hasNext()){
                Rectangle bullet = bulletIterator.next();
                boolean bulletRemoved = false;
                
                Iterator <ImageView> alienIterator = aliens.iterator();
                Iterator<Circle> circleIterator = circles.iterator();
                
                while(alienIterator.hasNext() && circleIterator.hasNext()){
                    ImageView aliena = alienIterator.next();
                    Circle circle = circleIterator.next();
                    
                    if(bullet.getBoundsInParent().intersects(circle.getBoundsInParent())){
                        System.out.println("COLISION");
                        
                        
                        Platform.runLater(() -> {
                            root.getChildren().removeAll(aliena, circle);
                            alienIterator.remove();
                            circleIterator.remove();
                        });
                        
                        Platform.runLater(() -> {
                            root.getChildren().remove(bullet);
                            bulletIterator.remove();
                        });
                        
                        /*root.getChildren().removeAll(aliena, circle);
                        alienIterator.remove();
                        circleIterator.remove();
                        
                        root.getChildren().remove(bullet);
                        bulletIterator.remove();
                        
                        bulletRemoved  = true;
                        break;
                    }
                }
                if(!bulletRemoved && bullet.getTranslateY() <= -670){
                    Platform.runLater(()->{
                        root.getChildren().remove(bullet);
                        bulletIterator.remove();
                    });
                    /*root.getChildren().remove(bullet);
                    bulletIterator.remove();
                }
                
            }
           
        })); */
       
       /*bulletTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            synchronized (bullets) {
                List<Rectangle> bulletsToRemove = new ArrayList<>();
                List<ImageView> aliensToRemove = new ArrayList<>();
                List<Circle> circlesToRemove = new ArrayList<>();

                for (Rectangle bullet : bullets) {
                    for (int i = 0; i < aliens.size(); i++) {
                        ImageView aliena = aliens.get(i);
                        Circle circle = circles.get(i);

                        if (bullet.getBoundsInParent().intersects(circle.getBoundsInParent())) {
                            bulletsToRemove.add(bullet);
                            aliensToRemove.add(aliena);
                            circlesToRemove.add(circle);
                            break;
                        }
                    }
                }

                Platform.runLater(() -> {
                    root.getChildren().removeAll(bulletsToRemove);
                    root.getChildren().removeAll(aliensToRemove);
                    root.getChildren().removeAll(circlesToRemove);

                    bullets.removeAll(bulletsToRemove);
                    aliens.removeAll(aliensToRemove);
                    circles.removeAll(circlesToRemove);
                });
            }
        }));*/
       
        bulletTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> detectCollisions()));
        bulletTimeline.setCycleCount(Timeline.INDEFINITE);
        bulletTimeline.play();
        
        
        root = new Group();
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement);  
        
        
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting);
        root.getChildren().addAll(btn, backView, spaceshipView);
        
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
    
    
    /*private void detectCollisions() {
        Iterator<Rectangle> bulletIterator = bullets.iterator();

        while (bulletIterator.hasNext()) {
            Rectangle bullet = bulletIterator.next();
            boolean bulletRemoved = false;

            Iterator<ImageView> alienIterator = aliens.iterator();
            Iterator<Circle> circleIterator = circles.iterator();

            while (alienIterator.hasNext() && circleIterator.hasNext()) {
                ImageView aliena = alienIterator.next();
                Circle circle = circleIterator.next();

                if (bullet.getBoundsInParent().intersects(circle.getBoundsInParent())) {
                    Platform.runLater(() -> {
                        root.getChildren().removeAll(aliena, circle);
                        alienIterator.remove();
                        circleIterator.remove();
                    });

                    Platform.runLater(() -> {
                        root.getChildren().remove(bullet);
                        bulletIterator.remove();
                    });

                    bulletRemoved = true;
                    break;
                }
            }

            if (!bulletRemoved && bullet.getTranslateY() <= -670) {
                Platform.runLater(() -> {
                    root.getChildren().remove(bullet);
                    bulletIterator.remove();
                });
            }
        }
    }*/
    
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
                    System.out.println(score);
                    
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
