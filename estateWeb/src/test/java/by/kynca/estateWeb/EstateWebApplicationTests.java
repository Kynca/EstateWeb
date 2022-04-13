package by.kynca.estateWeb;

import by.kynca.estateWeb.entity.Address;
import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.RealEstate;
import by.kynca.estateWeb.entity.RealEstateType;
import by.kynca.estateWeb.repository.RealEstateRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class EstateWebApplicationTests {

	private final RealEstateRepo realEstateRepo;

	@Autowired
	public EstateWebApplicationTests(RealEstateRepo realEstateRepo) {
		this.realEstateRepo = realEstateRepo;
	}



}
