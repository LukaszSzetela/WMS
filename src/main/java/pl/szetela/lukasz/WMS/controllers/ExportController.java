package pl.szetela.lukasz.WMS.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.szetela.lukasz.WMS.actions.*;
import pl.szetela.lukasz.WMS.dto.OrderDto;
import pl.szetela.lukasz.WMS.models.Customer;
import pl.szetela.lukasz.WMS.models.Product;
import pl.szetela.lukasz.WMS.models.User;
import pl.szetela.lukasz.WMS.services.ExportService;
import pl.szetela.lukasz.WMS.services.OrderService;
import pl.szetela.lukasz.WMS.services.ProductService;
import pl.szetela.lukasz.WMS.services.UserService;
import pl.szetela.lukasz.WMS.utils.StringUtils;
import pl.szetela.lukasz.WMS.views.OrdersExportView;
import pl.szetela.lukasz.WMS.views.InvoiceView;
import pl.szetela.lukasz.WMS.views.ProductsExportView;
import pl.szetela.lukasz.WMS.views.ProductsLogExportView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.Date;

@Controller
public class ExportController {

    private static final String STATUSES_TO_PRINT = "statusesToPrint";
    private static final String DATES_TO_EXPORT_ORDERS = "datesToExportOrders";
    private static final String DATES_TO_EXPORT_PRODUCTS = "datesToExportProducts";
    private static final String USER_TO_EXPORT_ORDERS = "userToExportOrders";
    private static final String PRODUCT_TO_EXPORT_ORDERS = "productToExportOrders";
    private OrderService orderService;
    private UserService userService;
    private ExportService exportService;
    private ProductService productService;
    private PrepareOrdersToExportAction prepareOrdersToExportAction;
    private PrepareOrdersPerCurrentDayExportAction prepareOrdersPerCurrentDayExportAction;
    private PrepareOrdersPerCurrentWeekExportAction prepareOrdersPerCurrentWeekExportAction;
    private PrepareOrdersPerCurrentMonthExportAction prepareOrdersPerCurrentMonthExportAction;
    private PrepareOrdersPerCurrentQuarterExportAction prepareOrdersPerCurrentQuarterExportAction;
    private PrepareOrdersPerCurrentYearExportAction prepareOrdersPerCurrentYearExportAction;
    private PrepareOrdersByDatesInRangeExportAction prepareOrdersByDatesInRangeExportAction;
    private PrepareOrdersByUserExportAction prepareOrdersByUserExportAction;
    private PrepareProductsShortagesExportAction prepareProductsShortagesExportAction;
    private PrepareProductsByDatesInRangeExportAction prepareProductsByDatesInRangeExportAction;
    private PrepareSalesByProductExportAction prepareSalesByProductExportAction;


    @Autowired
    public ExportController(OrderService orderService, UserService userService, ExportService exportService,
                            ProductService productService,
                            PrepareOrdersToExportAction prepareOrdersToExportAction,
                            PrepareOrdersPerCurrentDayExportAction prepareOrdersPerCurrentDayExportAction,
                            PrepareOrdersPerCurrentWeekExportAction prepareOrdersPerCurrentWeekExportAction,
                            PrepareOrdersPerCurrentMonthExportAction prepareOrdersPerCurrentMonthExportAction,
                            PrepareOrdersPerCurrentQuarterExportAction prepareOrdersPerCurrentQuarterExportAction,
                            PrepareOrdersPerCurrentYearExportAction prepareOrdersPerCurrentYearExportAction,
                            PrepareOrdersByDatesInRangeExportAction prepareOrdersByDatesInRangeExportAction,
                            PrepareOrdersByUserExportAction prepareOrdersByUserExportAction,
                            PrepareProductsShortagesExportAction prepareProductsShortagesExportAction,
                            PrepareProductsByDatesInRangeExportAction prepareProductsByDatesInRangeExportAction,
                            PrepareSalesByProductExportAction prepareSalesByProductExportAction) {
        this.orderService = orderService;
        this.userService = userService;
        this.exportService = exportService;
        this.productService = productService;
        this.prepareOrdersToExportAction = prepareOrdersToExportAction;
        this.prepareOrdersPerCurrentDayExportAction = prepareOrdersPerCurrentDayExportAction;
        this.prepareOrdersPerCurrentWeekExportAction = prepareOrdersPerCurrentWeekExportAction;
        this.prepareOrdersPerCurrentMonthExportAction = prepareOrdersPerCurrentMonthExportAction;
        this.prepareOrdersPerCurrentQuarterExportAction = prepareOrdersPerCurrentQuarterExportAction;
        this.prepareOrdersPerCurrentYearExportAction = prepareOrdersPerCurrentYearExportAction;
        this.prepareOrdersByDatesInRangeExportAction = prepareOrdersByDatesInRangeExportAction;
        this.prepareOrdersByUserExportAction = prepareOrdersByUserExportAction;
        this.prepareProductsShortagesExportAction = prepareProductsShortagesExportAction;
        this.prepareProductsByDatesInRangeExportAction = prepareProductsByDatesInRangeExportAction;
        this.prepareSalesByProductExportAction = prepareSalesByProductExportAction;
    }

    @RequestMapping(value = "/invoice/{orderId}", method = RequestMethod.GET)
    public ModelAndView printInvoice(@PathVariable("orderId") Long orderId) {

        OrderDto order = orderService.getOrderDto(orderId);
        Customer customer = userService.getCustomerByUserId(order.getOrdererId());

        return new ModelAndView(new InvoiceView(order, customer));
    }

    @RequestMapping(value = "/ordersByStatuses", method = RequestMethod.GET)
    public ModelAndView exportOrdersByStatuses(HttpServletRequest request) {
        String statuses = (String) request.getSession().getAttribute(STATUSES_TO_PRINT);
        prepareOrdersToExportAction.setStatuses(statuses);
        return new ModelAndView(new OrdersExportView(prepareOrdersToExportAction.execute(), "report_orders_by_statuses"));
    }

    @GetMapping(value = "/catchStatuses", params = {"statuses"})
    public ResponseEntity<Boolean> catchStatuses(@RequestParam("statuses") String statuses, HttpServletRequest request) {
        request.getSession().setAttribute(STATUSES_TO_PRINT, statuses);
        return ResponseEntity.ok(request.getSession().getAttribute(STATUSES_TO_PRINT) != null);
    }

    @RequestMapping(value = "/ordersPerCurrentDay", method = RequestMethod.GET)
    public ModelAndView exportOrdersPerCurrentDay() {
        prepareOrdersPerCurrentDayExportAction.setDate(LocalDate.now().toString());
        return new ModelAndView(new OrdersExportView(prepareOrdersPerCurrentDayExportAction.execute(), "report_orders_per_current_day"));
    }

    @RequestMapping(value = "/ordersPerCurrentWeek", method = RequestMethod.GET)
    public ModelAndView exportOrdersPerCurrentWeek() {
        prepareOrdersPerCurrentWeekExportAction.setDates(StringUtils.getDate(LocalDate.now()));
        return new ModelAndView(new OrdersExportView(prepareOrdersPerCurrentWeekExportAction.execute(), "report_orders_per_current_week"));
    }

    @RequestMapping(value = "/ordersPerCurrentMonth", method = RequestMethod.GET)
    public ModelAndView exportOrdersPerCurrentMonth() {
        prepareOrdersPerCurrentMonthExportAction.setDate(LocalDate.now().toString());
        return new ModelAndView(new OrdersExportView(prepareOrdersPerCurrentMonthExportAction.execute(), "report_orders_per_current_month"));
    }

    @RequestMapping(value = "/ordersPerCurrentQuarter", method = RequestMethod.GET)
    public ModelAndView exportOrdersPerCurrentQuarter() {
        prepareOrdersPerCurrentQuarterExportAction.setDate(LocalDate.now().toString());
        return new ModelAndView(new OrdersExportView(prepareOrdersPerCurrentQuarterExportAction.execute(), "report_orders_per_current_quarter"));
    }

    @RequestMapping(value = "/ordersPerCurrentYear", method = RequestMethod.GET)
    public ModelAndView exportOrdersPerCurrentYear() {
        prepareOrdersPerCurrentYearExportAction.setDate(LocalDate.now().toString());
        return new ModelAndView(new OrdersExportView(prepareOrdersPerCurrentYearExportAction.execute(), "report_orders_per_current_year"));
    }

    @GetMapping(value = "/catchDatesToReport", params = {"dateFrom", "dateTo"})
    public ResponseEntity<Boolean> catchDatesToReport(@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo, HttpServletRequest request) {
        String[] dates = exportService.prepareDates(dateFrom, dateTo);
        request.getSession().setAttribute(DATES_TO_EXPORT_ORDERS, dates);
        return ResponseEntity.ok(request.getSession().getAttribute(DATES_TO_EXPORT_ORDERS) != null);
    }

    @RequestMapping(value = "/ordersByDatesInRange", method = RequestMethod.GET)
    public ModelAndView exportOrdersByDatesInRange(HttpServletRequest request) {
        String[] dates = (String[]) request.getSession().getAttribute(DATES_TO_EXPORT_ORDERS);
        prepareOrdersByDatesInRangeExportAction.setDates(dates);
        return new ModelAndView(new OrdersExportView(prepareOrdersByDatesInRangeExportAction.execute(), "report_orders_by_dates_in_range"));
    }

    @GetMapping(value = "/catchUserToReport", params = {"user"})
    public ResponseEntity<Boolean> catchUserToReport(@RequestParam("user") String user, HttpServletRequest request) {
        String[] names = user.split(" ");
        User userToExportOrders = userService.getUserByFirstNameAndLastName(names[0], names[1]);
        request.getSession().setAttribute(USER_TO_EXPORT_ORDERS, userToExportOrders);
        return ResponseEntity.ok(request.getSession().getAttribute(USER_TO_EXPORT_ORDERS) != null);
    }

    @RequestMapping(value = "/ordersByUser", method = RequestMethod.GET)
    public ModelAndView exportOrdersByUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_TO_EXPORT_ORDERS);
        prepareOrdersByUserExportAction.setExecutorId(user.getId());
        return new ModelAndView(new OrdersExportView(prepareOrdersByUserExportAction.execute(), "report_orders_by_user"));
    }

    @RequestMapping(value = "/shortagesReport", method = RequestMethod.GET)
    public ModelAndView exportShortages() {
        return new ModelAndView(new ProductsExportView(prepareProductsShortagesExportAction.execute(), "shortages_report"));
    }

    @GetMapping(value = "/catchDatesToSalesReport", params = {"dateFrom", "dateTo"})
    public ResponseEntity<Boolean> catchDatesToSalesReport(@RequestParam("dateFrom") Date dateFrom, @RequestParam("dateTo") Date dateTo, HttpServletRequest request) {
        String[] dates = exportService.prepareDates(dateFrom, dateTo);
        request.getSession().setAttribute(DATES_TO_EXPORT_PRODUCTS, dates);
        return ResponseEntity.ok(request.getSession().getAttribute(DATES_TO_EXPORT_PRODUCTS) != null);
    }

    @RequestMapping(value = "/productsByDatesInRange", method = RequestMethod.GET)
    public ModelAndView exportProductsByDatesInRange(HttpServletRequest request) {
        String[] dates = (String[]) request.getSession().getAttribute(DATES_TO_EXPORT_PRODUCTS);
        prepareProductsByDatesInRangeExportAction.setDates(dates);
        return new ModelAndView(new ProductsLogExportView(prepareProductsByDatesInRangeExportAction.execute(), "report_products_by_dates_in_range"));
    }

    @GetMapping(value = "/catchProductToReport", params = {"product"})
    public ResponseEntity<Boolean> catchProductToReport(@RequestParam("product") String product, HttpServletRequest request) {
        Product productToExport = productService.getByName(product);
        request.getSession().setAttribute(PRODUCT_TO_EXPORT_ORDERS, productToExport);
        return ResponseEntity.ok(request.getSession().getAttribute(PRODUCT_TO_EXPORT_ORDERS) != null);
    }

    @RequestMapping(value = "/salesByProduct", method = RequestMethod.GET)
    public ModelAndView exportSalesByProduct(HttpServletRequest request) {
        Product product = (Product) request.getSession().getAttribute(PRODUCT_TO_EXPORT_ORDERS);
        prepareSalesByProductExportAction.setProductId(product.getProductId());
        return new ModelAndView(new ProductsLogExportView(prepareSalesByProductExportAction.execute(), "report_sales_by_product"));
    }
}
