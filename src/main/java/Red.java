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

    workerThread.interrupt();

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

      //Decides randomly where to move
      switch (location) {
        case 0: // moves to west or east sections of loop
          double rand = Math.random();
          if (!dxMode) {
            if (rand < 0.5) {
              location = 6;
            } else {
              location = 2;
            }
          } else {
            if (rand < 0.25) {
              location = 6;
            } else if (rand < 0.5) {
              location = 2;
            } else if (rand < 0.75) {
              location = 8;
            } else {
              location = 9;
            }
          }
          Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_ADVANCE));
          break;
        case 4: // moves to west or east hallway
          if (Math.random() < 0.5) {
            location = 8;
          } else {
            location = 9;
          }
          Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_ADVANCE));
          break;
        case 2: // in east section of loop, moves back if stared at, otherwise advances to south section of loop
          while (stareCounter < 500 && ignoreCounter < 1000) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }
            if (terminateSwitch) {IO.println("stopped"); return;}
            if (OSCN.getCurrentCamera() == location) {
              stareCounter++;
            } else {
              ignoreCounter++;
            }
          }
          if (stareCounter >= 500) {
            location = 0;
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
          } else  {
            location = 4;
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_ADVANCE));
          }
          resetCounters();
          break;
        case 6: // in west section of loop, moves back if stared at, otherwise advances to south section of loop
          while (stareCounter < 500 && ignoreCounter < 1000) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }
            if (terminateSwitch) {IO.println("stopped"); return;}
            if (OSCN.getCurrentCamera() == location) {
              IO.println("Red is being stared at");
              stareCounter++;
            } else {
              IO.println("Red is being ignored");
              ignoreCounter++;
            }
          }
          if (stareCounter >= 500) {
            location = 0;
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
          } else  {
            location = 4;
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_ADVANCE));
          }
          resetCounters();
          break;
        case 8: // in west hallway, moves back if left door is closed, otherwise kills
          for (int i = 0; i < 1000; i++) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }

            if (OSCN.leftIsClosed()) {
              location = 0;
              Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
              break;
            }
          }
          if (location != 0) {
            terminate();
            Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_DEATH));});
            return;
          }
          break;
        case 9: // in east hallway, moves back if right door is closed, otherwise kills
          for (int i = 0; i < 1000; i++) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }

            if (OSCN.rightIsClosed()) {
              location = 0;
              Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
              break;
            }
          }
          if (location != 0) {
            terminate();
            Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_DEATH));});
            return;
          }
          break;
        // write code for when you fail to close the door on Red here, I am too fucking tired for ts
      }
    }
  }
}
