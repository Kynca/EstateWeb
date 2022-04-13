package by.kynca.estateWeb.repository;

import by.kynca.estateWeb.entity.RealEstate;
import by.kynca.estateWeb.entity.RealEstateType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RealEstateRepo extends JpaRepository<RealEstate, Long> {
    Page<RealEstate> findRealEstatesByRealEstateType(RealEstateType realEstateType, Pageable pageable);

    long countByRealEstateType(RealEstateType realEstateType);

    List<RealEstate> findRealEstatesByClientClientId(Long clientId);
}
