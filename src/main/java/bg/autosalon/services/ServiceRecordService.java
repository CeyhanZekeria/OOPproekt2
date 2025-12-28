package bg.autosalon.services;

import bg.autosalon.dao.impl.ServiceRecordDao;
import bg.autosalon.entities.ServiceRecord;

import java.util.*;

public class ServiceRecordService {

    private final ServiceRecordDao serviceRecordDao = new ServiceRecordDao();

    public void addRecord(ServiceRecord record) {
        serviceRecordDao.save(record);
    }

    public void updateRecord(ServiceRecord record) {
        serviceRecordDao.update(record);
    }

    public ServiceRecord getRecord(Long id) {
        return serviceRecordDao.findById(id);
    }

    public List<ServiceRecord> getAllRecords() {
        return serviceRecordDao.findAll();
    }

    public void deleteRecord(Long id) {
        ServiceRecord record = serviceRecordDao.findById(id);
        if (record != null) {
            serviceRecordDao.delete(record);
        }
    }
}
