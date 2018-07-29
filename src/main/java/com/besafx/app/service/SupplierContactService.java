package com.besafx.app.service;

import com.besafx.app.entity.SupplierContact;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface SupplierContactService extends PagingAndSortingRepository<SupplierContact, Long>, JpaSpecificationExecutor<SupplierContact> {

    List<SupplierContact> findBySupplierId(Long id);
}
