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

   public void reset() {
     terminateSwitch = false;
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

     if (OSCN.getTime() >= 6) {
       Event.fireEvent(OSCN.stage, new NightEvent(NightEvent.NIGHT_END));
     }
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
