/*
 *  The counter that keeps track of the time that has passed in the night.
 *
 *  @author YoloMoloCuber
 */
import javafx.event.Event;
import javafx.event.EventType;

 public class NightTimer implements Runnable{
   private long waitPeriod;
   private volatile boolean terminateSwitch = false;

   public NightTimer() {
     this(60000);
   }
   public NightTimer(long waitPeriod) {
     this.waitPeriod = waitPeriod;
   }

   public terminate() {
     terminateSwitch = true;
     this.interrupt();
     this.notify();
     IO.println("Terminated Process: Night Timer");
   }

   public void run() {
     synchronized (this) {
       while (OSCN.getTime() < 6 && !terminateSwitch) {
         try {
           this.wait(waitPeriod);
         } catch (InterruptedException e) {
         }

         if (terminateSwitch) return; else {
           OSCN.progressTime();
           OSCN.updateTime();
         }
       }

       if (OSCN.isNightActive()) {
         Event.fireEvent(OSCN.stage, new NightEvent(NightEvent.NIGHT_END));
       }
     }
   }
 }
