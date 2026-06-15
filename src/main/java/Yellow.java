/**
 * The class for the Yellow threat.
 *
 * @author YoloMoloCuber
 */

import javafx.application.*;
import java.util.*;
import javafx.event.*;
import javafx.scene.input.*;

public class Yellow extends Threat{ // Code for Yellow/Chica
  private int cupcakeLocation = 0;
  private int nextLocation = 0;
  private volatile boolean cupcakeActive = false;

  public Yellow(int d, int l) {
      super(d, l, 2, "Yellow", "She spawns in with a soundcue.\nFind her cupcake (pink circle) and click on it to move it.\nMove the cupcake to her, do not move it past her!", "She now constanly attacks.\nTo compensate, she waits longer before killing you.", 5120);

  }
  public Yellow() {
      this(0, 0);
  }

  @Override
  public void terminate() {
    terminateSwitch = true;
    IO.println(workerThread.toString());

    try {
      workerThread.interrupt();
    } catch (NullPointerException e) {}

    IO.println("Terminated Process: Yellow");
    reset();
  }

  public int getCupcakeLocation() {
    return cupcakeLocation;
  }
  public void moveCupcake() {
    if (cupcakeLocation < 7) {
      cupcakeLocation++;
    } else {
      cupcakeLocation = 0;
    }
    IO.println("Cupcake location: " + cupcakeLocation);
  }

  @Override
  public void reset() {
    super.reset();
    nextLocation = 0;
    cupcakeLocation = 0;
    cupcakeActive = false;
    IO.println("cupcakeActive -> false");
  }

  public boolean isCupcakeActive() {
    IO.println("Getter sees: " + cupcakeActive);
    return cupcakeActive;
  }
  public void setCupcakeState(boolean bool) {
    cupcakeActive = bool;
  }

  public void cupcakeCheck(ThreatEvent e) {
    if (e.getEventType().getName().equals("CUPCAKE_MOVE")) {
      if (cupcakeLocation == location) { // sees if it was prompted to move when the cupcake was already at the same camera
        IO.println("dude why'd you take away my cupcake");
        Platform.runLater(() -> {
          Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.YELLOW_DEATH));
          terminateSwitch = true;
          return;
        });
      }
      moveCupcake();
      Event.fireEvent(OSCN.getStage(), new NightEvent(NightEvent.NIGHT_CAMERAS_REFRESH));
      if (cupcakeLocation == location) {
        workerThread.interrupt();
      }
    }
    e.consume();
  }

  @Override
  public void run() {
    workerThread = Thread.currentThread();
    cupcakeActive = false;
    IO.println("cupcakeActive -> false");
    IO.println(workerThread.toString());

    while (!terminateSwitch) {
      workerThread = Thread.currentThread();
      do { // sets the location of the cupcake to a location that isn't their current location
        cupcakeLocation = (int)(Math.floor(Math.random() * 8));
        nextLocation = (int)(Math.floor(Math.random() * 8));
        IO.println("Next Location: Camera " + (nextLocation + 1) + "\n Next Cupcake: Camera " + (cupcakeLocation + 1));
      } while (cupcakeLocation == nextLocation);
      if (!dxMode) {
        do {
          try {
            for (int i = 0; i < movementTimer / 10; i++) {
              Thread.sleep(10);
              if (terminateSwitch) {IO.println("stopped"); return;}
            }
          } catch (InterruptedException e) {
            if (terminateSwitch) {IO.println("stopped"); return;}
          }
        } while (!movementCheck());
      }
      if (terminateSwitch) {IO.println("stopped"); return;}

      location = nextLocation;
      cupcakeActive = true;
      IO.println("cupcakeActive -> true");
      IO.println("Yellow's new location: " + location);
      IO.println("Cupcake location: " + cupcakeLocation);
      Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.CUPCAKE_SPAWN));

      try {
        long tempTime;
        if (dxMode) {
          tempTime = 40000 - (difficulty * 500);
        } else {
          tempTime = 30000 - (difficulty * 500);
        }
        for (int i = 0; i < tempTime / 10; i++) {
          Thread.sleep(10);
          if (terminateSwitch) {IO.println("stopped"); return;}
        }
      } catch (InterruptedException e) {
        if (terminateSwitch) {IO.println("stopped"); return;}
      }

      if (terminateSwitch) {IO.println("stopped"); return;}

      if (cupcakeLocation != location) {
        IO.println("too slow");
        terminateSwitch = true;
        Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.YELLOW_DEATH));});

        return;
      } else {
        try {
          for (int i = 0; i < 350; i++) {
            Thread.sleep(10);
            if (terminateSwitch) {IO.println("stopped"); return;}
          }
        } catch (InterruptedException e) {
          if (terminateSwitch) {IO.println("stopped"); return;}
        }
      }

      if (terminateSwitch) {IO.println("stopped"); return;}

      location = 0;
      cupcakeActive = false;
      Event.fireEvent(OSCN.stage, new ThreatEvent(ThreatEvent.CUPCAKE_LEAVE));
      IO.println("cupcakeActive -> false");
    }
  }
}
