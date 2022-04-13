package by.kynca.estateWeb.repository;


import by.kynca.estateWeb.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findClientByEmail(String email);
}
