package vn.ute.utescore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.SystemConfigurations;

@Repository
public interface SystemConfigurationsRepository extends JpaRepository<SystemConfigurations, Integer> {
    
    @Query("SELECT s FROM SystemConfigurations s WHERE s.configKey = :configKey AND s.isActive = true")
    Optional<SystemConfigurations> findByConfigKey(@Param("configKey") String configKey);
    
    @Query("SELECT s FROM SystemConfigurations s WHERE s.isActive = true")
    java.util.List<SystemConfigurations> findAllActive();
}

