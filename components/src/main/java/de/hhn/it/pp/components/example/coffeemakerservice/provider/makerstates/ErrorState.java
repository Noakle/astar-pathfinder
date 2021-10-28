package de.hhn.it.pp.components.example.coffeemakerservice.provider.makerstates;

import de.hhn.it.pp.components.example.coffeemakerservice.State;
import de.hhn.it.pp.components.example.coffeemakerservice.provider.WnckCoffeeMaker;

public class ErrorState extends MakerState {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(ErrorState.class);

  public ErrorState(final WnckCoffeeMaker maker) {
    super(maker);
    state = State.ERROR;
  }

  @Override
  public void onSwitchOn() throws IllegalStateException {
    throw MessageHelper.errorState;
  }

  @Override
  public void onSwitchOff() throws IllegalStateException {
    maker.setMakerState(new SwitchOffState(maker));
  }

  @Override
  public void onBrew() throws IllegalStateException {
    throw MessageHelper.errorState;
  }

  @Override
  public void onCleanIt() throws IllegalStateException {
    throw MessageHelper.errorState;
  }
}
