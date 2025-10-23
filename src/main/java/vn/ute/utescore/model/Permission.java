package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Permission")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Permission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer permissionID;

    @Column(length = 100)
    private String permissionName;

    @Column(length = 255)
    private String moTa;
}
