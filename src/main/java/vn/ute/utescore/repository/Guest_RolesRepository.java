package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.Roles;

@Repository
public interface Guest_RolesRepository extends JpaRepository<Roles, Integer> {
    Roles findByRoleName(String roleName);
}
