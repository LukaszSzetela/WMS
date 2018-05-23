package pl.szetela.lukasz.WMS.actions;

import org.springframework.stereotype.Component;
import pl.szetela.lukasz.WMS.dto.ProductLogDto;

import java.util.List;

@Component
public abstract class AbstractPrepareProductsAction {

    protected abstract List<ProductLogDto> getProductsLog();

    public List<ProductLogDto> execute() {
        return getProductsLog();
    }
}
