package de.hhn.it.pp.components.example.coffeemakerservice.junit;

import de.hhn.it.pp.components.example.coffeemakerservice.CoffeeMakerDescriptor;
import de.hhn.it.pp.components.example.coffeemakerservice.CoffeeMakerListener;
import de.hhn.it.pp.components.example.coffeemakerservice.CoffeeMakerService;
import de.hhn.it.pp.components.example.coffeemakerservice.provider.AdminCoffeeMakerService;
import de.hhn.it.pp.components.example.coffeemakerservice.provider.WnckCoffeeMakerService;
import de.hhn.it.pp.components.exceptions.IllegalParameterException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("Test the CoffeeMakerService with bad cases.")
@ExtendWith(CoffeeMakerParameterResolver.class)
public class TestCoffeeMakerServiceGoodCases {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(TestCoffeeMakerServiceGoodCases.class);


  CoffeeMakerService coffeeMakerService;
  AdminCoffeeMakerService adminCoffeeMakerService;

  @BeforeEach
  void setup(List<CoffeeMakerDescriptor> descriptors) throws IllegalParameterException {
    WnckCoffeeMakerService wnckCoffeeMakerService = new WnckCoffeeMakerService();
    coffeeMakerService = wnckCoffeeMakerService;
    adminCoffeeMakerService = wnckCoffeeMakerService;

    for (CoffeeMakerDescriptor descriptor : descriptors) {
      adminCoffeeMakerService.addCoffeeMaker(descriptor);
    }
  }

  @Test
  @DisplayName("add and remove callbacks to a known coffeMaker.")
  void AddAndREmoveCallbacksToAKnownCoffeeMaker() throws IllegalParameterException {
    List<CoffeeMakerDescriptor> makers = coffeeMakerService.getCoffeeMakers();
    CoffeeMakerDescriptor maker = makers.get(0);
    CoffeeMakerListener listener = new DummyCallback();
    coffeeMakerService.addCallback(maker.getId(), listener);
    coffeeMakerService.removeCallback(maker.getId(), listener);

    IllegalParameterException exception = assertThrows(IllegalParameterException.class,
            () -> coffeeMakerService.removeCallback(maker.getId(), listener));

  }
}
