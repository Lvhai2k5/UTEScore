package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TinhNangSan")
public class TinhNangSan {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maTinhNang")
private TinhNang tinhNang;

@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "maSan")
private SanBong san;
}
