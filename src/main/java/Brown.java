/**
 * The class for the Brown threat.
 *
 * @author YoloMoloCuber
 */

import javafx.application.*;
import java.util.*;
import javafx.event.*;
import javafx.scene.input.*;

public class Brown extends Threat{ // Code for Brown/Freddy
  private int waitTimer = 0;
  private boolean attackQueued = false;
  private int counter = 0;
  //private boolean canAttack = false;

  public Brown(int d, int l) {
      super(d, l, 0, "Brown", "Brown will randomly appear in your office.\nWhen he does, you must react quickly and click on him.\nNo, seriously you better have a fast reaction time.", "He can appear while you have your cameras up\nand he now takes multiple clicks to expel.", 4700);
  }
  public Brown() {
      this(0, 0);
  }

  public void brownClicked(ThreatEvent e) {
    if (e.getEventType().getName().equals("BROWN_HONK")) {
      attackQueued = false;
    } else if (e.getEventType().getName().equals("BROWN_CONTINUE")) {
      waitTimer = 0;
      counter += (int)(Math.floor(Math.random() * 3) + 1);
      if (counter >= 5 || !dxMode) {
        Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BROWN_HONK));
      } else {
        Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BROWN_ACTIVE));
      }
    }
  }

  @Override
  public void terminate() {
    terminateSwitch = true;

    try {
      workerThread.interrupt();
    } catch (NullPointerException e) {}
    Thread.currentThread().interrupt();

    IO.println("Terminated Process: Brown");
    reset();
  }

  @Override
  public void reset() {
    super.reset();
    waitTimer = 0;
    attackQueued = false;
    counter = 0;
    //canAttack = false;
  }

  @Override
  public void run() {
    workerThread = Thread.currentThread();

    while (!terminateSwitch) {
      if (!attackQueued) do {
        try {
          for (int i = 0; i < movementTimer / 10; i++) {
            Thread.sleep(10);
            if (terminateSwitch) {IO.println("stopped"); return;}
          }
        } catch (InterruptedException e) {
          if (terminateSwitch) {IO.println("stopped"); return;}
        }
      } while (!movementCheck(35));

      attackQueued = true;
      IO.println("Brown queued to move");

      if (attackQueued && (!OSCN.getCameraStatus() || dxMode)) {
        Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BROWN_ACTIVE));
        waitTimer = 0;
        counter = 0;
        int killTimer = 300 - (difficulty * 10);
        if (dxMode) killTimer += 100;
        while ((waitTimer < killTimer) && attackQueued) {
          try {
            Thread.sleep(10);
          } catch (InterruptedException e) {
            if (terminateSwitch) {IO.println("stopped"); return;}
          }
          if (terminateSwitch) {IO.println("stopped"); return;}

          waitTimer++;
        }

        if (attackQueued) {
          terminate();
          Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BROWN_DEATH));});
          return;
        }
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
