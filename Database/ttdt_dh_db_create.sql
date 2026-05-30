------------------------------------------------------------
-- TRUNG TAM HOC TAP - DATABASE CREATION SCRIPT (PART 1)
-- Oracle SQL - CREATE TABLE + CONSTRAINTS
-- Tham chieu: RDBMS_Updated_Official.md
--             Phan tich CSDL Trung tam Dao Tao
------------------------------------------------------------

------------------------------------------------------------
-- 1. PEOPLE & ACCOUNTS
------------------------------------------------------------

-- VAITRO - Roles
CREATE TABLE VAITRO (
    MAVAITRO    NUMBER,
    TENVAITRO   NVARCHAR2(100) NOT NULL,
    CONSTRAINT pk_vaitro PRIMARY KEY (MAVAITRO),
    CONSTRAINT chk_vaitro_ten CHECK (
        TENVAITRO IN ('Admin', 'GiaoVien', 'HocVien', 'KeToan', 'NhanVienQuanLy')
    )
);

-- TAIKHOAN - System Accounts
CREATE TABLE TAIKHOAN (
    MATAIKHOAN  NUMBER,
    TENTAIKHOAN NVARCHAR2(100) NOT NULL,
    MATKHAU     NVARCHAR2(255) NOT NULL,
    MAVAITRO    NUMBER,
    TRANGTHAITK NVARCHAR2(20),
    NGAYTAO     TIMESTAMP DEFAULT SYSDATE,
    LANDN_CUOI  TIMESTAMP,
    CONSTRAINT pk_taikhoan PRIMARY KEY (MATAIKHOAN),
    CONSTRAINT uq_taikhoan_ten UNIQUE (TENTAIKHOAN),
    CONSTRAINT fk_taikhoan_vaitro FOREIGN KEY (MAVAITRO)
        REFERENCES VAITRO(MAVAITRO),
    CONSTRAINT chk_taikhoan_trangthai CHECK (
        TRANGTHAITK IN ('Hoat dong', 'Vo hieu')
    )
);

-- HOCVIEN - Students
CREATE TABLE HOCVIEN (
    MAHOCVIEN   NUMBER,
    MATAIKHOAN  NUMBER,
    HOTEN       NVARCHAR2(100) NOT NULL,
    NGAYSINH    DATE,
    GIOITINH    NVARCHAR2(10),
    DIACHI      NVARCHAR2(255),
    SDT         NVARCHAR2(15),
    EMAIL       NVARCHAR2(100),
    CONSTRAINT pk_hocvien PRIMARY KEY (MAHOCVIEN),
    CONSTRAINT fk_hocvien_taikhoan FOREIGN KEY (MATAIKHOAN)
        REFERENCES TAIKHOAN(MATAIKHOAN),
    CONSTRAINT chk_hocvien_gioitinh CHECK (
        GIOITINH IN ('Nam', 'Nu')
    ),
    CONSTRAINT uq_hocvien_email UNIQUE (EMAIL),
    CONSTRAINT uq_hocvien_sdt UNIQUE (SDT)
);

-- NHANVIEN - Staff (includes Teachers)
CREATE TABLE NHANVIEN (
    MANHANVIEN  NUMBER,
    HOTEN       NVARCHAR2(100) NOT NULL,
    GIOITINH    NVARCHAR2(10),
    NGAYSINH    DATE,
    SDT         NVARCHAR2(15),
    EMAIL       NVARCHAR2(100),
    CHUCVU      NVARCHAR2(50),
    MATAIKHOAN  NUMBER,
    CONSTRAINT pk_nhanvien PRIMARY KEY (MANHANVIEN),
    CONSTRAINT fk_nhanvien_taikhoan FOREIGN KEY (MATAIKHOAN)
        REFERENCES TAIKHOAN(MATAIKHOAN),
    CONSTRAINT chk_nhanvien_gioitinh CHECK (
        GIOITINH IN ('Nam', 'Nu')
    ),
    CONSTRAINT chk_nhanvien_chucvu CHECK (
        CHUCVU IN ('Giao vien', 'Ke toan', 'Quan ly', 'Admin')
    ),
    CONSTRAINT uq_nhanvien_email UNIQUE (EMAIL),
    CONSTRAINT uq_nhanvien_sdt UNIQUE (SDT)
);

-- GIAOVIEN - Teacher Extension
CREATE TABLE GIAOVIEN (
    MAGIAOVIEN  NUMBER,
    MANHANVIEN  NUMBER,
    MABOMON     NUMBER,
    BANGCAP     NVARCHAR2(100),
    TRANGTHAI   NVARCHAR2(20),
    CONSTRAINT pk_giaovien PRIMARY KEY (MAGIAOVIEN),
    CONSTRAINT fk_giaovien_nhanvien FOREIGN KEY (MANHANVIEN)
        REFERENCES NHANVIEN(MANHANVIEN),
    CONSTRAINT chk_giaovien_trangthai CHECK (
        TRANGTHAI IN ('Dang day', 'Nghi phep', 'Da nghi')
    )
);

-- BOMON - Departments
CREATE TABLE BOMON (
    MABOMON     NUMBER,
    TENBM      NVARCHAR2(100) NOT NULL,
    CONSTRAINT pk_bomon PRIMARY KEY (MABOMON)
);

------------------------------------------------------------
-- 2. ACADEMIC
------------------------------------------------------------

-- PHONGHOC - Classrooms
CREATE TABLE PHONGHOC (
    MAPHONGHOC  NUMBER,
    TENPHONG    NVARCHAR2(100) NOT NULL,
    SUCHUA      NUMBER,
    TRANGTHAI   NVARCHAR2(20),
    CONSTRAINT pk_phonghoc PRIMARY KEY (MAPHONGHOC),
    CONSTRAINT chk_phonghoc_trangthai CHECK (
        TRANGTHAI IN ('San sang', 'Dang dung', 'Bao tri')
    ),
    CONSTRAINT chk_phonghoc_suchua CHECK (SUCHUA > 0)
);

-- KHOAHOC - Courses
CREATE TABLE KHOAHOC (
    MAKHOAHOC    NUMBER,
    TENKH        NVARCHAR2(200) NOT NULL,
    MOTA         CLOB,
    HOCPHITHANG  NUMBER(12,2),
    HOCPHITOANKH NUMBER(12,2),
    MABOMON      NUMBER,
    CAPDO        NVARCHAR2(50),
    CONSTRAINT pk_khoahoc PRIMARY KEY (MAKHOAHOC),
    CONSTRAINT fk_khoahoc_bomon FOREIGN KEY (MABOMON)
        REFERENCES BOMON(MABOMON),
    CONSTRAINT chk_khoahoc_capdo CHECK (
        CAPDO IN ('Dai tra', 'Nang cao')
    ),
    CONSTRAINT chk_hocphi_thang CHECK (HOCPHITHANG IS NULL OR HOCPHITHANG > 0),
    CONSTRAINT chk_hocphi_toankh CHECK (HOCPHITOANKH IS NULL OR HOCPHITOANKH > 0)
);

-- LOPHOC - Classes
CREATE TABLE LOPHOC (
    MALOPHOC    NUMBER,
    TENLOP      NVARCHAR2(100) NOT NULL,
    NGAYBD      DATE,
    NGAYKT      DATE,
    MAKHOAHOC   NUMBER,
    MAGIAOVIEN  NUMBER,
    SISO        NUMBER,
    TANSUAT     NVARCHAR2(50),
    TRANGTHAI   NVARCHAR2(20),
    CONSTRAINT pk_lophoc PRIMARY KEY (MALOPHOC),
    CONSTRAINT fk_lophoc_khoahoc FOREIGN KEY (MAKHOAHOC)
        REFERENCES KHOAHOC(MAKHOAHOC),
    CONSTRAINT fk_lophoc_giaovien FOREIGN KEY (MAGIAOVIEN)
        REFERENCES GIAOVIEN(MAGIAOVIEN),
    CONSTRAINT chk_lophoc_trangthai CHECK (
        TRANGTHAI IN ('Dang mo', 'Dang hoc', 'Ket thuc', 'Huy')
    ),
    CONSTRAINT chk_lophoc_siso CHECK (SISO >= 1 AND SISO <= 25),
    CONSTRAINT chk_lophoc_ngay CHECK (NGAYBD < NGAYKT)
);

-- LICHHOC - Weekly Schedule Templates
CREATE TABLE LICHHOC (
    MALICH      NUMBER,
    MALOP       NUMBER,
    GIOBATDAU   NVARCHAR2(10),
    GIOKETTHUC  NVARCHAR2(10),
    THU         NVARCHAR2(10),
    CONSTRAINT pk_lichhoc PRIMARY KEY (MALICH),
    CONSTRAINT fk_lichhoc_lophoc FOREIGN KEY (MALOP)
        REFERENCES LOPHOC(MALOPHOC),
    CONSTRAINT chk_lichhoc_gio CHECK (GIOBATDAU < GIOKETTHUC),
    CONSTRAINT chk_lichhoc_thu CHECK (
        THU IN ('Thu 2', 'Thu 3', 'Thu 4', 'Thu 5', 'Thu 6', 'Thu 7')
    )
);

-- BUOIHOC - Actual Sessions
CREATE TABLE BUOIHOC (
    MABUOIHOC   NUMBER,
    MALOPHOC    NUMBER,
    MALICH      NUMBER,
    NGAYHOC     DATE NOT NULL,
    GIOHOC      NVARCHAR2(10),
    MAPHONGHOC  NUMBER,
    TRANGTHAI   NVARCHAR2(20),
    LOAIBUOI    NVARCHAR2(20),
    CONSTRAINT pk_buoihoc PRIMARY KEY (MABUOIHOC),
    CONSTRAINT fk_buoihoc_lophoc FOREIGN KEY (MALOPHOC)
        REFERENCES LOPHOC(MALOPHOC),
    CONSTRAINT fk_buoihoc_lichhoc FOREIGN KEY (MALICH)
        REFERENCES LICHHOC(MALICH),
    CONSTRAINT fk_buoihoc_phonghoc FOREIGN KEY (MAPHONGHOC)
        REFERENCES PHONGHOC(MAPHONGHOC),
    CONSTRAINT chk_buoihoc_trangthai CHECK (
        TRANGTHAI IN ('Sap dien ra', 'Dang hoc', 'Hoan thanh', 'Nghi')
    ),
    CONSTRAINT chk_buoihoc_loaibuoi CHECK (
        LOAIBUOI IN ('Thuong', 'Bu', 'Them')
    )
);

-- DIEMDANH - Attendance (Composite PK)
CREATE TABLE DIEMDANH (
    MABUOIHOC   NUMBER,
    MAHOCVIEN   NUMBER,
    TRANGTHAI   NVARCHAR2(20),
    CONSTRAINT pk_diemdanh PRIMARY KEY (MABUOIHOC, MAHOCVIEN),
    CONSTRAINT fk_diemdanh_buoihoc FOREIGN KEY (MABUOIHOC)
        REFERENCES BUOIHOC(MABUOIHOC),
    CONSTRAINT fk_diemdanh_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT chk_diemdanh_trangthai CHECK (
        TRANGTHAI IN ('Co mat', 'Vang mat', 'Tre', 'Nghi phep')
    )
);

-- BAIKIEMTRA - Exams / Tests
CREATE TABLE BAIKIEMTRA (
    MABAIKIEMTRA NUMBER,
    TENBKT       NVARCHAR2(200) NOT NULL,
    NGAYKT       DATE,
    THOIGIAN     NUMBER,
    LOAIBKT      NVARCHAR2(50),
    MALOP        NUMBER,
    DIEMTOIDA    NUMBER,
    DIEMDAT      NUMBER,
    CONSTRAINT pk_baikiemtra PRIMARY KEY (MABAIKIEMTRA),
    CONSTRAINT fk_baikiemtra_lophoc FOREIGN KEY (MALOP)
        REFERENCES LOPHOC(MALOPHOC),
    CONSTRAINT chk_baikiemtra_loai CHECK (
        LOAIBKT IN ('Giua ky', 'Cuoi ky', 'Thuong xuyen')
    ),
    CONSTRAINT chk_bkt_thoigian CHECK (THOIGIAN > 0),
    CONSTRAINT chk_bkt_diemtoida CHECK (DIEMTOIDA > 0),
    CONSTRAINT chk_bkt_diemdat CHECK (DIEMDAT >= 0),
    CONSTRAINT chk_bkt_diemdat_toida CHECK (DIEMDAT <= DIEMTOIDA)
);

-- THAMGIABKT - Exam Participation (Composite PK)
CREATE TABLE THAMGIABKT (
    MAHOCVIEN    NUMBER,
    MABAIKIEMTRA NUMBER,
    LANTHI       NUMBER DEFAULT 1,
    DIEMSO       NUMBER(5,2),
    NHANXET      NVARCHAR2(500),
    NGAYTHI      DATE,
    THOIGIANTHI  TIMESTAMP,
    CONSTRAINT pk_thamgiabkt PRIMARY KEY (MAHOCVIEN, MABAIKIEMTRA, LANTHI),
    CONSTRAINT fk_thamgiabkt_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT fk_thamgiabkt_bkt FOREIGN KEY (MABAIKIEMTRA)
        REFERENCES BAIKIEMTRA(MABAIKIEMTRA),
    CONSTRAINT chk_tbkt_diemso CHECK (DIEMSO IS NULL OR DIEMSO >= 0),
    CONSTRAINT chk_tbkt_lanthi CHECK (LANTHI >= 1)
);

-- TAILIEU - Learning Materials
CREATE TABLE TAILIEU (
    MATAILIEU   NUMBER,
    TENTAILIEU  NVARCHAR2(200) NOT NULL,
    LINK        NVARCHAR2(500),
    MALOPHOC    NUMBER,
    CONSTRAINT pk_tailieu PRIMARY KEY (MATAILIEU),
    CONSTRAINT fk_tailieu_lophoc FOREIGN KEY (MALOPHOC)
        REFERENCES LOPHOC(MALOPHOC)
);

------------------------------------------------------------
-- 3. ENROLLMENT & CLASS TRANSFER
------------------------------------------------------------

-- HOCBONG - Scholarships (tao truoc HOADONHOCPHI vi HOADON tham chieu HOCBONG)
CREATE TABLE HOCBONG (
    MAHOCBONG    NUMBER,
    MAHOCVIEN    NUMBER,
    GIATRI       NUMBER(12,2),
    NGAYCAP      DATE,
    TRANGTHAIHB  NVARCHAR2(20),
    CONSTRAINT pk_hocbong PRIMARY KEY (MAHOCBONG),
    CONSTRAINT fk_hocbong_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT chk_hocbong_trangthai CHECK (
        TRANGTHAIHB IN ('Da nhan', 'Chua nhan')
    ),
    CONSTRAINT chk_hocbong_giatri CHECK (GIATRI > 0)
);

-- HOADONHOCPHI - Invoices (self-referencing FK for MAHOADON_GOC)
CREATE TABLE HOADONHOCPHI (
    MAHOADON     NUMBER,
    MAHOCVIEN    NUMBER,
    MAHOCBONG    NUMBER,
    MALOP        NUMBER,
    NGAYLAP      DATE DEFAULT SYSDATE,
    TONGTIEN     NUMBER(12,2),
    TRANGTHAIHD  NVARCHAR2(20),
    LOAIHD       NVARCHAR2(50),
    HINHTHUC     NVARCHAR2(50),
    HINHTHUCTT   NVARCHAR2(20),
    THANGTT      NUMBER(2),
    NAMTT        NUMBER(4),
    MAHOADON_GOC NUMBER,
    GHICHU       NVARCHAR2(500),
    CONSTRAINT pk_hoadonhocphi PRIMARY KEY (MAHOADON),
    CONSTRAINT fk_hoadon_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT fk_hoadon_hocbong FOREIGN KEY (MAHOCBONG)
        REFERENCES HOCBONG(MAHOCBONG),
    CONSTRAINT fk_hoadon_lophoc FOREIGN KEY (MALOP)
        REFERENCES LOPHOC(MALOPHOC),
    CONSTRAINT fk_hoadon_goc FOREIGN KEY (MAHOADON_GOC)
        REFERENCES HOADONHOCPHI(MAHOADON),
    CONSTRAINT chk_hoadon_trangthai CHECK (
        TRANGTHAIHD IN ('Cho thanh toan', 'Da thanh toan', 'Huy')
    ),
    CONSTRAINT chk_hoadon_loai CHECK (
        LOAIHD IN ('Dong tien hoc phi', 'Hoc bong', 'Dieu chinh', 'Phi tre han')
    ),
    CONSTRAINT chk_hoadon_hinhthuc CHECK (
        HINHTHUC IN ('Tien mat', 'Chuyen khoan truc tiep')
    ),
    CONSTRAINT chk_hoadon_hinhthuctt CHECK (
        HINHTHUCTT IN ('1 thang', 'Toan khoa')
    ),
    -- Hoa don am chi duoc phep khi la hoa don dieu chinh
    CONSTRAINT chk_hd_negative_requires_dieuchinh CHECK (
        TONGTIEN >= 0 OR LOAIHD = 'Dieu chinh'
    ),
    -- Hoa don dieu chinh phai co hoa don goc tham chieu
    CONSTRAINT chk_hd_dieuchinh_needs_goc CHECK (
        LOAIHD <> 'Dieu chinh' OR MAHOADON_GOC IS NOT NULL
    ),
    -- Dong bo hoa chu ky dong hoc phi:
    -- Neu 'Toan khoa' => THANGTT va NAMTT phai NULL
    -- Neu '1 thang' => THANGTT (1-12) va NAMTT phai co gia tri
    CONSTRAINT chk_hoadon_chuky CHECK (
        (HINHTHUCTT = 'Toan khoa' AND THANGTT IS NULL AND NAMTT IS NULL)
        OR
        (HINHTHUCTT = '1 thang' AND THANGTT BETWEEN 1 AND 12 AND NAMTT IS NOT NULL)
        OR
        HINHTHUCTT IS NULL
    )
);

-- DANGKY - Enrollments (Composite PK)
CREATE TABLE DANGKY (
    MAHOCVIEN    NUMBER,
    MALOPHOC     NUMBER,
    NGAYDANGKY   DATE DEFAULT SYSDATE,
    TRANGTHAIDKY NVARCHAR2(30),
    MAHOADON     NUMBER,
    HINHTHUCTT   NVARCHAR2(20),
    CONSTRAINT pk_dangky PRIMARY KEY (MAHOCVIEN, MALOPHOC),
    CONSTRAINT fk_dangky_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT fk_dangky_lophoc FOREIGN KEY (MALOPHOC)
        REFERENCES LOPHOC(MALOPHOC),
    CONSTRAINT fk_dangky_hoadon FOREIGN KEY (MAHOADON)
        REFERENCES HOADONHOCPHI(MAHOADON),
    CONSTRAINT chk_dangky_trangthai CHECK (
        TRANGTHAIDKY IN ('Dang hoc', 'Hoan thanh', 'Huy', 'Chuyen lop', 'Cho thanh toan')
    ),
    CONSTRAINT chk_dangky_hinhthuctt CHECK (
        HINHTHUCTT IN ('1 thang', 'Toan khoa')
    )
);

-- YEUCAUCHUYENLOP - Class Transfer Requests
CREATE TABLE YEUCAUCHUYENLOP (
    MAYEUCAU    NUMBER,
    MAHOCVIEN   NUMBER,
    MALOPCU     NUMBER,
    MALOPMOI    NUMBER,
    LYDO        NVARCHAR2(500),
    NGAYYEUCAU  DATE DEFAULT SYSDATE,
    TRANGTHAI   NVARCHAR2(20),
    MANHANVIEN  NUMBER,
    NGAYXULY    DATE,
    GHICHU      NVARCHAR2(500),
    CONSTRAINT pk_yeucauchuyenlop PRIMARY KEY (MAYEUCAU),
    CONSTRAINT fk_yccl_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT fk_yccl_lopcu FOREIGN KEY (MALOPCU)
        REFERENCES LOPHOC(MALOPHOC) ON DELETE CASCADE,
    CONSTRAINT fk_yccl_lopmoi FOREIGN KEY (MALOPMOI)
        REFERENCES LOPHOC(MALOPHOC) ON DELETE CASCADE,
    CONSTRAINT fk_yccl_nhanvien FOREIGN KEY (MANHANVIEN)
        REFERENCES NHANVIEN(MANHANVIEN),
    CONSTRAINT chk_yccl_trangthai CHECK (
        TRANGTHAI IN ('Cho duyet', 'Chap thuan', 'Tu choi')
    ),
    CONSTRAINT chk_yccl_khac_lop CHECK (MALOPCU <> MALOPMOI)
);

------------------------------------------------------------
-- 4. FINANCIAL
------------------------------------------------------------

-- HOANTRA - Refund Records
CREATE TABLE HOANTRA (
    MAHOANTRA   NUMBER,
    MAHOADON    NUMBER,
    MAHOCVIEN   NUMBER,
    SOTIEN      NUMBER(12,2) NOT NULL,
    LYDO        NVARCHAR2(500),
    NGAYYEUCAU  DATE DEFAULT SYSDATE,
    NGAYHOANTRA DATE,
    TRANGTHAI   NVARCHAR2(20),
    MANHANVIEN  NUMBER,
    NGAYTAO     TIMESTAMP DEFAULT SYSDATE,
    HINHTHUC    NVARCHAR2(100),
    NGUOITAO    NUMBER,
    CONSTRAINT pk_hoantra PRIMARY KEY (MAHOANTRA),
    CONSTRAINT fk_hoantra_hoadon FOREIGN KEY (MAHOADON)
        REFERENCES HOADONHOCPHI(MAHOADON),
    CONSTRAINT fk_hoantra_hocvien FOREIGN KEY (MAHOCVIEN)
        REFERENCES HOCVIEN(MAHOCVIEN),
    CONSTRAINT fk_hoantra_nhanvien FOREIGN KEY (MANHANVIEN)
        REFERENCES NHANVIEN(MANHANVIEN),
    CONSTRAINT fk_hoantra_nguoitao FOREIGN KEY (NGUOITAO)
        REFERENCES TAIKHOAN(MATAIKHOAN),
    CONSTRAINT chk_hoantra_trangthai CHECK (
        TRANGTHAI IN ('Cho duyet', 'Chap thuan', 'Tu choi')
    ),
    CONSTRAINT chk_hoantra_sotien CHECK (SOTIEN > 0),
    -- Ngay xu ly hoan tra phai sau ngay gui yeu cau
    CONSTRAINT chk_hoantra_ngay CHECK (
        NGAYHOANTRA IS NULL OR NGAYHOANTRA >= NGAYYEUCAU
    ),
    CONSTRAINT chk_hoantra_hinhthuc CHECK (
        HINHTHUC IN ('Tien mat')
    )
);

------------------------------------------------------------
-- 5. SYSTEM & SCHEDULING
------------------------------------------------------------

-- NGAYNGHILE - Public Holidays
CREATE TABLE NGAYNGHILE (
    MANGAYNGHI  NUMBER,
    NGAYBATDAU  DATE NOT NULL,
    NGAYKETTHUC DATE NOT NULL,
    CONSTRAINT pk_ngaynghile PRIMARY KEY (MANGAYNGHI),
    CONSTRAINT chk_ngaynghi_dates CHECK (NGAYBATDAU <= NGAYKETTHUC)
);

------------------------------------------------------------
-- HOAN TAT TAO BANG - END OF PART 1
------------------------------------------------------------



