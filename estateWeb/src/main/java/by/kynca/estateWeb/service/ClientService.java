package by.kynca.estateWeb.service;

import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.Role;
import by.kynca.estateWeb.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Autowired;
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
        return clientRepo.save(client);
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

    @Override
    public List findAll(int page, String sort) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Client findById(Long id) {
        throw new UnsupportedOperationException();
    }

}
