/**
 * TO DO:
 * - Make an office
 * - Successfully load an image
 * - make the actual playable game???
 *
 * this code is a damn mess...
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
import javafx.scene.input.*;
import javafx.stage.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class OSCN extends Application{
  public static Group menu = new Group();
  public static Group office = new Group();
  public static Group cameras = new Group();
  public static final Color DEFAULT_COLOR = Color.BLACK;
  public static final double DEFAULT_WIDTH = 1.0;

  // initializes the threats at the start for easier processing later.
  Brown brown = new Brown(0, 0);
  Blue blue = new Blue(0, 0);
  Yellow yellow = new Yellow(0, 0);
  Red red = new Red(0, 0);

  Threat[] threats = {brown, blue, yellow, red};

  // Text that shows the AI values of the threats. (Part 1)
  Text aiValue = text(getAIValues(), 1300, 350, menu); // I had to move this out here for some reason or it wouldn't work.

  // Important variable for selecting threats and altering them later
  static int selectedIndex = -1;

  // Keeps track of the time of the night
  private static int nightHour = 0;

  // The time to show
  static Text currentTime = text("TIME", 20, 70, office);

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
      ImageView map = drawImage("src/assets/images/officeMap.png", 500, 500, 320, 530, cameras);

      // The text that is to show for character descriptions.
      Text desc = text("Hover over the icons above for character mechanics!", 50, 300, menu);
      desc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));

      // Formatting for text that was initialized outside start() that break the code below for some reason.
      aiValue.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));
      currentTime.setFont(Font.font("Courier New", FontWeight.NORMAL, 50));

      // Buttons that adjust the AI values of different threats.
      Button increm = drawButton("+", 1300, 100, 125, 50, 30, menu);
      Button decrem = drawButton("-", 1425, 100, 125, 50, 30, menu);
      Button setAllMin = drawButton("Set All 0", 1300, 50, 250, 50, 30, menu);
      Button setAllMax = drawButton("Set All 20", 1300, 150, 250, 50, 30, menu);
      Button dxMode = drawButton("Toggle DX", 1300, 200, 250, 50, 30, menu);
      Button dxModeAll = drawButton("Toggle All DX", 1300, 250, 250, 50, 30, menu);

      // Button to start the night
      Button startNight = drawButton("Begin Night", 1300, 800, 250, 50, 30, menu);

      // Purely just here to test if the transition between the main menu and office works
      if (false) {
        Text officeWorked = text("This should be the office screen!\nIt worked!", 50, 50, office);
        officeWorked.setFont(Font.font("Courier New", FontWeight.NORMAL, 100));
      }

      // Loads the screen
      stage.setTitle("Oversimplified Custom Night");
      Scene menuScene = new Scene(menu, 1600, 900);
      Scene officeScene = new Scene(office, 1600, 900);
      menuScene.setFill(Color.ALICEBLUE);
      stage.setScene(menuScene);
      stage.show();

      // Grabs the descriptions for the threats to describe how to deal with threats, also sets the selection index.
      brownIcon.setOnMouseEntered(e -> {
        desc.setText(brown.getDescription() + "\n\nDX Mechanic: " + brown.getDX_Description());
      });
      blueIcon.setOnMouseEntered(e -> {
        desc.setText(blue.getDescription() + "\n\nDX Mechanic: " + blue.getDX_Description());
      });
      yellowIcon.setOnMouseEntered(e -> {
        desc.setText(yellow.getDescription() + "\n\nDX Mechanic: " + yellow.getDX_Description());
      });
      redIcon.setOnMouseEntered(e -> {
        desc.setText(red.getDescription() + "\n\nDX Mechanic: " + red.getDX_Description());
      });

      // Selects the threat icons that are clicked on, sets selected to none if already selected.
      brownIcon.setOnMouseClicked(e -> {
        if (selectedIndex == brown.getIndex()) {
          setIndex(-1);
        } else {
          setIndex(brown.getIndex());
        }
        updateAIValues();
      });
      blueIcon.setOnMouseClicked(e -> {
        if (selectedIndex == blue.getIndex()) {
          setIndex(-1);
        } else {
          setIndex(blue.getIndex());
        }
        updateAIValues();
      });
      yellowIcon.setOnMouseClicked(e -> {
        if (selectedIndex == yellow.getIndex()) {
          setIndex(-1);
        } else {
          setIndex(yellow.getIndex());
        }
        updateAIValues();
      });
      redIcon.setOnMouseClicked(e -> {
        if (selectedIndex == red.getIndex()) {
          setIndex(-1);
        } else {
          setIndex(red.getIndex());
        }
        updateAIValues();
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

      // Code for leaving the main menu
      startNight.setOnMouseClicked(e -> {
        stage.setScene(officeScene);
        setTime(0);
        updateTime();

        NightTimer hour = new NightTimer(1000);
        hour.start();
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
    String output = "Click on the icons to\nedit their difficulties.\n\nSelected: ";
    if (selectedIndex == -1) {
      output = output + "None";
    } else {
      output = output + threats[selectedIndex].getThreatName();
    }
    output = output + "\n\nDifficulty Values";
    for (Threat t : threats) {
      output = output + "\n" + t.getThreatName() + ": " + t.getDifficulty();
      if (t.dxIsActive()) {
        output = output + " (DX on)";
      }
    }
    return output;
  }
  private void updateAIValues() { // I literally only made this so I wouldn't have to type a long line 20 times.
    aiValue.setText(getAIValues());
  }

  // time-related methods
  public static void updateTime() {
    currentTime.setText(printTime());
  }
  private static String printTime() { // This is so that I wouldn't have to type it repeatedly.
    if (nightHour < 0) {
      return (nightHour + 12) + " PM";
    } else if (nightHour == 0){
      return "12 AM";
    } else {
      return nightHour + " AM";
    }
  }
  public static int getTime() {
    return nightHour;
  }
  public static void progressTime() { // progresses time by 1 hour. Can be regressed with negative numbers
    progressTime(1);
  }
  public static void progressTime(int hours) { // progresses time by the specified number of hours. Can be regressed with negative numbers
    nightHour += hours;
  }
  public static void setTime(int time) { // sets the time to a specific hour after 12 AM.
    nightHour = time;
  }
}
// to build, you'll run
// mvn clean javafx:run
