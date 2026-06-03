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

  public void moveCupcake() {
    if (cupcakeLocation < 7) {
      cupcakeLocation++;
    } else {
      cupcakeLocation = 0;
    }
  }

  /* temp comment out to finish up cameras
  @Override
  public void run() {
    synchronized (this) {
      while (OSCN.isNightActive()) {
        do { // sets the location of the cupcake to a location that isn't their current location
          cupcakeLocation = (int)(Math.floor(Math.random() * 8));
          nextLocation = (int)(Math.floor(Math.random() * 10));
        } while (cupcakeLocation != nextLocation);

        try {
          wait(movementTimer);
        } catch (InterruptedException e) {}

        cupcakeActive = true;
        location = nextLocation;
        Event.fireEvent(OSCN.cupcake, new ThreatEvent(ThreatEvent.CUPCAKE_SPAWN));

        try {
          wait(12000);
        } catch (InterruptedException e) {}

        if (cupcakeLocation != location) {
          IO.println("cupcake not found, would jumpscare here");
        }
      }
    }
  }*/
}
