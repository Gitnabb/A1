package no.ntnu.backend.pentbrukt.Security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static no.ntnu.backend.pentbrukt.Security.UserPermissionsConfig.*;

public enum UserRoleConfig {

    USER(Sets.newHashSet()),
    USERLOGGEDIN(Sets.newHashSet(USER_READ, USER_WRITE, LISTING_READ, LISTING_WRITE));

    private final Set<UserPermissionsConfig> permissions;

    UserRoleConfig(Set<UserPermissionsConfig> permissions) {
        this.permissions = permissions;
    }

    public Set<UserPermissionsConfig> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }
}
