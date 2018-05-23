package pl.szetela.lukasz.WMS.queries;

public class ProductLogNativeQueries {

    public static final String FIND_DISTINCT_ALL = "SELECT MAX(product_id) 'product_id'," +
            " CEIL(sum(number)/12) 'number'," +
            " CEIL((((SQRT(MAX(delivery_time))) * (STDDEV(number))) * (1.644853627))) 'ZB'," +
            " CEIL(SUM(number)/100 * MAX(delivery_time) + CEIL((((SQRT(MAX(delivery_time))) * (STDDEV(number))) * (1.644853627)))) 'ZI'" +
            " FROM products_log GROUP BY product_id";
    public static final String FIND_ONE = "SELECT MAX(product_id) 'product_id'," +
            " CEIL(sum(number)/12) 'number'," +
            " CEIL((((SQRT(MAX(delivery_time))) * (STDDEV(number))) * (1.644853627))) 'ZB'," +
            " CEIL(SUM(number)/100 * MAX(delivery_time) + CEIL((((SQRT(MAX(delivery_time))) * (STDDEV(number))) * (1.644853627)))) 'ZI'" +
            " FROM products_log GROUP BY product_id Having product_id = ?";
    public static final String FIND_ALL_BETWEEN_DATES = "SELECT MAX(product_id) 'product_id', sum(number) 'demand'" +
            " from products_log where date  >= ? and date <= ? group by product_id";
    public static final String FIND_ONE_BETWEEN_DATES = "SELECT MAX(product_id) 'product_id', sum(number) 'demand' from products_log where date  >= ? and date <= ? group by product_id having product_id = ?";
    public static final String SAVE_PRODUCT_LOG = "INSERT INTO `products_log` (`name`, `price`, `shortage_cost`, `annual_cost_stock`, `delivery_time`," +
            "  `number`, `date`, `category`, `subcategory`, `product_id`) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String FIND_ALL = "SELECT `product_log_id`, `category`, `date`, `name`, `number`, `price`," +
            " `subcategory`, `product_id` FROM products_log WHERE (DATE(date) BETWEEN DATE(?) AND DATE(?))";
    public static final String FIND_ALL_By_PRODUCT_ID = "SELECT `product_log_id`, `category`, `date`, `name`, `number`, `price`," +
            " `subcategory`, `product_id` FROM products_log WHERE product_id = ?";

    private ProductLogNativeQueries() {
    }
}
