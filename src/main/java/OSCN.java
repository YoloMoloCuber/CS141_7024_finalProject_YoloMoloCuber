/**
 * TO DO:
 * - Make an office
 * - Make the cameras work and be functional
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
import javafx.event.*;
import java.util.*;
import java.lang.*;
import java.io.*;

public class OSCN extends Application{
  public static Group menu = new Group();
  public static Group office = new Group();
  public static Group cameras = new Group();
  public static final Color DEFAULT_COLOR = Color.BLACK;
  public static final double DEFAULT_WIDTH = 1.0;

  // Creates the custom colors used for the threats.
  public static Color customBrown = Color.web("6e4d10");
  public static Color customBlue = Color.web("3335ab");
  public static Color customYellow = Color.web("e6eb6e");
  public static Color customCupcake = Color.web("f9abff");
  public static Color customRed = Color.web("962626");

  // initializes the threats at the start for easier processing later.
  Brown brown = new Brown(0, 0);
  Blue blue = new Blue(0, 0);
  Yellow yellow = new Yellow(0, 0);
  Red red = new Red(0, 0);

  Threat[] threats = {brown, blue, yellow, red};

  // Generates the renderings of the threats that should be shown on the cameras
  static Circle cupcake = drawCircle(-5, -5, 0, customCupcake, cameras);

  // Text that shows the AI values of the threats. (Part 1)
  Text aiValue = text(getAIValues(), 1300, 350, menu); // I had to move this out here for some reason or it wouldn't work.

  // Important variable for selecting threats and altering them later
  static int selectedIndex = -1;

  // Keeps track of important night-related information
  private static int nightHour = 0;
  private static int power = 10000;
  private static volatile boolean activeNight = false;

  // The time to show
  static Text currentTime = text("TIME", 20, 70, cameras);

  // The time to show
  static Text remainingPower = text("POWER", 20, 880, cameras);

  // The camera that is actively being looked at & the camera names
  private static int currentCamera = 0; // refers to the index of the camera
  private static final String[] CAMERA_NAMES = {
    "Loop - North Section", // Camera # 1
    "Loop - Northeast Section", // Camera # 2
    "Loop - East Section", // Camera # 3
    "Loop - Southeast Section", // Camera # 4
    "Loop - South Section", // Camera # 5
    "Loop - Southwest Section", // Camera # 6
    "Loop - West Section", // Camera # 7
    "Loop - Northwest Section", // Camera # 8
    "West Hallway", // Camera # 9
    "East Hallway" // Camera # 10
  };

  // Stage and menuScene on the outside so that I can access it in outside methods
  public static Stage stage;
  private static Scene menuScene;

  // Creates the threads beforehand
  static NightTimer hour = new NightTimer(10000);
  static PowerDrain generator = new PowerDrain();

  Thread[] threadsList = new Thread[threats.length + 2];

  public void start(Stage primaryStage) throws FileNotFoundException {
      // sets the stage to primaryStage.
      this.stage = primaryStage;
      stage.addEventFilter(NightEvent.NIGHT_END, this::returnToMenu);

      // sets listeners for events
      //cupcake.addEventFilter(ThreatEvent.CUPCAKE_ANY, Yellow::cupcakeCheck);

      // adds the threads to the list of threads for later control.
      for (int i = 0; i < threats.length; i++) {
        threadsList[i] = threats[i];
      }
      threadsList[threadsList.length - 2] = hour;
      threadsList[threadsList.length - 1] = generator;

      // Draws the icons for the threats. Placeholders for now
      Rectangle brownIcon = drawRect(50, 50, 100, 100, customBrown, menu);
      Rectangle blueIcon = drawRect(200, 50, 100, 100, customBlue, menu);
      Rectangle yellowIcon = drawRect(350, 50, 100, 100, customYellow, menu);
      Rectangle redIcon = drawRect(500, 50, 100, 100, customRed, menu);

      // Loads the map to be used later
      ImageView map = drawImage("src/assets/images/officeMap.png", 1415, 610, 160, 265, cameras);

      // The text that is to show for character descriptions.
      Text desc = text("Hover over the icons above for character mechanics!", 50, 300, menu);
      desc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));

      // Formatting for text that was initialized outside start() that break the code below for some reason.
      aiValue.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));
      currentTime.setFont(Font.font("Courier New", FontWeight.NORMAL, 50));
      remainingPower.setFont(Font.font("Courier New", FontWeight.NORMAL, 30));

      // Buttons that adjust the AI values of different threats.
      Button increm = drawButton("+", 1300, 100, 125, 50, 30, menu);
      Button decrem = drawButton("-", 1425, 100, 125, 50, 30, menu);
      Button setAllMin = drawButton("Set All 0", 1300, 50, 250, 50, 30, menu);
      Button setAllMax = drawButton("Set All 20", 1300, 150, 250, 50, 30, menu);
      Button dxMode = drawButton("Toggle DX", 1300, 200, 250, 50, 30, menu);
      Button dxModeAll = drawButton("Toggle All DX", 1300, 250, 250, 50, 30, menu);

      // Camera Buttons
      CameraButton camOneButton = drawCamButton("1", 1475, 618, 40, 40, 15, cameras, 0);
      CameraButton camTwoButton = drawCamButton("2", 1528, 618, 40, 40, 15, cameras, 1);
      CameraButton camThrButton = drawCamButton("3", 1528, 671, 40, 40, 15, cameras, 2);
      CameraButton camFouButton = drawCamButton("4", 1528, 724, 40, 40, 15, cameras, 3);
      CameraButton camFivButton = drawCamButton("5", 1475, 724, 40, 40, 15, cameras, 4);
      CameraButton camSixButton = drawCamButton("6", 1422, 724, 40, 40, 15, cameras, 5);
      CameraButton camSevButton = drawCamButton("7", 1422, 671, 40, 40, 15, cameras, 6);
      CameraButton camEigButton = drawCamButton("8", 1422, 618, 40, 40, 15, cameras, 7);
      CameraButton camNinButton = drawCamButton("9", 1422, 777, 40, 40, 15, cameras, 8);
      CameraButton camTenButton = drawCamButton("10", 1528, 777, 40, 40, 15, cameras, 9);

      // Button to start the night
      Button startNight = drawButton("Begin Night", 1300, 800, 250, 50, 30, menu);

      // Buttons that toggle the cameras
      Button cameraUp = drawButton("^ CAMERAS ^", 300, 850, 1000, 50, 30, office);
      Button cameraDown = drawButton("v CAMERAS v", 300, 850, 1000, 50, 30, cameras);

      // Purely just here to test if the transition between the main menu and office works
      if (false) {
        Text officeWorked = text("This should be the office screen!\nIt worked!", 50, 50, office);
        officeWorked.setFont(Font.font("Courier New", FontWeight.NORMAL, 100));
      }

      // Loads the screen
      stage.setTitle("Oversimplified Custom Night");
      menuScene = new Scene(menu, 1600, 900);
      Scene officeScene = new Scene(office, 1600, 900);
      Scene cameraScene = new Scene(cameras, 1600, 900);
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
        startNight();
        setTime(0);
        updateTime();
        updatePower();

        // starts the Threads
        hour.start();
        generator.start();
        for (Threat t : threats) {
          t.reset();
          t.start();
        }
      });

      // Office controls
      cameraUp.setOnMouseClicked(e -> {
        stage.setScene(cameraScene);
      });
      cameraDown.setOnMouseClicked(e -> {
        stage.setScene(officeScene);
      });

      camOneButton.setOnMouseClicked(e -> {
        setCamera(camOneButton.getIndex());
        IO.println("Camera 1 clicked");
      });
      camTwoButton.setOnMouseClicked(e -> {
        setCamera(camTwoButton.getIndex());
        IO.println("Camera 2 clicked");
      });
      camThrButton.setOnMouseClicked(e -> {
        setCamera(camThrButton.getIndex());
        IO.println("Camera 3 clicked");
      });
      camFouButton.setOnMouseClicked(e -> {
        setCamera(camFouButton.getIndex());
        IO.println("Camera 4 clicked");
      });
      camFivButton.setOnMouseClicked(e -> {
        setCamera(camFivButton.getIndex());
        IO.println("Camera 5 clicked");
      });
      camSixButton.setOnMouseClicked(e -> {
        setCamera(camSixButton.getIndex());
        IO.println("Camera 6 clicked");
      });
      camSevButton.setOnMouseClicked(e -> {
        setCamera(camSevButton.getIndex());
        IO.println("Camera 7 clicked");
      });
      camEigButton.setOnMouseClicked(e -> {
        setCamera(camEigButton.getIndex());
        IO.println("Camera 8 clicked");
      });
      camNinButton.setOnMouseClicked(e -> {
        setCamera(camNinButton.getIndex());
        IO.println("Camera 9 clicked");
      });
      camTenButton.setOnMouseClicked(e -> {
        setCamera(camTenButton.getIndex());
        IO.println("Camera 10 clicked");
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
  public static CameraButton drawCamButton(String str, int x, int y, int w, int h, int t, Group group, int i) {
      // parameters: Button text, x position, y position, width, height, font size, group to include in, index
      if (w < 0 || h < 0 || t < 0) {
        throw new IllegalArgumentException("Dimensions and font size cannot be negative!");
      }

      CameraButton button = new CameraButton(str);
      button.setMinWidth(w);
      button.setMinHeight(h);
      button.setPrefWidth(w);
      button.setPrefHeight(h);
      button.setMaxWidth(w);
      button.setMaxHeight(h);
      button.relocate(x, y);
      group.getChildren().add(button);
      button.setFont(Font.font(t));
      button.setIndex(i);
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

  // power-related methods
  public static void updatePower() {
    remainingPower.setText(printPower() + "%");
  }
  private static String printPower() {
    return ((int) Math.floor((double) power / 100.0)) + "";
  }
  public static int getPower() {
    return power;
  }
  public static void changePower() { // decreases power by 0.01%
    changePower(-1);
  }
  public static void changePower(int num) { // changes power by an inputted amount. Positive numbers increase power, negative numbers decrease it.
    power += num;
  }
  public static void setPower(int num) { // sets power to a certain percentage. 100 corresponds to 1% power.
    power = num;
  }

  // camera-related methods
  public static int getCurrentCamera() {
    return currentCamera;
  }
  private static String getCurrentCameraName() {
    return CAMERA_NAMES[currentCamera];
  }
  public static String cameraToString() {
    return "CAM " + (currentCamera + 1) + ": " + getCurrentCameraName();
  }
  public static void setCamera(int cameraIndex) {
    currentCamera = cameraIndex;
  }
  public static void nextCamera() {
    if (currentCamera < 9) {
      currentCamera++;
    } else {
      setCamera(0);
    }
  }
  public static void prevCamera() {
    if (currentCamera > 0) {
      currentCamera--;
    } else {
      setCamera(9);
    }
  }

  // methods to start and stop the night
  public static void startNight() { // sets activeNight to true
    activeNight = true;
  }
  public static void stopNight() { // sets activeNight to false
    activeNight = false;
  }
  public static boolean isNightActive() {
    return activeNight;
  }

  // methods to handle events
  private void returnToMenu(NightEvent event) {
    stopNight();
    for (Thread t : threadsList) {
      synchronized (t) {
        t.notify();
      }
    }
    Platform.runLater(() -> {
      stage.setScene(menuScene);
      stage.show();
    });
  }
  public static Stage getStage() {
    return stage;
  }
}
// to build, you'll run
// mvn clean javafx:run
