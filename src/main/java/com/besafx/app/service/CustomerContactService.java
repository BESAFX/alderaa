package com.besafx.app.service;

import com.besafx.app.entity.CustomerContact;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface CustomerContactService extends PagingAndSortingRepository<CustomerContact, Long>, JpaSpecificationExecutor<CustomerContact> {

    List<CustomerContact> findByCustomerId(Long id);
}
