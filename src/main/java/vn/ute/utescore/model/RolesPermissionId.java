package vn.ute.utescore.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor
@Embeddable
public class RolesPermissionId implements Serializable {
    private Integer RoleID;
    private Integer PermissionID;
}
