package model;

public class AddToCart {
    private String Item;
    private String Price;

    public AddToCart() {

    }

    public AddToCart(String item, String price) {
        Item = item;
        Price = price;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
