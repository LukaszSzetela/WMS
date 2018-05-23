package pl.szetela.lukasz.WMS.services;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Service
public class ExportService {

    public String[] prepareDates(Date dateFrom, Date dateTo) {
        String from = dateFrom.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        String to = dateTo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
        return new String[]{from, to};
    }
}
