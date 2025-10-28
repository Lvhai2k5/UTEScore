package vn.ute.utescore.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.ute.utescore.model.TinhNangSan;
import vn.ute.utescore.model.TinhNangSanId;
import vn.ute.utescore.model.TinhNang;

import java.util.List;

@Repository
public interface CustomerTinhNangSanRepository extends JpaRepository<TinhNangSan, TinhNangSanId> {

    /** 🔹 Lấy toàn bộ đối tượng TinhNang của một sân */
    @Query("""
        SELECT ts.tinhNang
        FROM TinhNangSan ts
        WHERE ts.sanBong.maSan = :maSan
    """)
    List<TinhNang> findTinhNangByMaSan(@Param("maSan") Integer maSan);

    /** 🔹 Lấy danh sách tên tính năng (String) của một sân */
    @Query("""
    	    SELECT ts.tinhNang.TenTinhNang
    	    FROM TinhNangSan ts
    	    WHERE ts.sanBong.maSan = :maSan
    	""")
    	List<String> findTenTinhNangByMaSan(@Param("maSan") Integer maSan);
}
