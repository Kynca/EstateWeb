package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.Role;
import by.kynca.estateWeb.repository.ClientRepo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
class ClientServiceTest {

    private final ClientService clientService;

    @Autowired
    public ClientServiceTest(ClientService clientService) {
        this.clientService = clientService;
    }

    @MockBean
    private ClientRepo clientRepo;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    public void signUp() {
        Client client = new Client();
        client.setPassword("pass");
        client.setEmail("mail@mail.ru");
        when(clientRepo.save(client)).thenReturn(new Client(1L,"pass", Role.USER, "mail@ail.ru", 123456789L, true));
        Assertions.assertNotNull(clientService.save(client));
    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }
}