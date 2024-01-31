package com.comp301.a08shopping;

public class ProductImpl implements Product {
  private final String name;
  private final double basePrice;
  private int inventory;
  private double percentOff;

  public ProductImpl(String name, double basePrice) {
    this(name, basePrice, 0);
  }

  public ProductImpl(String name, double basePrice, int inventory) {
    if (basePrice <= 0) {
      throw new IllegalArgumentException();
    }
    this.name = name;
    this.basePrice = basePrice;
    this.percentOff = 0.00;
    this.inventory = inventory;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public double getBasePrice() {
    return basePrice;
  }

  public int getInventory() {
    return inventory;
  }

  public void setInventory(int numItems) {
    this.inventory += numItems;
  }

  public double getSale() {
    return percentOff;
  }

  public void setSale(double percentOff) {
    this.percentOff = percentOff;
  }
}
