/*
 *  This assists with passive power drain over the span of the night.
 *
 *  @author YoloMoloCuber
 */
import javafx.event.Event;
import javafx.event.EventType;

 public class PowerDrain implements Runnable{
   private long waitPeriod;
   protected volatile boolean terminateSwitch = false;
   protected volatile Thread workerThread;

   public PowerDrain() {
     this(50);
   }
   public PowerDrain(long waitPeriod) {
     this.waitPeriod = waitPeriod;
   }

   public void terminate() {
     terminateSwitch = true;

     if (workerThread != null) {
       workerThread.interrupt();
     }

     IO.println("Terminated Process: Power Drain");
   }

   public void run() {
     workerThread = Thread.currentThread();

     while (OSCN.getPower() > 0 && !terminateSwitch) {
       try {
         Thread.sleep(waitPeriod);
       } catch (InterruptedException e) { if (terminateSwitch) return; }

       if (terminateSwitch) return;
       OSCN.changePower();
       OSCN.updatePower();
     }
   }
 }
