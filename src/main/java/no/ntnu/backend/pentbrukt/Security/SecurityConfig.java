package no.ntnu.backend.pentbrukt.Security;

import no.ntnu.backend.pentbrukt.Repository.UserRepository;
import no.ntnu.backend.pentbrukt.Service.UserPrincipalDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserPrincipalDetailsService userPrincipalDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // JWT FILTERS (Authentication, Authorization)
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), this.userRepository))
                .authorizeRequests()
                // Restrictions
                .antMatchers("/login").permitAll()
                .antMatchers("/api/listings").hasRole("USERLOGGEDIN")
                .anyRequest().authenticated();


    }

    // Authentication provider
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(this.userPrincipalDetailsService);

        return daoAuthenticationProvider;
    }

    // "USER" represents a user that has limited access. Can only use GET method. Essentially, a
    // non-logged in user.

    // "USERLOGGEDIN" represents a user that is of course logged in, and is granted full access to
    // PUT POST DELETE methods.


    /*   @Override

    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails userloggedin = User.builder()
                .username("kjetilhammerseth@gmail.com")
                .password(passwordEncoder.encode("userloggedin"))
                .authorities(USERLOGGEDIN.getGrantedAuthorities())
                .build();

        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("user"))
                .authorities(USER.getGrantedAuthorities())
                .build();


        return new InMemoryUserDetailsManager(
                userloggedin,
                user);
    }*/
}
