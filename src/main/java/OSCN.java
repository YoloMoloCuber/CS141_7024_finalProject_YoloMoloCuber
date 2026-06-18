/**
 * TO DO:
 * - add instructions for gameplay
 *
 * @author YoloMoloCuber
 */

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.media.*;
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
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.lang.*;
import java.io.*;

public class OSCN extends Application{
  static public Group menu = new Group();
  static public Group office = new Group();
  static public Group cameras = new Group();
  private Scene loadScene;
  static public final Color DEFAULT_COLOR = Color.BLACK;
  static public final double DEFAULT_WIDTH = 1.0;
  static private boolean powerActive = true;
  static private boolean isCharging = false;

  // Creates the custom colors used for the threats.
  static public final Color CUSTOM_BROWN = Color.web("6e4d10");
  static public final Color CUSTOM_BLUE = Color.web("3335ab");
  static public final Color CUSTOM_YELLOW = Color.web("e6eb6e");
  static public final Color CUSTOM_CUPCAKE = Color.web("f9abff");
  static public final Color CUSTOM_RED = Color.web("962626");

  // initializes the threats at the start for easier processing later.
  private Brown brown = new Brown(0, 0);
  private Blue blue = new Blue(0, 0);
  private Yellow yellow = new Yellow(0, 0);
  private Red red = new Red(0, 0);

  Threat[] threats = {brown, blue, yellow, red};

  // Creates the sound effects
  MediaPlayer cupcakeSpawnSound = createSFX("src/assets/audio/chickenCall.wav");
  MediaPlayer cupcakeLeaveSound = createSFX("src/assets/audio/chickenCall.wav");
  static MediaPlayer doorSlam = createSFX("src/assets/audio/doorSlam.wav");
  static MediaPlayer stepsAdvance = createSFX("src/assets/audio/red_advance.wav");
  static MediaPlayer stepsRetreat = createSFX("src/assets/audio/red_retreat.wav");
  static MediaPlayer beepSound = createSFX("src/assets/audio/blue_beep.wav");
  static MediaPlayer brownStalking = createSFX("src/assets/audio/brown_mumble.wav");
  static MediaPlayer honkSound = createSFX("src/assets/audio/honk.wav");

  // Keypad Buttons
  KeypadButton padZerButton = drawPadButton("0", 780, 530, 40, 40, 15, office, 0);
  KeypadButton padOneButton = drawPadButton("1", 730, 480, 40, 40, 15, office, 1);
  KeypadButton padTwoButton = drawPadButton("2", 780, 480, 40, 40, 15, office, 2);
  KeypadButton padThrButton = drawPadButton("3", 830, 480, 40, 40, 15, office, 3);
  KeypadButton padFouButton = drawPadButton("4", 730, 430, 40, 40, 15, office, 4);
  KeypadButton padFivButton = drawPadButton("5", 780, 430, 40, 40, 15, office, 5);
  KeypadButton padSixButton = drawPadButton("6", 830, 430, 40, 40, 15, office, 6);
  KeypadButton padSevButton = drawPadButton("7", 730, 380, 40, 40, 15, office, 7);
  KeypadButton padEigButton = drawPadButton("8", 780, 380, 40, 40, 15, office, 8);
  KeypadButton padNinButton = drawPadButton("9", 830, 380, 40, 40, 15, office, 9);

  // Generates the renderings of the threats that should be shown on the cameras/in the office
  static Circle cupcake = drawCircle(-5, -5, 0, CUSTOM_CUPCAKE, cameras);
  Rectangle brownThreat = drawRect(-5, -5, 0, 0, CUSTOM_BROWN, office);
  Rectangle blueThreat = drawRect(-5, -5, 0, 0, CUSTOM_BLUE, cameras);
  Text blueSequence = text("", 900, 275, cameras);
  Rectangle yellowThreat = drawRect(-5, -5, 0, 0, CUSTOM_YELLOW, cameras);
  Rectangle redThreat = drawRect(-5, -5, 0, 0, CUSTOM_RED, cameras);

  // button :D
  static Button refillGenerator = drawButton("Recharge Power", 1415, 500, 160, 80, 20, cameras);

  // Renders the doors in the office plus the two buttons next to the doors, also declares door-related variables.
  static Rectangle leftDoor = drawRect(0, 100, 300, 700, Color.BLACK, office);
  static Rectangle rightDoor = drawRect(1300, 100, 300, 700, Color.BLACK, office);
  static Button leftButton = drawButton("", 350, 425, 50, 50, 0, office);
  static Button rightButton = drawButton("", 1200, 425, 50, 50, 0, office);
  static private boolean leftClosed = false;
  static private boolean rightClosed = false;

  // Checks if the cameras are up or down.
  static private boolean camerasUp = false;

  // Text that shows the AI values of the threats. (Part 1)
  Text aiValue = text(getAIValues(), 1300, 350, menu); // I had to move this out here for some reason or it wouldn't work.

  // Important variable for selecting threats and altering them later
  int selectedIndex = -1;

  // Keeps track of important night-related information
  static private int nightHour = 0;
  static private int power = 10000;
  static private volatile boolean activeNight = false;

  // The time to show
  static Text currentTime = text("TIME", 20, 70, cameras);

  // The time to show
  static Text remainingPower = text("POWER", 20, 880, cameras);

  // death cause
  Text deathTip = text("", 20, 800, menu);

  // The camera that is actively being looked at & the camera names
  static private int currentCamera = 0; // refers to the index of the camera
  static private final String[] CAMERA_NAMES = {
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
  static Text cameraName = text(CAMERA_NAMES[currentCamera], 20, 120, cameras);

  // Stage and menuScene on the outside so that I can access it in outside methods
  static public Stage stage;
  private Scene menuScene;
  private Scene officeScene;
  private Scene cameraScene;

  // keypad-related variables
  static private int buttonCall = -1;

  static public int getButtonPressed() {
    return buttonCall;
  }

  // Creates the threads beforehand
  NightTimer hour = new NightTimer();
  PowerDrain generator = new PowerDrain();

  int totalTasks = threats.length + 2;

  // Creates an ExcexutorService item for later use
  ExecutorService executor = Executors.newFixedThreadPool(totalTasks);

  public void start(Stage primaryStage) throws FileNotFoundException {
      // sets the stage to primaryStage.
      this.stage = primaryStage;

      // sets listeners for events
      stage.addEventFilter(NightEvent.NIGHT_END, this::returnToMenu);
      stage.addEventFilter(NightEvent.NIGHT_CAMERAS, this::refreshCameras);
      stage.addEventFilter(ThreatEvent.CUPCAKE_SPAWN, this::cupcakeSpawnCue);
      stage.addEventFilter(ThreatEvent.CUPCAKE_LEAVE, this::cupcakeLeaveCue);
      stage.addEventFilter(ThreatEvent.DEATH, this::getDeathDetails);
      stage.addEventFilter(ThreatEvent.RED_MOVEMENT, this::redMoveCue);
      stage.addEventFilter(ThreatEvent.BLUE_ACTIVE, this::blueAction);
      stage.addEventFilter(ThreatEvent.BLUE_PACIFIED, this::blueAction);
      stage.addEventFilter(ThreatEvent.BROWN_ACTIVE, this::brownActive);
      stage.addEventFilter(ThreatEvent.BROWN_HONK, this::brownLeave);
      stage.addEventFilter(ThreatEvent.BROWN_HONK, brown::brownClicked);
      stage.addEventFilter(ThreatEvent.BROWN_CONTINUE, brown::brownClicked);
      stage.addEventFilter(ThreatEvent.CUPCAKE_ANY, yellow::cupcakeCheck);
      stage.addEventFilter(NightEvent.KEYPAD_PRESSED, blue::checkNumber);
      stage.addEventFilter(NightEvent.POWER_OUTAGE, this::disableTools);
      //stage.addEventFilter(NightEvent.CAMERAS_UP, brown::cameraWentDown);
      //stage.addEventFilter(NightEvent.CAMERAS_DOWN, brown::cameraWentUp);

      VBox loadingRoot = new VBox();
      Label loadingLabel = new Label("Returning to Menu...");
      loadingRoot.getChildren().add(loadingLabel);
      loadScene = new Scene(loadingRoot, 1600, 900);

      // Draws the icons for the threats. Placeholders for now
      Rectangle brownIcon = drawRect(50, 50, 100, 100, CUSTOM_BROWN, menu);
      Rectangle blueIcon = drawRect(200, 50, 100, 100, CUSTOM_BLUE, menu);
      Rectangle yellowIcon = drawRect(350, 50, 100, 100, CUSTOM_YELLOW, menu);
      Rectangle redIcon = drawRect(500, 50, 100, 100, CUSTOM_RED, menu);

      // Loads the map to be used later
      ImageView map = drawImage("src/assets/images/officeMap.png", 1415, 610, 160, 265, cameras);

      // The text that is to show for character descriptions.
      Text desc = text("Hover over the icons above for character mechanics!", 50, 300, menu);
      desc.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));

      // Formatting for text that was initialized outside start() that break the code below for some reason.
      aiValue.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));
      currentTime.setFont(Font.font("Courier New", FontWeight.NORMAL, 50));
      remainingPower.setFont(Font.font("Courier New", FontWeight.NORMAL, 30));
      cameraName.setFont(Font.font("Courier New", FontWeight.NORMAL, 40));
      blueSequence.setFont(Font.font("Courier New", FontWeight.NORMAL, 20));
      blueSequence.setFill(CUSTOM_BLUE);

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
      officeScene = new Scene(office, 1600, 900);
      cameraScene = new Scene(cameras, 1600, 900);
      menuScene.setFill(Color.ALICEBLUE);
      stage.setScene(menuScene);
      stage.show();

      addEscapeHandler(menuScene, stage);
      addTermEscapeHandler(officeScene, stage);
      addTermEscapeHandler(cameraScene, stage);
      addTermEscapeHandler(loadScene, stage);

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

      // Deals with Brown when clicked
      brownThreat.setOnMouseClicked(e -> {
        if (brown.dxIsActive()) {
          Event.fireEvent(stage, new ThreatEvent(ThreatEvent.BROWN_CONTINUE));
        } else {
          Event.fireEvent(stage, new ThreatEvent(ThreatEvent.BROWN_HONK));
        }
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
      refillGenerator.setOnMousePressed(e -> {
        isCharging = true;
      });
      refillGenerator.setOnMouseReleased(e -> {
        isCharging = false;
      });

      // Code for leaving the main menu
      startNight.setOnMouseClicked(e -> {
        stage.setScene(officeScene);
        startNight();
        setTime(0);
        setPower(10000);
        updateTime();
        updatePower();
        setCamera(0);

        // starts the Threads
        executor = Executors.newFixedThreadPool(totalTasks);

        hour.reset();
        generator.reset();
        executor.execute(hour);
        executor.execute(generator);
        for (Threat t : threats) {
          t.reset();
          executor.execute(t);
        }
      });

      // Office controls
      cameraUp.setOnMouseClicked(e -> {
        if (powerActive) {
          stage.setScene(cameraScene);
          refreshCameras();
          setCameraStatus(true);
        }
      });
      cameraDown.setOnMouseClicked(e -> {
        stage.setScene(officeScene);
        setCameraStatus(false);
      });
      leftButton.setOnMouseClicked(e -> {
        if (powerActive) toggleLeftDoor();
      });
      rightButton.setOnMouseClicked(e -> {
        if (powerActive) toggleRightDoor();
      });

      // Threat interactions
      cupcake.setOnMouseClicked(e -> {
        Event.fireEvent(cupcake, new ThreatEvent(ThreatEvent.CUPCAKE_MOVE));
      });
  }

  // rendering shapes
  static public Rectangle drawRect(int x, int y, int w, int h, Group group){
      return drawRect(x,y,w,h,DEFAULT_COLOR,group);
  }
  static public Rectangle drawRect(int x, int y, int w, int h, Color c, Group group){
      Rectangle rect = new Rectangle(x, y, w, h);
      rect.setFill(c);
      rect.setStroke(DEFAULT_COLOR);
      rect.setStrokeWidth(DEFAULT_WIDTH);
      group.getChildren().add(rect);
      return rect;
  }

  static public Circle drawCircle(int x, int y, int r, Group group){
      return drawCircle(x, y, r, DEFAULT_COLOR, group);
  }
  static public Circle drawCircle(int x, int y, int r, Color c, Group group){
      Circle circle = new Circle(x, y, r);
      circle.setFill(c);
      circle.setStroke(DEFAULT_COLOR);
      circle.setStrokeWidth(DEFAULT_WIDTH);
      group.getChildren().add(circle);
      return circle;
  }

  static public Text text(String str, int x, int y, Group group){
        Text text = new Text(str);
        text.setX(x);
        text.setY(y);
        text.setFocusTraversable(true);
        group.getChildren().add(text);
        return text;
  }

  static public Button drawButton(String str, int x, int y, int w, int h, int t, Group group) {
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
  static public CameraButton drawCamButton(String str, int x, int y, int w, int h, int t, Group group, int i) {
      // parameters: Button text, x position, y position, width, height, font size, group to include in, index
      if (w < 0 || h < 0 || t < 0) {
        throw new IllegalArgumentException("Dimensions and font size cannot be negative!");
      }

      CameraButton button = new CameraButton(str, i);
      button.setMinWidth(w);
      button.setMinHeight(h);
      button.setPrefWidth(w);
      button.setPrefHeight(h);
      button.setMaxWidth(w);
      button.setMaxHeight(h);
      button.relocate(x, y);
      group.getChildren().add(button);
      button.setFont(Font.font(t));
      return button;
  }
  static public KeypadButton drawPadButton(String str, int x, int y, int w, int h, int t, Group group, int i) {
      // parameters: Button text, x position, y position, width, height, font size, group to include in, index
      if (w < 0 || h < 0 || t < 0) {
        throw new IllegalArgumentException("Dimensions and font size cannot be negative!");
      }

      KeypadButton button = new KeypadButton(str, i);
      button.setMinWidth(w);
      button.setMinHeight(h);
      button.setPrefWidth(w);
      button.setPrefHeight(h);
      button.setMaxWidth(w);
      button.setMaxHeight(h);
      button.relocate(x, y);
      group.getChildren().add(button);
      button.setFont(Font.font(t));
      return button;
  }

  public void setIndex(int index) {
      selectedIndex = index;
  }

  // Renders images from src/assets/images
  static public ImageView drawImage(String imgDir, int x, int y, int w, int h, Group group) throws FileNotFoundException {
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

  // Renders image from src/assets/audio
  static public MediaPlayer createSFX(String medDir) {
    final Media sound = new Media(new File(medDir).toURI().toString());
    final MediaPlayer soundPlayer = new MediaPlayer(sound);
    return soundPlayer;
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
  static public void updateTime() {
    currentTime.setText(printTime());
  }
  static private String printTime() { // This is so that I wouldn't have to type it repeatedly.
    if (nightHour < 0) {
      return (nightHour + 12) + " PM";
    } else if (nightHour == 0){
      return "12 AM";
    } else {
      return nightHour + " AM";
    }
  }
  static public int getTime() {
    return nightHour;
  }
  static public void progressTime() { // progresses time by 1 hour. Can be regressed with negative numbers
    progressTime(1);
  }
  static public void progressTime(int hours) { // progresses time by the specified number of hours. Can be regressed with negative numbers
    nightHour += hours;
  }
  static public void setTime(int time) { // sets the time to a specific hour after 12 AM.
    nightHour = time;
  }

  // power-related methods
  static public void updatePower() {
    remainingPower.setText(printPower() + "%");
  }
  static private String printPower() {
    return ((int) Math.floor((double) power / 100.0)) + "";
  }
  static public int getPower() {
    return power;
  }
  static public void changePower() { // decreases power by 0.01%
    changePower(-1);
  }
  static public void changePower(int num) { // changes power by an inputted amount. Positive numbers increase power, negative numbers decrease it.
    power += num;
    if (power > 10000) {
      power = 10000;
    }
    if (power < 0) {
      power = 0;
    }
  }
  static public void setPower(int num) { // sets power to a certain percentage. 100 corresponds to 1% power.
    power = num;
  }
  static public boolean powerIsCharging() {
    return isCharging;
  }

  // door related methods
  static public void toggleLeftDoor() {
    leftClosed = !leftClosed;
    doorSlam.stop();
    doorSlam.play();
    refreshOffice();
  }
  static public void toggleRightDoor() {
    rightClosed = !rightClosed;
    doorSlam.stop();
    doorSlam.play();
    refreshOffice();
  }
  static public void setLeftDoor(boolean state) {
    leftClosed = state;
    doorSlam.stop();
    doorSlam.play();
    refreshOffice();
  }
  static public void setRightDoor(boolean state) {
    rightClosed = state;
    doorSlam.stop();
    doorSlam.play();
    refreshOffice();
  }
  static public boolean leftIsClosed() {
    return leftClosed;
  }
  static public boolean rightIsClosed() {
    return rightClosed;
  }

  // camera-related methods
  static public int getCurrentCamera() {
    return currentCamera;
  }
  static private String getCurrentCameraName() {
    return CAMERA_NAMES[currentCamera];
  }
  static public String cameraToString() {
    return "CAM " + (currentCamera + 1) + ": " + getCurrentCameraName();
  }
  static public void setCamera(int cameraIndex) {
    currentCamera = cameraIndex;
    Event.fireEvent(getStage(), new NightEvent(NightEvent.NIGHT_CAMERAS_REFRESH));
  }
  static public void nextCamera() {
    if (currentCamera < 9) {
      currentCamera++;
    } else {
      setCamera(0);
    }
  }
  static public void prevCamera() {
    if (currentCamera > 0) {
      currentCamera--;
    } else {
      setCamera(9);
    }
  }
  static public boolean getCameraStatus() {
    return camerasUp;
  }
  static public void setCameraStatus(boolean b) {
    camerasUp = b;
  }

  // methods to start and stop the night
  public void startNight() { // sets activeNight to true
    activeNight = true;
    powerActive = true;
  }
  public void stopNight() { // sets activeNight to false
    activeNight = false;
  }
  static public boolean isNightActive() {
    return activeNight;
  }

  // methods to handle events
  static public Stage getStage() {
    return stage;
  }
  private void returnToMenu(NightEvent event) {
    stopNight();
    stage.setScene(loadScene);

    for (Threat t : threats) {
      t.terminate();
    }
    hour.terminate();
    generator.terminate();

    brownThreat.setX(-5);
    brownThreat.setY(-5);
    brownThreat.setWidth(0);
    brownThreat.setHeight(0);
    brownStalking.stop();

    executor.shutdownNow();

    Thread shutdownThread = new Thread(() -> {
      try {

        boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
        IO.println("Executor shutdown complete: " + finished);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      Platform.runLater(() -> {stage.setScene(menuScene);});
    });

    shutdownThread.setDaemon(true);
    shutdownThread.start();

    event.consume();
  }
  private void cupcakeSpawnCue(ThreatEvent event) {
    cupcakeSpawnSound.setRate(1.0);
    cupcakeSpawnSound.stop();
    cupcakeSpawnSound.play();
    refreshCameras();

    event.consume();
  }
  private void cupcakeLeaveCue(ThreatEvent event) {
    cupcakeLeaveSound.setRate(0.75);
    cupcakeLeaveSound.stop();
    cupcakeLeaveSound.play();
    refreshCameras();

    event.consume();
  }
  private void brownActive(ThreatEvent event) {
    brownStalking.stop();
    brownStalking.play();
    brownThreat.setX((int)(Math.floor(Math.random() * 1101) + 100));
    brownThreat.setY(300);
    brownThreat.setWidth(300);
    brownThreat.setHeight(600);
    event.consume();
  }
  private void brownLeave(ThreatEvent event) {
    brownStalking.stop();
    honkSound.stop();
    honkSound.play();
    brownThreat.setX(-5);
    brownThreat.setY(-5);
    brownThreat.setWidth(0);
    brownThreat.setHeight(0);
    event.consume();
  }
  private void redMoveCue(ThreatEvent e) {
    refreshCameras();
    if (e.getEventType().getName().equals("RED_ADVANCE")) {
      stepsAdvance.stop();
      stepsAdvance.play();
    }
    if (e.getEventType().getName().equals("RED_RETREAT")) {
      stepsRetreat.stop();
      stepsRetreat.play();
    }
    e.consume();
  }
  private void blueAction(ThreatEvent e) {
    refreshCameras();
    if (e.getEventType().getName().equals("BLUE_ACTIVE")) {
      beepSound.stop();
      beepSound.play();
    }
    e.consume();
  }
  private void disableTools(NightEvent e) {
    powerActive = false;
    setLeftDoor(false);
    setRightDoor(false);
    stage.setScene(officeScene);
    refreshOffice();

    e.consume();
  }

  // Refreshes the screen when something happens.
  static public void keypadButtonPressed(int i) {
    buttonCall = i;
    Event.fireEvent(OSCN.stage, new NightEvent(NightEvent.KEYPAD_PRESSED));
  }
  static private void refreshOffice() {
    leftDoor.setFill(leftClosed ? Color.GRAY : Color.BLACK);
    rightDoor.setFill(rightClosed ? Color.GRAY : Color.BLACK);
  }
  private void refreshCameras(NightEvent event) {
    if (event.getEventType().getName().equals("NIGHT_CAMERAS_REFRESH")) {
      refreshCameras();
    }
  }
  private void refreshCameras() {
    IO.println("Camera Refresh Called, Current Camera: " + getCurrentCamera());
    cameraName.setText(getCurrentCameraName());
    if (getCurrentCamera() == 7) {
      refillGenerator.resizeRelocate(1415, 500, 160, 80);
    } else {
      refillGenerator.resizeRelocate(-5000, -5000, 0, 0);
    }
    if (blue.getLocation() == getCurrentCamera()) {
      blueThreat.setX(900);
      blueThreat.setY(300);
      blueThreat.setWidth(180);
      blueThreat.setHeight(240);
      if (blue.isActive()) {
        if (blue.dxIsActive()) {
          blueSequence.setText("" + blue.getCurrentNumber());
        } else {
          blueSequence.setText(blue.getSequence());
        }
      } else {
        blueSequence.setText("... Inactive");
      }
    } else {
      blueThreat.setX(-5);
      blueThreat.setY(-5);
      blueThreat.setWidth(0);
      yellowThreat.setHeight(0);
      blueSequence.setText("");
    }
    if (yellow.getLocation() == getCurrentCamera()) {
      if (yellow.isCupcakeActive()) {
        yellowThreat.setX(100);
        yellowThreat.setY(700);
        yellowThreat.setWidth(300);
        yellowThreat.setHeight(400);
      } else {
        yellowThreat.setX(100);
        yellowThreat.setY(300);
        yellowThreat.setWidth(180);
        yellowThreat.setHeight(240);
      }
    } else {
      yellowThreat.setX(-5);
      yellowThreat.setY(-5);
      yellowThreat.setWidth(0);
      yellowThreat.setHeight(0);
    }
    if (yellow.isCupcakeActive()) {
      IO.println("Cupcake on Camera " + (yellow.getCupcakeLocation() + 1) + ", looking at Camera " + (getCurrentCamera() + 1));
      if (yellow.getCupcakeLocation() == getCurrentCamera()) {
        cupcake.setCenterX(Math.floor(Math.random() * 601) + 500);
        cupcake.setCenterY(Math.floor(Math.random() * 301) + 300);
        cupcake.setRadius(50);
      } else {
        cupcake.setCenterX(-5);
        cupcake.setCenterY(-5);
        cupcake.setRadius(0);
      }
    } else {
      cupcake.setCenterX(-5);
      cupcake.setCenterY(-5);
      cupcake.setRadius(0);
    }
    if (red.getLocation() == getCurrentCamera()) {
      redThreat.setX(1450);
      redThreat.setY(400);
      redThreat.setWidth(300);
      redThreat.setHeight(400);
    } else {
      redThreat.setX(-5);
      redThreat.setY(-5);
      redThreat.setWidth(0);
      redThreat.setHeight(0);
    }
  }
  private void getDeathDetails(ThreatEvent e) {
    String eventName = e.getEventType().getName();
    String deathCause = "";
    switch (eventName) {
      case "BROWN_DEATH":
        deathCause = "You died to Brown.\nYou were a tad too slow for Brown! Act faster!";
        break;
      case "BLUE_MISPRESS_DEATH":
        deathCause = "You died to Blue.\nOoh, well, you could've done it, if only you pressed the other button\nthat was just a bit in the other direction...";
        break;
      case "BLUE_PATIENCE_DEATH":
        deathCause = "You died to Blue.\nI think Blue broke down waiting for you. Maybe try putting in the\nreboot code faster?";
        break;
      case "YELLOW_CUPCAKE_DEATH":
        deathCause = "You died to Yellow.\nYellow doesn't like it when you take her things away from her.";
        break;
      case "YELLOW_PATIENCE_DEATH":
        deathCause = "You died to Yellow.\nYellow would appreciate it if you could get her things to her faster.";
        break;
      case "RED_DEATH":
        deathCause = "You died to Red.\nI know, right? Red runs fast. Like, really fast. He's really camera shy, though.\nAt least, until he gets near you.";
        break;
      default:
        deathCause = "You died to... something?\nCouldn't get the cause of death. What the heck did you die to?";
        break;
    }
    Event.fireEvent(OSCN.stage, new NightEvent(NightEvent.NIGHT_END));
    deathTip.setText(deathCause);
    e.consume();
  }

  private void addEscapeHandler(Scene scene, Stage stage) {
      scene.setOnKeyPressed(event -> {
          if (event.getCode() == KeyCode.ESCAPE) {
              Platform.exit();
          }
      });
  }
  private void addTermEscapeHandler(Scene scene, Stage stage) {
      scene.setOnKeyPressed(event -> {
          if (event.getCode() == KeyCode.ESCAPE) {
            for (Threat t : threats) {
              t.terminate();
            }
            hour.terminate();
            generator.terminate();
            executor.shutdownNow();
            Platform.exit();

            Thread shutdownThread = new Thread(() -> {
              try {

                boolean finished = executor.awaitTermination(10, TimeUnit.SECONDS);
                IO.println("Executor shutdown complete: " + finished);
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
              }
            });

            shutdownThread.setDaemon(true);
            shutdownThread.start();
          }
      });
  }
}
// to build, you'll run
// mvn clean javafx:run

/*
 * Copyright 2026 Axel Liman
 *
 * This file is part of Oversimplified Custom Night (OSCN).
 * OSCN is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * OSCN is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with OSCN. If not, see <https://www.gnu.org/licenses/>.
 */
