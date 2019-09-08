import javafx.scene.paint.Color;

public enum Gamemode
{
    //Enums
    FADED("Faded",Color.BLACK,Color.WHITE),

    CLASSIC("Classic"),

    ZENCLASSIC("Zen Classic",true);

    //Paramaters
    private String name;
    private Color backgroundColor;
    private Color colorA;
    private Color colorB;
    private boolean zen = false;

    //Constructors
    Gamemode(String name, Color backgroundColor, Color colorA, Color colorB, boolean zen) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.colorA = colorA;
        this.colorB = colorB;
        this.zen = zen;
    }

    Gamemode(String name, Color backgroundColor, Color colorA, Color colorB) {
        this.name = name;
        this.backgroundColor = backgroundColor;
        this.colorA = colorA;
        this.colorB = colorB;
    }

    Gamemode(String name, Color colorA, Color colorB, boolean zen) {
        this.name = name;
        this.colorA = colorA;
        this.colorB = colorB;
        this.zen = zen;
    }

    Gamemode(String name, Color colorA, Color colorB) {
        this.name = name;
        this.colorA = colorA;
        this.colorB = colorB;
    }

    Gamemode(String name, boolean zen) {
        this.name = name;
        this.zen = zen;
    }

    Gamemode(String name) {
        this.name = name;
    }

    //Getters
    String getName() {
        return this.name;
    }

    Color getBackgroundColor() {
        return this.backgroundColor;
    }

    Color getColorA() {
        return this.colorA;
    }

    Color getColorB() {
        return this.colorB;
    }

    boolean getZen() {
        return this.zen;
    }
}
