/*
 *  This assists with passive power drain over the span of the night.
 *
 *  @author YoloMoloCuber
 */
import javafx.event.Event;
import javafx.event.EventType;

 public class PowerDrain implements Runnable{
   private long waitPeriod;
   private volatile boolean terminateSwitch = false;

   public PowerDrain() {
     this(50);
   }
   public PowerDrain(long waitPeriod) {
     this.waitPeriod = waitPeriod;
   }

   public terminate() {
     terminateSwitch = true;
     this.interrupt();
     this.notify();
     IO.println("Terminated Process: Power Drain");
   }

   public void run() {
     synchronized (this) {
       while (OSCN.getPower() > 0 && !terminateSwitch) {
         try {
           this.wait(waitPeriod);
         } catch (InterruptedException e) {
         }

         if (terminateSwitch) return; else {
           OSCN.changePower();
           OSCN.updatePower();
         }
       }
     }
   }
 }
