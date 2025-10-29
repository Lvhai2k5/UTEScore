package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.Camera;

import java.util.List;

@Repository
public interface CameraRepository extends JpaRepository<Camera, Integer> {

    @Query("SELECT c FROM Camera c JOIN FETCH c.sanBong sb LEFT JOIN FETCH c.nhanVienPhuTrach nv")
    List<Camera> findAllWithRelations();

    @Query("SELECT c FROM Camera c WHERE c.sanBong.maSan = :sanId")
    List<Camera> findBySanId(@Param("sanId") Integer sanId);
}


