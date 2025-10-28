package vn.ute.utescore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.ute.utescore.model.ConfigurationHistory;

@Repository
public interface ConfigurationHistoryRepository extends JpaRepository<ConfigurationHistory, Integer> {
    
    List<ConfigurationHistory> findByConfigKeyOrderByCreatedAtDesc(String configKey);
    
    List<ConfigurationHistory> findByChangedByOrderByCreatedAtDesc(String changedBy);
    
    List<ConfigurationHistory> findAllByOrderByCreatedAtDesc();
}

