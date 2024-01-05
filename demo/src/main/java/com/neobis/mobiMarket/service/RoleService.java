package com.neobis.mobiMarket.service;

import com.neobis.mobiMarket.entity.Role;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

public interface RoleService {

    Optional<Role> findByName(String name) throws RoleNotFoundException;
}
