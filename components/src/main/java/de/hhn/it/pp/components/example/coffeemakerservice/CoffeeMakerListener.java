package de.hhn.it.pp.components.example.coffeemakerservice;

import de.hhn.it.pp.components.example.coffeemakerservice.provider.makerstates.MakerState;

public interface CoffeeMakerListener {
  void newState(State state);
}
