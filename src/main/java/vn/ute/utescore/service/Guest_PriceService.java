
package vn.ute.utescore.service;

import vn.ute.utescore.model.GiaThue;
import java.util.List;

public interface Guest_PriceService {
    List<GiaThue> getActivePricesByLoaiSan(String loaiSan);
}
