package de.hhn.it.pp.components.example.coffeemakerservice.provider;

import de.hhn.it.pp.components.example.coffeemakerservice.CoffeeMakerDescriptor;
import de.hhn.it.pp.components.example.coffeemakerservice.CoffeeMakerListener;
import de.hhn.it.pp.components.example.coffeemakerservice.Recipe;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;

public interface CoffeeMaker {

  void switchOn() throws IllegalStateException;

  void switchOff() throws IllegalStateException;

  void brew(Recipe recipe) throws IllegalParameterException, IllegalStateException;

  void cleanIt() throws IllegalStateException;

  void addCallback(CoffeeMakerListener listener) throws IllegalParameterException;

  void removeCallback(CoffeeMakerListener listener) throws IllegalParameterException;

  CoffeeMakerDescriptor getDescriptor();
}
