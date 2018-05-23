package pl.szetela.lukasz.WMS.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;
import pl.szetela.lukasz.WMS.services.ProductLogService;

import java.util.List;

@Component
public class PrepareProductsByDatesInRangeExportAction extends AbstractPrepareProductsAction {

    private ProductLogService productLogService;
    private String[] dates;

    @Autowired
    public PrepareProductsByDatesInRangeExportAction(ProductLogService productLogService) {
        this.productLogService = productLogService;
    }

    @Override
    protected List<ProductLogDto> getProductsLog() {
        return productLogService.getAll(dates[0], dates[1]);
    }

    public void setDates(String[] dates) {
        this.dates = dates;
    }
}
