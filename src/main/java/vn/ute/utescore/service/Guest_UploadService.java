package vn.ute.utescore.service;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class Guest_UploadService {
	@Autowired
    private Cloudinary cloudinary;

    /**
     * Upload ảnh lên Cloudinary và trả về link URL
     *
     * @param file MultipartFile ảnh upload
     * @return String đường dẫn ảnh
     */
    public String uploadImage(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null; // hoặc trả ảnh default
            }

            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "folder", "utescore/images" // ảnh sẽ được lưu trong thư mục riêng trên Cloudinary
            ));

            return uploadResult.get("secure_url").toString();

        } catch (Exception e) {
            throw new RuntimeException("❌ Upload ảnh thất bại: " + e.getMessage(), e);
        }
    }

}
