package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "TinhNangSan")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class TinhNangSan {
    @EmbeddedId
    private TinhNangSanId id;

    @ManyToOne @MapsId("MaTinhNang")
    @JoinColumn(name = "MaTinhNang")
    private TinhNang tinhNang;

    @ManyToOne @MapsId("MaSan")
    @JoinColumn(name = "MaSan")
    private SanBong sanBong;
}
