1. **Xem lịch dạy**	

| Tên Use case | Xem lịch dạy  |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên tra cứu thời khóa biểu giảng dạy của các lớp do mình phụ trách dưới dạng lưới phân theo từng tuần. |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đã đăng nhập thành công vào hệ thống. |
| **Hậu điều kiện** | Hệ thống hiển thị lưới thời khóa biểu chính xác của tuần được chọn. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện Lịch Giảng Dạy. 2\. Hệ thống tự động truy vấn dữ liệu lớp học đang phụ trách và lịch học tương ứng. 3\. Hệ thống hiển thị thời khóa biểu dạng lưới của tuần hiện tại. 4\. Giáo viên có thể nhấn các nút "\<", "\>" hoặc "Hôm nay" để tra cứu lịch của các tuần khác. |
| **Luồng sự kiện phụ** | 3a. Chưa có lịch dạy: Lưới thời khóa biểu hiển thị trống và góc dưới hiển thị thông báo "Chưa có lịch giảng dạy." |

2. **Nhập điểm** 

| Tên Use case | Nhập điểm |
| :---- | :---- |
| **Mô tả Use case** | Cho phép giáo viên nhập điểm số và lời nhận xét cho các bài kiểm tra của học viên trong lớp. Giáo viên cũng có thể tạo mới một bài kiểm tra cho lớp. |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đăng nhập thành công và phụ trách ít nhất một lớp học. |
| **Hậu điều kiện** | Điểm số và nhận xét của học viên được cập nhật vào cơ sở dữ liệu để học viên có thể xem. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện Nhập Điểm Bài Kiểm Tra. 2\. Hệ thống tự động chọn lớp học đầu tiên và tự động chọn bài kiểm tra mới nhất của lớp đó. 3\. Hệ thống hiển thị danh sách học viên kèm cột Điểm và Nhận xét. 4\. Giáo viên có thể đổi Lớp hoặc Bài kiểm tra khác từ danh sách thả xuống. 5\. Giáo viên nhập điểm số và ghi chú vào lưới dữ liệu. 6\. Giáo viên nhấn "Lưu Điểm". 7\. Hệ thống kiểm tra định dạng điểm. 8\. Hệ thống cập nhật vào CSDL. 9\. Hệ thống hiển thị thông báo: "Lưu điểm thành công\!". |
| **Luồng sự kiện phụ** | 3a. Lớp chưa có Bài kiểm tra nào: Bảng rỗng, có nút yêu cầu tạo BKT. 4a. Giáo viên bấm "+ Tạo BKT": Điền form \-\> Hệ thống lưu BKT mới vào CSDL \-\> Tự động load BKT vừa tạo. |
| **Luồng sự kiện lỗi hoặc ngoại lệ** | 4a-1. Ngày tạo BKT sai định dạng: Báo lỗi "Ngày không hợp lệ". 7a. Bỏ trống hoặc nhập chữ/số âm: Báo lỗi "Vui lòng nhập số hợp lệ" hoặc "Điểm không được để trống". |

3. **Điểm danh:**

| Tên Use case | Điểm danh |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên ghi nhận trạng thái của học viên theo từng buổi học. Hỗ trợ tạo mới buổi học. |
| **Actors** | Giáo viên |
| **Tiền điều kiện**  | Giáo viên đăng nhập thành công và phụ trách ít nhất một lớp học. |
| **Hậu điều kiện** | Trạng thái điểm danh được lưu vào CSDL. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện Điểm danh. 2\. Hệ thống tự động chọn lớp học đầu tiên và buổi học mới nhất.  3\. Hệ thống hiển thị danh sách học viên ra bảng. 4\. Giáo viên chỉnh sửa cột Trạng thái của từng học viên. 5\. Giáo viên nhấn "Lưu Điểm Danh". 6\. Hệ thống hiển thị thông báo thành công. |
| **Luồng sự kiện phụ** | 3a. Lớp chưa có buổi học: Bảng rỗng, hiển thị thông báo yêu cầu tạo buổi học. 3b. Đổi lớp/buổi học: Giáo viên chủ động chọn một Lớp hoặc Buổi học khác từ danh sách. Hệ thống load lại danh sách học viên tương ứng. 4a. Giáo viên bấm "+ Tạo Buổi Học": Điền form \-\> Lưu CSDL \-\> Tự động load buổi học mới. |
| **Luồng sự kiện lỗi hoặc ngoại lệ** | 4a-1. Lỗi định dạng ngày khi tạo: Báo lỗi "Ngày không hợp lệ". |

4. **Quản lý tài liệu:**

| Tên Use case | Quản lý tài liệu |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên thêm mới tài liệu học tập cho các lớp do mình phụ trách. Học viên của lớp tương ứng sẽ lập tức thấy tài liệu này. |
| **Actors** | Giáo viên |
| **Tiền điều kiện** | Giáo viên đăng nhập thành công và phụ trách ít nhất một lớp học. |
| **Hậu điều kiện** | Dữ liệu tài liệu được lưu trữ vào CSDL và tự động đồng bộ sang giao diện xem tài liệu của học viên lớp đó. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện Quản lý tài liệu dành cho giáo viên. 2\. Giáo viên chọn một lớp học đang phụ trách từ danh sách thả xuống. 3\. Hệ thống hiển thị danh sách các tài liệu hiện có của lớp đó. 4\. Giáo viên nhấn nút "Thêm tài liệu". 5\. Hệ thống hiển thị form nhập liệu: Tên tài liệu và Đường dẫn. 6\. Giáo viên điền thông tin và nhấn "Lưu". 7\. Hệ thống kiểm tra tính hợp lệ của dữ liệu. 8\. Hệ thống lưu bản ghi vào CSDL 9\. Hệ thống thông báo "Thêm tài liệu thành công" và hiển thị tài liệu mới lên lưới dữ liệu. |
| **Luồng sự kiện phụ** | 3a. Lớp học chưa từng có tài liệu nào: Lưới danh sách tài liệu hiển thị rỗng, không báo lỗi.  |
| **Luồng sự kiện lỗi hoặc ngoại lệ** | 7a. Giáo viên bỏ trống Tên tài liệu hoặc Đường dẫn: Hệ thống báo lỗi, chặn tiến trình lưu và giữ nguyên form nhập liệu. 8a. Hệ thống mất kết nối tới Cơ sở dữ liệu khi đang lưu: Hiển thị thông báo lỗi, tiến trình bị hủy bỏ. |

5. **Xem thống kê lớp học:** 

| Tên Use case | Xem thống kê lớp học |
| :---- | :---- |
| **Mô tả Use case** | Giáo viên xem các thông tin tổng quan và danh sách học viên chi tiết của các lớp do mình phụ trách. |
| **Actors** | Giáo viên |
| **Tiền điều kiện** | Giáo viên đăng nhập thành công vào hệ thống |
| **Hậu điều kiện** | Hệ thống hiển thị thông tin tổng quan và danh sách học viên chính xác theo lớp học được chọn. |
| **Luồng sự kiện chính** | 1\. Hệ thống hiển thị giao diện thống kê lớp học. 2\. Hệ thống tải danh sách các lớp học do giáo viên phụ trách (có trạng thái "Đang mở" hoặc "Đang học"). 3\. Giáo viên chọn một lớp học từ danh sách. 4\. Hệ thống truy vấn dữ liệu từ các đối tượng Lớp học, Đăng ký và Học viên. 5\. Hệ thống hiển thị các số liệu tổng quan: Tổng số học viên, Sĩ số tối đa, Trạng thái lớp. 6\. Hệ thống hiển thị danh sách học viên của lớp dưới dạng bảng cùng với thông tin học viên. |
| **Luồng sự kiện phụ** | 3a. Lớp học chưa có học viên nào đăng ký: Hệ thống cập nhật Tổng số học viên bằng 0 và hiển thị bảng danh sách trống. |

