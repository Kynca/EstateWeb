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

/**
 * service for work with RealEstates
 */
@Service
public class RealEstateService implements ServiceActions<RealEstate> {

    @Value("${page.size.estate}")
    private Integer pageSize;

    private final RealEstateRepo realEstateRepo;

    @Autowired
    public RealEstateService(RealEstateRepo realEstateRepo) {
        this.realEstateRepo = realEstateRepo;
    }

    /**
     * method for saving user in db
     * @param realEstate for save
     * @return realEstate, if it doesn't meet the conditions return null
     */
    @Transactional
    @Override
    public RealEstate save(RealEstate realEstate) {
        if (realEstate.getRealEstateId() != null && !realEstateRepo.existsById(realEstate.getRealEstateId())) {
            return null;
        }
        return realEstateRepo.save(realEstate);
    }

    /**
     * method for finding real estates with pagination
     * @param pageNum num of page
     * @param sort type of sort
     * @return paginated list of estates
     */
    @Override
    public List<RealEstate> findAll(int pageNum, String sort) {
        Pageable page = PageRequest.of(pageNum, pageSize, sortDefine(sort));
        return realEstateRepo.findAll(page).getContent();
    }

    /**
     * method for deleting estates from db(soft deleting set isDeleted field to 1)
     * @param id of estate for delete
     */
    public void delete(Long id) {
        if (realEstateRepo.existsById(id)) {
            realEstateRepo.deleteById(id);
        }
    }

    /**
     * find real estate by id
     * @param id of searched estate
     * @return estate if it was found, if not return null
     */
    @Override
    public RealEstate findById(Long id) {
        return realEstateRepo.findById(id).orElse(null);
    }

    /**
     * find paginated estates by it type
     * @param type of estates(house, land or flat)
     * @param pageNum num of the page
     * @param sort type of sort
     * @return paginated list of estates
     */
    public List<RealEstate> findAllByType(String type, int pageNum, String sort) {
        RealEstateType estateType = RealEstateType.getByCode(type.toUpperCase());

        if (estateType == null) {
            return null;
        }

        Pageable page = PageRequest.of(pageNum, pageSize, sortDefine(sort));
        return realEstateRepo.findRealEstatesByRealEstateType(estateType, page).getContent();
    }

    /**
     * @return amount of estate pages
     */
    public int getAllPagesAmount() {
        return (int) (realEstateRepo.count() - 1) / pageSize;
    }

    /**
     * @param type type of estate
     * @return amount of certain estates pages
     */
    public int getByTypePagesAmount(RealEstateType type) {
        return (realEstateRepo.countByRealEstateType(type) - 1) / pageSize;
    }

    /**
     * @param client authorized client
     * @return list client's estates
     */
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
