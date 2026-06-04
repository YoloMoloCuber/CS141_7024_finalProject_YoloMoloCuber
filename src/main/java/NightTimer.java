/*
 *  The counter that keeps track of the time that has passed in the night.
 *
 *  @author YoloMoloCuber
 */
import javafx.event.Event;
import javafx.event.EventType;

 public class NightTimer extends Thread{
   private long waitPeriod;

   public NightTimer() {
     this(60000);
   }
   public NightTimer(long waitPeriod) {
     this.waitPeriod = waitPeriod;
   }

   public void run() {
     synchronized (this) {
       while (OSCN.getTime() < 6 && OSCN.isNightActive()) {
         try {
           this.wait(waitPeriod);
         } catch (InterruptedException e) {
         }

         OSCN.progressTime();
         OSCN.updateTime();
       }

       if (OSCN.isNightActive()) {
         Event.fireEvent(OSCN.stage, new NightEvent(NightEvent.NIGHT_END));
       }
     }
   }
 }
