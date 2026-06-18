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
  private boolean kill = false;

  public Red(int d, int l) {
      super(d, l, 3, "Red", "Red will start at the north room of the loop.\n\nHe runs to either the east or west room of the loop with a sound cue.\nWhen he does this, make sure to stare at him.\nThe longer you ignore him, the longer he will take to go away.\nIf he is ignored in this stage, he moves to the south room of the loop, then to either hallway.\n\nOnce at either hallway, close the door on him or he kills you.\nHe stays at the door based on how long you leave him there with the door open.", "He can now just skip to either hallway as he pleases.\nHe can kill you if you miss any of his movements,\nnot just if you forget to close the door on him.", 4500);
  }
  public Red() {
      this(0, 0);
  }

  @Override
  public void terminate() {
    terminateSwitch = true;

    workerThread.interrupt();
    Thread.currentThread().interrupt();

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
    kill = false;
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
        } while (!movementCheck(30));
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
        case 6: // in west section of loop, moves back if stared at, otherwise advances to south section of loop
          while (stareCounter < (200 + (ignoreCounter / 2)) && ignoreCounter < 1000) {
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
          if (ignoreCounter < 1000) {
            location = 0;
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
          } else  {
            if (!dxMode) {
              location = 4;
              Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_ADVANCE));
            } else {
              terminate();
              Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_DEATH));});
              return;
            }
          }
          resetCounters();
          break;
        case 8: // in west hallway, moves back if left door is closed, otherwise kills
          while (stareCounter < (300 + (ignoreCounter)) && ignoreCounter < 1000) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }

            if (OSCN.leftIsClosed()) {
              stareCounter++;
            } else {
              ignoreCounter++;
            }
            if (ignoreCounter >= 1000) { kill = true; }
          }
          if (kill) {
            terminate();
            Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_DEATH));});
            return;
          } else {
            location = 0;
            resetCounters();
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
          }
          break;
        case 9: // in east hallway, moves back if right door is closed, otherwise kills
          while (stareCounter < (300 + (ignoreCounter)) && ignoreCounter < 1000) {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              if (terminateSwitch) {IO.println("stopped"); return;}
            }

            if (OSCN.rightIsClosed()) {
              stareCounter++;
            } else {
              ignoreCounter++;
            }
            if (ignoreCounter >= 1000) { kill = true; }
          }
          if (kill) {
            terminate();
            Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_DEATH));});
            return;
          } else {
            location = 0;
            resetCounters();
            Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.RED_RETREAT));
          }
          break;
      }
    }
  }
}
/*
 * Copyright 2026 Axel Liman
 *
 * This file is part of Oversimplified Custom Night (OSCN).
 * OSCN is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * OSCN is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with OSCN. If not, see <https://www.gnu.org/licenses/>.
 */
