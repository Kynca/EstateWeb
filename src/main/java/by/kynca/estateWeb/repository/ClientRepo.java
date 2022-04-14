package by.kynca.estateWeb.repository;


import by.kynca.estateWeb.entity.Client;
import by.kynca.estateWeb.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClientRepo extends JpaRepository<Client, Long> {
    Optional<Client> findClientByEmail(String email);
    Page<Client> findAllByRoleNot(Role role, Pageable page);
}
