/**
 * Custom event that I am currently making to manage things that should happen during the night.
 *
 * @author YoloMoloCuber
 */

import javafx.event.Event;
import javafx.event.EventType;

public class NightEvent extends Event {
  public static final EventType<NightEvent> NIGHT = new EventType<>(Event.ANY, "ANY");
  public static final EventType<NightEvent> ANY = NIGHT;
  public static final EventType<NightEvent> NIGHT_END = new EventType<>(NightEvent.ANY, "NIGHT_END");
  public static final EventType<NightEvent> CLOSE_PROGRAM = new EventType<>(NightEvent.NIGHT_END, "CLOSE_PROGRAM");
  public static final EventType<NightEvent> NIGHT_CAMERAS = new EventType<>(NightEvent.ANY, "NIGHT_CAMERAS");
  public static final EventType<NightEvent> NIGHT_CAMERAS_REFRESH = new EventType<>(NightEvent.NIGHT_CAMERAS, "NIGHT_CAMERAS_REFRESH");
  public static final EventType<NightEvent> KEYPAD_PRESSED = new EventType<>(NightEvent.ANY, "KEYPAD_PRESSED");

  public NightEvent(EventType<? extends Event> eventType) {
    super(eventType);
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
