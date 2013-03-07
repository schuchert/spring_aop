package shoe.example.toggles;

import org.springframework.stereotype.Component;

@Component("trackMetrics")
public class TrackMetrics {
  private boolean enabled = true;

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean value) {
    enabled = value;
  }
}
