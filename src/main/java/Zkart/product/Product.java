package Zkart.product;

public class Product {

    private long id;
    private String category;
    private String brand;
    private String model;
    private int stock;
    private double price;
    private double discountedPrice;

    public Product(String category, String brand, String model, double price, int stock) {
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.price = price;
        this.stock = stock;
    }
    // Except discounted Price
    public Product(long id, String category, String brand, String model, int stock, double price) {
        this.id = id;
        this.category = category;
        this.brand = brand;
        this.model = model;
        this.stock = stock;
        this.price = price;
    }
    // Getters and Setters


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

}
