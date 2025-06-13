package com.hackaton.desafio.services;

import com.hackaton.desafio.dto.benefitDTO.BenefitResponse;
import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.entity.PartnershipEntity;
import com.hackaton.desafio.entity.UserEntity;
import com.hackaton.desafio.repository.BenefitRepository;
import com.hackaton.desafio.repository.EnterpriseRepository;
import com.hackaton.desafio.repository.PartnershipRepository;
import com.hackaton.desafio.repository.UserRepository;
import com.hackaton.desafio.util.AuthUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Objects;

@Service
public class BenefitService {

    private final BenefitRepository benefitRepository;
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AuthUtil authUtil;
    private final PartnershipRepository partnershipRepository;

    public BenefitService(BenefitRepository benefitRepository, UserRepository userRepository, EnterpriseRepository enterpriseRepository, AuthUtil authUtil, PartnershipRepository partnershipRepository) {
        this.benefitRepository = benefitRepository;
        this.userRepository = userRepository;
        this.enterpriseRepository = enterpriseRepository;
        this.authUtil = authUtil;
        this.partnershipRepository = partnershipRepository;
    }

    public ResponseEntity<?> getBenefitsByEnterprise() {

        UserEntity user = AuthUtil.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

        EnterpriseEntity enterprise = user.getEnterprise();

        List<BenefitEntity> benefits = benefitRepository.findByEnterpriseId(user.getEnterprise().getId());

        List<BenefitResponse> response = benefits.stream()
                .map(b -> new BenefitResponse(b.getId(), b.getDescription(), b.getSupplierEnterprise().getEnterprise(), b.getCategory()))
                .toList();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getBenefitOfPartneship(){

        UserEntity user = AuthUtil.getAuthenticatedUser()
                .orElseThrow(() -> new RuntimeException("User not authenticated"));

         EnterpriseEntity userEnterprise = enterpriseRepository.findById(user.getEnterprise().getId()).orElseThrow(()-> new RuntimeException("Enterprise not found"));

        List<PartnershipEntity> partnerships = partnershipRepository.findByEnterpriseId(user.getEnterprise().getId());

        List<Long> partnershipIds = partnerships.stream().map(p ->{
             if (Objects.equals(p.getConsumerEnterprise().getId(), user.getEnterprise().getId())){
                 return p.getSupplierEnterprise().getId();
             }else {
                 return p.getConsumerEnterprise().getId();
             }
         }).distinct().toList();

         List<BenefitEntity> benefits = benefitRepository.findBySupplierEnterpriseIdIn(partnershipIds);

         List<BenefitResponse> responses = benefits.stream().map( b -> new BenefitResponse(b.getId(),b.getDescription(),b.getSupplierEnterprise().getEnterprise(), b.getCategory())).toList();

         return ResponseEntity.ok(responses);


    }

    public ResponseEntity<List<BenefitResponse>> getBenefitsByCategory(String category) {
        try {

            BenefitCategory cat = BenefitCategory.valueOf(category.toUpperCase());

            UserEntity user = AuthUtil.getAuthenticatedUser().orElseThrow(()-> new UsernameNotFoundException("User not found"));

            EnterpriseEntity enterprise = user.getEnterprise();

            var partnerships = partnershipRepository.findByEnterpriseId(enterprise.getId());

            List<EnterpriseEntity> partnerEnterprises = partnerships.stream()
                    .map(partnership -> {
                        if (Objects.equals(partnership.getConsumerEnterprise().getId(), enterprise.getId())) {
                            return partnership.getSupplierEnterprise();
                        } else {
                            return partnership.getConsumerEnterprise();
                        }
                    })
                    .distinct()
                    .toList();



            List<BenefitEntity> benefits = benefitRepository.findByCategoryAndSupplierEnterpriseIn(cat, partnerEnterprises);


            System.out.println("benefits: " + benefits);

            List<BenefitResponse> responses = benefits.stream()
                    .map(b -> new BenefitResponse(
                            b.getId(),
                            b.getDescription(),
                            b.getSupplierEnterprise().getEnterprise(),
                            b.getCategory()
                    )).toList();

            return ResponseEntity.ok(responses);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(List.of());
        }
    }

}
