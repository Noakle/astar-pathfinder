package de.hhn.it.pp.components.example.coffeemakerservice;

public class Recipe {
  private static final org.slf4j.Logger logger =
          org.slf4j.LoggerFactory.getLogger(Recipe.class);

  private Quantity coffee;
  private Quantity milk;
  private Quantity sugar;
  private Quantity milkFroth;

  /**
   * Constructor for a coffee recipe.
   *
   * @param coffee Quantity of coffee
   * @param milk Quantity of milk
   * @param sugar Quantity of sugar
   * @param milkFroth Quantity of milkFroth
   */
  public Recipe(final Quantity coffee, final Quantity milk, final Quantity sugar, final Quantity
          milkFroth) {
    this.coffee = coffee;
    this.milk = milk;
    this.sugar = sugar;
    this.milkFroth = milkFroth;
  }

  public Quantity getCoffee() {
    return coffee;
  }

  public void setCoffee(final Quantity coffee) {
    this.coffee = coffee;
  }

  public Quantity getMilk() {
    return milk;
  }

  public void setMilk(final Quantity milk) {
    this.milk = milk;
  }

  public Quantity getSugar() {
    return sugar;
  }

  public void setSugar(final Quantity sugar) {
    this.sugar = sugar;
  }

  public Quantity getMilkFroth() {
    return milkFroth;
  }

  public void setMilkFroth(final Quantity milkFroth) {
    this.milkFroth = milkFroth;
  }
}
