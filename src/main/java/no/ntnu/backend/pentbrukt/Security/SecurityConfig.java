package no.ntnu.backend.pentbrukt.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static no.ntnu.backend.pentbrukt.Security.UserRoleConfig.USERLOGGEDIN;
import static no.ntnu.backend.pentbrukt.Security.UserRoleConfig.USER;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable() // TODO: WILL GO THROUGH
                .authorizeRequests()
                .antMatchers("/", "index","/css/*", "/js/*").permitAll()
                //.antMatchers("/api/**").hasRole(USER.name())
/*                .antMatchers(HttpMethod.DELETE,"/api/**").hasAuthority(USER.name())
                .antMatchers(HttpMethod.POST,"/api/**").hasAuthority(USER.name())
                .antMatchers(HttpMethod.PUT,"/api/**").hasAuthority(USER.name())
                .antMatchers(HttpMethod.GET,"/api/**").hasAnyRole(USER.name())*/
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();

    }


    // "USER" represents a user that has limited access. Can only use GET method. Essentially, a
    // non-logged in user.

    // "USERLOGGEDIN" represents a user that is of course logged in, and is granted full access to
    // PUT POST DELETE methods.
    @Override
    @Bean
    protected UserDetailsService userDetailsService() {

        UserDetails userloggedin = User.builder()
                .username("userloggedin")
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
    }
}
