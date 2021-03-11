package com.nuwantha.demo.auth;

import com.google.common.collect.Lists;
import com.nuwantha.demo.security.ApplicationUserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("fake")
public class FakeApplicationUserDaoService implements ApplicationUserDao{

    public final PasswordEncoder passwordEncoder;

    @Autowired
    public FakeApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers;
        applicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        "nuwantha",
                        passwordEncoder.encode("123"),
                        ApplicationUserRoles.STUDENT.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "linda",
                        passwordEncoder.encode("123"),
                        ApplicationUserRoles.ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "tom",
                        passwordEncoder.encode("123"),
                        ApplicationUserRoles.ADMIN_TRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
        return applicationUsers;
    }
}
