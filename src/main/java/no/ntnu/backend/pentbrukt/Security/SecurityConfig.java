package no.ntnu.backend.pentbrukt.Security;

import no.ntnu.backend.pentbrukt.Service.UserPrincipalDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserPrincipalDetailsService userPrincipalDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(UserPrincipalDetailsService userPrincipalDetailsService, PasswordEncoder passwordEncoder) {
        this.userPrincipalDetailsService = userPrincipalDetailsService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin().loginProcessingUrl("/login").defaultSuccessUrl("/");

    }

    // Authentication provider
    @Bean
    DaoAuthenticationProvider authenticationProvider(){
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
