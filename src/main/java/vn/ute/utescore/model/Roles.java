package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Roles")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Roles {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer roleID;

    @Column(length = 50)
    private String roleName;

    @Column(length = 255)
    private String moTa;
}
