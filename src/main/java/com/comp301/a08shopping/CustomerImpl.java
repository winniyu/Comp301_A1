package com.comp301.a08shopping;

import com.comp301.a08shopping.events.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerImpl implements Customer {
  private final String name;
  private final List<ReceiptItem> receiptItems;
  private double budget;

  public CustomerImpl(String name, double budget) {
    if (name == null || budget <= 0) {
      throw new IllegalArgumentException();
    }
    this.name = name;
    this.budget = budget;
    this.receiptItems = new ArrayList<ReceiptItem>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public double getBudget() {
    return budget;
  }

  @Override
  public void purchaseProduct(Product product, Store store) {
    if (product == null || store == null) {
      throw new IllegalArgumentException();
    }
    double price = store.getSalePrice(product);
    if (price > budget) {
      throw new IllegalStateException();
    }
    budget -= price;
    receiptItems.add(store.purchaseProduct(product));
  }

  @Override
  public List<ReceiptItem> getPurchaseHistory() {
    return receiptItems;
  }

  @Override
  public void update(StoreEvent event) {
    if (event == null) {
      throw new IllegalArgumentException();
    }
    Product product = event.getProduct();
    Store store = event.getStore();
    if (event instanceof BackInStockEvent) {
      System.out.println(product.getName() + " is back in stock at " + store.getName());
    } else if (event instanceof OutOfStockEvent) {
      System.out.println(product.getName() + " is now out of stock at " + store.getName());
    } else if (event instanceof PurchaseEvent) {
      System.out.println("Someone purchased " + product.getName() + " at " + store.getName());
    } else if (event instanceof SaleEndEvent) {
      System.out.println(
          "The sale for " + product.getName() + " at " + store.getName() + " has ended");
    } else {
      System.out.println("New sale for " + product.getName() + " at " + store.getName() + "!");
    }
  }
}
