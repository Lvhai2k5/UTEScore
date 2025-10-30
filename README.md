# BÁO CÁO CUỐI KỲ  
# XÂY DỰNG WEBSITE QUẢN LÝ SÂN BÓNG ĐÁ UTESCORE

## THÔNG TIN ĐỒ ÁN
- **Tên đồ án:** Xây dựng website quản lý sân bóng đá UTEScore
- **Môn học:** Lập trình Web (Web Programming)
- **Học kỳ:** 2 - Năm học 2024-2025
- **Giảng viên hướng dẫn:** ThS. Nguyễn Hữu Trung
- **Lớp học phần:** WEPR330479_03
- **Nhóm thực hiện:** Nhóm 10  
  - Lê Vũ Hải – 23110209  
  - Phạm Võ Nhất Kha – 23110235  
  - Huỳnh Hoài Phương – 23110289  
  - Lê Thị Thảo – 23110321

---

## MỤC LỤC
1. [Đặt vấn đề](#đặt-vấn-đề)
2. [Kiến trúc & công nghệ](#kiến-trúc--công-nghệ)
3. [Yêu cầu hệ thống](#yêu-cầu-hệ-thống)
4. [Cài đặt & sử dụng](#cài-đặt--sử-dụng)
    - [Cấu hình](#cấu-hình)
    - [Chạy ứng dụng](#chạy-ứng-dụng)
5. [Chi tiết chức năng & sơ đồ luồng](#chi-tiết-chức-năng--sơ-đồ-luồng)
    - [Khách hàng](#khách-hàng)
    - [Nhân viên](#nhân-viên)
    - [Quản trị](#quản-trị)
6. [Tài khoản mẫu](#tài-khoản-mẫu)
7. [Cấu trúc thư mục dự án](#cấu-trúc-thư-mục-dự-án)
8. [Tích hợp bên thứ ba](#tích-hợp-bên-thứ-ba)
9. [Bảo mật & phân quyền](#bảo-mật--phân-quyền)
10. [Kiểm thử nhanh](#kiểm-thử-nhanh)
11. [Khắc phục sự cố](#khắc-phục-sự-cố)
12. [Đánh giá – Hướng phát triển](#đánh-giá--hướng-phát-triển)
13. [Tài liệu tham khảo](#tài-liệu-tham-khảo)

---

## 1. ĐẶT VẤN ĐỀ
Quản lý vận hành sân bóng đá phát sinh nhiều quy trình phức tạp: từ đặt sân, thanh toán, đến check-in thực tế, phân quyền đa vai trò (khách hàng, nhân viên, quản trị viên). UTEScore số hóa toàn bộ nghiệp vụ đặt – thanh toán – vận hành – báo cáo sân bóng cho cơ sở thể thao hiện đại.

**Mục tiêu:**  
- Tự động hóa nghiệp vụ đặt sân, thanh toán không tiền mặt, xuất hóa đơn, quản lý tiện ích & lịch bảo trì.
- Đảm bảo bảo mật, phân quyền chặt chẽ.
- Hỗ trợ thống kê, báo cáo, nâng cao hiệu quả vận hành.

---

## 2. KIẾN TRÚC & CÔNG NGHỆ
- **Backend:** Spring Boot 3 (MVC, Security, JWT, JPA/Hibernate)
- **Frontend:** Thymeleaf, Bootstrap 5, Javascript (ở admin/customer/employee)
- **Realtime/Chat:** Spring WebSocket
- **Media:** Cloudinary
- **Báo cáo/Excel:** Apache POI
- **Database:** SQL Server (MSSQL)

---

## 3. YÊU CẦU HỆ THỐNG
- JDK 17+
- Maven 3.9 hoặc mới hơn
- SQL Server (khuyến nghị bản Developer)
- Tài khoản Cloudinary, VNPay sandbox (tùy mục thanh toán)

---

## 4. CÀI ĐẶT & SỬ DỤNG
### Cấu hình
Bổ sung/sửa `src/main/resources/application.properties`:
- CSDL: `spring.datasource.url`, `.username`, `.password`
- Cloudinary: `cloudinary.cloud_name`, `cloudinary.api_key`...
- VNPay: `vnpay.tmn_code`, `vnpay.hash_secret`, ...
- Email: `spring.mail.*` nếu cần gửi OTP

### Chạy ứng dụng
- **Trực tiếp:**
  - Windows: `mvnw.cmd spring-boot:run`
  - Linux/Mac: `./mvnw spring-boot:run`
  - Mặc định UI tại: http://localhost:8080
- **Đóng gói JAR:**
  - `./mvnw clean package`
  - `java -jar target/UTEScore-1.0.0.jar`

---

## 5. CHI TIẾT CHỨC NĂNG & SƠ ĐỒ LUỒNG
### Khách hàng
- Duyệt sân, xem chi tiết, lọc loại/khu vực/trạng thái/giá
- Đặt sân: Chọn sân – Chọn giờ – Đặt/giữ chỗ – Thanh toán (VNPay/điểm)
- Lịch sử hóa đơn, phản hồi, chat hỗ trợ, tra cứu/check-in QR

### Nhân viên
- Xem lịch ca, danh sách khách/ngày
- Xác nhận, chỉnh sửa thanh toán, xử lý đơn hoàn tiền/sự cố
- Quản lý sự cố, thông báo nội bộ, báo cáo theo ca

### Quản trị
- Thêm/sửa/xóa sân: hình ảnh, mô tả, giờ mở/đóng, tiện ích, trạng thái
- Thay đổi trạng thái: Hoạt động – Bảo trì – Tạm đóng – Đóng vĩnh viễn (lưu nhật ký)
- Quản lý giá thuê theo loại/khung giờ, đặt & check lịch bảo trì, xuất Excel
- Quản lý user, phân quyền, cấu hình hệ thống

---

## 6. TÀI KHOẢN MẪU
| Vai trò     | SoDienThoai         | Password   |
|-------------|---------------------|------------|
| Admin       | 0987654321          | Levuhai2@5 |
| Staff       | 0799513501          | Lvhai2@5   |
| Customer    | 0344969875          | 123456     |

> (Tạo mới/tùy chỉnh dữ liệu seed tuỳ môi trường)

---

## 7. CẤU TRÚC THƯ MỤC DỰ ÁN
```
UTEScore-main/
├── README.md                 # Hướng dẫn dự án
├── pom.xml                   # Cấu hình Maven/Dependencies
├── mvnw, mvnw.cmd            # Maven Wrapper script
├── bin/                      # Log hoặc tool phụ trợ (tùy môi trường)
├── logs/                     # Log ứng dụng
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── vn/ute/utescore/
│   │   │       ├── config/         # Cấu hình ứng dụng, bảo mật, websocket, vnPay...
│   │   │       ├── controller/     # REST API và Thymeleaf Controllers (admin, guest, employee, ...)
│   │   │       ├── dto/            # Đối tượng truyền dữ liệu (Data Transfer Objects)
│   │   │       ├── exception/      # Custom exception (dùng cho xử lý lỗi tập trung)
│   │   │       ├── model/          # Entity JPA đại diện bảng CSDL
│   │   │       ├── repository/     # Repository kết nối thao tác DB (JPA interface)
│   │   │       ├── security/       # Lớp bảo mật (JWT, filter, config)
│   │   │       ├── service/        # Layer nghiệp vụ + thư mục impl (implementations)
│   │   │       │   └── impl/       # Lớp cài đặt chi tiết cho service
│   │   │       ├── utils/          # Tiện ích phụ trợ (mã hóa, phiên, mapper, ...)
│   │   │       └── UteScoreApplication.java   # Main Spring Boot app entrypoint
│   │   └── resources/
│   │       ├── application.properties    # Cấu hình môi trường chính
│   │       ├── messages.properties      # Đa ngôn ngữ/thông báo
│   │       ├── static/
│   │       │   ├── css/             # Giao diện style
│   │       │   ├── js/              # Javascript
│   │       │   └── uploads/         # Ảnh/logo static
│   │       └── templates/           # Giao diện Thymeleaf
│   │           ├── admin/           # UI quản trị
│   │           ├── customer/        # UI khách hàng
│   │           ├── employee/        # UI nhân viên
│   │           ├── guest/           # UI khách vãng lai/đăng ký
│   │           ├── fragments/       # Header/Footer/dùng chung
│   │           └── layout/          # Base/layout chung
│   └── test/
│       └── java/
│           └── vn/ute/utescore/     # Các bài test đơn vị/nâng cao
└── ...
```
- **Lưu ý:**
    - Folder `repository`, `service`, `impl`, `security`, `exception`, `utils`... đã được liệt kê đúng thực tế.
    - Có thể mở rộng hoặc thêm chi tiết subfolder nếu thầy/cô yêu cầu kiểm tra kỹ hơn.
    - `target/` là thư mục build output, không commit lên Git.

---

## 8. TÍCH HỢP BÊN THỨ BA
- **VNPay:** Dùng API tích hợp thanh toán, cập nhật trạng thái đơn
- **Cloudinary:** Lưu & lấy link ảnh, upload online
- **Email:** Gửi OTP/khôi phục — sử dụng smtp cấu hình trong app

---

## 9. BẢO MẬT & PHÂN QUYỀN
- JWT Auth, Spring Security phân vai trò Customer/Employee/Admin
- Endpoint `/admin/*` chỉ cho Admin đăng nhập, `/employee/*` chỉ nhân viên
- Tất cả API backend/private đều bảo vệ role, front-end shield tương ứng

---

## 10. KIỂM THỬ NHANH
1. Thêm sân mới tại giao diện admin, kiểm tra giao diện khách hàng
2. Đặt sân theo flow khách, thử cả VNPay và bằng điểm
3. Nhân viên xác nhận hóa đơn, giải quyết hoàn tiền
4. Thay đổi trạng thái, đặt lịch bảo trì, xuất báo cáo Excel
5. Đăng nhập các vai trò trải nghiệm toàn hệ thống

---

## 11. KHẮC PHỤC SỰ CỐ
- **Không vào database:** Kiểm tra application.properties, quyền, port SQL Server
- **Ảnh lỗi:** Cloudinary thiếu key hoặc chưa bật "resource public"
- **Thanh toán lỗi:** check hash_secret, mã TMN, return_url
- **Không login/jwt:** Kiểm tra ngày hết hạn token, quyền user
- **Build lỗi:** check JDK version/Maven, xoá cache .m2 nếu cần

---

## 12. ĐÁNH GIÁ – HƯỚNG PHÁT TRIỂN
- Hoàn thiện quy trình đặt/giữ/chăm sóc/thuê/trả/bảo trì sân bóng, UI/UX thân thiện
- Code rõ ràng, bảo mật, chuẩn RESTful, dễ mở rộng cho mobile/app khác
- Tương lai có thể đa trung tâm, thêm machine learning dự báo, mở rộng event thể thao

---

## 13. TÀI LIỆU THAM KHẢO
1. Sách Spring in Action, Manning
2. Java Brains (YouTube) — Spring Boot & Security
3. Tài liệu [Spring Boot](https://spring.io/projects/spring-boot), [Thymeleaf](https://www.thymeleaf.org/)
4. Docs: API VNPay, Cloudinary, SQL Server 
