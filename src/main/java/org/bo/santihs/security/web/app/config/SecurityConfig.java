package org.bo.santihs.security.web.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    //! This method will return a SecurityFilterChain, it is used to set up the behavior of spring security
//    @Bean
    //! HttpSecurity is a bean, it helps us to configure the security
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        return httpSecurity
    //? This property is Cross-Site Request Forgery
    //? It is a vulnerability that appears working with forms
    //? It can get vulnerable info (endpoints, url, etc)
    //! If u will work with forms, dont disable it
////                .csrf().disable()
    //? Set up the protected and not protected url
//                .authorizeHttpRequests()
    //? Here go the not protected urls
//                    .antMatchers("/v1/index2").permitAll()
//                    .anyRequest().authenticated()
//                .and()
//                .formLogin().permitAll()
//                .and()
//                .build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests(auth -> {
                    auth.antMatchers("/v1/index2").permitAll();
                    auth.anyRequest().authenticated();
                })
                .formLogin()
                //? Manage the form login when it is success
                .successHandler(successHandler())
                .permitAll()
                .and()
                //? Set up the session management (Advantage: work with user info)
                .sessionManagement(sm -> {
                    //! ALWAYS: Create a session if there is no other one and reuse one it exists
                    //! IF_REQUIRED: Create a new session if it is necessary, more strict than ALWAYS
                    //! NEVER: Non create a session, if there is session, it is used
                    //! STATELESS: Dont work with sessions, all requests are independents
                    sm.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
                    sm.invalidSessionUrl("/login");
                    sm.maximumSessions(1)
                            .expiredUrl("/login")
                            //? Enable to read the user data
                            .sessionRegistry(sessionRegistry());
                    //? Someone can violate the security by the id session
                    //? It is possible to use 3 set ups
                    //! MIGRATE SESSION: Spring will generate another id if someone try to use the id session
                    //!                  Copy the data in the new migrated session
                    //! NEW SESSION: As same as migrate session, but dont copy the data
                    //! NONE: Disable the security of session fixation
                    sm.sessionFixation().migrateSession();
                })
                //? Authentication to use the headers
                //? Use when the security is not important at all
//                .httpBasic(hb -> {
//
//                })
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> response.sendRedirect("/v1/session"));
    }
}
