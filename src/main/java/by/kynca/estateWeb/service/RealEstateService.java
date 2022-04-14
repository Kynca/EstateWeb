package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.RealEstate;
import by.kynca.estateWeb.entity.RealEstateType;
import by.kynca.estateWeb.repository.RealEstateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RealEstateService implements ServiceActions<RealEstate> {

    @Value("${page.size.estate}")
    private Integer pageSize;

    private final RealEstateRepo realEstateRepo;

    @Autowired
    public RealEstateService(RealEstateRepo realEstateRepo) {
        this.realEstateRepo = realEstateRepo;
    }

    @Transactional
    public RealEstate save(RealEstate realEstate) {
        if (realEstate.getRealEstateId() != null && !realEstateRepo.existsById(realEstate.getRealEstateId())) {
            return null;
        }
        return realEstateRepo.save(realEstate);
    }

    @Override
    public List<RealEstate> findAll(int pageNum, String sort) {
        Pageable page = PageRequest.of(pageNum, pageSize, sortDefine(sort));
        return realEstateRepo.findAll(page).getContent();
    }

    public void delete(Long id) {
        if (realEstateRepo.existsById(id)) {
            realEstateRepo.deleteById(id);
        }
    }

    @Override
    public RealEstate findById(Long id) {
        return realEstateRepo.findById(id).orElse(null);
    }

    public List<RealEstate> findAllByType(String type, int pageNum, String sort) {
        RealEstateType estateType = RealEstateType.getByCode(type.toUpperCase());

        if (estateType == null) {
            return null;
        }

        Pageable page = PageRequest.of(pageNum, pageSize, sortDefine(sort));
        return realEstateRepo.findRealEstatesByRealEstateType(estateType, page).getContent();
    }

    public long getAllPagesAmount() {
        return (realEstateRepo.count() - 1) / pageSize;
    }

    public long getByTypePagesAmount(RealEstateType type) {
        return (realEstateRepo.countByRealEstateType(type) - 1) / pageSize;
    }

    public List<RealEstate> findClientEstates(Client client) {
        return realEstateRepo.findRealEstatesByClientClientId(client.getClientId());
    }

    private Sort sortDefine(String sort) {
        return switch (sort.toUpperCase()) {
            case "PRICE" -> Sort.by("price");
            case "AREA" -> Sort.by("area");
            case "FLAT.ROOMS" -> Sort.by("flat.rooms");
            default -> Sort.by("realEstateId").descending();
        };
    }


}
