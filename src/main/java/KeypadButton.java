/*
 * Custom class that I made to make code more consise & so I don't have to write the same line over and over again.
 * Only purpose of this was to make it store an index for the number it's associated with.
 * I made this and the CameraButton class seperate so I could more easily distinguish between keypad buttons and camera buttons.
 *
 *  @author YoloMoloCuber
 */

import java.util.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;

public class KeypadButton extends Button {
  private int index = -1;

  public KeypadButton() {
    super();
  }
  public KeypadButton(String text) {
    super(text);
  }
  public KeypadButton(String text, int i) {
    super(text);
    index = i;

    setOnMouseClicked(e -> {
      OSCN.setCamera(this.index);
    });
  }
  public KeypadButton(String text, Node graphic) {
    super(text, graphic);
  }

  public int getIndex() {
    return index;
  }

  public void setIndex(int index) {
    try {
      if (index < 0 || index > 9) {
        throw new IllegalArgumentException("Input Parameter out of bounds; must be an integer within the range [0,9]!");
      }
    } catch (IllegalArgumentException e) {
      IO.println(e.getMessage());
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
