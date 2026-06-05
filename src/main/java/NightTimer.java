/*
 *  The counter that keeps track of the time that has passed in the night.
 *
 *  @author YoloMoloCuber
 */
import javafx.event.Event;
import javafx.event.EventType;

 public class NightTimer implements Runnable{
   private long waitPeriod;
   protected volatile boolean terminateSwitch = false;
   protected volatile Thread workerThread;

   public NightTimer() {
     this(60000);
   }
   public NightTimer(long waitPeriod) {
     this.waitPeriod = waitPeriod;
   }

   public void terminate() {
     terminateSwitch = true;

     if (workerThread != null) {
       workerThread.interrupt();
     }

     IO.println("Terminated Process: Night Timer");
   }

   public void run() {
     workerThread = Thread.currentThread();

     while (OSCN.getTime() < 6 && !terminateSwitch) {
       try {
         Thread.sleep(waitPeriod);
       } catch (InterruptedException e) { if (terminateSwitch) return; }

       if (terminateSwitch) return;

        OSCN.progressTime();
        OSCN.updateTime();
     }

     if (OSCN.isNightActive()) {
       Event.fireEvent(OSCN.stage, new NightEvent(NightEvent.NIGHT_END));
     }
   }
 }
