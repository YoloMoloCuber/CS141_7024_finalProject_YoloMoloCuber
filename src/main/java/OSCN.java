/**
 * TO DO:
 * - literally everything
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

public class OSCN extends Application{
  public static Group menu = new Group();
  public static final Color DEFAULT_COLOR = Color.BLACK;
  public static final double DEFAULT_WIDTH = 1.0;

  // initializes the threats at the start for easier processing later.
  Brown brown = new Brown(4, 0);
  Blue blue = new Blue(6, 2);
  Yellow yellow = new Yellow(1, 5);
  Red red = new Red(8, 2);

  Threat[] threats = {brown, blue, yellow, red};

  public void start(Stage stage){
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

      // Important variable for selecting threats and altering them later
      int selectedIndex = -1;

      // The text that is to show for character descriptions.
      Text desc = text("Hover over the icons above for character mechanics!", 50, 200, menu);
      desc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));

      stage.setTitle("Oversimplified Custom Night");
      Scene scene = new Scene(menu, 1600, 900);
      scene.setFill(Color.ALICEBLUE);
      stage.setScene(scene);
      stage.show();

      brownIcon.setOnMouseEntered(e -> {
        desc.setText(brown.getDescription() + "\n\nCurrent AI Level: " + brown.getDifficulty());

      });
      blueIcon.setOnMouseEntered(e -> {
        desc.setText(blue.getDescription() + "\n\nCurrent AI Level: " + blue.getDifficulty());
      });
      yellowIcon.setOnMouseEntered(e -> {
        desc.setText(yellow.getDescription() + "\n\nCurrent AI Level: " + yellow.getDifficulty());
      });
      redIcon.setOnMouseEntered(e -> {
        desc.setText(red.getDescription() + "\n\nCurrent AI Level: " + red.getDifficulty());
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
  public final int THREAT_INDEX;
  protected int difficulty;
  protected int location;
  protected int[][] path = new int[0][0];
  protected String description;

  public Threat(int d, int l, int i) {
      if (d < 0) {
        d = 0; // Sets the difficulty variable to 0 if the input is lower.
      } else if (d > 20) {
        d = 20; // Sets the difficulty variable to 20 if the input is higher.
      }
      if (i < 0) {
        throw new IllegalArgumentException("Index cannot be negative!");
      }
      difficulty = d;
      location = l;
      THREAT_INDEX = i;
  }
  public Threat() {
      this (0, 0, 0);
  }

  public int getDifficulty() { // Returns the AI levels of the threat
    return difficulty;
  }
  public String getDescription() { // Returns the description text.
    return description;
  }
}

class Brown extends Threat{ // Code for Brown/Freddy

  public Brown(int d, int l) {
      super(d, l, 0);
      description = "Placeholder for Brown";
  }
  public Brown() {
      this(0, 0);
  }

  public String getDescription() { // Returns the description text.
    return description;
  }
}

class Blue extends Threat{ // Code for Blue/Bonnie

  public Blue(int d, int l) {
      super(d, l, 1);
      description = "Placeholder for Blue";
  }
  public Blue() {
      this(0, 0);
  }

  public String getDescription() { // Returns the description text.
    return description;
  }
}

class Yellow extends Threat{ // Code for Yellow/Chica

  public Yellow(int d, int l) {
      super(d, l, 2);
      description = "Placeholder for Yellow";

  }
  public Yellow() {
      this(0, 0);
  }

  public String getDescription() { // Returns the description text.
    return description;
  }
}

class Red extends Threat{ // Code for Red/Foxy

  public Red(int d, int l) {
      super(d, l, 3);
      description = "Placeholder for Red";
  }
  public Red() {
      this(0, 0);
  }

  public String getDescription() { // Returns the description text.
    return description;
  }
}

// to build, you'll run
// mvn clean javafx:run
