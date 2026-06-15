/**
 * The class for the Red threat.
 *
 * @author YoloMoloCuber
 */

import javafx.application.*;
import java.util.*;
import javafx.event.*;
import javafx.scene.input.*;

public class Red extends Threat{ // Code for Red/Foxy
  private int stareCounter = 0;
  private int ignoreCounter = 0;

  public Red(int d, int l) {
      super(d, l, 3, "Red", "Placeholder for Red", "DX Placeholder for Red", 4500);
  }
  public Red() {
      this(0, 0);
  }

  @Override
  public void terminate() {
    terminateSwitch = true;

    try {
      workerThread.interrupt();
    } catch (NullPointerException e) {}

    IO.println("Terminated Process: Red");
    reset();
  }

  @Override
  public void reset() {
    super.reset();
    resetCounters();
  }

  private void resetCounters() {
    stareCounter = 0;
    ignoreCounter = 0;
  }

  @Override
  public void run() {
    workerThread = Thread.currentThread();

    while (!terminateSwitch) {
      if (location == 0 || location == 4){
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

      //Decides randomly where to movement
      switch (location) {
        case 0:
          if ((int) Math.floor(Math.random() * 2) == 0) {
            location = 6;
          } else location = 2;
          Event.fireEvent(OSCN.getStage(), new NightEvent(NightEvent.NIGHT_CAMERAS_REFRESH));
          break;
        case 4:
          if ((int) Math.floor(Math.random() * 2) == 0) {
            location = 8;
          } else location = 9;
          Event.fireEvent(OSCN.getStage(), new NightEvent(NightEvent.NIGHT_CAMERAS_REFRESH));
          break;
        case 2:
          for (;stareCounter >= 500 || ignoreCounter >= 1000;) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }
            if (terminateSwitch) {IO.println("stopped"); return;}
            if (OSCN.getCurrentCamera() == location) stareCounter++; else ignoreCounter++;
          }
          if (stareCounter >= 500) location = 4; else location = 0;
          break;
        case 6:
          for (;stareCounter >= 500 || ignoreCounter >= 1000;) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }
            if (terminateSwitch) {IO.println("stopped"); return;}
            if (OSCN.getCurrentCamera() == location) stareCounter++; else ignoreCounter++;
          }
          if (stareCounter >= 500) location = 4; else location = 0;
          break;
        case 8:
          for (int i = 0; i < 1000; i++) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }

            if (OSCN.leftIsClosed()) {
              location = 0;
              break;
            }
          }
        // write code for when you fail to close the door on Red here, I am too fucking tired for ts
      }
    }
  }
}
