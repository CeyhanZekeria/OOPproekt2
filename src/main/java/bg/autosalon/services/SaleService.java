package bg.autosalon.services;

import bg.autosalon.dao.impl.SaleDao;
import bg.autosalon.entities.Sale;

import java.util.*;

public class SaleService {

    private final SaleDao saleDao = new SaleDao();

    public void addSale(Sale sale) {
        saleDao.save(sale);
    }

    public void updateSale(Sale sale) {
        saleDao.update(sale);
    }

    public Sale getSale(Long id) {
        return saleDao.findById(id);
    }

    public List<Sale> getAllSales() {
        return saleDao.findAll();
    }

    public void deleteSale(Long id) {
        Sale sale = saleDao.findById(id);
        if (sale != null) {
            saleDao.delete(sale);
        }
    }
}
