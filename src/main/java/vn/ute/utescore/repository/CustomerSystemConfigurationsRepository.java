package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.SystemConfigurations;

@Repository
public interface CustomerSystemConfigurationsRepository extends JpaRepository<SystemConfigurations, Integer> {

    @Query("SELECT s.configValue FROM SystemConfigurations s " +
           "WHERE s.isActive = true AND LOWER(s.configKey) = LOWER(:key)")
    String findValueByKey(String key);
}
