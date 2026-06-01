/**
 * TO DO:
 * - Make an office
 * - Successfully load an image
 * - make the actual playable game???
 *
 * @author YoloMoloCuber
 */

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.image.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class OSCN extends Application{
  public static Group menu = new Group();
  public static final Color DEFAULT_COLOR = Color.BLACK;
  public static final double DEFAULT_WIDTH = 1.0;

  // initializes the threats at the start for easier processing later.
  Brown brown = new Brown(0, 0);
  Blue blue = new Blue(0, 0);
  Yellow yellow = new Yellow(0, 0);
  Red red = new Red(0, 0);

  Threat[] threats = {brown, blue, yellow, red};

  // Text that shows the AI values of the threats. (Part 1)
  Text aiValue = text(getAIValues(), 1300, 325, menu); // I had to move this out here for some reason or it wouldn't work.

  // Important variable for selecting threats and altering them later
  static int selectedIndex = -1;

  public void start(Stage stage) throws FileNotFoundException {
      // Creates the custom colors used for the threats.
      Color customBrown = Color.web("6e4d10");
      Color customBlue = Color.web("3335ab");
      Color customYellow = Color.web("e6eb6e");
      Color customRed = Color.web("962626");

      // Draws the icons for the threats. Placeholders for now
      Rectangle brownIcon = drawRect(50, 50, 100, 100, customBrown, menu);
      Rectangle blueIcon = drawRect(200, 50, 100, 100, customBlue, menu);
      Rectangle yellowIcon = drawRect(350, 50, 100, 100, customYellow, menu);
      Rectangle redIcon = drawRect(500, 50, 100, 100, customRed, menu);

      // Loads the map to be used later
      ImageView map = drawImage("../../../assets/officeMap.png", 500, 500, 320, 530, menu);

      // The text that is to show for character descriptions.
      Text desc = text("Hover over the icons above for character mechanics!", 50, 300, menu);
      desc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));

      // Text that shows the AI values of the threats. (Part 2)
      aiValue.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));

      // Buttons that adjust the AI values of different threats.
      Button increm = drawButton("+", 1300, 100, 125, 50, 30, menu);
      Button decrem = drawButton("-", 1425, 100, 125, 50, 30, menu);
      Button setAllMin = drawButton("Set All 0", 1300, 50, 250, 50, 30, menu);
      Button setAllMax = drawButton("Set All 20", 1300, 150, 250, 50, 30, menu);
      Button dxMode = drawButton("Toggle DX", 1300, 200, 250, 50, 30, menu);
      Button dxModeAll = drawButton("Toggle All DX", 1300, 250, 250, 50, 30, menu);

      // Loads the screen
      stage.setTitle("Oversimplified Custom Night");
      Scene scene = new Scene(menu, 1600, 900);
      scene.setFill(Color.ALICEBLUE);
      stage.setScene(scene);
      stage.show();

      // Grabs the descriptions for the threats to describe how to deal with threats, also sets the selection index.
      brownIcon.setOnMouseEntered(e -> {
        desc.setText(brown.getDescription() + "\n\nDX Mechanic: " + brown.getDX_Description());
        setIndex(brown.getIndex());
      });
      blueIcon.setOnMouseEntered(e -> {
        desc.setText(blue.getDescription() + "\n\nDX Mechanic: " + blue.getDX_Description());
        setIndex(blue.getIndex());
      });
      yellowIcon.setOnMouseEntered(e -> {
        desc.setText(yellow.getDescription() + "\n\nDX Mechanic: " + yellow.getDX_Description());
        setIndex(yellow.getIndex());
      });
      redIcon.setOnMouseEntered(e -> {
        desc.setText(red.getDescription() + "\n\nDX Mechanic: " + red.getDX_Description());
        setIndex(red.getIndex());
      });

      // Code for the buttons to alter the AI values of the threats from the main menu
      increm.setOnMouseClicked(e -> {
        if (selectedIndex != -1) {
          threats[selectedIndex].incrementDifficulty();
          updateAIValues();
        }
      });
      decrem.setOnMouseClicked(e -> {
        if (selectedIndex != -1) {
          threats[selectedIndex].decrementDifficulty();
          updateAIValues();
        }
      });
      setAllMin.setOnMouseClicked(e -> {
        for (Threat t : threats) {
          t.setDifficulty(0);
        }
        updateAIValues();
      });
      setAllMax.setOnMouseClicked(e -> {
        for (Threat t : threats) {
          t.setDifficulty(20);
        }
        updateAIValues();
      });
      dxMode.setOnMouseClicked(e -> {
        if (selectedIndex != -1) {
          threats[selectedIndex].toggleDX();
          updateAIValues();
        }
      });
      dxModeAll.setOnMouseClicked(e -> {
        for (Threat t : threats) {
          t.toggleDX();
        }
        updateAIValues();
      });
  }

  public static Rectangle drawRect(int x, int y, int w, int h, Group group){
      return drawRect(x,y,w,h,DEFAULT_COLOR,group);
  }
  public static Rectangle drawRect(int x, int y, int w, int h, Color c, Group group){
      Rectangle rect = new Rectangle(x, y, w, h);
      rect.setFill(c);
      rect.setStroke(DEFAULT_COLOR);
      rect.setStrokeWidth(DEFAULT_WIDTH);
      group.getChildren().add(rect);
      return rect;
  }

  public static Circle drawCircle(int x, int y, int r, Group group){
      return drawCircle(x, y, r, DEFAULT_COLOR, group);
  }
  public static Circle drawCircle(int x, int y, int r, Color c, Group group){
      Circle circle = new Circle(x, y, r);
      circle.setFill(c);
      circle.setStroke(DEFAULT_COLOR);
      circle.setStrokeWidth(DEFAULT_WIDTH);
      group.getChildren().add(circle);
      return circle;
  }

  public static Text text(String str, int x, int y, Group group){
        Text text = new Text(str);
        text.setX(x);
        text.setY(y);
        text.setFocusTraversable(true);
        group.getChildren().add(text);
        return text;
  }

  public static Button drawButton(String str, int x, int y, int w, int h, int t, Group group) {
      // parameters: Button text, x position, y position, width, height, font size, group to include in.
      if (w < 0 || h < 0 || t < 0) {
        throw new IllegalArgumentException("Dimensions and font size cannot be negative!");
      }

      Button button = new Button(str);
      button.setPrefWidth(w);
      button.setPrefHeight(h);
      button.relocate(x, y);
      group.getChildren().add(button);
      button.setFont(Font.font(t));
      return button;
  }

  public void setIndex(int index) {
      selectedIndex = index;
  }

  public ImageView drawImage(String imgDir, int x, int y, int w, int h, Group group) throws FileNotFoundException {
    Image myImage = new Image(new FileInputStream(imgDir)); // This is what loads our image into the program
    ImageView imageOut = new ImageView(myImage);
    imageOut.setX(x); // We then set it's initial position, size, ratio preservation, and if it's traversable, and alt text.
    imageOut.setY(y);
    imageOut.setFitWidth(w);
    imageOut.setFitHeight(h);
    imageOut.setPreserveRatio(true);
    imageOut.setFocusTraversable(true);
    group.getChildren().add(imageOut);
    return imageOut;
  }

  private String getAIValues() { // Goes through every threat and retrieves their name and AI level to print as text.
    String output = "Difficulty Values";
    for (Threat t : threats) {
      output = output + "\n" + t.getName() + ": " + t.getDifficulty();
      if (t.dxIsActive()) {
        output = output + " (DX on)";
      }
    }
    return output;
  }
  private void updateAIValues() { // I literally only made this so I wouldn't have to type a long line 20 times.
    aiValue.setText(getAIValues());
  }
}

/**
 * Generic information for all the Threats:
 *
 * difficulty refers to their AI levels, used in determining when they will move
 * every movement opportinity.
 *
 * location is their current location on the map.
 *
 * path is the movement paths they will take, if at all.
 *
 * description is the description for their behavior that will show in the main
 * menu.
 */
class Threat{
  public final int THREAT_INDEX; // Index of the threat in the threat array.
  protected int difficulty; // AI level, how often the threat should move
  protected int failCount = 0; // counts the number of failed movements
  protected int location; // Current location on the map.
  protected int[][] path = new int[0][0]; // The pathing they should have. "Rows" correspond to the room that they're in, "columns" correspond to the rooms that they can move to.
  protected final String NAME; // Self explanatory
  protected final String DESCRIPTION; // DESCRIPTION that shows in the main menu.
  protected final String DX_DESCRIPTION; // Description of the DX mechanic that they have.
  protected boolean dxMode = false; // Just indicates if dxMode is on or off

  // Constructors
  public Threat(int d, int l, int i, String name, String desc, String dxDesc) {
      if (d < 0) {
        d = 0; // Sets the difficulty variable to 0 if the input is lower.
      } else if (d > 20) {
        d = 20; // Sets the difficulty variable to 20 if the input is higher.
      }
      if (i < 0) { // Checks for a valid index position. This is used to refer to the Threat array later.
        throw new IllegalArgumentException("Index cannot be negative!");
      }
      difficulty = d;
      location = l;
      NAME = name;
      THREAT_INDEX = i;
      DESCRIPTION = desc;
      DX_DESCRIPTION = dxDesc;
  }
  public Threat() {
      this (0, 0, 0, "unnamed", "Placeholder", "DX Placeholder");
  }

  // Accessor methods
  public int getDifficulty() { // Returns the AI levels of the threat
    return difficulty;
  }
  public String getName() { // Returns the description text.
    return NAME;
  }
  public String getDescription() { // Returns the description text.
    return DESCRIPTION;
  }
  public String getDX_Description() { // Returns the DX description text.
    return DX_DESCRIPTION;
  }
  public int getIndex() {
    return THREAT_INDEX;
  }
  public boolean dxIsActive() {
    return dxMode;
  }

  // Mutator methods
  public void setDifficulty(int diff) { // Sets the AI value to a random value
    setDifficulty(diff, false);
  }
  public void setDifficulty(int diff, boolean uncap) { // Sets the AI value to a random value. uncap tells the program to ignore the upper limit of 20.
    if (diff < 0) {
      diff = 0; // Sets the difficulty variable to 0 if the input is lower.
    } else if (diff > 20 && !uncap) {
      diff = 20; // Sets the difficulty variable to 20 if the input is higher and uncap is false
    }

    difficulty = diff;
  }
  public void incrementDifficulty() { // Increases the AI value by 1
    incrementDifficulty(false);
  }
  public void incrementDifficulty(boolean uncap) { // Increases the AI value by 1. uncap tells the program to ignore the upper limit of 20.
    if (difficulty < 20 || uncap) {
      difficulty++;
    } else {
      difficulty = 20;
    }
  }
  public void decrementDifficulty() { // Lowers the AI value by 1
    if (difficulty > 0) {
      difficulty--;
    }
  }
  public void setPath(int[][] path) {
    this.path = path;
  }
  public void toggleDX() {
    dxMode = !dxMode;
  }

  // Functional methods
  public boolean movementCheck(int maxNum) {
    return (int)(Math.floor(Math.random() * (maxNum + 1))) <= difficulty;
  }
  public boolean movementCheck() { // default of 20
    return movementCheck(20);
  }
}

class Brown extends Threat{ // Code for Brown/Freddy

  public Brown(int d, int l) {
      super(d, l, 0, "Brown", "Placeholder for Brown", "DX Placeholder for Brown");
  }
  public Brown() {
      this(0, 0);
  }
}

class Blue extends Threat{ // Code for Blue/Bonnie

  public Blue(int d, int l) {
      super(d, l, 1, "Blue", "Placeholder for Blue", "DX Placeholder for Blue");
  }
  public Blue() {
      this(0, 0);
  }
}

class Yellow extends Threat{ // Code for Yellow/Chica

  public Yellow(int d, int l) {
      super(d, l, 2, "Yellow", "Placeholder for Yellow", "DX Placeholder for Yellow");

  }
  public Yellow() {
      this(0, 0);
  }
}

class Red extends Threat{ // Code for Red/Foxy

  public Red(int d, int l) {
      super(d, l, 3, "Red", "Placeholder for Red", "DX Placeholder for Red");
  }
  public Red() {
      this(0, 0);
  }
}

// to build, you'll run
// mvn clean javafx:run
