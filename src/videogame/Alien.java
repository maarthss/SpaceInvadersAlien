package videogame;

import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class Alien {
    
    private final ImageView alienView;
    private final Circle alienCircle;
    private boolean isDestroyed;

    public Alien(ImageView alienView, Circle alienCircle) {
        this.alienView = alienView;
        this.alienCircle = alienCircle;  
        this.isDestroyed = false;
                
    }
    
    public boolean isDestroyed(){
        return isDestroyed;
    }
    
    public void setIsDestroyed(boolean isDestroyed){
        this.isDestroyed = isDestroyed;
    }
    
    public ImageView getAlienView(){
        return alienView;
    }
    
    public Circle getAlienCircle(){
        return alienCircle;
    }
    
    
}
