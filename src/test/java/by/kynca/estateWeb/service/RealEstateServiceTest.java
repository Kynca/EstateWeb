package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Address;
import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.RealEstate;
import by.kynca.estateWeb.entity.RealEstateType;
import by.kynca.estateWeb.repository.RealEstateRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class RealEstateServiceTest {

    private final RealEstateService estateService;

    @Autowired
    public RealEstateServiceTest(RealEstateService estateService) {
        this.estateService = estateService;
    }

    @MockBean
    private RealEstateRepo repo;

    @Test
    void save() {
        RealEstate realEstate = new RealEstate(1L, 100.0, 10.0, "add", true,
                RealEstateType.FLAT, false, new Address(), new Client(), null);
        estateService.save(realEstate);
        when(repo.findById(realEstate.getRealEstateId())).thenReturn(null);
        when(repo.save(realEstate)).thenReturn(realEstate);
        verify(repo, times(0)).save(realEstate);


        realEstate.setRealEstateId(null);
        Assertions.assertEquals(realEstate, estateService.save(realEstate));
        verify(repo, times(1)).save(realEstate);
    }

    @Test
    void deleteById() {
        RealEstate realEstate = new RealEstate();
        realEstate.setRealEstateId(1L);
        when(repo.existsById(realEstate.getRealEstateId())).thenReturn(true);

        estateService.delete(realEstate.getRealEstateId());
        verify(repo, times(1)).existsById(realEstate.getRealEstateId());
        verify(repo, times(1)).deleteById(realEstate.getRealEstateId());
    }

    @Test
    void findById() {
        estateService.findById(1L);
        verify(repo, times(1)).findById(1L);
    }

    @Test
    void getAllPagesAmount() {
        when(repo.count()).thenReturn(12L);
        Assertions.assertEquals(1, estateService.getAllPagesAmount());
    }

    @Test
    void getByTypePagesAmount() {
        when(repo.countByRealEstateType(RealEstateType.FLAT)).thenReturn(12);
        Assertions.assertEquals(1, estateService.getByTypePagesAmount(RealEstateType.FLAT));
    }
}