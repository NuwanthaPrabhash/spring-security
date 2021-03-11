package com.nuwantha.demo.security;

import com.nuwantha.demo.auth.ApplicationUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // for @preauthorize
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;

    private static final String MANAGEMENT_API = "management/api/**";

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, ApplicationUserService applicationUserService) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // in here this says
        http
//                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) // use this when use browser
//                .and()
                .csrf().disable() // use to protect from hackers. but this only works for browser client but we use here postman that's why disable this
                .authorizeRequests() // we need to authorized request
                .antMatchers("/", "index", "/css/*", "/js/*").permitAll() // url paths we want to access without permissions (whitelist url)
                .antMatchers("/api/**").hasRole(ApplicationUserRoles.STUDENT.name()) // only STUDENT role can access urls start with /api
//                .antMatchers(HttpMethod.DELETE, MANAGEMENT_API).hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.POST, MANAGEMENT_API).hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.PUT, MANAGEMENT_API).hasAuthority(ApplicationUserPermission.COURSE_WRITE.getPermission())
//                .antMatchers(HttpMethod.GET, MANAGEMENT_API).hasAnyRole(ApplicationUserRoles.ADMIN.name(), ApplicationUserRoles.ADMIN_TRAINEE.name())
                .anyRequest()    // any request
                .authenticated() // must be authenticated
                .and()
//                .httpBasic();    // mechanism to enforced the authenticity is basic authentication.
                .formLogin() // for form base authentication
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true) // for false redirect (login to page if you are already login) not sure why is this need.
                .passwordParameter("password") // password parameter and username parameter are the same as name in the form in login.html if you want to change the name
                .usernameParameter("username")                          // you should change the both place both in here and field name in html page
                .and()
                .rememberMe() // default to 2 weeks
//              .tokenRepository() // this use when we use database like postgress
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) // this is 21 Days
                .key("somethingverysecured") // this is the key use to generate md5 (md5 in remember me cookie)
                .rememberMeParameter("remember-me") // same as the password parameter
                .and()
                .logout()
                .logoutUrl("/logout") // this is one come from default.
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))  // if csrf() not disabled remove this line.
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me") // to delete cookies. we can see the JSESSIONID and remember-me inside the cookies from inspect the web page
                .logoutSuccessUrl("/login"); // after logout this redirect the page to login
    }

//    @Override
//    @Bean // to instanciate  for us
//    protected UserDetailsService userDetailsService() {
//        UserDetails userDetails = User.builder()
//                .username("nuwantha")
//                .password(passwordEncoder.encode("123")) // password must be BCrypt type that's why we use passwordEncoder.encode()
//                .authorities(ApplicationUserRoles.STUDENT.getGrantedAuthorities())
//                .build();
//
//        UserDetails adminUser = User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("123"))
//                .authorities(ApplicationUserRoles.ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tom = User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("123"))
//                .authorities(ApplicationUserRoles.ADMIN_TRAINEE.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(userDetails, adminUser, tom);
//    }


    // how you wire things up
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

//    provider
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }
}
