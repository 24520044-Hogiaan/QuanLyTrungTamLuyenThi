# BÁO CÁO KIỂM TRA VÀ SỬA LỖI DỰ ÁN QUẢN LÝ TRUNG TÂM ĐÀO TẠO

**Ngày kiểm tra:** 25/05/2026  
**Người thực hiện:** Claude AI Assistant

---

## 1. TỔNG QUAN DỰ ÁN

### Cấu trúc dự án:
- **Database:** Oracle SQL với 19 bảng chính, 12 triggers, 5 stored procedures, 1 view
- **Backend:** Java 21 + JDBC (ojdbc11)
- **Frontend:** Swing GUI với FlatLaf Look & Feel
- **Kiến trúc:** MVC pattern (Model - DAO - Controller - UI)

### Các module chính:
1. **Module Giáo Viên:** Quản lý giáo viên, điểm danh, nhập điểm, lịch giảng dạy
2. **Module Học Viên:** Quản lý học viên, đăng ký lớp, xem điểm, học phí

---

## 2. CÁC VẤN ĐỀ ĐÃ PHÁT HIỆN

### 2.1. LỖI BIÊN DỊCH (CRITICAL - ĐÃ SỬA)

#### **Lỗi 1: Phụ thuộc compile-time vào FlatLaf**
- **File:** `AppLauncher.java`
- **Mô tả:** Import trực tiếp `com.formdev.flatlaf.FlatLightLaf` gây lỗi biên dịch khi thiếu thư viện
- **Ảnh hưởng:** Không thể biên dịch project bằng `javac` thuần
- **Giải pháp:** Sử dụng reflection để load FlatLaf động, fallback về System L&F nếu không có
```java
// Trước:
import com.formdev.flatlaf.FlatLightLaf;
FlatLightLaf.setup();

// Sau:
Class<?> lafClass = Class.forName("com.formdev.flatlaf.FlatLightLaf");
lafClass.getMethod("setup").invoke(null);
```

### 2.2. CHỨC NĂNG CHƯA ĐỒNG BỘ VỚI DATABASE (HIGH - ĐÃ SỬA)

#### **Vấn đề 2: Panel Giáo Viên dùng dữ liệu mẫu thay vì DB**
- **File:** `ui/giaovien/GiaoVienPanel.java`
- **Mô tả:** Phương thức `loadSampleData()` tạo 5 giáo viên giả, không đọc từ database
- **Ảnh hưởng:** Dữ liệu hiển thị không phản ánh thực tế trong DB
- **Giải pháp:** Thay bằng `loadData()` gọi `GiaoVienController.layDanhSach()`

#### **Vấn đề 3: Panel Học Viên dùng dữ liệu mẫu**
- **File:** `ui/hocvien/DanhSachHocVienPanel.java`
- **Mô tả:** Tương tự panel giáo viên, dùng 5 học viên giả
- **Ảnh hưởng:** Không đồng bộ với bảng HOCVIEN
- **Giải pháp:** Thay bằng `loadData()` gọi `HocVienController.layDanhSach()`

### 2.3. CHỨC NĂNG CHƯA HOÀN THIỆN (MEDIUM - CHƯA SỬA)

#### **Vấn đề 4: Panel Điểm Danh dùng dữ liệu tĩnh**
- **File:** `ui/giaovien/DiemDanhPanel.java`
- **Mô tả:** 
  - Danh sách lớp hardcode: `{"Toán 12A", "Lý 11B", "Hóa 10C"}`
  - Danh sách học viên hardcode trong mảng `HV_PER_LOP`
  - Buổi học được tạo trong memory, không lưu DB
- **Cần làm:** Tích hợp với `LopHocDAO`, `BuoiHocDAO`, `DiemDanhDAO`

#### **Vấn đề 5: Panel Nhập Điểm dùng dữ liệu tĩnh**
- **File:** `ui/giaovien/NhapDiemPanel.java`
- **Mô tả:** Tương tự panel điểm danh, chưa tích hợp `BaiKiemTraDAO`, `ThamGiaBKTDAO`
- **Cần làm:** Đọc/ghi dữ liệu thật từ bảng BAIKIEMTRA, THAMGIABKT

#### **Vấn đề 6: Thiếu DAO cho nhiều bảng**
- **Các DAO đã có:** HocVienDAO, GiaoVienDAO, TaiKhoanDAO, LopHocDAO, KhoaHocDAO, NhanVienDAO
- **Các DAO còn thiếu:**
  - BuoiHocDAO (bảng BUOIHOC)
  - DiemDanhDAO (bảng DIEMDANH)
  - BaiKiemTraDAO (bảng BAIKIEMTRA)
  - ThamGiaBKTDAO (bảng THAMGIABKT)
  - DangKyDAO (bảng DANGKY)
  - HoaDonHocPhiDAO (bảng HOADONHOCPHI)
  - LichHocDAO (bảng LICHHOC)
  - YeuCauChuyenLopDAO (bảng YEUCAUCHUYENLOP)
  - HoanTraDAO (bảng HOANTRA)
  - BoMonDAO (bảng BOMON)
  - PhongHocDAO (bảng PHONGHOC)

### 2.4. VẤN ĐỀ THIẾT KẾ DATABASE (LOW - GHI CHÚ)

#### **Vấn đề 7: Không nhất quán kiểu dữ liệu MABOMON**
- **Mô tả:**
  - Bảng `BOMON`: `MABOMON NUMBER` (khóa chính)
  - Bảng `GIAOVIEN`: `MABOMON NVARCHAR2(200)` (khóa ngoại)
  - Bảng `KHOAHOC`: `MABOMON NUMBER` (khóa ngoại)
- **Ảnh hưởng:** 
  - Không thể tạo foreign key constraint từ GIAOVIEN.MABOMON → BOMON.MABOMON
  - Model Java `GiaoVien.maBoMon` là String, `KhoaHoc.maBoMon` là int
- **Khuyến nghị:** Thống nhất GIAOVIEN.MABOMON thành NUMBER và thêm FK constraint

#### **Vấn đề 8: Mock data không khớp với constraint**
- **File:** `Database/ttdt_dh_mock_data.sql`
- **Dòng 90-99:** Insert GIAOVIEN với MABOMON = '1', '2', '3', '4', '5' (string)
- **Vấn đề:** Nếu sửa GIAOVIEN.MABOMON thành NUMBER, cần sửa mock data

---

## 3. CÁC CHỨC NĂNG ĐÃ HOÀN THIỆN

### 3.1. Quản lý Tài khoản & Đăng nhập
- ✅ TaiKhoanDAO: Đăng nhập, cập nhật lần đăng nhập cuối
- ✅ Hỗ trợ 5 vai trò: Admin, GiaoVien, HocVien, KeToan, NhanVienQuanLy

### 3.2. Quản lý Giáo viên
- ✅ GiaoVienDAO: CRUD đầy đủ với JOIN NHANVIEN
- ✅ UI: Xem danh sách, tìm kiếm (đã sửa để đọc từ DB)

### 3.3. Quản lý Học viên
- ✅ HocVienDAO: CRUD đầy đủ
- ✅ UI: Xem danh sách, lọc theo giới tính (đã sửa để đọc từ DB)

### 3.4. Quản lý Lớp học & Khóa học
- ✅ LopHocDAO: Đọc danh sách lớp
- ✅ KhoaHocDAO: Đọc danh sách khóa học

### 3.5. Database Triggers & Procedures
- ✅ 12 triggers đầy đủ: kiểm tra trạng thái, cascade, validation
- ✅ 5 stored procedures: đăng ký, chuyển lớp, thanh toán, hoàn trả, thống kê vắng

---

## 4. TỔNG KẾT CÁC THAY ĐỔI ĐÃ THỰC HIỆN

### Files đã sửa:

1. **App/src/main/java/com/trungtam/AppLauncher.java**
   - Bỏ import trực tiếp FlatLaf
   - Dùng reflection để load động
   - Fallback về System L&F

2. **App/src/main/java/com/trungtam/ui/giaovien/GiaoVienPanel.java**
   - Thêm `GiaoVienController giaoVienController`
   - Thay `loadSampleData()` → `loadData()`
   - Xóa phương thức `buildSample()` và dữ liệu giả

3. **App/src/main/java/com/trungtam/ui/hocvien/DanhSachHocVienPanel.java**
   - Thêm `HocVienController hocVienController`
   - Thay `loadSampleData()` → `loadData()`
   - Xóa phương thức `buildSample()` và dữ liệu giả

### Kết quả kiểm tra:
```bash
javac -encoding UTF-8 -d target/classes @target/sources.txt
# Exit code: 0 (SUCCESS)
```

---

## 5. KHUYẾN NGHỊ TIẾP THEO

### Ưu tiên cao:
1. **Tạo các DAO còn thiếu** (BuoiHocDAO, DiemDanhDAO, BaiKiemTraDAO, v.v.)
2. **Tích hợp DiemDanhPanel với database** thay vì dữ liệu tĩnh
3. **Tích hợp NhapDiemPanel với database**
4. **Sửa schema:** Thống nhất GIAOVIEN.MABOMON thành NUMBER

### Ưu tiên trung bình:
5. Hoàn thiện các panel học viên còn lại (đăng ký lớp, xem điểm, học phí)
6. Thêm chức năng CRUD cho giáo viên/học viên (hiện tại chỉ xem)
7. Tích hợp stored procedures vào controller

### Ưu tiên thấp:
8. Thêm validation phía client
9. Xử lý lỗi database chi tiết hơn
10. Thêm logging

---

## 6. KẾT LUẬN

### Điểm mạnh:
- ✅ Database schema thiết kế tốt với triggers/procedures đầy đủ
- ✅ Kiến trúc MVC rõ ràng, dễ mở rộng
- ✅ UI đẹp, nhất quán với design tokens
- ✅ Code đã biên dịch thành công sau khi sửa

### Điểm cần cải thiện:
- ⚠️ Nhiều panel UI chưa tích hợp database (dùng dữ liệu giả)
- ⚠️ Thiếu nhiều DAO cho các bảng quan trọng
- ⚠️ Không nhất quán kiểu dữ liệu MABOMON trong schema
- ⚠️ Chưa có unit test

### Tỷ lệ hoàn thành:
- **Database:** 95% (schema + triggers + procedures hoàn chỉnh)
- **Backend (DAO/Controller):** 40% (6/17 DAO đã có)
- **Frontend (UI):** 60% (layout đẹp nhưng nhiều chức năng dùng mock data)
- **Tích hợp end-to-end:** 30%

**Đánh giá chung:** Dự án có nền tảng tốt, cần tập trung hoàn thiện tầng DAO và tích hợp UI với database.
