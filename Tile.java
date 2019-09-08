import java.util.Random;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;

public class Tile
{
    private Rectangle rectangle;
    private Color color;
    private double score;
    private double timer = -1;
    private double timerMax = -1;

    //Constructors
    public Tile()
    {
        rectangle = new Rectangle(0,-95.5,95,95);
        rectangle.setStrokeWidth(4);
        setRandomColor();
        setTimer(-1);
    }

    public Tile(double timer)
    {
        rectangle = new Rectangle(0,-95.5,95,95);
        rectangle.setStrokeWidth(4);
        setRandomColor();
        setTimer(timer);
        timerMax = getTimer();
    }
    
    public Tile(double timer, Color colorA, Color colorB)
    {
        rectangle = new Rectangle(0,-95.5,95,95);
        rectangle.setStrokeWidth(4);
        setRandomColor(colorA,colorB);
        setTimer(timer);
        timerMax = getTimer();
    }
    
    public Tile(Color colorA, Color colorB)
    {
        rectangle = new Rectangle(0,-95.5,95,95);
        rectangle.setStrokeWidth(4);
        setRandomColor(colorA,colorB);
        setTimer(-1);
        timerMax = getTimer();
    }

    //Setters
    public void setColor(Color color)
    {
        this.color = color;
        rectangle.setFill(color);
        rectangle.setStroke(color.darker());
    }

    //Choose a random color above brightness 50 and below brightness 250
    public void setRandomColor() {
        Random rand = new Random();
        setColor(Color.rgb(rand.nextInt(200) + 50, rand.nextInt(200) + 50,rand.nextInt(200) + 50));
        
    }

    //Random interpolation between colorA & colorB
    public void setRandomColor(Color colorA, Color colorB)
    {
        Random rand = new Random();
        setColor(colorA.interpolate(colorB,rand.nextDouble()));
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setTimer(double timer) {
        this.timer = timer;
    }

    public void setX(double x) {
        rectangle.setX(x);
    }

    public void setY(double y) {
        rectangle.setY(y);
    }

    //Getters
    public Rectangle getRectangle() {
        return rectangle;
    }

    public Color getColor() {
        return color;
    }

    public double getScore() {
        return score;
    }

    public double getTimer() {
        return timer;
    }

    public double getX() {
        return rectangle.getX();
    }

    public double getY() {
        return rectangle.getY();
    }

    //Return timer text for the tile
    public Text getText() {
        if (timer != -1) {
            Text timerText = new Text();

            //Adding extra 0 and choosing number
            if (((int)Math.floor(timer/6)+1) > 9) {
                timerText.setText(""+((int)Math.floor(timer/6)+1));
            } else {
                timerText.setText("0"+((int)Math.floor(timer/6)+1));
            }

            timerText.setOpacity((timer/timerMax)-Math.floor(timer/timerMax)+0.2);
            timerText.setX(rectangle.getX() + 22);
            timerText.setY(rectangle.getY() + 62);
            timerText.setFill(Color.WHITE);
            timerText.setStroke(Color.BLACK);
            timerText.setStrokeWidth(1);
            timerText.setFont(Font.font("Courier"
                ,FontWeight.BOLD
                ,FontPosture.REGULAR
                ,45));

            return timerText;
        }
        //Returns empty text if timer isn't set
        return new Text();
    }
}
