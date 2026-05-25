------------------------------------------------------------
-- TRUNG TAM HOC TAP - STORED PROCEDURE SCRIPT (PART 3)
-- Oracle PL/SQL - 5 STORED PROCEDURE nghiep vu chinh
-- Tham chieu: List_of_Procedure_ttdt.sql
--             Phan tich CSDL Trung tam Dao Tao
------------------------------------------------------------
-- Thu tu cai dat:
--   1. SP_ENROLL_STUDENT          (Ghi danh + sinh hoa don hoc phi)
--   2. SP_EXECUTE_CLASS_TRANSFER  (Duyet chuyen lop hoc vien)
--   3. SP_ANALYZE_ABSENCE_ALERT   (Thong ke canh bao vang mat)
--   4. SP_PROCESS_PAYMENT         (Xu ly thanh toan hoa don)
--   5. SP_PROCESS_REFUND          (Duyet va thuc hien hoan tien)
------------------------------------------------------------

SET SERVEROUTPUT ON;

------------------------------------------------------------
-- PROCEDURE 1: Ghi nhan dang ky mon hoc va tu dong sinh hoa don hoc phi
-- Muc dich: Thuc hien toan bo quy trinh dang ky hoc:
--   1. Kiem tra si so lop con cho trong
--   2. Tinh tong tien hoc phi theo phuong thuc thanh toan
--   3. Tao hoa don hoc phi o trang thai 'Cho thanh toan'
--   4. Tao ban ghi dang ky o trang thai 'Cho thanh toan'
-- Luu y: Hoc bong la khoan thuong doc lap, KHONG tru vao hoc phi
------------------------------------------------------------
CREATE OR REPLACE PROCEDURE SP_ENROLL_STUDENT (
    p_student_id   IN NUMBER,
    p_class_id     IN NUMBER,
    p_payment_plan IN VARCHAR2 -- '1 thang' hoac 'Toan khoa'
) AS
    v_current_enrollment NUMBER;
    v_max_siso           NUMBER;
    v_course_fee         NUMBER(12,2);
    v_invoice_id         NUMBER;
    v_course_id          NUMBER;
    v_invoice_total      NUMBER(12,2);
BEGIN
    -- 1. Kiem tra si so hien tai cua lop hoc
    SELECT SISO, MAKHOAHOC INTO v_max_siso, v_course_id
    FROM LOPHOC WHERE MALOPHOC = p_class_id;

    SELECT COUNT(*) INTO v_current_enrollment
    FROM DANGKY WHERE MALOPHOC = p_class_id AND TRANGTHAIDKY = 'Dang hoc';

    IF v_current_enrollment >= v_max_siso THEN
        RAISE_APPLICATION_ERROR(-20601,
            'Lop hoc da dat si so toi da (' || v_max_siso || '). Khong the dang ky them.');
    END IF;

    -- 2. Xac dinh don gia goc theo phuong thuc thanh toan
    SELECT CASE p_payment_plan
        WHEN '1 thang' THEN HOCPHITHANG
        WHEN 'Toan khoa' THEN HOCPHITOANKH
    END INTO v_course_fee
    FROM KHOAHOC WHERE MAKHOAHOC = v_course_id;

    -- 3. Tinh tong tien thuc thu
    -- Hoc bong la khoan thuong doc lap => tong tien = hoc phi goc
    v_invoice_total := v_course_fee;

    -- 4. Phat sinh so hoa don moi
    SELECT NVL(MAX(MAHOADON), 0) + 1 INTO v_invoice_id FROM HOADONHOCPHI;

    INSERT INTO HOADONHOCPHI (
        MAHOADON, MAHOCVIEN, MAHOCBONG, MALOP, NGAYLAP, TONGTIEN,
        TRANGTHAIHD, LOAIHD, HINHTHUCTT, THANGTT, NAMTT
    ) VALUES (
        v_invoice_id, p_student_id, NULL, p_class_id, SYSDATE, v_invoice_total,
        'Cho thanh toan', 'Dong tien hoc phi', p_payment_plan,
        CASE WHEN p_payment_plan = '1 thang' THEN TO_NUMBER(TO_CHAR(SYSDATE, 'MM')) ELSE NULL END,
        CASE WHEN p_payment_plan = '1 thang' THEN TO_NUMBER(TO_CHAR(SYSDATE, 'YYYY')) ELSE NULL END
    );

    -- 5. Tao ban ghi dang ky khoa hoc o trang thai cho thanh toan
    INSERT INTO DANGKY (
        MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, MAHOADON, HINHTHUCTT
    ) VALUES (
        p_student_id, p_class_id, SYSDATE, 'Cho thanh toan', v_invoice_id, p_payment_plan
    );

    DBMS_OUTPUT.PUT_LINE('Thanh cong: Hoc vien ID ' || p_student_id ||
                         ' da dang ky lop ID ' || p_class_id ||
                         '. Hoa don ID ' || v_invoice_id || ' cho thanh toan.');
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20603, 'Khong tim thay lop hoc hoac khoa hoc tuong ung.');
END;
/

------------------------------------------------------------
-- PROCEDURE 2: Duyet yeu cau chuyen lop hoc cua hoc vien
-- Muc dich: Xu ly toan bo quy trinh chuyen lop:
--   1. Xac thuc yeu cau chuyen lop con o trang thai 'Cho duyet'
--   2. Kiem tra si so lop moi con cho trong
--   3. Cap nhat dang ky lop cu -> 'Chuyen lop'
--   4. Tao dang ky moi tai lop moi -> 'Dang hoc'
--   5. Cap nhat trang thai yeu cau -> 'Chap thuan'/'Tu choi'
-- Luu y: Khong cap nhat MALOP tren hoa don cu de tranh xung dot
--   voi TRG_INVOICE_LOCK (hoa don 'Da thanh toan' bi khoa)
CREATE OR REPLACE PROCEDURE SP_EXECUTE_CLASS_TRANSFER (
    p_request_id IN NUMBER,
    p_staff_id   IN NUMBER,
    p_note       IN VARCHAR2
) AS
    v_student_id         NUMBER;
    v_old_class          NUMBER;
    v_new_class          NUMBER;
    v_invoice_id         NUMBER;
    v_payment_plan       VARCHAR2(20);
    v_request_status     VARCHAR2(20);
    v_current_new_enroll NUMBER;
    v_new_max_siso       NUMBER;
BEGIN
    -- 1. Trich xuat thong tin yeu cau chuyen lop
    SELECT MAHOCVIEN, MALOPCU, MALOPMOI, TRANGTHAI
    INTO v_student_id, v_old_class, v_new_class, v_request_status
    FROM YEUCAUCHUYENLOP
    WHERE MAYEUCAU = p_request_id;

    IF v_request_status <> 'Cho duyet' THEN
        RAISE_APPLICATION_ERROR(-20602, 'Yeu cau nay da duoc xu ly truoc do.');
    END IF;

    -- 2. Kiem tra si so lop moi co con cho trong khong
    SELECT SISO INTO v_new_max_siso FROM LOPHOC WHERE MALOPHOC = v_new_class;
    SELECT COUNT(*) INTO v_current_new_enroll
    FROM DANGKY WHERE MALOPHOC = v_new_class AND TRANGTHAIDKY = 'Dang hoc';

    --DBMS_LOCK.SLEEP(10); -- Gia su co xu ly cham de kiem tra tinh dong thoi cua cac yeu cau

    IF v_current_new_enroll >= v_new_max_siso THEN
        -- Tu choi do het cho
        UPDATE YEUCAUCHUYENLOP
        SET TRANGTHAI  = 'Tu choi',
            MANHANVIEN = p_staff_id,
            NGAYXULY   = SYSDATE,
            GHICHU     = 'Tu choi do lop moi da dat si so toi da'
        WHERE MAYEUCAU = p_request_id;
        DBMS_OUTPUT.PUT_LINE('Tu choi: Lop moi ID ' || v_new_class || ' da dat si so toi da.');
        COMMIT;
        RETURN;
    END IF;

    -- 3. Trich xuat thong tin hoa don tai chinh dang lien ket voi lop cu
    SELECT MAHOADON, HINHTHUCTT INTO v_invoice_id, v_payment_plan
    FROM DANGKY
    WHERE MAHOCVIEN = v_student_id AND MALOPHOC = v_old_class AND TRANGTHAIDKY = 'Dang hoc';

    -- 4. Thuc hien chuyen giao trang thai dang ky
    -- 4b. Tao dang ky moi tai lop moi voi trang thai 'Dang hoc'
        INSERT INTO DANGKY (
        MAHOCVIEN, MALOPHOC, NGAYDANGKY, TRANGTHAIDKY, MAHOADON, HINHTHUCTT
    ) VALUES (
        v_student_id, v_new_class, SYSDATE, 'Dang hoc', v_invoice_id, v_payment_plan
    );

    -- 5. Hoan thanh phe duyet yeu cau chuyen lop
    UPDATE YEUCAUCHUYENLOP
    SET TRANGTHAI  = 'Chap thuan',
        MANHANVIEN = p_staff_id,
        NGAYXULY   = SYSDATE,
        GHICHU     = p_note
    WHERE MAYEUCAU = p_request_id;

        -- 4a. Danh dau dang ky lop cu la 'Chuyen lop'
    UPDATE DANGKY
    SET TRANGTHAIDKY = 'Chuyen lop'
    WHERE MAHOCVIEN = v_student_id AND MALOPHOC = v_old_class;

    DBMS_OUTPUT.PUT_LINE('Thanh cong: Hoc vien ID ' || v_student_id ||
                         ' da chuyen tu lop ' || v_old_class || ' sang lop ' || v_new_class);
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20604, 'Khong tim thay yeu cau chuyen lop hoac dang ky tuong ung.');
END;
/

------------------------------------------------------------
-- PROCEDURE 3: Thong ke chuyen sau va canh bao hoc vien vang qua so buoi quy dinh
-- Muc dich: In danh sach hoc vien co ty le vang mat > 30%
--   dua tren cac buoi hoc da hoan thanh cua lop
-- Ket qua: Xuat ra DBMS_OUTPUT (danh sach canh bao)
------------------------------------------------------------
CREATE OR REPLACE PROCEDURE SP_ANALYZE_ABSENCE_ALERT (
    p_class_id IN NUMBER
) AS
    v_total_sessions NUMBER;
    v_class_name     NVARCHAR2(100);
BEGIN
    -- Lay ten lop hoc
    SELECT TENLOP INTO v_class_name FROM LOPHOC WHERE MALOPHOC = p_class_id;

    -- Dem tong so buoi hoc thuc te da ket thuc cua lop hoc
    SELECT COUNT(*) INTO v_total_sessions
    FROM BUOIHOC
    WHERE MALOPHOC = p_class_id AND TRANGTHAI = 'Hoan thanh';

    IF v_total_sessions = 0 THEN
        DBMS_OUTPUT.PUT_LINE('Lop hoc "' || v_class_name || '" chua dien ra buoi hoc nao de thong ke.');
        RETURN;
    END IF;

    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('  DANH SACH HOC VIEN CANH BAO CHUYEN CAN (VANG > 30%)');
    DBMS_OUTPUT.PUT_LINE('============================================================');
    DBMS_OUTPUT.PUT_LINE('Lop hoc: ' || v_class_name || ' (ID: ' || p_class_id || ')');
    DBMS_OUTPUT.PUT_LINE('Tong so buoi da hoc: ' || v_total_sessions);
    DBMS_OUTPUT.PUT_LINE('------------------------------------------------------------');

    -- Truy van thong ke hoc vien vang mat
    FOR r_student IN (
        SELECT d.MAHOCVIEN, h.HOTEN, h.SDT,
               COUNT(CASE WHEN dd.TRANGTHAI = 'Vang mat' THEN 1 END) AS so_buoi_vang,
               ROUND((COUNT(CASE WHEN dd.TRANGTHAI = 'Vang mat' THEN 1 END) / v_total_sessions) * 100, 2) AS ty_le_vang
        FROM DANGKY d
        JOIN HOCVIEN h ON d.MAHOCVIEN = h.MAHOCVIEN
        LEFT JOIN BUOIHOC b ON b.MALOPHOC = p_class_id AND b.TRANGTHAI = 'Hoan thanh'
        LEFT JOIN DIEMDANH dd ON dd.MABUOIHOC = b.MABUOIHOC AND dd.MAHOCVIEN = d.MAHOCVIEN
        WHERE d.MALOPHOC = p_class_id
          AND d.TRANGTHAIDKY IN ('Dang hoc', 'Hoan thanh')
        GROUP BY d.MAHOCVIEN, h.HOTEN, h.SDT
        HAVING (COUNT(CASE WHEN dd.TRANGTHAI = 'Vang mat' THEN 1 END) / v_total_sessions) > 0.30
        ORDER BY ty_le_vang DESC
    ) LOOP
        DBMS_OUTPUT.PUT_LINE(
            'Hoc vien: ' || r_student.HOTEN ||
            ' (ID: ' || r_student.MAHOCVIEN || ')' ||
            ' - SDT: ' || NVL(r_student.SDT, 'N/A') ||
            ' | Vang: ' || r_student.so_buoi_vang || '/' || v_total_sessions ||
            ' buoi (' || r_student.ty_le_vang || '%)'
        );
    END LOOP;

    DBMS_OUTPUT.PUT_LINE('============================================================');
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20605, 'Khong tim thay lop hoc ID: ' || p_class_id);
END;
/

------------------------------------------------------------
-- PROCEDURE 4: Xu ly thanh toan hoa don
-- Muc dich: Ghi nhan thanh toan cho hoa don dang cho:
--   1. Xac thuc hoa don o trang thai 'Cho thanh toan'
--   2. Validate phuong thuc thanh toan hop le theo schema
--   3. Cap nhat hoa don -> 'Da thanh toan'
--   4. Cap nhat dang ky -> 'Dang hoc'
-- Luu y: HINHTHUC chi chap nhan 'Tien mat' hoac 'Chuyen khoan truc tiep'
--   theo constraint chk_hoadon_hinhthuc trong schema
------------------------------------------------------------
CREATE OR REPLACE PROCEDURE SP_PROCESS_PAYMENT (
    p_invoice_id     IN NUMBER,
    p_payment_method IN VARCHAR2,  -- 'Tien mat' hoac 'Chuyen khoan truc tiep'
    p_staff_id       IN NUMBER
) AS
    v_current_status VARCHAR2(20);
    v_student_id     NUMBER;
    v_class_id       NUMBER;
BEGIN
    -- 1. Kiem tra trang thai hien tai cua hoa don
    SELECT TRANGTHAIHD, MAHOCVIEN, MALOP
    INTO v_current_status, v_student_id, v_class_id
    FROM HOADONHOCPHI
    WHERE MAHOADON = p_invoice_id;

    IF v_current_status <> 'Cho thanh toan' THEN
        RAISE_APPLICATION_ERROR(-20801,
            'Hoa don ID ' || p_invoice_id ||
            ' khong o trang thai "Cho thanh toan" (hien tai: "' || v_current_status || '").');
    END IF;

    -- 2. Validate phuong thuc thanh toan (dong bo voi chk_hoadon_hinhthuc)
    IF p_payment_method NOT IN ('Tien mat', 'Chuyen khoan truc tiep') THEN
        RAISE_APPLICATION_ERROR(-20802,
            'Phuong thuc thanh toan khong hop le: "' || p_payment_method ||
            '". Chi chap nhan: Tien mat, Chuyen khoan truc tiep.');
    END IF;

    -- 3. Cap nhat trang thai hoa don
    UPDATE HOADONHOCPHI
    SET TRANGTHAIHD = 'Da thanh toan',
        HINHTHUC    = p_payment_method
    WHERE MAHOADON = p_invoice_id;

    -- 4. Cap nhat trang thai dang ky tu 'Cho thanh toan' -> 'Dang hoc'
    UPDATE DANGKY
    SET TRANGTHAIDKY = 'Dang hoc'
    WHERE MAHOCVIEN = v_student_id
      AND MALOPHOC  = v_class_id
      AND TRANGTHAIDKY = 'Cho thanh toan'
      AND MAHOADON  = p_invoice_id;

    DBMS_OUTPUT.PUT_LINE('Thanh cong: Hoa don ID ' || p_invoice_id ||
                         ' da duoc ghi nhan thanh toan qua "' || p_payment_method || '".');
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20803, 'Khong tim thay hoa don ID: ' || p_invoice_id);
END;
/

------------------------------------------------------------
-- PROCEDURE 5: Duyet va thuc hien hoan tien
-- Muc dich: Xu ly toan bo quy trinh hoan tien:
--   Neu APPROVE:
--     1. Kiem tra so tien hoan khong vuot hoa don goc
--     2. Cap nhat HOANTRA -> 'Chap thuan'
--     3. Tao hoa don dieu chinh am (LOAIHD = 'Dieu chinh')
--        voi HINHTHUC lay dong tu HOANTRA.HINHTHUC (khong gan cung)
--   Neu REJECT:
--     Cap nhat HOANTRA -> 'Tu choi'
-- Luu y: HOANTRA.TRANGTHAI chi cho phep 'Cho duyet'/'Chap thuan'/'Tu choi'
--   (khong co gia tri 'Da hoan'). HINHTHUC lay dong tu HOANTRA de dong bo
--   voi hinh thuc xu ly thuc te tai quay.
------------------------------------------------------------
CREATE OR REPLACE PROCEDURE SP_PROCESS_REFUND (
    p_refund_id IN NUMBER,
    p_staff_id  IN NUMBER,
    p_approve   IN VARCHAR2,  -- 'APPROVE' hoac 'REJECT'
    p_note      IN VARCHAR2 DEFAULT NULL
) AS
    v_refund_status  VARCHAR2(20);
    v_original_total NUMBER(12,2);
    v_refund_amount  NUMBER(12,2);
    v_invoice_id     NUMBER;
    v_student_id     NUMBER;
    v_hinhthuc_ht    VARCHAR2(50);
    v_new_invoice_id NUMBER;
    v_class_id       NUMBER;
BEGIN
    -- 1. Lay thong tin yeu cau hoan tien va hinh thuc thuc te tai quay
    SELECT TRANGTHAI, SOTIEN, MAHOADON, MAHOCVIEN, HINHTHUC
    INTO v_refund_status, v_refund_amount, v_invoice_id, v_student_id, v_hinhthuc_ht
    FROM HOANTRA
    WHERE MAHOANTRA = p_refund_id;

    IF v_refund_status <> 'Cho duyet' THEN
        RAISE_APPLICATION_ERROR(-20901,
            'Yeu cau hoan tien ID ' || p_refund_id || ' da duoc xu ly.');
    END IF;

    IF p_approve = 'APPROVE' THEN
        -- 2a. Kiem tra so tien hoan khong vuot hoa don goc
        SELECT TONGTIEN, MALOP INTO v_original_total, v_class_id
        FROM HOADONHOCPHI WHERE MAHOADON = v_invoice_id;

        IF v_refund_amount > v_original_total THEN
            RAISE_APPLICATION_ERROR(-20902,
                'So tien hoan tra (' || v_refund_amount ||
                ') vuot qua gia tri hoa don goc (' || v_original_total || ').');
        END IF;

        -- 2b. Duyet hoan tien - cap nhat HOANTRA sang 'Chap thuan'
        UPDATE HOANTRA
        SET TRANGTHAI   = 'Chap thuan',
            MANHANVIEN  = p_staff_id,
            NGAYHOANTRA = SYSDATE
        WHERE MAHOANTRA = p_refund_id;

        -- 2c. Phat sinh so hoa don dieu chinh moi
        SELECT NVL(MAX(MAHOADON), 0) + 1 INTO v_new_invoice_id FROM HOADONHOCPHI;

        -- 2d. Tao hoa don dieu chinh am
        -- HINHTHUC lay dong tu HOANTRA.HINHTHUC (khong gan cung 'Chuyen khoan')
        INSERT INTO HOADONHOCPHI (
            MAHOADON, MAHOCVIEN, MALOP, NGAYLAP, TONGTIEN,
            TRANGTHAIHD, LOAIHD, HINHTHUC, MAHOADON_GOC, GHICHU
        ) VALUES (
            v_new_invoice_id,
            v_student_id,
            v_class_id,
            SYSDATE,
            -v_refund_amount,
            'Da thanh toan',
            'Dieu chinh',
            v_hinhthuc_ht,
            v_invoice_id,
            NVL(p_note, 'Hoan tra theo yeu cau ID: ' || p_refund_id)
        );

        DBMS_OUTPUT.PUT_LINE('Thanh cong: Yeu cau hoan tien ID ' || p_refund_id ||
                             ' da duoc duyet. Hoa don dieu chinh ID ' || v_new_invoice_id ||
                             ' da duoc tao voi so tien -' || v_refund_amount ||
                             ' (hinh thuc: ' || v_hinhthuc_ht || ').');

    ELSIF p_approve = 'REJECT' THEN
        -- 3. Tu choi hoan tien
        UPDATE HOANTRA
        SET TRANGTHAI   = 'Tu choi',
            MANHANVIEN  = p_staff_id,
            NGAYHOANTRA = SYSDATE
        WHERE MAHOANTRA = p_refund_id;

        DBMS_OUTPUT.PUT_LINE('Yeu cau hoan tien ID ' || p_refund_id || ' da bi tu choi.');

    ELSE
        RAISE_APPLICATION_ERROR(-20903,
            'Gia tri p_approve khong hop le: "' || p_approve ||
            '". Chi chap nhan APPROVE hoac REJECT.');
    END IF;

    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RAISE_APPLICATION_ERROR(-20904, 'Khong tim thay yeu cau hoan tien ID: ' || p_refund_id);
END;
/

------------------------------------------------------------
-- HOAN TAT CAI DAT PROCEDURE - END OF PART 3
------------------------------------------------------------
-- Kiem tra tat ca procedure da duoc tao thanh cong
SELECT OBJECT_NAME, OBJECT_TYPE, STATUS
FROM USER_OBJECTS
WHERE OBJECT_TYPE = 'PROCEDURE'
  AND OBJECT_NAME IN (
    'SP_ENROLL_STUDENT',
    'SP_EXECUTE_CLASS_TRANSFER',
    'SP_ANALYZE_ABSENCE_ALERT',
    'SP_PROCESS_PAYMENT',
    'SP_PROCESS_REFUND'
)
ORDER BY OBJECT_NAME;

