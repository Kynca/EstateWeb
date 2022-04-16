package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.Role;
import by.kynca.estateWeb.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service which work with client
 */
@Service
public class ClientService implements UserDetailsService, ServiceActions<Client> {
    @Value("${page.size.client}")
    private int pageSize;

    private final ClientRepo clientRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        return clientRepo.findClientByEmail(mail).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * save client in db
     * @param client client to save
     * @return saved client
     */
    @Transactional
    @Override
    public Client save(Client client) {
        boolean exist = clientRepo.findClientByEmail(client.getEmail()).isPresent();

        if (exist) {
            return null;
        }

        client.setRole(Role.USER);
        client.setPassword(encoder.encode(client.getPassword()));
        client.setEnabled(true);
        return clientRepo.save(client);
    }

    /**
     * find all client in db where client role not null
     * @param page number of paeg
     * @param sort type of sort
     * @return List of client
     */
    @Override
    public List<Client> findAll(int page, String sort) {
        Pageable pageable = PageRequest.of(page, pageSize, Sort.by(sort).descending());
        return clientRepo.findAllByRoleNot(Role.ADMIN, pageable).getContent();
    }

    /**
     * method for enable/disable client
     * @param id id of edited client
     */
    public void setEnable(Long id) {
        Client client = clientRepo.findById(id).orElse(null);
        if (client != null && client.getRole() != Role.ADMIN) {
            client.setEnabled(!client.isEnabled());
        } else {
            return;
        }
        clientRepo.save(client);
    }

    /**
     * fond client by id
     * @param id of searching client
     * @return null if client doesn't exist, client from db it it is
     */
    @Override
    public Client findById(Long id) {
        return clientRepo.findById(id).orElse(null);
    }

}
