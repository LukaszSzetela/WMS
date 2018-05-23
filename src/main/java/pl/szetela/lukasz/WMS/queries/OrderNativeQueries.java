package pl.szetela.lukasz.WMS.queries;

public class OrderNativeQueries {
    public static final String SAVE_ORDER = "INSERT INTO `orders` (`order_date`, `shipping_cost`, `status`, `sub_total`, `tax`, `tax_rate`," +
            "  `total_cost`, `orderer`) " +
            "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    public static final String FIND_ORDER_BY_ORDERER_ID = "SELECT order_id FROM orders WHERE order_date = (SELECT MAX(order_date) FROM orders WHERE orderer = ?)";
    public static final String SAVE_ORDER_PRODUCT = "INSERT INTO `order_product` (`order_id`, `product_id`, `number`, `ordinal`, `status`) VALUES(?, ?, ?, ?, ?)";
    public static final String FIND_ORDER_PRODUCT = "SELECT product_id, ordinal, number, status FROM order_product WHERE order_id = ?";
    public static final String UPDATE_ORDER = "UPDATE `orders` SET status = ?, executor = ?, picking_time = ?, current_realize_timestamp = ? WHERE order_id = ?";
    public static final String UPDATE_ORDER_PRODUCT = "UPDATE `order_product` SET status = ? WHERE order_id = ? AND product_id = ?";
    public static final String FIND_ORDERS_BY_STATUSES = "SELECT `order_id`, `order_date`, `shipping_cost`, `status`," +
            " `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer` FROM `orders` WHERE `status` IN";
    public static final String FIND_ORDERS_BY_DATE = "SELECT `order_id`, `order_date`, `shipping_cost`, `status`," +
            " `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer`, `picking_time`" +
            " FROM orders WHERE DATE(order_date) = DATE(?)";
    public static final String FIND_ORDERS_BY_DATES_IN_RANGE = "SELECT `order_id`, `order_date`, `shipping_cost`," +
            " `status`, `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer`, `picking_time`" +
            " FROM orders WHERE (DATE(order_date) BETWEEN DATE(?) AND DATE(?))";
    public static final String FIND_ORDERS_BY_MONTH = "SELECT `order_id`, `order_date`, `shipping_cost`, `status`," +
            " `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer`, `picking_time`" +
            " FROM orders WHERE MONTH(order_date) = MONTH(?)";
    public static final String FIND_ORDERS_BY_QUARTER = "SELECT `order_id`, `order_date`, `shipping_cost`, `status`," +
            " `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer`, `picking_time`" +
            " FROM orders WHERE QUARTER(order_date) = QUARTER(?)";
    public static final String FIND_ORDERS_BY_YEAR = "SELECT `order_id`, `order_date`, `shipping_cost`, `status`," +
            " `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer`, `picking_time`" +
            " FROM orders WHERE YEAR(order_date) = YEAR(?)";
    public static final String FIND_ORDERS_BY_EXECUTOR = "SELECT `order_id`, `order_date`, `shipping_cost`, `status`," +
            " `sub_total`, `tax`, `tax_rate`, `total_cost`, `executor`, `orderer`, `picking_time`" +
            " FROM orders WHERE executor = ?";


    private OrderNativeQueries() {
    }
}
