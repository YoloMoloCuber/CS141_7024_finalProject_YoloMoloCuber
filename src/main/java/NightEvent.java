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

  public NightEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
