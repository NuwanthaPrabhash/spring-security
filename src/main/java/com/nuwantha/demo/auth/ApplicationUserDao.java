package com.nuwantha.demo.auth;

import java.util.Optional;

// to fetch users from loadUserByUsername in ApplicationUserService
public interface ApplicationUserDao {
    Optional<ApplicationUser> selectApplicationUserByUsername(String username);
}
