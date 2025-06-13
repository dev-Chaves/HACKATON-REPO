package com.hackaton.desafio;

import com.hackaton.desafio.entity.BenefitEntity;
import com.hackaton.desafio.entity.EnterpriseEntity;
import com.hackaton.desafio.entity.Enum.BenefitCategory;
import com.hackaton.desafio.repository.BenefitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
public class BenefityRepositoryTest {

    @Autowired
    private BenefitRepository benefitRepository;

    @Autowired
    private TestEntityManager entityManager;

    private EnterpriseEntity enterprise1 = new EnterpriseEntity();
    private EnterpriseEntity enterprise2 = new EnterpriseEntity();
    private BenefitEntity benefit1 = new BenefitEntity();
    private BenefitEntity benefit2 = new BenefitEntity();
    private BenefitEntity benefit3 = new BenefitEntity();

    @BeforeEach
    void setUp() {
        // Criando empresas
        enterprise1.setEnterprise("Empresa A");
        enterprise1.setCnpj("11111111111111");
        enterprise1.setCreatedAt(LocalDateTime.now());
        enterprise1 = entityManager.persistAndFlush(enterprise1);

        enterprise2.setEnterprise("Empresa B");
        enterprise2.setCnpj("22222222222222");
        enterprise2.setCreatedAt(LocalDateTime.now());
        enterprise2 = entityManager.persistAndFlush(enterprise2);

        benefit1.setDescription("Vale Alimentação");
        benefit1.setCategory(BenefitCategory.MEAL);
        benefit1.setSupplierEnterprise(enterprise1);
        benefit1.setCreatedAt(LocalDateTime.now());
        benefit1 = entityManager.persistAndFlush(benefit1);

        benefit2.setDescription("Desconto Farmácia");
        benefit2.setCategory(BenefitCategory.DRUGSTORE);
        benefit2.setSupplierEnterprise(enterprise1);
        benefit2.setCreatedAt(LocalDateTime.now());
        benefit2 = entityManager.persistAndFlush(benefit2);

        benefit3.setDescription("Vale Refeição");
        benefit3.setCategory(BenefitCategory.MEAL);
        benefit3.setSupplierEnterprise(enterprise2);
        benefit3.setCreatedAt(LocalDateTime.now());
        benefit3 = entityManager.persistAndFlush(benefit3);

        entityManager.clear();
    }

    @Test
    @DisplayName("Buscar benefícios por ID da empresa")
    void findBenefitsByEnterpriseId() {
        List<BenefitEntity> benefits = benefitRepository.findByEnterpriseId(enterprise1.getId());

        System.out.println("Benefícios encontrados: " + benefits.stream().map(Object::toString));

        assertThat(benefits).hasSize(2);

        assertThat(benefits).extracting(BenefitEntity::getDescription)
                .containsExactlyInAnyOrder("Vale Alimentação", "Desconto Farmácia");

        assertThat(benefits).allMatch(b -> b.getSupplierEnterprise().getId().equals(enterprise1.getId()));
    }
}


