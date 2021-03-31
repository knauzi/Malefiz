package tdl_stuff.net;

import java.io.Serializable;

public interface Unit extends Serializable {
  
  /**
   * Recomputes the value of this hidden unit, querying it's
   * prior inputs.
   */
  public void recompute();
  
  /**
   * Returns the current value of this input
   *
   * @return The current value of this input
   */
  public double getValue();
  
}