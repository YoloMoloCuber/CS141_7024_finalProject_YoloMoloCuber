/**
 * Custom event that I am currently making to manage things that the threats should do
 *
 * @author YoloMoloCuber
 */

import javafx.event.Event;
import javafx.event.EventType;

public class ThreatEvent extends Event {
  public static final EventType<ThreatEvent> THREAT = new EventType<>(Event.ANY, "ANY");
  public static final EventType<ThreatEvent> ANY = THREAT;
  public static final EventType<ThreatEvent> CUPCAKE_ANY = new EventType<>(ThreatEvent.ANY, "CUPCAKE_ANY");
  public static final EventType<ThreatEvent> CUPCAKE_SPAWN = new EventType<>(ThreatEvent.CUPCAKE_ANY, "CUPCAKE_SPAWN");
  public static final EventType<ThreatEvent> CUPCAKE_MOVE = new EventType<>(ThreatEvent.CUPCAKE_ANY, "CUPCAKE_MOVE");

  public ThreatEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
