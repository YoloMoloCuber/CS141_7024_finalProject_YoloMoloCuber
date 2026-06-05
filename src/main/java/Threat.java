public abstract class Threat implements Runnable{
  public final int THREAT_INDEX; // Index of the threat in the threat array.
  protected int difficulty; // AI level, how often the threat should move
  protected int failCount = 0; // counts the number of failed movements
  protected final int DEFAULT_LOCATION; // Default location on the map.
  protected int location; // Current location on the map.
  protected int[][] path = new int[0][0]; // The pathing they should have. "Rows" correspond to the room that they're in, "columns" correspond to the rooms that they can move to.
  protected final String NAME; // Self explanatory
  protected final String DESCRIPTION; // DESCRIPTION that shows in the main menu.
  protected final String DX_DESCRIPTION; // Description of the DX mechanic that they have.
  protected boolean dxMode = false; // Just indicates if dxMode is on or off
  protected long movementTimer; // How long threats should wait in between movements
  protected volatile boolean terminateSwitch = false;
  protected volatile Thread workerThread;

  // Constructors
  public Threat(int d, int l, int i, String name, String desc, String dxDesc, long millis) {
      if (d < 0) {
        d = 0; // Sets the difficulty variable to 0 if the input is lower.
      } else if (d > 20) {
        d = 20; // Sets the difficulty variable to 20 if the input is higher.
      }
      if (i < 0) { // Checks for a valid index position. This is used to refer to the Threat array later.
        throw new IllegalArgumentException("Index cannot be negative!");
      }
      difficulty = d;
      DEFAULT_LOCATION = l;
      location = DEFAULT_LOCATION;
      NAME = name;
      THREAT_INDEX = i;
      DESCRIPTION = desc;
      DX_DESCRIPTION = dxDesc;
      movementTimer = millis;
  }
  public Threat() {
      this (0, 0, 0, "unnamed", "Placeholder", "DX Placeholder", 5000);
  }

  // termination methods
  public void terminate() { // Kills the thread
    terminateSwitch = true;

    if (workerThread != null) {
      workerThread.interrupt();
    }

    IO.println("Terminated Process: Threat");
  }
  public void reset() { // resets the threat back to its starting spot.
    location = DEFAULT_LOCATION;
    failCount = 0;
    terminateSwitch = false;
  }

  // Accessor methods
  public int getDifficulty() { // Returns the AI levels of the threat
    return difficulty;
  }
  public int getLocation() {
    IO.println("Getter sees : Camera" + (location + 1));
    return location;
  }
  public String getThreatName() { // Returns the description text.
    return NAME;
  }
  public String getDescription() { // Returns the description text.
    return DESCRIPTION;
  }
  public String getDX_Description() { // Returns the DX description text.
    return DX_DESCRIPTION;
  }
  public int getIndex() {
    return THREAT_INDEX;
  }
  public boolean dxIsActive() {
    return dxMode;
  }

  // Mutator methods
  public void setDifficulty(int diff) { // Sets the AI value to a random value
    setDifficulty(diff, false);
  }
  public void setDifficulty(int diff, boolean uncap) { // Sets the AI value to a random value. uncap tells the program to ignore the upper limit of 20.
    if (diff < 0) {
      diff = 0; // Sets the difficulty variable to 0 if the input is lower.
    } else if (diff > 20 && !uncap) {
      diff = 20; // Sets the difficulty variable to 20 if the input is higher and uncap is false
    }

    difficulty = diff;
  }
  public void setLocation(int l) {
    try {
      if (location < 0) {
        throw new IllegalArgumentException();
      }
      location = l;
    } catch (IllegalArgumentException e) {
      IO.println("Cannot have a negative location index!");
    }
  }
  public void incrementDifficulty() { // Increases the AI value by 1
    incrementDifficulty(false);
  }
  public void incrementDifficulty(boolean uncap) { // Increases the AI value by 1. uncap tells the program to ignore the upper limit of 20.
    if (difficulty < 20 || uncap) {
      difficulty++;
    } else {
      difficulty = 20;
    }
  }
  public void decrementDifficulty() { // Lowers the AI value by 1
    if (difficulty > 0) {
      difficulty--;
    }
  }
  public void setPath(int[][] path) {
    this.path = path;
  }
  public void toggleDX() {
    dxMode = !dxMode;
  }

  // Functional methods
  public void run() {
    synchronized (this) {
      while (!terminateSwitch) {
        try {
          Thread.sleep(movementTimer);
        } catch (InterruptedException e) { if (terminateSwitch) return; }

        if (terminateSwitch) return;

        IO.println(getThreatName() + " attempted movement. Suceeded? " + Boolean.toString(movementCheck()));

      }
    }
  }
  protected boolean movementCheck(int maxNum) { // picks a random number from 1 to 20.
    return (int)(Math.ceil(Math.random() * (maxNum))) <= difficulty;
  }
  protected boolean movementCheck() { // default of 20
    return movementCheck(20);
  }
}
