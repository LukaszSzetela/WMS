package pl.szetela.lukasz.WMS.queries;

public class ProductNativeQueries {
    public static final String FIND_ALL = "SELECT product_id, name, category, subcategory," +
            " number, price, annual_cost_stock, shortage_cost, delivery_time, reserved_number" +
            " FROM products";
    public static final String FIND_ONE = "SELECT product_id, name, category, subcategory," +
            " number, price, annual_cost_stock, shortage_cost, delivery_time, reserved_number" +
            " FROM products WHERE product_id = ?";

    private ProductNativeQueries() {
    }
}
