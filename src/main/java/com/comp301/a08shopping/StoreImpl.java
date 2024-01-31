package com.comp301.a08shopping;

import com.comp301.a08shopping.events.*;
import com.comp301.a08shopping.exceptions.OutOfStockException;
import com.comp301.a08shopping.exceptions.ProductNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class StoreImpl implements Store {
  private final String name;
  private final List<StoreObserver> observers;
  private final List<Product> products;

  public StoreImpl(String name) {
    if (name == null) {
      throw new IllegalArgumentException();
    }
    this.name = name;
    this.observers = new ArrayList<StoreObserver>();
    this.products = new ArrayList<Product>();
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void addObserver(StoreObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException();
    }
    observers.add(observer);
  }

  @Override
  public void removeObserver(StoreObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException();
    }
    observers.remove(observer);
  }

  @Override
  public List<Product> getProducts() {
    List<Product> temp = new ArrayList<>();
    for (Product product : products) {
      temp.add(product);
    }
    return temp;
  }

  @Override
  public Product createProduct(String name, double basePrice, int inventory) {
    if (name == null || basePrice <= 0 || inventory < 0) {
      throw new IllegalArgumentException();
    }
    Product product = new ProductImpl(name, basePrice, inventory);
    products.add(product);
    return product;
  }

  @Override
  public ReceiptItem purchaseProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    if (((ProductImpl) temp).getInventory() <= 0) {
      throw new OutOfStockException();
    }
    ((ProductImpl) temp).setInventory(-1);
    notifyObservers(new PurchaseEvent(temp, this));
    if (((ProductImpl) temp).getInventory() == 0) {
      notifyObservers(new OutOfStockEvent(product, this));
    }
    return new ReceiptItemImpl(temp.getName(), getSalePrice(temp), name);
  }

  @Override
  public void restockProduct(Product product, int numItems) {
    if (product == null || numItems < 0) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    if (((ProductImpl) temp).getInventory() == 0) {
      StoreEvent storeEvent = new BackInStockEvent(product, this);
      notifyObservers(storeEvent);
    }
    ((ProductImpl) temp).setInventory(numItems);
  }

  @Override
  public void startSale(Product product, double percentOff) {
    if (product == null || percentOff < 0 || percentOff > 1) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    ((ProductImpl) temp).setSale(percentOff);
    notifyObservers(new SaleStartEvent(temp, this));
  }

  @Override
  public void endSale(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    ((ProductImpl) temp).setSale(0);
    notifyObservers(new SaleEndEvent(temp, this));
  }

  @Override
  public int getProductInventory(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    int inventory = ((ProductImpl) temp).getInventory();
    return inventory;
  }

  @Override
  public boolean getIsInStock(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    return ((ProductImpl) temp).getInventory() > 0;
  }

  @Override
  public double getSalePrice(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    double salePrice =
        Math.round((temp.getBasePrice() * (1 - ((ProductImpl) temp).getSale())) * 100.0) / 100.0;
    return salePrice;
  }

  @Override
  public boolean getIsOnSale(Product product) {
    if (product == null) {
      throw new IllegalArgumentException();
    }
    if (!products.contains(product)) {
      throw new ProductNotFoundException();
    }
    Product temp = products.get(products.indexOf(product));
    return getSalePrice(temp) < temp.getBasePrice();
  }

  private void notifyObservers(StoreEvent e) {
    for (StoreObserver o : observers) {
      o.update(e);
    }
  }
}
