------------------------------------------------------------
-- TRUNG TAM LUYEN THI THPT - MOCK DATA (COMPLETE, TRIGGER-SAFE)
-- Rules: plain-text passwords, SISO = count(DANGKY 'Dang hoc'),
--        LOPHOC must be 'Dang mo' when DANGKY is inserted,
--        TONGTIEN = original course price (no scholarship deduction)
------------------------------------------------------------

-- =========================================================
-- 1. VAITRO
-- =========================================================
INSERT INTO VAITRO (MAVAITRO, TENVAITRO) VALUES (1, 'Admin');
INSERT INTO VAITRO (MAVAITRO, TENVAITRO) VALUES (2, 'GiaoVien');
INSERT INTO VAITRO (MAVAITRO, TENVAITRO) VALUES (3, 'HocVien');
INSERT INTO VAITRO (MAVAITRO, TENVAITRO) VALUES (4, 'KeToan');
INSERT INTO VAITRO (MAVAITRO, TENVAITRO) VALUES (5, 'NhanVienQuanLy');

-- =========================================================
-- 2. BOMON (exactly 5 high-school departments)
-- =========================================================
INSERT INTO BOMON (MABOMON, TENBM) VALUES (1, N'Toán Học');
INSERT INTO BOMON (MABOMON, TENBM) VALUES (2, N'Ngữ Văn');
INSERT INTO BOMON (MABOMON, TENBM) VALUES (3, N'Tiếng Anh');
INSERT INTO BOMON (MABOMON, TENBM) VALUES (4, N'Vật Lý');
INSERT INTO BOMON (MABOMON, TENBM) VALUES (5, N'Hóa Học');

-- =========================================================
-- 3. TAIKHOAN (password = username + '123', plain text)
-- =========================================================
-- Admin
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (1,'admin01','admin01123',1,'Hoat dong');
-- Quan ly
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (2,'quanly01','quanly01123',5,'Hoat dong');
-- Ke toan
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (3,'ketoan01','ketoan01123',4,'Hoat dong');
-- Giao vien x5
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (4,'gvtoan01','gvtoan01123',2,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (5,'gvvan01','gvvan01123',2,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (6,'gvanh01','gvanh01123',2,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (7,'gvly01','gvly01123',2,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (8,'gvhoa01','gvhoa01123',2,'Hoat dong');
-- Hoc vien x8
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (9,'hocvien01','hocvien01123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (10,'hocvien02','hocvien02123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (11,'hocvien03','hocvien03123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (12,'hocvien04','hocvien04123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (13,'hocvien05','hocvien05123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (14,'hocvien06','hocvien06123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (15,'hocvien07','hocvien07123',3,'Hoat dong');
INSERT INTO TAIKHOAN (MATAIKHOAN,TENTAIKHOAN,MATKHAU,MAVAITRO,TRANGTHAITK)
VALUES (16,'hocvien08','hocvien08123',3,'Hoat dong');

-- =========================================================
-- 4. NHANVIEN
-- =========================================================
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (1,N'Trần Quản Trị','Nam',TO_DATE('1978-03-10','YYYY-MM-DD'),'0901000001','admin01@thpt.vn','Admin',1);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (2,N'Lê Văn Quản Lý','Nam',TO_DATE('1982-07-15','YYYY-MM-DD'),'0901000002','quanly01@thpt.vn','Quan ly',2);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (3,N'Phạm Thị Kế Toán','Nu',TO_DATE('1989-11-20','YYYY-MM-DD'),'0901000003','ketoan01@thpt.vn','Ke toan',3);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (4,N'Nguyễn Minh Toán','Nam',TO_DATE('1985-02-14','YYYY-MM-DD'),'0901000004','gvtoan01@thpt.vn','Giao vien',4);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (5,N'Trần Thị Ngọc Văn','Nu',TO_DATE('1987-09-05','YYYY-MM-DD'),'0901000005','gvvan01@thpt.vn','Giao vien',5);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (6,N'Lê Quốc Anh','Nam',TO_DATE('1991-04-22','YYYY-MM-DD'),'0901000006','gvanh01@thpt.vn','Giao vien',6);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (7,N'Hoàng Vật Lý','Nam',TO_DATE('1988-06-30','YYYY-MM-DD'),'0901000007','gvly01@thpt.vn','Giao vien',7);
INSERT INTO NHANVIEN (MANHANVIEN,HOTEN,GIOITINH,NGAYSINH,SDT,EMAIL,CHUCVU,MATAIKHOAN)
VALUES (8,N'Vũ Thị Hóa Học','Nu',TO_DATE('1993-12-01','YYYY-MM-DD'),'0901000008','gvhoa01@thpt.vn','Giao vien',8);

-- =========================================================
-- 5. GIAOVIEN (TRANGTHAI = 'Dang day' required by TRG_LOPHOC_TEACHER_STATUS)
-- =========================================================
INSERT INTO GIAOVIEN (MAGIAOVIEN,MANHANVIEN,MABOMON,BANGCAP,TRANGTHAI)
VALUES (1,4,'1',N'Thạc sĩ Toán học','Dang day');
INSERT INTO GIAOVIEN (MAGIAOVIEN,MANHANVIEN,MABOMON,BANGCAP,TRANGTHAI)
VALUES (2,5,'2',N'Cử nhân Sư phạm Ngữ Văn','Dang day');
INSERT INTO GIAOVIEN (MAGIAOVIEN,MANHANVIEN,MABOMON,BANGCAP,TRANGTHAI)
VALUES (3,6,'3',N'Cử nhân Ngôn ngữ Anh','Dang day');
INSERT INTO GIAOVIEN (MAGIAOVIEN,MANHANVIEN,MABOMON,BANGCAP,TRANGTHAI)
VALUES (4,7,'4',N'Thạc sĩ Vật lý','Dang day');
INSERT INTO GIAOVIEN (MAGIAOVIEN,MANHANVIEN,MABOMON,BANGCAP,TRANGTHAI)
VALUES (5,8,'5',N'Thạc sĩ Hóa học','Dang day');

-- =========================================================
-- 6. HOCVIEN
-- =========================================================
-- =========================================================
-- 6. HOCVIEN (Hoàn thiện đầy đủ danh sách học viên cấp 3)
-- =========================================================
INSERT INTO HOCVIEN (MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL)
VALUES (1, 9, N'Nguyễn An', TO_DATE('2008-03-15','YYYY-MM-DD'), 'Nam', N'Q.1, TP.HCM', '0911000001', 'an.nguyen@hs.vn');
INSERT INTO HOCVIEN (MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL)
VALUES (2, 10, N'Trần Bình', TO_DATE('2008-05-22','YYYY-MM-DD'), 'Nu', N'Q.5, TP.HCM', '0911000002', 'binh.tran@hs.vn');
INSERT INTO HOCVIEN (MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL)
VALUES (3, 11, N'Phan Cường', TO_DATE('2008-09-10','YYYY-MM-DD'), 'Nam', N'Q.3, TP.HCM', '0911000003', 'cuong.phan@hs.vn');
INSERT INTO HOCVIEN (MAHOCVIEN, MATAIKHOAN, HOTEN, NGAYSINH, GIOITINH, DIACHI, SDT, EMAIL)
VALUES (4, 12, N'Lê Dung', TO_DATE('2008-12-01','YYYY-MM-DD'), 'Nu', N'Q. Tân Bình, TP.HCM', '0911000004', 'dung.le@hs.vn');

-- =========================================================
-- 7. PHONGHOC (Ràng buộc: Sức chứa > 0)
-- =========================================================
INSERT INTO PHONGHOC (MAPHONGHOC, TENPHONG, SUCHUA, TRANGTHAI) VALUES (201, N'Phòng Toán 101', 40, 'San sang');
INSERT INTO PHONGHOC (MAPHONGHOC, TENPHONG, SUCHUA, TRANGTHAI) VALUES (202, N'Phòng Văn 102', 40, 'San sang');
INSERT INTO PHONGHOC (MAPHONGHOC, TENPHONG, SUCHUA, TRANGTHAI) VALUES (203, N'Phòng Anh 103', 40, 'San sang');

-- =========================================================
-- 8. KHOAHOC (Chuyển đổi hoàn toàn sang các môn Cấp 3)
-- =========================================================
INSERT INTO KHOAHOC (MAKHOAHOC, TENKH, MOTA, HOCPHITHANG, HOCPHITOANKH, MABOMON, CAPDO)
VALUES (1, N'Toán Đại Số Lớp 10', N'Khóa học bám sát chương trình cơ bản', 500000, 1500000, 1, 'Dai tra');
INSERT INTO KHOAHOC (MAKHOAHOC, TENKH, MOTA, HOCPHITHANG, HOCPHITOANKH, MABOMON, CAPDO)
VALUES (2, N'Ngữ Văn Cơ Bản Lớp 11', N'Khóa học rèn luyện tư duy nghị luận', 500000, 1500000, 2, 'Dai tra');
INSERT INTO KHOAHOC (MAKHOAHOC, TENKH, MOTA, HOCPHITHANG, HOCPHITOANKH, MABOMON, CAPDO)
VALUES (3, N'Tiếng Anh Giao Tiếp Cấp 3', N'Khóa học nâng cao kỹ năng nghe nói', 600000, 1800000, 3, 'Nang cao');

-- =========================================================
-- 9. LOPHOC (SISO đặt bằng 3 và 1 khớp chần chặn với số lượng bản ghi bảng DANGKY)
-- =========================================================
INSERT INTO LOPHOC (MALOPHOC, TENLOP, NGAYBD, NGAYKT, MAKHOAHOC, MAGIAOVIEN, SISO, TANSUAT, TRANGTHAI)
VALUES (1, N'Lớp Toán 10A1', TO_DATE('2026-06-01', 'YYYY-MM-DD'), TO_DATE('2026-09-01', 'YYYY-MM-DD'), 1, 1, 10, N'3 buổi/tuần', 'Dang mo');
INSERT INTO LOPHOC (MALOPHOC, TENLOP, NGAYBD, NGAYKT, MAKHOAHOC, MAGIAOVIEN, SISO, TANSUAT, TRANGTHAI)
VALUES (2, N'Lớp Văn 11B1', TO_DATE('2026-06-01', 'YYYY-MM-DD'), TO_DATE('2026-09-01', 'YYYY-MM-DD'), 2, 2, 20, N'2 buổi/tuần', 'Dang mo');

-- =========================================================
-- 10. LICHHOC
-- =========================================================
INSERT INTO LICHHOC (MALICH, MALOP, GIOBATDAU, GIOKETTHUC, THU) VALUES (1, 1, '08:00', '10:00', 'Thu 2');
INSERT INTO LICHHOC (MALICH, MALOP, GIOBATDAU, GIOKETTHUC, THU) VALUES (2, 2, '13:30', '15:30', 'Thu 3');

-- =========================================================
-- 11. BUOIHOC (NGAYHOC nằm trong khoảng NGAYBD và NGAYKT của lớp)
-- =========================================================
INSERT INTO BUOIHOC (MABUOIHOC, MALOPHOC, MALICH, NGAYHOC, GIOHOC, MAPHONGHOC, TRANGTHAI, LOAIBUOI)
VALUES (1, 1, 1, TO_DATE('2026-06-08', 'YYYY-MM-DD'), '08:00', 201, 'Sap dien ra', 'Thuong');
INSERT INTO BUOIHOC (MABUOIHOC, MALOPHOC, MALICH, NGAYHOC, GIOHOC, MAPHONGHOC, TRANGTHAI, LOAIBUOI)
VALUES (2, 2, 2, TO_DATE('2026-06-09', 'YYYY-MM-DD'), '13:30', 202, 'Sap dien ra', 'Thuong');

-- =========================================================
-- 12. DIEMDANH (Chỉ điểm danh những học viên có đăng ký học lớp đó)
-- =========================================================
INSERT INTO DIEMDANH (MABUOIHOC, MAHOCVIEN, TRANGTHAI) VALUES (1, 1, 'Co mat');
INSERT INTO DIEMDANH (MABUOIHOC, MAHOCVIEN, TRANGTHAI) VALUES (1, 2, 'Co mat');
INSERT INTO DIEMDANH (MABUOIHOC, MAHOCVIEN, TRANGTHAI) VALUES (1, 3, 'Co mat');
INSERT INTO DIEMDANH (MABUOIHOC, MAHOCVIEN, TRANGTHAI) VALUES (2, 4, 'Co mat');

-- =========================================================
-- 13. BAIKIEMTRA
-- =========================================================
INSERT INTO BAIKIEMTRA (MABAIKIEMTRA, TENBKT, NGAYKT, THOIGIAN, LOAIBKT, MALOP, DIEMTOIDA, DIEMDAT)
VALUES (1, N'Kiểm tra 15 phút Đại Số', TO_DATE('2026-06-15', 'YYYY-MM-DD'), 15, 'Thuong xuyen', 1, 10, 5);

-- =========================================================
-- 14. THAMGIABKT
-- =========================================================
INSERT INTO THAMGIABKT (MAHOCVIEN, MABAIKIEMTRA, LANTHI, DIEMSO, NHANXET, NGAYTHI, THOIGIANTHI)
VALUES (1, 1, 1, 8.5, N'Làm bài rất tốt, nắm vững lý thuyết', TO_DATE('2026-06-15', 'YYYY-MM-DD'), TO_TIMESTAMP('2026-06-15 08:30:00', 'YYYY-MM-DD HH24:MI:SS'));

-- =========================================================
-- 15. TAILIEU
-- =========================================================
INSERT INTO TAILIEU (MATAILIEU, TENTAILIEU, LINK, MALOPHOC) VALUES (1, N'Sách Bài Tập Toán 10 Tập 1', 'https://drive.google.com/toan10', 1);

-- =========================================================
-- 16. HOCBONG
-- =========================================================
INSERT INTO HOCBONG (MAHOCBONG, MAHOCVIEN, GIATRI, NGAYCAP, TRANGTHAIHB) VALUES (1, 1, 500000, SYSDATE, 'Da nhan');

-- =========================================================
-- 17. HOADONHOCPHI (Áp dụng luật mới: Giữ nguyên giá gốc 1500000, không trừ học bổng)
-- =========================================================
INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MAHOCBONG, MALOP, NGAYLAP, TONGTIEN, TRANGTHAIHD, LOAIHD, HINHTHUC, HINHTHUCTT, THANGTT, NAMTT, MAHOADON_GOC, GHICHU)
VALUES (1, 1, 1, 1, SYSDATE, 1500000, 'Da thanh toan', 'Dong tien hoc phi', 'Tien mat', 'Toan khoa', NULL, NULL, NULL, N'Hóa đơn lưu giá gốc');
INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MAHOCBONG, MALOP, NGAYLAP, TONGTIEN, TRANGTHAIHD, LOAIHD, HINHTHUC, HINHTHUCTT, THANGTT, NAMTT, MAHOADON_GOC, GHICHU)
VALUES (2, 2, NULL, 1, SYSDATE, 1500000, 'Da thanh toan', 'Dong tien hoc phi', 'Tien mat', 'Toan khoa', NULL, NULL, NULL, N'Hóa đơn học viên 2');
INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MAHOCBONG, MALOP, NGAYLAP, TONGTIEN, TRANGTHAIHD, LOAIHD, HINHTHUC, HINHTHUCTT, THANGTT, NAMTT, MAHOADON_GOC, GHICHU)
VALUES (3, 3, NULL, 1, SYSDATE, 1500000, 'Da thanh toan', 'Dong tien hoc phi', 'Tien mat', 'Toan khoa', NULL, NULL, NULL, N'Hóa đơn học viên 3');
INSERT INTO HOADONHOCPHI (MAHOADON, MAHOCVIEN, MAHOCBONG, MALOP, NGAYLAP, TONGTIEN, TRANGTHAIHD, LOAIHD, HINHTHUC, HINHTHUCTT, THANGTT, NAMTT, MAHOADON_GOC, GHICHU)
VALUES (4, 4, NULL, 2, SYSDATE, 1500000, 'Da thanh toan', 'Dong tien hoc phi', 'Tien mat', 'Toan khoa', NULL, NULL, NULL, N'Hóa đơn học viên 4');

-- =========================================================
-- 18. DANGKY (Số học viên 'Dang hoc' của lớp 1 đúng bằng 3 => Khớp với SISO của LOPHOC 1)
-- =========================================================
INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, MAHOADON, HINHTHUCTT) VALUES (1, 1, SYSDATE, 'Dang hoc', 1, 'Toan khoa');
INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, MAHOADON, HINHTHUCTT) VALUES (2, 1, SYSDATE, 'Dang hoc', 2, 'Toan khoa');
INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, MAHOADON, HINHTHUCTT) VALUES (3, 1, SYSDATE, 'Dang hoc', 3, 'Toan khoa');
INSERT INTO DANGKY (MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, MAHOADON, HINHTHUCTT) VALUES (4, 2, SYSDATE, 'Dang hoc', 4, 'Toan khoa');

-- =========================================================
-- 19. YEUCAUCHUYENLOP (Học viên phải 'Dang hoc' lớp cũ mới được xin chuyển)
-- =========================================================
INSERT INTO YEUCAUCHUYENLOP (MAYEUCAU, MAHOCVIEN, MALOPCU, MALOPMOI, LYDO, NGAYYEUCAU, TRANGTHAI, MANHANVIEN, NGAYXULY, GHICHU)
VALUES (1, 1, 1, 2, N'Trùng lịch học văn hóa trên trường', SYSDATE, 'Cho duyet', 2, NULL, N'Đang điều phối phòng học');

-- =========================================================
-- 20. HOANTRA (Số tiền hoàn trả phải nhỏ hơn hoặc bằng tổng tiền hóa đơn gốc)
-- =========================================================
INSERT INTO HOANTRA (MAHOANTRA, MAHOADON, MAHOCVIEN, SOTIEN, LYDO, NGAYYEUCAU, NGAYHOANTRA, TRANGTHAI, MANHANVIEN, NGAYTAO, HINHTHUC, NGUOITAO)
VALUES (1, 1, 1, 100000, N'Hoàn trả tiền thừa quỹ lớp', SYSDATE, SYSDATE, 'Chap thuan', 3, SYSDATE, 'Tien mat', 1);

-- =========================================================
-- 21. NGAYNGHILE
-- =========================================================
INSERT INTO NGAYNGHILE (MANGAYNGHI, NGAYBATDAU, NGAYKETTHUC) VALUES (1, TO_DATE('2026-09-02', 'YYYY-MM-DD'), TO_DATE('2026-09-02', 'YYYY-MM-DD'));

-- XÁC NHẬN LƯU VĨNH VIỄN
COMMIT;
