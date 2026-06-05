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
      super(d, l, 2, "Yellow", "Placeholder for Yellow", "DX Placeholder for Yellow", 5120);

  }
  public Yellow() {
      this(0, 0);
  }

  @Override
  public void terminate() {
    terminateSwitch = true;

    if (workerThread != null) {
      workerThread.interrupt();
    }

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

    while (!terminateSwitch) {
      do { // sets the location of the cupcake to a location that isn't their current location
        cupcakeLocation = (int)(Math.floor(Math.random() * 8));
        nextLocation = (int)(Math.floor(Math.random() * 8));
        IO.println("Next Location: Camera " + (nextLocation + 1) + "\n Next Cupcake: Camera " + (cupcakeLocation + 1));
      } while (cupcakeLocation == nextLocation);

      do {
        try {
          Thread.sleep(movementTimer);
        } catch (InterruptedException e) {
          if (terminateSwitch) return;
        }
      } while (!movementCheck());

      if (terminateSwitch) return;

      location = nextLocation;
      cupcakeActive = true;
      IO.println("cupcakeActive -> true");
      IO.println("Yellow's new location: " + location);
      IO.println("Cupcake location: " + cupcakeLocation);
      Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.CUPCAKE_SPAWN));

      try {
        Thread.sleep(20000);
      } catch (InterruptedException e) {
        if (terminateSwitch) return;
      }

      if (terminateSwitch) return;

      if (cupcakeLocation != location) {
        IO.println("too slow");

        Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.YELLOW_DEATH));
        terminateSwitch = true;
        return;
      } else {
        try {
          Thread.sleep(3500);
        } catch (InterruptedException e) {
          if (terminateSwitch) return;
        }
      }

      if (terminateSwitch) return;

      Event.fireEvent(OSCN.stage, new ThreatEvent(ThreatEvent.CUPCAKE_LEAVE));
      location = 0;
      cupcakeActive = false;
      IO.println("cupcakeActive -> false");
    }
  }
}
