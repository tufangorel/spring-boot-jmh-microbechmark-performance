package com.company.customerinfo.service;


import com.company.customerinfo.model.Store;
import com.company.customerinfo.repository.StoreRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class StoreService {

    private final StoreRepository storeRepository;

    public StoreService(StoreRepository storeRepository) {
        this.storeRepository = storeRepository;
    }

    @Transactional
    public Store saveStoreWithProducts(Store store){
        return storeRepository.save(store);
    }

    @Transactional
    public List<Store> saveStoresWithProducts( List<Store> stores){
        return storeRepository.saveAll(stores);
    }

    public List<Store> findAll(){
        return storeRepository.findAll();
    }

}