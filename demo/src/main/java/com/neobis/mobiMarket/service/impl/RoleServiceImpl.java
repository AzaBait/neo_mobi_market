package com.neobis.mobiMarket.service.impl;

import com.neobis.mobiMarket.entity.Role;
import com.neobis.mobiMarket.repository.RoleRepo;
import com.neobis.mobiMarket.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    @Override
    public Optional<Role> findByName(String name) throws RoleNotFoundException {
        Optional<Role> userRole = roleRepo.findByName(name);
        if (userRole.isEmpty()) {
            throw new RoleNotFoundException("Role " + name + " not found!");
        }
        return (userRole);
    }
}
