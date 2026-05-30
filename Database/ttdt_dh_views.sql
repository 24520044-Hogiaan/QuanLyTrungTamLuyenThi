------------------------------------------------------------
-- TRUNG TAM HOC TAP - DATABASE VIEWS SCRIPT
-- Oracle SQL - View tinh toan thuoc tinh tong hop
-- Tham chieu: Muc 2.4 - Phan tich CSDL Trung tam Dao Tao
------------------------------------------------------------

-- View: V_DIEM_TRUNG_BINH_KHOA_HOC
-- Muc dich: Tu dong tinh toan diem tong ket mon hoc cua tung hoc vien.
-- Cong thuc: ((Tong diem cac bai kiem tra Thuong xuyen) + (Diem Giua ky * 2) + (Diem Cuoi ky * 3)) / 9
-- Ghi chu: Truong hop hoc vien thi nhieu lan (LANTHI > 1), he thong se lay diem cao nhat (MAX(DIEMSO)) cua bai kiem tra do.

CREATE OR REPLACE VIEW V_DIEM_TRUNG_BINH_KHOA_HOC AS
WITH DiemBaiThi AS (
    -- B1: Loc lay diem so cao nhat (DIEM_CHOT) neu hoc vien thi nhieu lan (LANTHI)
    SELECT MAHOCVIEN, MABAIKIEMTRA, MAX(DIEMSO) AS DIEM_CHOT
    FROM THAMGIABKT
    GROUP BY MAHOCVIEN, MABAIKIEMTRA
)
SELECT 
    d.MAHOCVIEN,
    hv.HOTEN AS TEN_HOC_VIEN,
    d.MALOPHOC AS MALOP,
    l.TENLOP AS TEN_LOP,
    -- B2: Tinh tong tung loai diem theo trong so cua tung loai bai kiem tra
    NVL(SUM(CASE WHEN b.LOAIBKT = 'Thuong xuyen' THEN t.DIEM_CHOT ELSE 0 END), 0) AS TONG_DIEM_THUONG_XUYEN,
    NVL(SUM(CASE WHEN b.LOAIBKT = 'Giua ky' THEN t.DIEM_CHOT ELSE 0 END), 0) AS DIEM_GIUA_KY,
    NVL(SUM(CASE WHEN b.LOAIBKT = 'Cuoi ky' THEN t.DIEM_CHOT ELSE 0 END), 0) AS DIEM_CUOI_KY,
    -- B3: Tinh diem trung binh khoa hoc (lam tron 2 chu so thap phan)
    ROUND(
        (
            NVL(SUM(CASE WHEN b.LOAIBKT = 'Thuong xuyen' THEN t.DIEM_CHOT ELSE 0 END), 0) +
            NVL(SUM(CASE WHEN b.LOAIBKT = 'Giua ky' THEN t.DIEM_CHOT * 2 ELSE 0 END), 0) +
            NVL(SUM(CASE WHEN b.LOAIBKT = 'Cuoi ky' THEN t.DIEM_CHOT * 3 ELSE 0 END), 0)
        ) / 9, 2
    ) AS DIEM_TRUNG_BINH
FROM DANGKY d
JOIN HOCVIEN hv ON d.MAHOCVIEN = hv.MAHOCVIEN
JOIN LOPHOC l ON d.MALOPHOC = l.MALOPHOC
-- Ket noi de lay bai kiem tra cua lop hoc (LEFT JOIN de giu danh sach hoc vien chua co diem)
LEFT JOIN BAIKIEMTRA b ON d.MALOPHOC = b.MALOP
-- Ket noi de lay diem cua hoc vien cho bai kiem tra tuong ung
LEFT JOIN DiemBaiThi t ON b.MABAIKIEMTRA = t.MABAIKIEMTRA AND t.MAHOCVIEN = d.MAHOCVIEN
-- Chi tinh cho cac hoc vien dang hoat dong hoac da hoan thanh
WHERE d.TRANGTHAIDKY IN ('Dang hoc', 'Hoan thanh')
GROUP BY 
    d.MAHOCVIEN, 
    hv.HOTEN, 
    d.MALOPHOC, 
    l.TENLOP;

/*SELECT MAHOCVIEN, TEN_HOC_VIEN, DIEM_TRUNG_BINH 
FROM V_DIEM_TRUNG_BINH_KHOA_HOC 
WHERE MALOP = 2
ORDER BY DIEM_TRUNG_BINH DESC
FETCH FIRST 1 ROWS ONLY; -- (Hoặc dùng ROWNUM <= 1 tùy phiên bản Oracle)

SELECT * FROM V_DIEM_TRUNG_BINH_KHOA_HOC
WHERE MALOP=2
ORDER BY  DIEM_TRUNG_BINH;
CAU LENH MAU DE TIM DIEM TRUNG BINH CAO NHAT CUA LOP 2 (VD)
*/