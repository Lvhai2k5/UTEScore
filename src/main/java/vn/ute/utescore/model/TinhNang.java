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
@Table(name = "TinhNang")
public class TinhNang {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer maTinhNang;

@Column(length = 20)
private String tenTinhNang;

@Column(length = 255)
private String moTa;
}
