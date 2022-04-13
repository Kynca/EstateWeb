package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.RealEstate;
import by.kynca.estateWeb.entity.RealEstateType;
import by.kynca.estateWeb.repository.RealEstateRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RealEstateService implements ServiceActions<RealEstate> {

    private static final Integer PAGE_SIZE = 9;

    private final RealEstateRepo realEstateRepo;

    @Autowired
    public RealEstateService(RealEstateRepo realEstateRepo) {
        this.realEstateRepo = realEstateRepo;
    }

    @Transactional
    public RealEstate save(RealEstate realEstate) {
        if (realEstate.getRealEstateId() != null && realEstateRepo.findById(realEstate.getRealEstateId()).orElse(null) == null) {
            return null;
        }
        return realEstateRepo.save(realEstate);
    }

    @Override
    public List<RealEstate> findAll(int pageNum, String sort) {
        Pageable page = PageRequest.of(pageNum, PAGE_SIZE, sortDefine(sort));
        return realEstateRepo.findAll(page).getContent();
    }

    @Override
    public void deleteById(Long id) {
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

        Pageable page = PageRequest.of(pageNum, PAGE_SIZE, sortDefine(sort));
        return realEstateRepo.findRealEstatesByRealEstateType(estateType, page).getContent();
    }

    public long getAllPagesAmount() {
        return (realEstateRepo.count() - 1) / 9;
    }

    public long getByTypePagesAmount(RealEstateType type) {
        return (realEstateRepo.countByRealEstateType(type) - 1) / 9;
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
