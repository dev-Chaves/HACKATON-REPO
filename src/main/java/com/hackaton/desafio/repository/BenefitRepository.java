package com.hackaton.desafio.repository;

import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenefitRepository extends JpaRepository<BenefitEntity, Long> {

    @Query(value = "SELECT * FROM tb_benefit WHERE supplier_enterprise_id = :enterpriseId", nativeQuery = true)
    List<BenefitEntity> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

    @Query(value = "SELECT * FROM tb_benefit WHERE supplier_enterprise_id IN :enterpriseIds", nativeQuery = true)
    List<BenefitEntity> findBySupplierEnterpriseIdIn(@Param("enterpriseIds") List<Long> enterpriseIds);

    List<BenefitEntity> findByCategoryAndSupplierEnterpriseIn(BenefitCategory category, List<EnterpriseEntity> enterprises);


}
