import javafx.application.Application; 

import javafx.stage.Stage;
import javafx.stage.Screen;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Rectangle2D;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.FontPosture;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.*;
import javafx.scene.input.KeyEvent;

import javafx.animation.AnimationTimer;

public class NewGame extends Application {
    private static Pane pane;

    private int activeWorld = 0;
    private double worldSwitchTimer = 0.0;

    private double defaultWidth = 500;
    private double defaultHeight = 500;

    private boolean tutorialActive = true;

    private Text score = new Text(20,60,"");
    private Text name = new Text(20,30,"");
    private Text highscore = new Text(defaultWidth-82,23,"");

    private Rectangle background = new Rectangle();
    private Rectangle tutorial = new Rectangle(0,0);
    private ArrayList<World> worlds = new ArrayList<World>();

    private boolean dead = false;

    public static void main() {
        Application.launch();
    }

    @Override
    public void start(Stage primary) {
        //Creating Worlds
        worlds.add(new World(Gamemode.FADED));
        worlds.add(new World(Gamemode.CLASSIC));
        worlds.add(new World(Gamemode.ZENCLASSIC));

        //Setup
        primary.setWidth(defaultWidth);
        primary.setHeight(defaultHeight);
        primary.centerOnScreen();
        pane = new Pane();
        primary.setResizable(false);

        Scene scene = new Scene(pane, 200, 200);
        scene.setOnKeyPressed(event -> this.handle(event));
        primary.setTitle("Lux");
        primary.setScene(scene);
        primary.show();

        primary.setWidth(pane.getWidth());
        primary.setHeight(pane.getHeight());

        //Text Font Settings
        score.setFont(Font.font("Courier"
            ,FontWeight.BOLD
            ,FontPosture.ITALIC
            ,30));

        name.setFont(Font.font("Courier"
            ,FontWeight.BOLD
            ,FontPosture.ITALIC
            ,15));

        highscore.setFont(Font.font("Courier"
            ,FontWeight.BOLD
            ,FontPosture.ITALIC
            ,10));

        //Colored Background Rectangle
        background.setX(0);
        background.setY(0);
        background.setWidth(pane.getWidth());
        background.setHeight(pane.getHeight());

        //Textured Background Image
        ImageView backgroundImageView = new ImageView(new Image("bg.jpg"));
        backgroundImageView.setOpacity(0.1);
        backgroundImageView.setX(0);
        backgroundImageView.setY(0);

        //Arrow Box Rectangles
        Rectangle leftArrow = new Rectangle(0,(pane.getHeight()/2)-40,15,80);
        Rectangle rightArrow = new Rectangle(pane.getWidth()-20,(pane.getHeight()/2)-40,15,80);

        leftArrow.setStrokeWidth(2);
        leftArrow.setFill(Color.LIGHTGRAY);
        leftArrow.setStroke(Color.LIGHTGRAY.darker());
        rightArrow.setStrokeWidth(2);
        rightArrow.setFill(Color.LIGHTGRAY);
        rightArrow.setStroke(Color.LIGHTGRAY.darker());

        //Arrow Polygons
        Polygon leftArrowTriangle = new Polygon();
        leftArrowTriangle.getPoints().addAll(new Double[]{
                10.0,(double)(pane.getHeight()/2)-10,
                5.0,(double) (pane.getHeight()/2)-0,
                10.0,(double) (pane.getHeight()/2)+10 });
        leftArrowTriangle.setFill(Color.DARKGRAY);
        leftArrowTriangle.setStroke(Color.DARKGRAY.darker());

        Polygon rightArrowTriangle = new Polygon();
        rightArrowTriangle.getPoints().addAll(new Double[]{
                pane.getWidth()-15.0,(double)(pane.getHeight()/2)-10,
                pane.getWidth()-10.0,(double) (pane.getHeight()/2)-0,
                pane.getWidth()-15.0,(double) (pane.getHeight()/2)+10 });
        rightArrowTriangle.setFill(Color.DARKGRAY);
        rightArrowTriangle.setStroke(Color.DARKGRAY.darker());

        //Tutorial Screen Text
        Text tutorialText = new Text((pane.getWidth()/2)-170,(pane.getHeight()/2)-125,"If the new tile is lighter than the previous, press up.\n                    If it's darker, press down");
        tutorialText.setFont(Font.font("Courier"
            ,FontPosture.ITALIC
            ,15));
        
        //Tutorial Blocking White Rectangle
        tutorial.setWidth(pane.getWidth());
        tutorial.setHeight(pane.getHeight());
        tutorial.setFill(Color.WHITE);
        
        //Tutorial Down Key
        Tile tutorialKey = new Tile(9999999);
        tutorialKey.setX((pane.getWidth()/2)-105/2);
        tutorialKey.setY((pane.getHeight()/2));
        
        Reflection reflection = new Reflection();
        reflection.setFraction(0.3);

        tutorialKey.getRectangle().setEffect(reflection);
        
        //Tutorial Down Key Arrow
        Text tutorialArrow = tutorialKey.getText();
        tutorialArrow.setText("🡻");

        //Animation
        new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                //Tick

                //Clear render buffer
                pane.getChildren().clear();

                //Backgrounds
                background.setFill(worlds.get(activeWorld).getBackgroundColor());
                background.setOpacity(0.1);
                pane.getChildren().add(background);
                pane.getChildren().add(backgroundImageView);

                //Score Text
                int scoreNumber = worlds.get(activeWorld).getTiles().size()-2;
                if (scoreNumber < 0) {
                    scoreNumber = 0;
                }
                score.setText(""+scoreNumber);
                pane.getChildren().add(score);

                //World Name
                name.setText(worlds.get(activeWorld).toString());
                pane.getChildren().add(name);

                //Highscore
                highscore.setText("Highscore: "+(worlds.get(activeWorld).getHighscore()-2));
                pane.getChildren().add(highscore);

                //Create 2 new tiles on active blank world
                if (worlds.get(activeWorld).getTiles().size() < 2 && tutorial.getOpacity() <= 0) {
                    worlds.get(activeWorld).addNewTile();
                }

                //If the tile reverse death animation is finished, clear the world's tiles
                if (dead && worlds.get(activeWorld).getTiles().get(0).getRectangle().getY() < 0) {
                    dead = false;
                    worlds.get(activeWorld).getTiles().clear();
                }
                
                //Every tile in every world
                for (World world: worlds) {
                    for (Tile tile: world.getTiles()) {
                        //Reverse Death Animation
                        if (dead) {
                            if (7*world.getTiles().size() > 90) {
                                tile.getRectangle().setY(tile.getRectangle().getY()-90);
                            } else {
                                tile.getRectangle().setY(tile.getRectangle().getY()-(7*world.getTiles().size()));
                            }
                        }
                        //X Slide (Switch) Animation
                        if (worldSwitchTimer >= 0) {
                            tile.getRectangle().setX(((pane.getWidth()/2)-105/2)+(worlds.indexOf(world) - activeWorld)*(pane.getWidth()-((worldSwitchTimer/10)*pane.getWidth())));
                        } else {
                            tile.getRectangle().setX(((pane.getWidth()/2)-105/2)+(worlds.indexOf(world) - activeWorld)*(pane.getWidth()+((worldSwitchTimer/10)*pane.getWidth())));
                        }
                        //Render
                        pane.getChildren().add(tile.getRectangle());
                        pane.getChildren().add(tile.getText());
                    }

                    //If the world isn't active and a switch isn't happening then reset world's tiles
                    if (activeWorld != worlds.indexOf(world) && worldSwitchTimer == 0) {
                        if (world.getTiles().size() > 0) {
                            world.getTiles().clear();
                        }
                    }
                }
                
                //Every tile in the active world
                for (Tile tile: worlds.get(activeWorld).getTiles()) {
                    //Slide Tiles Down
                    if (tile.getY() < (worlds.get(activeWorld).getTiles().size()-worlds.get(activeWorld).getTiles().indexOf(tile)-1) * 100 && !dead) {
                        tile.setY(tile.getY()+10);
                    }
                    
                    //Decrease Tile's Timer
                    if (worlds.get(activeWorld).getTiles().indexOf(tile) == worlds.get(activeWorld).getTiles().size()-1) {
                        if (tile.getTimer() > 0) {
                            tile.setTimer(tile.getTimer()-1);
                        }
                    }

                    //Out of time death trigger
                    if (tile.getTimer() == 0) {
                        dead = true;
                        break;
                    }
                }
                
                //Normalizing switchTimer
                if (worldSwitchTimer > 0) {
                    worldSwitchTimer-=1;
                }
                if (worldSwitchTimer < 0) {
                    worldSwitchTimer+=1;
                }

                //Arrow Opacity
                if (activeWorld == 0) {
                    leftArrow.setOpacity(0.15);
                    leftArrowTriangle.setOpacity(0.15);
                } else {
                    leftArrow.setOpacity(0.8);
                    leftArrowTriangle.setOpacity(0.8);
                }
                if (worlds.size()-1 > activeWorld) {
                    rightArrow.setOpacity(0.8);
                    rightArrowTriangle.setOpacity(0.8);
                } else {
                    rightArrow.setOpacity(0.15);
                    rightArrowTriangle.setOpacity(0.15);
                }

                //Render
                pane.getChildren().add(leftArrow);
                pane.getChildren().add(rightArrow);
                pane.getChildren().add(leftArrowTriangle);
                pane.getChildren().add(rightArrowTriangle);

                //Tutorial Screen
                if (!tutorialActive) {
                    tutorial.setOpacity(tutorial.getOpacity()-0.02);
                    tutorialText.setOpacity(tutorialText.getOpacity()-0.02);
                    tutorialKey.getRectangle().setOpacity(tutorialKey.getRectangle().getOpacity()-0.02);
                    tutorialArrow.setOpacity(tutorialArrow.getOpacity()-0.02);
                }
                
                //Tutorial Key Press Animation
                if (tutorialActive) {
                    tutorialKey.setColor(Color.LIGHTGRAY);
                } else {
                    tutorialKey.setColor(Color.DARKGRAY);
                }
                
                //Render
                pane.getChildren().add(tutorial);
                pane.getChildren().add(tutorialText);
                pane.getChildren().add(tutorialKey.getRectangle());
                pane.getChildren().add(tutorialArrow);
            }
        }.start();
    }

    //Checks if a user's response is correct
    private void testCorrect(boolean up) {
        //Calculate scores for all the tiles in the current world
        worlds.get(activeWorld).scoreTiles();

        //If the top tile has a bigger score than the tile below it
        if (worlds.get(activeWorld).getTiles().get(worlds.get(activeWorld).getTiles().size()-1).getScore() > worlds.get(activeWorld).getTiles().get(worlds.get(activeWorld).getTiles().size()-2).getScore()) {
            if (up) {
                //Add new tile
                worlds.get(activeWorld).addNewTile(calculateTimer());
            } else {
                dead = true;
            }
        } else {
            if (up) {
                dead = true;
            } else {
                //Add new tile
                worlds.get(activeWorld).addNewTile(calculateTimer());
            }
        }
    }

    //Keypress
    private void handle(KeyEvent event) {
        if (event.getCode().getName().equals("Up")) {
            if (worlds.get(activeWorld).getTiles().size() > 1) {
                testCorrect(true);
            }
        }
        if (event.getCode().getName().equals("Down")) {
            //Tutorial End
            if (tutorialActive) {
                tutorialActive = false;
            }
            if (worlds.get(activeWorld).getTiles().size() > 1) {
                testCorrect(false);
            }
        }
        if (event.getCode().getName().equals("Left") && tutorial.getOpacity() <= 0) {
            //If a world exists to the left and the worlds aren't currently switching
            if (activeWorld >= 1 && worldSwitchTimer == 0) {
                activeWorld--;
                worldSwitchTimer = -10;
            }
        }
        if (event.getCode().getName().equals("Right")) {
            //If a world exists to the right and the worlds aren't currently switching
            if (activeWorld < worlds.size()-1 && worldSwitchTimer == 0 && tutorial.getOpacity() <= 0) {
                activeWorld++;
                worldSwitchTimer = 10;
            }
        }
    }

    //Calculates the timer for tiles (difficulty)
    private int calculateTimer() {
        //If the calculated timer is above the minimum timer allowed
        if (160-Math.floor(worlds.get(activeWorld).getTiles().size()*2) > 50) {
            return 160-(int)Math.floor(worlds.get(activeWorld).getTiles().size()*2);
        } else {
            return 50;
        }
    }
}
