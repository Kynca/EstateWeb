package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.Role;
import by.kynca.estateWeb.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ClientService implements UserDetailsService, ServiceActions<Client>{

    private final ClientRepo clientRepo;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Autowired
    public ClientService(ClientRepo clientRepo) {
        this.clientRepo = clientRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientRepo.findClientByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public Client save(Client client) {
       if(client.getClientId() != null && !clientRepo.existsById(client.getClientId()) ){
           return null;
       }
      return clientRepo.save(client);
    }

    @Override
    public List<Client> findAll(int page, String sort) {
        Pageable pageable = Pageable.ofSize(page);
        return clientRepo.findAllByRoleNot(Role.ADMIN, pageable).getContent();
    }

    public void disable(Long id) {
       Client client = clientRepo.findById(id).orElse(null);
       if(client != null && client.getRole() == Role.ADMIN){
           client.setEnabled(false);
       } else {
           return;
       }
       clientRepo.save(client);
    }

    @Override
    public Client findById(Long id) {
        return clientRepo.findById(id).orElse(null);
    }

    public Client signUp(Client client){
        boolean exist = clientRepo.findClientByEmail(client.getEmail()).isPresent();

        if(exist){
            return null;
        }

        client.setRole(Role.USER);
        client.setPassword(encoder.encode(client.getPassword()));
        return save(client);
    }

}
