1. **Cập nhật tình trạng buổi học:**

| Tên Use case | Quản lý buổi học |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên thay đổi trạng thái  của các buổi học  |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đăng nhập thành công  |
| **Hậu điều kiện** | Thông tin tình trạng buổi học được cập nhật.  |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị danh sách các lớp học phụ trách. 2\. Giáo viên chọn một lớp học để xem chi tiết. 3\. Hệ thống hiển thị danh sách các buổi học 4\. Giáo viên chọn buổi học cần cập nhật. 5\. Giáo viên cập nhật tình trạng cho lớp học 6\. Hệ thống yêu cầu xác nhận. 7\. Hệ thống cập nhật tình trạng buổi học và thông báo "Cập nhật thông tin buổi học thành công".  |
| **Luồng sự kiện phụ** | 6a. Hủy xác nhận: Giáo viên nhấn nút hủy, hệ thống sẽ hủy hành động |

2. **Xem lịch dạy**	

| Tên Use case | Xem lịch dạy  |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên tra cứu lịch dạy theo tuần/tháng và xem danh sách chi tiết thông tin cá nhân, khối thi nguyện vọng của học viên trong các lớp mình dạy. |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đã đăng nhập thành công vào hệ thống. |
| **Hậu điều kiện** | Hệ thống hiển thị đầy đủ, chính xác lịch trình giảng dạy và thông tin nhân sự của lớp học. |
| **Luồng sự kiện chính** | 1\. Giáo viên chọn mục lịch dạy 2\. Hệ thống thực hiện truy vấn thông tin lịch học của các lớp học giáo viên đang dạy 3\. Hệ thống hiển thị thời khóa biểu  |
| **Luồng sự kiện phụ** | 3a. Chưa có lịch: Hệ thống hiển thị thời khóa biểu trống |

3. **Nhập điểm** 

| Tên Use case | Nhập điểm |
| :---- | :---- |
| **Mô tả Use case** | Cho phép giáo viên nhập điểm số và lời nhận xét cho các bài kiểm tra của học viên trong lớp. |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đã đăng nhập và lớp học đã có bài kiểm tra được tạo  |
| **Hậu điều kiện** | Điểm số và nhận xét của học viên được cập nhật vào cơ sở dữ liệu để học viên có thể xem. |
| **Luồng sự kiện chính** | 1\. Giáo viên chọn chức năng nhập điểm. 2\. Giáo viên chọn lớp học. 3\. Hệ thống hiển thị danh sách bài kiểm tra 4\. Giáo viên chọn bài kiểm tra 5\. Hệ thống hiển thị bảng điểm hiện tại của bài kiểm tra. 6\. Giáo viên tiến hành nhập mới hoặc chỉnh sửa điểm số tại các ô tương ứng. 7\. Giáo viên nhấn nút "Lưu thay đổi". 8\. Hệ thống kiểm tra tính hợp lệ và định dạng của điểm số nhập vào. 9\. Hệ thống cập nhật dữ liệu vào Cơ sở dữ liệu. 10\. Hệ thống hiển thị thông báo "Cập nhật bảng điểm thành công".  |
| **Luồng sự kiện phụ** | 8a. Dữ liệu nhập không hợp lệ (Ví dụ: Điểm bỏ trống, điểm âm, hoặc điểm lớn hơn 10). Hệ thống hiển thị thông báo lỗi và yêu cầu giáo viên nhập lại.  |

4. **Điểm danh:**

| Tên Use case | Điểm danh |
| :---- | :---- |
| **Mô tả Use case** | Cho phép Giáo viên ghi nhận tình trạng chuyên cần (Có mặt, Vắng mặt, Đi trễ) của từng học viên trong một buổi học cụ thể. |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đã đăng nhập và đang xem danh sách các lớp mình được phân công giảng dạy. |
| **Hậu điều kiện** | Trạng thái điểm danh được lưu vào CSDL. Hệ thống cập nhật tỷ lệ chuyên cần của học viên và gửi dữ liệu về trang cá nhân của học viên đó. |
| **Luồng sự kiện chính** | 1\. Giáo viên chọn chức năng nhập điểm danh. 2\. Hệ thống hiển thị danh sách lớp học. 3\. Giáo viên chọn lớp học. 4\. Hệ thống hiển thị danh sách buổi học. 5\. Giáo viên chọn buổi học. 6\. Hệ thống hiển thị danh sách học viên của lớp. 7\. Giáo viên tiến hành cập nhật trạng thái điểm danh cho từng học viên. 8\. Giáo viên nhấn nút "Ghi nhận điểm danh". 9\. Hệ thống hiển thị thông báo "Điểm danh buổi học thành công".  |
| **Luồng sự kiện phụ** | Không có |

5. **Quản lý tài liệu:**

| Tên Use case | Quản lý tài liệu |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên thêm mới tài liệu học tập (thông qua đường dẫn liên kết) cho các lớp do mình phụ trách. Học viên của lớp tương ứng sẽ lập tức thấy tài liệu này. |
| **Actors** | Giáo viên |
| **Tiền điều kiện** | Giáo viên đăng nhập thành công và phụ trách ít nhất một lớp học. |
| **Hậu điều kiện** | Dữ liệu tài liệu được lưu trữ vào CSDL và tự động đồng bộ sang giao diện xem tài liệu của học viên lớp đó. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện Quản lý tài liệu dành cho giáo viên. 2\. Giáo viên chọn một lớp học đang phụ trách từ danh sách thả xuống. 3\. Hệ thống hiển thị danh sách các tài liệu hiện có của lớp đó (nếu có). 4\. Giáo viên nhấn nút "Thêm tài liệu". 5\. Hệ thống hiển thị form nhập liệu: Tên tài liệu và Đường dẫn (Link). 6\. Giáo viên điền thông tin và nhấn "Lưu". 7\. Hệ thống kiểm tra tính hợp lệ của dữ liệu (không được để trống). 8\. Hệ thống lưu bản ghi vào CSDL (bảng TAILIEU liên kết với MALOPHOC). 9\. Hệ thống thông báo "Thêm tài liệu thành công" và hiển thị tài liệu mới lên lưới dữ liệu. |
| **Luồng sự kiện phụ** | 3a. Lớp học chưa từng có tài liệu nào: Lưới danh sách tài liệu hiển thị rỗng, không báo lỗi.  |
| **Luồng sự kiện lỗi hoặc ngoại lệ** | 7a. Giáo viên bỏ trống Tên tài liệu hoặc Đường dẫn: Hệ thống báo lỗi "Vui lòng nhập đầy đủ thông tin", chặn tiến trình lưu và giữ nguyên form nhập liệu. 8a. Hệ thống mất kết nối tới Cơ sở dữ liệu khi đang lưu: Hiển thị thông báo "Lỗi kết nối máy chủ", tiến trình bị hủy bỏ. |

6. **Xem thống kê lớp học:** 

| Tên Use case | Xem thống kê lớp học |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên xem các thông tin tổng quan (tổng học viên, sĩ số tối đa, trạng thái lớp) và danh sách học viên chi tiết của các lớp do mình phụ trách. |
| **Actors** | Giáo viên |
| **Tiền điều kiện** | Giáo viên đăng nhập thành công vào hệ thống |
| **Hậu điều kiện** | Hệ thống hiển thị thông tin tổng quan và danh sách học viên chính xác theo lớp học được chọn. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện thống kê lớp học. 2\. Hệ thống tải danh sách các lớp học do giáo viên phụ trách (có trạng thái "Đang mở" hoặc "Đang học"). 3\. Giáo viên chọn một lớp học từ danh sách. 4\. Hệ thống truy vấn dữ liệu từ các đối tượng Lớp học, Đăng ký và Học viên. 5\. Hệ thống hiển thị các số liệu tổng quan: Tổng số học viên, Sĩ số tối đa, Trạng thái lớp. 6\. Hệ thống hiển thị danh sách học viên của lớp dưới dạng bảng (Mã HV, Họ tên, Email, SĐT, Trạng thái đăng ký). |
| **Luồng sự kiện phụ** | 3a. Lớp học chưa có học viên nào đăng ký: Hệ thống cập nhật Tổng số học viên bằng 0 và hiển thị bảng danh sách trống. |

