public class Blue extends Threat{ // Code for Blue/Bonnie
  private final int[] DEFAULT_SEQUENCE = {-1,-1,-1,-1,-1,-1,-1};
  private int[] sequence = DEFAULT_SEQUENCE;
  private int required = 0;

  public Blue(int d, int l) {
      super(d, l, 1, "Blue", "Placeholder for Blue", "DX Placeholder for Blue", 4830);
  }
  public Blue() {
      this(0, 0);
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
  }

  // Methods to deal with individual elements of seuquence
  public in

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
  }

  @Override
  public void run() {
    workerThread = Thread.currentThread();
    /*
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
      } while (!movementCheck());

      generateSequence();

    }*/
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
