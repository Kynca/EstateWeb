package by.kynca.estateWeb.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.util.Collection;
import java.util.Collections;

@Entity
public class Client extends AbstractBean implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long clientId;

    @NotEmpty(message = "password could not be empty")
    @Size(min = 3)
    private String password;
    @Enumerated(value = EnumType.STRING)
    @NotNull
    private Role role;

    @Column(unique = true)
    @Email(message = "it is not an email")
    private String email;
    @Min(value = 100000000, message = "it is not a phone number")
    @Max(value = 1000000000L, message = "it is not a phone number")
    private long phoneNum;

    public Client() {
    }

    public Client(Long clientId, String password, Role role, String email, long phoneNum) {
        this.clientId = clientId;
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public Client(String password, Role role, String email, long phoneNum) {
        this.password = password;
        this.role = role;
        this.email = email;
        this.phoneNum = phoneNum;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long id) {
        this.clientId = id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.name());
        return Collections.singletonList(authority);
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(long phoneNum) {
        this.phoneNum = phoneNum;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
