/**
 * Custom event that I am currently making to manage things that the threats should do
 *
 * holy shit that's a lot of custom events
 *
 * @author YoloMoloCuber
 */

import javafx.event.Event;
import javafx.event.EventType;

public class ThreatEvent extends Event {
  public static final EventType<ThreatEvent> THREAT = new EventType<>(Event.ANY, "ANY_THREAT");
  public static final EventType<ThreatEvent> ANY = THREAT;
  public static final EventType<ThreatEvent> CUPCAKE_ANY = new EventType<>(ThreatEvent.ANY, "CUPCAKE_ANY");
  public static final EventType<ThreatEvent> CUPCAKE_SPAWN = new EventType<>(ThreatEvent.CUPCAKE_ANY, "CUPCAKE_SPAWN");
  public static final EventType<ThreatEvent> CUPCAKE_MOVE = new EventType<>(ThreatEvent.CUPCAKE_ANY, "CUPCAKE_MOVE");
  public static final EventType<ThreatEvent> CUPCAKE_LEAVE = new EventType<>(ThreatEvent.CUPCAKE_ANY, "CUPCAKE_LEAVE");
  public static final EventType<ThreatEvent> DEATH = new EventType<>(ThreatEvent.ANY, "DEATH");
  public static final EventType<ThreatEvent> BROWN_DEATH = new EventType<>(ThreatEvent.DEATH, "BROWN_DEATH");
  public static final EventType<ThreatEvent> BROWN_ACTIVE = new EventType<>(ThreatEvent.ANY, "BROWN_ACTIVE");
  public static final EventType<ThreatEvent> BROWN_CONTINUE = new EventType<>(ThreatEvent.ANY, "BROWN_CONTINUE");
  public static final EventType<ThreatEvent> BROWN_HONK = new EventType<>(ThreatEvent.ANY, "BROWN_HONK");
  public static final EventType<ThreatEvent> BLUE_PATIENCE_DEATH = new EventType<>(ThreatEvent.DEATH, "BLUE_PATIENCE_DEATH");
  public static final EventType<ThreatEvent> BLUE_MISPRESS_DEATH = new EventType<>(ThreatEvent.DEATH, "BLUE_MISPRESS_DEATH");
  public static final EventType<ThreatEvent> YELLOW_DEATH = new EventType<>(ThreatEvent.DEATH, "YELLOW_DEATH");
  public static final EventType<ThreatEvent> BLUE_ACTIVE = new EventType<>(ThreatEvent.ANY, "BLUE_ACTIVE");
  public static final EventType<ThreatEvent> BLUE_PACIFIED = new EventType<>(ThreatEvent.ANY, "BLUE_PACIFIED");
  public static final EventType<ThreatEvent> YELLOW_CUPCAKE_DEATH = new EventType<>(ThreatEvent.DEATH, "YELLOW_CUPCAKE_DEATH");
  public static final EventType<ThreatEvent> YELLOW_PATIENCE_DEATH = new EventType<>(ThreatEvent.DEATH, "YELLOW_PATIENCE_DEATH");
  public static final EventType<ThreatEvent> RED_DEATH = new EventType<>(ThreatEvent.DEATH, "RED_DEATH");
  public static final EventType<ThreatEvent> RED_MOVEMENT = new EventType<>(ThreatEvent.ANY, "RED_MOVEMENT");
  public static final EventType<ThreatEvent> RED_ADVANCE = new EventType<>(ThreatEvent.RED_MOVEMENT, "RED_ADVANCE");
  public static final EventType<ThreatEvent> RED_RETREAT = new EventType<>(ThreatEvent.RED_MOVEMENT, "RED_RETREAT");

  public ThreatEvent(EventType<? extends Event> eventType) {
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
