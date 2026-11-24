package com.bagsys.users.repositories;

import com.bagsys.users.entities.Role;
import com.bagsys.users.enums.RoleCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByCode(RoleCode code);
}
