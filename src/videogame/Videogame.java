package videogame;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
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
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Videogame extends Application {
    
    //Posició inicial de la spaceship
    private int posX = 300;
    private int posY = 670;
    
    private static final int MOVE = 10;
    private int vidas = 5;
    private int score = 0;
    
    
    private List<ImageView> aliens = new ArrayList<>();
    private List<Rectangle> bullets = new ArrayList<>();
    private List<Circle> circles = new ArrayList<>();
    
    private Group root;
    
    //Contenidor per emmagatzemar els cors de les vides
    private HBox box;
    
    private Timeline alienTimeline;
    private Timeline bulletTimeline;
    
    //Variables necessàries per augmentar la velocitat a la que baixen els aliens
    private double alienDuration = 6000;
    private double durationDecrement = 1000;
    private double minDuration = 2000; 
    
    //Variables necessàries per fer que els aliens baixin de cada vegada amb més freqüència
    private double alienInterval = 2.0;
    private double intervalDecrement = 0.2;
    private double minInterval = 0.5;
    
    //Comptador intern per dur el control de la dificultat del joc
    private int cont = 0;
    
    private Text textScore;
    
    Media death;
    Media gameMusic;
    Media hurt;
    
    MediaPlayer gameMusicPlayer;
    
    @Override
    public void start(Stage stage) {
        
        //Accés a la imatge de la spaceship
        Image spaceship = new Image(new File(".\\src\\resources\\images\\spaceship.png").toURI().toString());
        ImageView spaceshipView = new ImageView(spaceship);
        spaceshipView.setFitHeight(130);
        spaceshipView.setFitWidth(130);
        spaceshipView.setY(posY);

        //Acces a la imatge de l'alien (tan sols la imatge perquè l'ImatgeView es genera despres
        Image alien = new Image(new File(".\\src\\resources\\images\\alien.png").toURI().toString());

        
        //Background picture
        Image background = new Image(new File(".\\src\\resources\\images\\estrellas7.png").toURI().toString());        
        ImageView backView = new ImageView(background);
        backView.setFitHeight(850);
        backView.setFitWidth(600);
        
        //Inicialització dels media
        death = new Media(new File(".\\src\\resources\\audios\\death.mp3").toURI().toString());
        
        gameMusic = new Media(new File(".\\src\\resources\\audios\\song1.mp3").toURI().toString());
        gameMusicPlayer = new MediaPlayer(gameMusic);
        gameMusicPlayer.setVolume(0.2);
        gameMusicPlayer.play();
        
        hurt = new Media(new File(".\\src\\resources\\audios\\hurt.mp3").toURI().toString());
        
        //Hearts box
        Image heartImg = new Image(new File(".\\src\\resources\\images\\heart.png").toURI().toString());
        box = new HBox(3);
        box.setAlignment(Pos.TOP_RIGHT);
        box.setTranslateX(320);
        box.setTranslateY(15);
        
        //Bucle for per afegir les vides al HBox
        for(int i = 0; i < vidas; i++){
            ImageView heart = new ImageView(heartImg);
            heart.setFitWidth(50);
            heart.setFitHeight(50);
            box.getChildren().add(heart);
        }

        //Inicialització de la timeline de la caiguda dels aliens. Aquesta crida al mètode spawnAlien, que conté l'agoritme i està definit més abaix
        alienTimeline = new Timeline(new KeyFrame(Duration.seconds(alienInterval), e -> spawnAlien(alien, spaceshipView)));
        alienTimeline.setCycleCount(Timeline.INDEFINITE);
        alienTimeline.play();
        
        //Un eventHandler per gestionar el moviment de la nau espacial
        EventHandler<KeyEvent> getMovement = new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent event) {
                
                //Actualització constant de la posició de la nau
                double currentX = spaceshipView.getTranslateX();
                double newX = currentX;
                
                //Si es pitja la tecla D o la fletxa de la dreta la nau es mou a la dreta 10 pixels (variable move definida adalt) i el mateix cap a l'esquerra
                switch(event.getCode()){
                    case D, RIGHT -> newX += MOVE;
                    case A, LEFT -> newX -= MOVE;
                }
                
                //Definició els limits del moviment de la nau
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
                
        //EventHandler que gestiona els dispars
        EventHandler<MouseEvent> getShooting = event -> {
            double xspaceship = spaceshipView.localToScene(spaceshipView.getBoundsInLocal()).getMinX(); //Agafa la posició de la nau
            double spaceshipCenter = xspaceship + spaceshipView.getBoundsInLocal().getWidth() / 2; //Dins la posicio de la nau, agafa el centre

            //Cada vegada que es fa clic es crea un nou rectangle (una bala)
            Rectangle bullet = new Rectangle(); 
            bullet.setX(spaceshipCenter); 
            bullet.setY(posY);
            bullet.setWidth(2);
            bullet.setHeight(25);
            bullet.setStroke(Color.MINTCREAM);
            bullet.setStrokeWidth(5);

            //S'afegeix la bala al group root
            root.getChildren().add(bullet);
            
            //Moviment de la bala
            TranslateTransition bulletTransition = new TranslateTransition(Duration.millis(1000), bullet);
            bulletTransition.setByY(-670); 
                
            bulletTransition.play();
            
            bullets.add(bullet); //S'afegeix la bala actual a la llista de bales definida globalment

            event.consume();
        };
        
       
        //Inicialització de la timeline de la bala, que crida al metode detectColisions, que gestiona les colisions entre les bales i els aliens
        bulletTimeline = new Timeline(new KeyFrame(Duration.millis(16), event -> detectCollisions()));
        bulletTimeline.setCycleCount(Timeline.INDEFINITE);
        bulletTimeline.play();
        
        //El text on es mostra l'score que es duu actualment
        textScore = new Text("SCORE: " + score);
        textScore.setFont(Font.font("Press Start 2P", 24)); 
        textScore.setFill(Color.WHITE);
        textScore.setTranslateX(30);
        textScore.setTranslateY(50);
        
        root = new Group();
        
        Button btn = new Button();
        btn.addEventFilter(KeyEvent.KEY_PRESSED, getMovement); //Es posa l'event que maneja el moviment de la nau damunt un botó, sinó no funciona, ja que no està assignat a res
        
        
        root.addEventFilter(MouseEvent.MOUSE_CLICKED, getShooting); //Igual que amb l'anterior event, aquest s'aplica damunt root per poder fer click a qualsevol banda del group i que s'agafi
        root.getChildren().addAll(btn, backView, spaceshipView, textScore, box); //Afegir la resta d'elements al root
        
        Scene scene = new Scene(root, 600, 820);

        stage.setTitle("The 8th passenger");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
    
    
    private void spawnAlien(Image alienImage, ImageView spaceshipView) {
        Platform.runLater(() -> {  //Com em creava problemes de concurrència, he hagut de sincronitzar diverses coses al codi per a que no es quedàs congelat. 
                                    //Els he sincronitzat utilitzant Platform.runLater
            ImageView alienCopy = new ImageView(alienImage); //Per cada alien es crea un imageView seu propi
            alienCopy.setFitWidth(70);
            alienCopy.setFitHeight(70);

            //Es defineixen els limits màxims on poden fer spawn (de manera random) els aliens (tenguent en compte que han d'apareixer com a mínim al centre de la nau quan aquesta és al limit,
            //ja que sinó no hi ha possibilitat de disparar-lo
            double minSpawn = spaceshipView.getFitWidth() / 2;
            double maxSpawn = 600 - spaceshipView.getFitWidth() / 2 - alienCopy.getFitWidth();
            int alienRandom2 = (int) (minSpawn + Math.random() * (maxSpawn - minSpawn));
            alienCopy.setX(alienRandom2);
            alienCopy.setY(-70);

            
            //Es defineix un cercle que té el mateix tamany que l'alien per poder gestionar d'una manera més còmoda les colisions
            Circle c2 = new Circle();
            c2.setFill(Color.TRANSPARENT);
            c2.setCenterX(alienRandom2 + alienCopy.getFitWidth() / 2);
            c2.setCenterY(-25);
            c2.setRadius(25);
            
            //Es crea un nou objecte alien per després verificar si s'ha destruit l'alien o no
            Alien a = new Alien(alienCopy, c2);

            //Gestió del moviment de l'alien
            TranslateTransition aTransition = new TranslateTransition(Duration.millis(alienDuration), alienCopy);
            aTransition.setByY(670);
            
            //Quan l'animació de l'alien acaba es mira si la llista d'ImageViews definida al principi conté l'imageView actual i 
            //si l'alien ha estat destruït
            aTransition.setOnFinished(ev -> Platform.runLater(() -> {
                
                if(aliens.contains(alienCopy) && !a.isDestroyed()){
                    //En cas de que si hagi estat destruit o de que si formi part de la llista, es resta una vida i es crida al mètode loseLife, que 
                    //actualitza els cors de vida de la interfície
                    vidas--;
                    loseLife();
                    
                    //Si les vides són menors o iguals a 0 es crida al mètode gameOver, que acaba el joc 
                    if(vidas <= 0){
                        try {
                            gameOver();
                            
                            //Efecte de so per quan s'acaben les vides
                            MediaPlayer deathPlayer = new MediaPlayer(death);
                            deathPlayer.setVolume(0.3);
                            deathPlayer.play();
                            gameMusicPlayer.stop();
                        } catch (Exception ex) {
                            Logger.getLogger(Videogame.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
                
                //Quan s'acaba l'animació de l'alien es lleva el cercle i l'alien de la seva respectiva llista
                aliens.remove(alienCopy);
                circles.remove(c2);
                root.getChildren().removeAll(alienCopy, c2); //També s'eliminen de l'interfície
            }));

            //Gestió de l'animació del cercle que envolta l'alien
            TranslateTransition cTransition = new TranslateTransition(Duration.millis(alienDuration), c2);
            cTransition.setByY(670);

            //S'afegeix l'alien i el cercle actual a les seves respectives llistes i s'afegeixen a l'interfície
            aliens.add(alienCopy);
            circles.add(c2);
            root.getChildren().addAll(c2, alienCopy);

            aTransition.play();
            cTransition.play();

            //El comptador suma un per cada alien que es crea
            cont++;
            //Si el comptador arriba a 5: 
            if (cont == 5) {
                
                //Es mira si la duració de lo que tarda l'alien en baixar es major a la duració minima(definida gloobalment)
                if (alienDuration > minDuration) {
                    //Si es major, es decrementa 
                    alienDuration -= durationDecrement;
                }

                //També es mira la freqüència amb sa que surten els aliens. Globalment hi ha un interval definit i un interval minim (com a la duracio)
                //Si la freqüència és major que la minima es decrementa
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
    
    
    //Mètode que serveix per detectar les colisions
    private void detectCollisions() {
        
        //Es crea una llista per cada item que tenim per emmagatzemar de cada un els que tenim que borrar 
        List<Rectangle> bulletsToRemove = new ArrayList<>();
        List<ImageView> aliensToRemove = new ArrayList<>();
        List<Circle> circlesToRemove = new ArrayList<>();

        //Es recorr la llista de bales, i com que si estàn allà significa que no s'han eliminat, establim un booleà amb valor false
        for (Rectangle bullet : bullets) {
            boolean bulletRemoved = false;

            //Recorrem la llista d'aliens i per cada iteració cream un alien i un cercle, i cream un objecte alien que crida a un mètode que gestiona l'alien actual
            for (int i = 0; i < aliens.size(); i++) {
                ImageView alien = aliens.get(i);
                Circle circle = circles.get(i);
                Alien a = currentAlien(alien);

                //Miram si la bala i el cercle s'initersecten
                if (bullet.getBoundsInParent().intersects(circle.getBoundsInParent())) {
                    
                    //Si s'intersecten canviam l'estat del mètode booleà d'alien a true
                    a.setIsDestroyed(true);
                    
                    //Augmentam els punts
                    score += 30;
                    textScore.setText("SCORE: " + Integer.toString(score));
                    
                    //Afegim a les llistes de "basura" cada item que volem borrar
                    aliensToRemove.add(alien);
                    circlesToRemove.add(circle);
                    bulletsToRemove.add(bullet);
                    bulletRemoved = true;
                    break;
                }
            }

            //Ens asseguram de que la bala quan surti de la pantalla s'elimini
            if (!bulletRemoved && bullet.getTranslateY() <= -670) {
                bulletsToRemove.add(bullet);
            }
        }


        //Eliminam de les llistes tots els items que hi ha a les llistes "basura"
        Platform.runLater(() -> {
            bullets.removeAll(bulletsToRemove);
            aliens.removeAll(aliensToRemove);
            circles.removeAll(circlesToRemove);

            //Els eliminam de l'interfície
            root.getChildren().removeAll(bulletsToRemove);
            root.getChildren().removeAll(aliensToRemove);
            root.getChildren().removeAll(circlesToRemove);
        });
    }
    
    //Mètode que serveix per retornar l'alien i el cercle actual
    public Alien currentAlien(ImageView av){
        for(int i = 0; i < aliens.size(); i++){
            if(aliens.get(i).equals(av)){
                return new Alien(aliens.get(i), circles.get(i));
            }
        }
        
        return null;
    }
    
    //Mètode que gestiona com s'eliminen les vides a l'interfície
    private void loseLife(){
     
        Platform.runLater(() -> {
            if(box.getChildren().size() > 0){
                box.getChildren().remove(box.getChildren().size() - 1);
                
                MediaPlayer hurtPlayer = new MediaPlayer(hurt);
                hurtPlayer.setVolume(0.3);
                hurtPlayer.play();
            }
        });
    }
    
    //Mètode que acaba el joc i passa a la següent pantalla (la de game over)
    private void gameOver() throws Exception{
        System.out.println ("Game Over");
        alienTimeline.stop();
        GameOver go = new GameOver();
        go.start((Stage) root.getScene().getWindow());
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
