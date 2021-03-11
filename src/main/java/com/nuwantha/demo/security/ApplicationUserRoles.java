package com.nuwantha.demo.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.nuwantha.demo.security.ApplicationUserPermission.*;

public enum ApplicationUserRoles {
    STUDENT(Sets.newHashSet()),  // in here STUDENT role have no any permission
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)), // in here ADMIN role have 4 permission
    ADMIN_TRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<ApplicationUserPermission> applicationUserPermissions;

    ApplicationUserRoles(Set<ApplicationUserPermission> applicationUserPermissions) {
        this.applicationUserPermissions = applicationUserPermissions;
    }

    public Set<ApplicationUserPermission> getApplicationUserPermissions() {
        return applicationUserPermissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permission = getApplicationUserPermissions().stream()
                .map(applicationUserPermission -> new SimpleGrantedAuthority(applicationUserPermission.getPermission()))
                .collect(Collectors.toSet());

        permission.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return permission;
    }
}
