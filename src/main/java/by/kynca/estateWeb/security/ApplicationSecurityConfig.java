package by.kynca.estateWeb.security;

import by.kynca.estateWeb.entity.Role;
import by.kynca.estateWeb.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ClientService clientService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ClientService clientService) {
        this.passwordEncoder = passwordEncoder;
        this.clientService = clientService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/estates/", "/estates/house/", "/estates/flat/", "/estates/land/", "/estates/get/*", "/register","/static/**").permitAll()
                .antMatchers(HttpMethod.GET,"/clients/").hasAuthority(Role.ADMIN.name())
                .antMatchers(HttpMethod.PUT,"/client/**").hasAuthority(Role.ADMIN.name())
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .usernameParameter("email")
                .defaultSuccessUrl("/estates/")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/estates/");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(clientService);
        return provider;
    }
//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username()
//                .password(passwordEncoder.encode())
//                .authorities()
//                .build();
//    }
}
