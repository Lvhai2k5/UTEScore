package vn.ute.utescore.model;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Data @NoArgsConstructor @AllArgsConstructor
@Embeddable
public class TinhNangSanId implements Serializable {
    private Integer MaTinhNang;
    private Integer MaSan;
}
