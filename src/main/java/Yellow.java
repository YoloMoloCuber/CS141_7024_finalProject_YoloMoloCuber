/**
 * The class for the Yellow threat.
 *
 * @author YoloMoloCuber
 */

import java.util.*;
import javafx.event.*;
import javafx.scene.input.*;

public class Yellow extends Threat{ // Code for Yellow/Chica
  private int cupcakeLocation = 0;
  private int nextLocation = 0;
  private boolean cupcakeActive;
  public Yellow(int d, int l) {
      super(d, l, 2, "Yellow", "Placeholder for Yellow", "DX Placeholder for Yellow", 5120);

  }
  public Yellow() {
      this(0, 0);
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
  }

  public boolean isCupcakeActive() {
    return cupcakeActive;
  }
  public void setCupcakeState(boolean bool) {
    cupcakeActive = bool;
  }

  public void cupcakeCheck(ThreatEvent e) {
    if (e.getEventType().getName().equals("CUPCAKE_MOVE")) {
      if (cupcakeLocation == location) { // sees if it was prompted to move when the cupcake was already at the same camera
        IO.println("dude why'd you take away my cupcake");
        Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.YELLOW_DEATH));
        return;
      }
      moveCupcake();
      Event.fireEvent(OSCN.getStage(), new NightEvent(NightEvent.NIGHT_CAMERAS_REFRESH));
      if (cupcakeLocation == location) {
        this.interrupt();
      }
    }
    e.consume();
  }

  @Override
  public void run() {
    synchronized (this) {
      while (OSCN.isNightActive()) {
        do { // sets the location of the cupcake to a location that isn't their current location
          cupcakeLocation = (int)(Math.floor(Math.random() * 8));
          nextLocation = (int)(Math.floor(Math.random() * 8));
        } while (cupcakeLocation == nextLocation);

        do {
          try {
            wait(movementTimer);
          } catch (InterruptedException e) {
            interrupted();
          }
        } while (!movementCheck());

        location = nextLocation;
        IO.println("Yellow's new location: " + location);
        Event.fireEvent(OSCN.cupcake, new ThreatEvent(ThreatEvent.CUPCAKE_SPAWN));

        try {
          wait(20000);
        } catch (InterruptedException e) {
          interrupted();
        }

        if (cupcakeLocation != location) {
          IO.println("too slow");
          Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.YELLOW_DEATH));
          return;
        } else {
          try {
            wait(3500);
          } catch (InterruptedException e) {
            interrupted();
          }
          location = 0;
          Event.fireEvent(OSCN.stage, new ThreatEvent(ThreatEvent.CUPCAKE_LEAVE));
        }
      }
    }
  }
}
