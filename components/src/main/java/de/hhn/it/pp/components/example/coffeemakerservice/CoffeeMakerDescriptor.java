package de.hhn.it.pp.components.example.coffeemakerservice;

public class CoffeeMakerDescriptor {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(CoffeeMakerDescriptor.class);

  private String location;
  private String model;
  private State state;
  private int id;

  /**
   * Constructor stating location and model of the CoffeeMaker.
   *
   * @param location location where the CoffeeMaker is accessible
   * @param model model of the CoffeeMaker
   */
  public CoffeeMakerDescriptor(final String location, final String model) {
    this.location = location;
    this.model = model;
    id = 0;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(final String location) {
    this.location = location;
  }

  public String getModel() {
    return model;
  }

  public void setModel(final String model) {
    this.model = model;
  }

  public State getState() {
    return state;
  }

  public void setState(final State state) {
    this.state = state;
  }

  public int getId() {
    return id;
  }

  public void setId(final int id) {
    this.id = id;
  }

  @Override
  public String toString() {
    return "CoffeeMakerDescriptor{"
            + "location='" + location + '\''
            + ", model='" + model + '\''
            + ", state=" + state
            + ", id=" + id
            + '}';
  }
}
