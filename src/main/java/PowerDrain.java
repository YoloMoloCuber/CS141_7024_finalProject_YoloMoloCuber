/*
 *  This assists with passive power drain over the span of the night.
 *
 *  @author YoloMoloCuber
 */
import javafx.event.Event;
import javafx.event.EventType;

 public class PowerDrain extends Thread{
   private long waitPeriod;

   public PowerDrain() {
     this(50);
   }
   public PowerDrain(long waitPeriod) {
     this.waitPeriod = waitPeriod;
   }

   public void run() {
     synchronized (this) {
       while (OSCN.getPower() > 0 && OSCN.isNightActive()) {
         try {
           this.wait(waitPeriod);
         } catch (InterruptedException e) {
         }

         OSCN.changePower();
         OSCN.updatePower();
       }
     }
   }
 }
