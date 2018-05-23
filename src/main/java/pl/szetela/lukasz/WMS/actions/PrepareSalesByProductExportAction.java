package pl.szetela.lukasz.WMS.actions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;
import pl.szetela.lukasz.WMS.services.ProductLogService;

import java.util.List;

@Component
public class PrepareSalesByProductExportAction extends AbstractPrepareProductsAction {

    private ProductLogService productLogService;
    private Long productId;

    @Autowired
    public PrepareSalesByProductExportAction(ProductLogService productLogService) {
        this.productLogService = productLogService;
    }

    @Override
    protected List<ProductLogDto> getProductsLog() {
        return productLogService.getAllByProductId(productId);
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
