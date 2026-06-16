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

   public void reset() {
     terminateSwitch = false;
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
       if (OSCN.leftIsClosed()) { OSCN.changePower(); }
       if (OSCN.rightIsClosed()) { OSCN.changePower(); }
       OSCN.changePower();
       OSCN.updatePower();
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
