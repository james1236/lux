import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.io.*;

public class World
{
    private Color backgroundColor = Color.WHITE;
    private Color colorA;
    private Color colorB;
    private int highscore = 0;
    private String name;
    private static int width = 500;
    private static int height = 500;
    private ArrayList<Tile> tiles = new ArrayList<Tile>();
    private boolean zen = false;

    //Constructors
    public World() {}
    
    public World(Gamemode gamemode) {
        setName(gamemode.getName());
        setInterpolationColors(gamemode.getColorA(),gamemode.getColorB());
        setBackgroundColor(gamemode.getBackgroundColor());
        setZen(gamemode.getZen());
    }

    //Setters
    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setInterpolationColors(Color colorA, Color colorB) {
        this.colorA = colorA;
        this.colorB = colorB;
    }

    public void setName(String name) {
        this.name = name;
    }

    //Zen mode? (If timers exist)
    public void setZen(boolean zen) {
        this.zen = zen;
    }

    //Getters
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    public int getHighscore() {
        return highscore;
    }

    public String toString() {
        return name;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }

    //Methods
    
    //Manually adds tile
    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public void addNewTile() {
        //If interpolation settings are off, create a tile with a random color
        if (colorA == null) {
            tiles.add(new Tile());
        } else {
            tiles.add(new Tile(colorA,colorB));
        }

        //If the brightness of the tile is too similar to the previous tile, retry
        if (tiles.size() > 1) {
            scoreTiles();
            if (Math.abs(tiles.get(tiles.size()-1).getScore() - tiles.get(tiles.size()-2).getScore()) < 0.1) {
                tiles.remove(tiles.size()-1);
                addNewTile();
            }
        }

        //If highscore is beaten
        if (tiles.size() > highscore) {
            highscore = tiles.size();
            saveHighscore();
        }
    }

    public void addNewTile(double timer) {
        //If zen mode is active, just use the regular tile adder
        if (zen) {
            addNewTile();
            return;
        }
        
        //If interpolation settings are off, create a tile with a random color
        if (colorA == null) {
            tiles.add(new Tile(timer));
        } else {
            tiles.add(new Tile(timer,colorA,colorB));
        }

        //If the brightness of the tile is too similar to the previous tile, retry
        if (tiles.size() > 1) {
            scoreTiles();
            if (Math.abs(tiles.get(tiles.size()-1).getScore() - tiles.get(tiles.size()-2).getScore()) < 0.1) {
                tiles.remove(tiles.size()-1);
                addNewTile(timer);
            }
        }

        //If highscore is beaten
        if (tiles.size() > highscore) {
            highscore = tiles.size();
            saveHighscore();
        }
    }

    //Give every tile a score
    public void scoreTiles() {
        for (Tile tile: tiles) {
            tile.setScore(tile.getColor().getBrightness());
        }
    }

    private void loadHighscore() {
        try {
            FileReader fileReader = new FileReader(name+"_data.dat");
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            highscore = Integer.parseInt(bufferedReader.readLine());
        } catch (IOException error) {
            //If highscore dosen't exist, set it to 0 then save it
            highscore = 0;
            saveHighscore();
        }
    }

    private void saveHighscore() {
        try {
            File file = new File(name+"_data.dat");
            file.createNewFile();

            FileWriter fileWriter = new FileWriter(name+"_data.dat");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(""+highscore);
            bufferedWriter.close();
        } catch(IOException error) {}
    }
}
