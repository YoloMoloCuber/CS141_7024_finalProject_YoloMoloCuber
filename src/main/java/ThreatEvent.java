/**
 * Custom event that I am currently making to manage things that the threats should do
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
  public static final EventType<ThreatEvent> YELLOW_DEATH = new EventType<>(ThreatEvent.DEATH, "YELLOW_DEATH");
  public static final EventType<ThreatEvent> YELLOW_CUPCAKE_DEATH = new EventType<>(ThreatEvent.DEATH, "YELLOW_CUPCAKE_DEATH");
  public static final EventType<ThreatEvent> YELLOW_PATIENCE_DEATH = new EventType<>(ThreatEvent.DEATH, "YELLOW_PATIENCE_DEATH");

  public ThreatEvent(EventType<? extends Event> eventType) {
    super(eventType);
  }
}
