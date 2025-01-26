
package videogame;

import java.io.File;
import java.io.InputStream;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class GameOver extends Application{
    
    @Override
    public void start(Stage stage) throws Exception {

        Image img = new Image(new File(".\\src\\resources\\images\\estrellas3.gif").toURI().toString());
        ImageView iv = new ImageView(img);
        iv.setFitHeight(820);
        iv.setFitWidth(600);
        
        Text t = new Text("GAME OVER");
        t.setFont(Font.font("Press Start 2P", 35));
        t.setFill(Color.WHITE);
        t.setY(410);
        t.setX(140);
        
        Group root = new Group();
        
        root.getChildren().addAll(iv, t);
        
        Scene scene = new Scene(root, 600, 820);
        stage.setScene(scene);
        stage.setTitle("Game over");
        stage.show();
    }
    
    
    public static void main(String[] args){
        launch(args);
    }
}

