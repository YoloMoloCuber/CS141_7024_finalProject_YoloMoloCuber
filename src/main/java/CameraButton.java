/*
 * Custom class that I made to make code more consise & so I don't have to write the same line over and over again.
 * Only purpose of this was to make it store an index for the camera it's associated with.
 *
 *  @author YoloMoloCuber
 */

import java.util.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.control.*;

public class CameraButton extends Button {
  private int index = -1;

  public CameraButton() {
    super();
  }
  public CameraButton(String text) {
    super(text);
  }
  public CameraButton(String text, int i) {
    super(text);
    index = i;
  }
  public CameraButton(String text, Node graphic) {
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
