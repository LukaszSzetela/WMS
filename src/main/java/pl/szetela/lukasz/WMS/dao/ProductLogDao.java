package pl.szetela.lukasz.WMS.dao;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import pl.szetela.lukasz.WMS.dto.ProductDto;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;
import pl.szetela.lukasz.WMS.mappers.ProductDtoInDateRangeRowMapper;
import pl.szetela.lukasz.WMS.mappers.ProductDtoRowMapper;
import pl.szetela.lukasz.WMS.mappers.ProductLogDtoMapper;
import pl.szetela.lukasz.WMS.models.ProductLog;
import java.util.List;
import static pl.szetela.lukasz.WMS.queries.ProductLogNativeQueries.*;

@Repository
public class ProductLogDao {

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(ProductLogDao.class);
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductLogDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public ProductDto findById(Long id) {
        ProductDto productDto = new ProductDto();
        try {
            productDto = jdbcTemplate.queryForObject(FIND_ONE, new Object[]{id},
                    new BeanPropertyRowMapper<>(ProductDto.class));
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No past data for product: " + id);
        }
        return productDto;
    }

    public void save(ProductLog productLog) {
        jdbcTemplate.update(SAVE_PRODUCT_LOG, productLog.getName(), productLog.getPrice(), productLog.getShortageCost(),
                productLog.getAnnualCostStock(), productLog.getDeliveryTime(), productLog.getNumber(), productLog.getDate(),
                productLog.getCategory(), productLog.getSubcategory(), productLog.getProduct().getProductId());
    }

    public List<ProductDto> findDistinctAll() {
        return jdbcTemplate.query(FIND_DISTINCT_ALL, new ProductDtoRowMapper());
    }

    public ProductDto findOneBetweenDates(String dateFrom, String dateTo, Long id) {
        ProductDto productDto = new ProductDto();
        try {
            productDto = jdbcTemplate.queryForObject(FIND_ONE_BETWEEN_DATES, new Object[]{dateFrom, dateTo, id}, new BeanPropertyRowMapper<>(ProductDto.class));
        } catch (EmptyResultDataAccessException e) {
            logger.warn("No past data for product: " + id);
        }
        return productDto;
    }


    public List<ProductDto> findDistinctBetweenDates(String dateFrom, String dateTo) {
        return jdbcTemplate.query(FIND_ALL_BETWEEN_DATES, new Object[]{dateFrom, dateTo}, new ProductDtoInDateRangeRowMapper());
    }

    public List<ProductLogDto> findAll(String dateFrom, String dateTo) {
        return jdbcTemplate.query(FIND_ALL, new Object[]{dateFrom, dateTo}, new ProductLogDtoMapper());
    }

    public List<ProductLogDto> findAllByProductId(Long productId) {
        return jdbcTemplate.query(FIND_ALL_By_PRODUCT_ID, new Object[]{productId}, new ProductLogDtoMapper());
    }
}
