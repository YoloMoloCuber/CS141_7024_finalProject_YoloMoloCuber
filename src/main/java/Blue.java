/**
 * The class for the Blue threat.
 *
 * @author YoloMoloCuber
 */

import javafx.application.*;
import java.util.*;
import javafx.event.*;
import javafx.scene.input.*;

public class Blue extends Threat { // Code for Blue/Bonnie
  private final int[] DEFAULT_SEQUENCE = {-1,-1,-1,-1,-1,-1,-1};
  private int[] sequence = DEFAULT_SEQUENCE;
  private int required = 0;
  private int currentIndex = 0;
  private int killTimer = 0;
  private int waitTimer = 0;
  private boolean kill = false;

  public Blue(int d, int l) {
      super(d, l, 1, "Blue", "Blue has been malfunctioning lately, and you need to calibrate him.\nHe will occasionally beep, indicating that he has moved.\nWhen he moves, find him and get his reboot code\nthen input that into the keypad in the office.", "Making a mistake will lead to death instantly.\nHe also now only shows one number at a time,\nand moves with each number that you input.\nTo compensate, his kill timer resets when you input each number.", 4830);
  }
  public Blue() {
      this(0, 0);
  }

  public boolean isActive() {
    return kill;
  }

  // Methods to modify and retrieve sequence
  public void generateSequence() {
    for (int i = 0; i < sequence.length; i++) {
      if (i >= required) {
        sequence[i] = -1; // -1 is treated as a stopper
      } else {
        sequence[i] = (int)(Math.floor(Math.random() * 10));
      }
    }
  }

  public String getSequence() {
    String output = "";
    for (int i = 0; i < sequence.length; i++) {
      if (sequence[i] < 0) { // stops if the number is lower than 0
        break;
      } else if (i != 0) { // adds a seperator if it's not the first number.
        output = output + ", ";
      }

      output = output + sequence[i];
    }
    return output;
  }

  public void resetSequence() {
    sequence = DEFAULT_SEQUENCE;
    currentIndex = 0;
    required = 0;
    waitTimer = 0;
  }

  // Methods to deal with individual elements of seuquence
  public int getCurrentNumber() {
    if (currentIndex < 0 || currentIndex >= sequence.length) return -1; else return sequence[currentIndex];
  }
  public void checkNumber(NightEvent e) {
    IO.println("received number: " + OSCN.getButtonPressed());

    if (OSCN.getButtonPressed() < 0) return; // assumes a mistake has been made if a negative number is called.

    if (OSCN.getButtonPressed() == sequence[currentIndex]) {
      currentIndex++;
      if (dxMode) {
        location = (int)(Math.floor(Math.random() * 10));
        waitTimer = 0;
        Event.fireEvent(OSCN.getStage(), new NightEvent(NightEvent.NIGHT_CAMERAS_REFRESH));
      }
    } else if (dxMode && difficulty > 0) {
      terminate();
      Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BLUE_DEATH));});
      return;
    }
  }

  // Functional methods
  @Override
  public void terminate() {
    terminateSwitch = true;

    workerThread.interrupt();

    IO.println("Terminated Process: Blue");
    reset();
  }

  @Override
  public void reset() {
    super.reset();
    resetSequence();
    kill = false;
    killTimer = 0;
  }

  @Override
  public void run() {
    workerThread = Thread.currentThread();
    while (!terminateSwitch) {
      do {
        try {
          for (int i = 0; i < movementTimer / 10; i++) {
            Thread.sleep(10);
            if (terminateSwitch) {IO.println("stopped"); return;}
          }
        } catch (InterruptedException e) {
          if (terminateSwitch) {IO.println("stopped"); return;}
        }
      } while (!movementCheck(25));

      resetSequence();
      if (difficulty <= 10) {
        required = 3;
        killTimer = 900;
      } else if (difficulty <= 14) {
        required = 4;
        killTimer = 1050;
      } else if (difficulty <= 17) {
        required = 5;
        killTimer = 1200;
      } else if (difficulty <= 19) {
        required = 6;
        killTimer = 1350;
      } else {
        required = 7;
        killTimer = 1500;
      }
      if (dxMode) killTimer = 600;
      generateSequence();
      IO.println("Blue spawned, required sequence: " + getSequence());
      location = (int)(Math.floor(Math.random() * 10));
      kill = true;
      Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BLUE_ACTIVE));

      while (waitTimer < killTimer) {
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) {
          if (terminateSwitch) {IO.println("stopped"); return;}
        }
        if (terminateSwitch) {IO.println("stopped"); return;}

        if (currentIndex >= sequence.length || sequence[currentIndex] < 0) {
          kill = false;
          location = 0;
          IO.println("Blue has been appeased... for now.");
          break;
        }

        waitTimer++;
      }

      if (kill) {
        terminate();
        Platform.runLater(() -> {Event.fireEvent(OSCN.getStage(), new ThreatEvent(ThreatEvent.BLUE_DEATH));});
        return;
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
