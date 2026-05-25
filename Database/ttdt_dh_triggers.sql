------------------------------------------------------------
-- TRUNG TAM HOC TAP - TRIGGER INSTALLATION SCRIPT (PART 2)
-- Oracle PL/SQL - Toan bo 12 TRIGGER rang buoc toan ven
-- Tham chieu: List_of_Trigger_ttdt.sql
--             Phan tich CSDL Trung tam Dao Tao
------------------------------------------------------------
-- Muc 2.3 - Rang buoc trang thai & cascade:
--   1. TRG_DANGKY_CLASS_STATUS       (DANGKY - kiem tra trang thai lop khi ghi danh)
--   2. TRG_LOPHOC_TEACHER_STATUS     (LOPHOC - kiem tra trang thai giao vien khi phan cong)
--   3. TRG_LOPHOC_STATUS_CASCADE     (LOPHOC - cascade trang thai khi lop ket thuc/huy)
-- Muc 2.2 & Nghiep vu tai chinh:
--   4. TRG_HOCBONG_MARK_USED         (HOADONHOCPHI - chan phat thuong hoc bong trung lap)
--   5. TRG_HOADON_CORRECTION_VALIDATE(HOADONHOCPHI - xac thuc hoa don dieu chinh)
-- Muc 2.3 - Compound Trigger nang cao:
--   6. TRG_DIEMDANH_LIMIT_CHECK      (DIEMDANH - kiem soat si so diem danh)
--   7. TRG_BUOIHOC_RESOURCE_VAL      (BUOIHOC - trung lich/phong + suc chua phong)
-- Muc 2.2 - Rang buoc lien thuoc tinh lien quan he (BO SUNG):
--   8. TRG_THAMGIABKT_SCORE_VAL      (THAMGIABKT - kiem soat toan ven bai kiem tra)
--   9. TRG_INVOICE_LOCK              (HOADONHOCPHI - ngan chan sua doi hoa don da thanh toan)
--  10. TRG_HOANTRA_AMOUNT_CHECK      (HOANTRA - han muc hoan tra <= tong tien hoa don)
--  11. TRG_YCCL_ENROLLMENT_CHECK     (YEUCAUCHUYENLOP - kiem tra dieu kien chuyen lop)
-- Muc 2.3 - Rang buoc lien bo lien quan he (BO SUNG):
--  12. TRG_DANGKY_ENROLLMENT_LIMIT   (DANGKY - si so ghi danh <= SISO lop)
------------------------------------------------------------

SET SERVEROUTPUT ON;

------------------------------------------------------------
-- TRIGGER 1: Kiem tra trang thai lop hoc khi ghi danh
-- Muc dich: Chi cho phep hoc vien ghi danh vao lop 'Dang mo'
-- Bang: DANGKY (BEFORE INSERT)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_DANGKY_CLASS_STATUS
BEFORE INSERT ON DANGKY
FOR EACH ROW
DECLARE
    v_class_status VARCHAR2(20);
BEGIN
    SELECT TRANGTHAI INTO v_class_status
    FROM LOPHOC
    WHERE MALOPHOC = :NEW.MALOPHOC;

    IF v_class_status NOT IN ('Dang mo') THEN
        RAISE_APPLICATION_ERROR(-20701,
            'Khong the ghi danh: Lop hoc ID ' || :NEW.MALOPHOC ||
            ' dang o trang thai "' || v_class_status ||
            '". Chi cho phep ghi danh khi lop "Dang mo".');
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 2: Kiem tra trang thai giao vien khi phan cong lop
-- Muc dich: Chi giao vien 'Dang day' moi duoc phan cong giang day lop moi
-- Bang: LOPHOC (BEFORE INSERT OR UPDATE OF MAGIAOVIEN)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_LOPHOC_TEACHER_STATUS
BEFORE INSERT OR UPDATE OF MAGIAOVIEN ON LOPHOC
FOR EACH ROW
DECLARE
    v_teacher_status VARCHAR2(20);
BEGIN
    IF :NEW.MAGIAOVIEN IS NOT NULL THEN
        SELECT TRANGTHAI INTO v_teacher_status
        FROM GIAOVIEN
        WHERE MAGIAOVIEN = :NEW.MAGIAOVIEN;

        IF v_teacher_status <> 'Dang day' THEN
            RAISE_APPLICATION_ERROR(-20702,
                'Khong the phan cong: Giao vien ID ' || :NEW.MAGIAOVIEN ||
                ' dang o trang thai "' || v_teacher_status ||
                '". Chi giao vien "Dang day" moi duoc phan cong lop moi.');
        END IF;
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 3: Tu dong cap nhat trang thai dang ky khi lop ket thuc/huy
-- Muc dich: Cascade trang thai tu LOPHOC xuong DANGKY va HOADONHOCPHI
--   - Lop 'Ket thuc': DANGKY 'Dang hoc' -> 'Hoan thanh'
--   - Lop 'Huy':      DANGKY 'Dang hoc'/'Cho thanh toan' -> 'Huy'
--                      HOADONHOCPHI 'Cho thanh toan' -> 'Huy'
-- Bang: LOPHOC (AFTER UPDATE OF TRANGTHAI)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_LOPHOC_STATUS_CASCADE
AFTER UPDATE OF TRANGTHAI ON LOPHOC
FOR EACH ROW
BEGIN
    -- Khi lop ket thuc: cap nhat trang thai dang ky 'Dang hoc' -> 'Hoan thanh'
    IF :NEW.TRANGTHAI = 'Ket thuc' AND :OLD.TRANGTHAI <> 'Ket thuc' THEN
        UPDATE DANGKY
        SET TRANGTHAIDKY = 'Hoan thanh'
        WHERE MALOPHOC = :NEW.MALOPHOC
          AND TRANGTHAIDKY = 'Dang hoc';
    END IF;

    -- Khi lop bi huy: cap nhat trang thai dang ky va hoa don lien quan
    IF :NEW.TRANGTHAI = 'Huy' AND :OLD.TRANGTHAI <> 'Huy' THEN
        UPDATE DANGKY
        SET TRANGTHAIDKY = 'Huy'
        WHERE MALOPHOC = :NEW.MALOPHOC
          AND TRANGTHAIDKY IN ('Dang hoc', 'Cho thanh toan');

        -- Huy cac hoa don chua thanh toan lien quan
        UPDATE HOADONHOCPHI
        SET TRANGTHAIHD = 'Huy'
        WHERE MALOP = :NEW.MALOPHOC
          AND TRANGTHAIHD = 'Cho thanh toan';
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 4: Chan phat thuong hoc bong trung lap (Anti-Fraud)
-- Muc dich: Hoc bong la khoan thuong doc lap phat bang tien mat,
--   KHONG tru vao hoc phi. Trigger nay dam bao:
--   - Moi ma hoc bong chi duoc lap hoa don phat thuong (LOAIHD = 'Hoc bong')
--     MỘT LAN DUY NHAT.
--   - Neu TRANGTHAIHB = 'Da nhan' -> CHAN LAI (da phat thuong roi)
--   - Neu TRANGTHAIHB = 'Chua nhan' -> CHO QUA va tu dong chuyen sang 'Da nhan'
-- Bang: HOADONHOCPHI (BEFORE INSERT)
-- Luu y: Dung BEFORE INSERT de co the chan truoc khi dong duoc ghi vao bang.
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_HOCBONG_MARK_USED
BEFORE INSERT ON HOADONHOCPHI
FOR EACH ROW
DECLARE
    v_status VARCHAR2(20);
BEGIN
    -- Chi xu ly khi hoa don nay la loai phat thuong hoc bong va co gan ma hoc bong
    IF :NEW.LOAIHD = 'Hoc bong' AND :NEW.MAHOCBONG IS NOT NULL THEN
        -- Kiem tra trang thai hien tai cua hoc bong
        SELECT TRANGTHAIHB INTO v_status
        FROM HOCBONG
        WHERE MAHOCBONG = :NEW.MAHOCBONG;

        -- Neu hoc bong da duoc phat thuong truoc do roi thi chan lai
        IF v_status = 'Da nhan' THEN
            RAISE_APPLICATION_ERROR(-20703,
                'Loi nghiep vu: Hoc bong ID ' || :NEW.MAHOCBONG ||
                ' da duoc phat thuong truoc do. Khong duoc phep chi tien trung lap!');
        END IF;

        -- Hoc bong 'Chua nhan' -> Tu dong cap nhat sang 'Da nhan'
        UPDATE HOCBONG
        SET TRANGTHAIHB = 'Da nhan'
        WHERE MAHOCBONG = :NEW.MAHOCBONG;
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 5: Xac thuc hoa don dieu chinh phai tham chieu hoa don da thanh toan
-- Muc dich: Hoa don dieu chinh (LOAIHD = 'Dieu chinh') bat buoc:
--   - Phai co ma hoa don goc (MAHOADON_GOC NOT NULL)
--   - Hoa don goc phai o trang thai 'Da thanh toan'
-- Bang: HOADONHOCPHI (BEFORE INSERT)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_HOADON_CORRECTION_VALIDATE
BEFORE INSERT ON HOADONHOCPHI
FOR EACH ROW
DECLARE
    v_original_status VARCHAR2(20);
BEGIN
    -- Neu la hoa don dieu chinh, hoa don goc phai da thanh toan
    IF :NEW.LOAIHD = 'Dieu chinh' THEN
        IF :NEW.MAHOADON_GOC IS NULL THEN
            RAISE_APPLICATION_ERROR(-20704,
                'Hoa don dieu chinh bat buoc phai co ma hoa don goc (MAHOADON_GOC).');
        END IF;

        SELECT TRANGTHAIHD INTO v_original_status
        FROM HOADONHOCPHI
        WHERE MAHOADON = :NEW.MAHOADON_GOC;

        IF v_original_status <> 'Da thanh toan' THEN
            RAISE_APPLICATION_ERROR(-20705,
                'Chi duoc dieu chinh hoa don co trang thai "Da thanh toan". ' ||
                'Hoa don goc ID ' || :NEW.MAHOADON_GOC ||
                ' dang o trang thai "' || v_original_status || '".');
        END IF;
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 6: Kiem soat diem danh khong vuot qua si so thuc te
-- Muc dich: So luong hoc vien duoc diem danh trong bat ky buoi hoc nao
--   khong duoc vuot qua si so toi da quy dinh cua lop (LOPHOC.SISO)
-- Ky thuat: COMPOUND TRIGGER de tranh loi mutating table
--   - Pha 1 (BEFORE EACH ROW): Thu thap ma buoi hoc bi anh huong
--   - Pha 2 (AFTER STATEMENT): Doi chieu du lieu khi bang da o trang thai tinh
-- Bang: DIEMDANH (FOR INSERT OR UPDATE)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_DIEMDANH_LIMIT_CHECK
FOR INSERT OR UPDATE ON DIEMDANH
COMPOUND TRIGGER

    -- Khai bao mang dong luu tru cac ma buoi hoc bi anh huong trong phien giao dich
    TYPE t_session_list IS TABLE OF DIEMDANH.MABUOIHOC%TYPE INDEX BY PLS_INTEGER;
    g_affected_sessions t_session_list;

    -- PHA 1: Truoc khi chay dong, thu thap ma buoi hoc de xu ly sau
    BEFORE EACH ROW IS
    BEGIN
        g_affected_sessions(g_affected_sessions.COUNT + 1) := :NEW.MABUOIHOC;
    END BEFORE EACH ROW;

    -- PHA 2: Sau khi lenh thuc thi hoan tat, doi chieu du lieu mot cach an toan
    AFTER STATEMENT IS
        v_current_attendance NUMBER;
        v_max_capacity       NUMBER;
        v_class_id           NUMBER;
    BEGIN
        IF g_affected_sessions.COUNT > 0 THEN
            FOR i IN 1..g_affected_sessions.COUNT LOOP
                -- Lay si so toi da quy dinh va ma lop hoc cua buoi hoc tuong ung
                SELECT l.SISO, l.MALOPHOC INTO v_max_capacity, v_class_id
                FROM BUOIHOC b
                JOIN LOPHOC l ON b.MALOPHOC = l.MALOPHOC
                WHERE b.MABUOIHOC = g_affected_sessions(i);

                -- Dem tong so hoc vien duoc diem danh thuc te trong buoi hoc do
                SELECT COUNT(*) INTO v_current_attendance
                FROM DIEMDANH
                WHERE MABUOIHOC = g_affected_sessions(i);

                -- Chan hanh dong neu so hoc vien diem danh vuot qua si so tran cua lop
                IF v_current_attendance > v_max_capacity THEN
                    RAISE_APPLICATION_ERROR(-20501,
                        'Loi vi pham si so: Buoi hoc ID ' || g_affected_sessions(i) ||
                        ' thuoc lop ID ' || v_class_id ||
                        ' chi cho phep toi da ' || v_max_capacity ||
                        ' hoc vien diem danh.');
                END IF;
            END LOOP;
        END IF;
    END AFTER STATEMENT;

END TRG_DIEMDANH_LIMIT_CHECK;
/

------------------------------------------------------------
-- TRIGGER 7: Kiem soat trung lich giang day, trung phong hoc va suc chua phong
-- Muc dich: Dam bao:
--   1. Khong trung lich phong hoc: Mot phong hoc chi duoc xep cho 1 buoi hoc
--      tai cung 1 ngay va khung gio (tru buoi hoc co trang thai 'Nghi')
--   2. Khong trung lich giao vien: Mot giao vien khong the day 2 lop khac nhau
--      cung ngay va cung khung gio
--   3. Suc chua phong hoc: Si so lop (LOPHOC.SISO) khong duoc vuot qua
--      suc chua vat ly cua phong (PHONGHOC.SUCHUA)
-- Ky thuat: COMPOUND TRIGGER de tranh loi mutating table
--   - Pha 1 (BEFORE EACH ROW): Thu thap du lieu truoc khi cac dong thay doi vat ly
--   - Pha 2 (AFTER STATEMENT): Doi chieu toan bang khi du lieu da o trang thai tinh
-- Bang: BUOIHOC (FOR INSERT OR UPDATE OF NGAYHOC, GIOHOC, MAPHONGHOC)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_BUOIHOC_RESOURCE_VAL
FOR INSERT OR UPDATE OF NGAYHOC, GIOHOC, MAPHONGHOC ON BUOIHOC
COMPOUND TRIGGER

    -- Record type luu tru thong tin buoi hoc can kiem tra
    TYPE r_buoihoc IS RECORD (
        mabuoihoc   BUOIHOC.MABUOIHOC%TYPE,
        malophoc    BUOIHOC.MALOPHOC%TYPE,
        ngayhoc     BUOIHOC.NGAYHOC%TYPE,
        giohoc      BUOIHOC.GIOHOC%TYPE,
        maphong     BUOIHOC.MAPHONGHOC%TYPE
    );
    TYPE t_buoihoc_queue IS TABLE OF r_buoihoc;
    g_queue t_buoihoc_queue := t_buoihoc_queue();

    -- Thu thap du lieu truoc khi cac dong thay doi vat ly
    BEFORE EACH ROW IS
    BEGIN
        g_queue.EXTEND;
        g_queue(g_queue.LAST).mabuoihoc := :NEW.MABUOIHOC;
        g_queue(g_queue.LAST).malophoc  := :NEW.MALOPHOC;
        g_queue(g_queue.LAST).ngayhoc   := :NEW.NGAYHOC;
        g_queue(g_queue.LAST).giohoc    := :NEW.GIOHOC;
        g_queue(g_queue.LAST).maphong   := :NEW.MAPHONGHOC;
    END BEFORE EACH ROW;

    -- Thuc hien doi chieu toan bang khi du lieu da o trang thai tinh
    AFTER STATEMENT IS
        v_overlap_count  NUMBER;
        v_teacher_id     NUMBER;
        v_class_siso     NUMBER;
        v_room_capacity  NUMBER;
    BEGIN
        FOR i IN 1..g_queue.COUNT LOOP
            -- 1. Dam bao khong trung lich phong hoc
            IF g_queue(i).maphong IS NOT NULL THEN
                SELECT COUNT(*) INTO v_overlap_count
                FROM BUOIHOC
                WHERE NGAYHOC = g_queue(i).ngayhoc
                  AND GIOHOC = g_queue(i).giohoc
                  AND MAPHONGHOC = g_queue(i).maphong
                  AND TRANGTHAI <> 'Nghi'
                  AND MABUOIHOC <> NVL(g_queue(i).mabuoihoc, -1);

                IF v_overlap_count > 0 THEN
                    RAISE_APPLICATION_ERROR(-20301,
                        'Xung dot tai nguyen: Phong hoc ID ' || g_queue(i).maphong ||
                        ' da duoc xep lich cho buoi hoc khac vao ngay ' ||
                        TO_CHAR(g_queue(i).ngayhoc, 'DD/MM/YYYY') ||
                        ' luc ' || g_queue(i).giohoc);
                END IF;

                -- 3. Dam bao suc chua phong hoc >= si so lop
                -- Lay si so toi da cua lop
                SELECT SISO INTO v_class_siso
                FROM LOPHOC
                WHERE MALOPHOC = g_queue(i).malophoc;

                -- Lay suc chua vat ly cua phong hoc
                SELECT SUCHUA INTO v_room_capacity
                FROM PHONGHOC
                WHERE MAPHONGHOC = g_queue(i).maphong;

                IF v_class_siso > v_room_capacity THEN
                    RAISE_APPLICATION_ERROR(-20303,
                        'Vi pham suc chua: Lop hoc ID ' || g_queue(i).malophoc ||
                        ' co si so ' || v_class_siso ||
                        ' vuot qua suc chua phong hoc ID ' || g_queue(i).maphong ||
                        ' (toi da ' || v_room_capacity || ' cho ngoi).');
                END IF;
            END IF;

            -- 2. Dam bao khong trung lich giang day cua giao vien
            SELECT MAGIAOVIEN INTO v_teacher_id
            FROM LOPHOC
            WHERE MALOPHOC = g_queue(i).malophoc;

            IF v_teacher_id IS NOT NULL THEN
                SELECT COUNT(*) INTO v_overlap_count
                FROM BUOIHOC b
                JOIN LOPHOC l ON b.MALOPHOC = l.MALOPHOC
                WHERE b.NGAYHOC = g_queue(i).ngayhoc
                  AND b.GIOHOC = g_queue(i).giohoc
                  AND l.MAGIAOVIEN = v_teacher_id
                  AND b.TRANGTHAI <> 'Nghi'
                  AND b.MABUOIHOC <> NVL(g_queue(i).mabuoihoc, -1);

                IF v_overlap_count > 0 THEN
                    RAISE_APPLICATION_ERROR(-20302,
                        'Xung dot lich trinh: Giao vien ID ' || v_teacher_id ||
                        ' da co lich day lop khac vao ngay ' ||
                        TO_CHAR(g_queue(i).ngayhoc, 'DD/MM/YYYY') ||
                        ' khung gio ' || g_queue(i).giohoc);
                END IF;
            END IF;
        END LOOP;
        g_queue.DELETE;
    END AFTER STATEMENT;

END TRG_BUOIHOC_RESOURCE_VAL;
/

------------------------------------------------------------
-- TRIGGER 8: Kiem soat tinh toan ven cua bai kiem tra
-- Muc dich:
--   1. Diem thi khong duoc vuot qua diem toi da cua de thi (BAIKIEMTRA.DIEMTOIDA)
--   2. Hoc vien phai thuc su dang hoc tai lop to chuc bai thi
--      (DANGKY.TRANGTHAIDKY = 'Dang hoc')
-- Bang: THAMGIABKT (BEFORE INSERT OR UPDATE)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_THAMGIABKT_SCORE_VAL
BEFORE INSERT OR UPDATE ON THAMGIABKT
FOR EACH ROW
DECLARE
    v_max_score     NUMBER;
    v_class_id      NUMBER;
    v_is_registered NUMBER;
BEGIN
    -- Truy van diem toi da quy dinh va lop to chuc bai thi
    SELECT DIEMTOIDA, MALOP INTO v_max_score, v_class_id
    FROM BAIKIEMTRA
    WHERE MABAIKIEMTRA = :NEW.MABAIKIEMTRA;

    -- Kiem tra mien gia tri diem so
    IF :NEW.DIEMSO < 0 OR :NEW.DIEMSO > v_max_score THEN
        RAISE_APPLICATION_ERROR(-20401,
            'Loi diem so: Diem dat duoc phai nam trong khoang tu 0 den ' || v_max_score);
    END IF;

    -- Kiem tra hoc vien co thuc su dang hoc tai lop to chuc bai thi khong
    SELECT COUNT(*) INTO v_is_registered
    FROM DANGKY
    WHERE MAHOCVIEN = :NEW.MAHOCVIEN
      AND MALOPHOC = v_class_id
      AND TRANGTHAIDKY = 'Dang hoc';

    IF v_is_registered = 0 THEN
        RAISE_APPLICATION_ERROR(-20402,
            'Loi ghi nhan: Hoc vien ID ' || :NEW.MAHOCVIEN ||
            ' khong thuoc danh sach dang hoc cua lop to chuc thi (Lop ID: ' || v_class_id || ').');
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 9: Ngan chan sua doi hoa don da thanh toan (Bao ve dong tien)
-- Muc dich: Moi hoa don da co trang thai 'Da thanh toan' se bi KHOA CUNG,
--   khong cho phep UPDATE hay DELETE de bao ve tinh toan ven dong tien
-- Bang: HOADONHOCPHI (BEFORE UPDATE OR DELETE)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_INVOICE_LOCK
BEFORE UPDATE OR DELETE ON HOADONHOCPHI
FOR EACH ROW
BEGIN
    -- Neu hoa don cu da thanh toan thanh cong, chan moi hanh vi thay doi du lieu dong tien
    IF :OLD.TRANGTHAIHD = 'Da thanh toan' THEN
        RAISE_APPLICATION_ERROR(-20003,
            'Loi bao mat: Hoa don da thanh toan thanh cong. Khong cho phep chinh sua hoac xoa du lieu!');
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 10: Han muc hoan tra khong vuot qua tong tien hoa don goc
-- Muc dich: So tien hoan lai (HOANTRA.SOTIEN) khong duoc phep vuot qua
--   tong so tien thuc te tren hoa don goc (HOADONHOCPHI.TONGTIEN)
-- Tham chieu: Muc 2.2 - Rang buoc lien thuoc tinh lien quan he
-- Bang: HOANTRA (BEFORE INSERT OR UPDATE)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_HOANTRA_AMOUNT_CHECK
BEFORE INSERT OR UPDATE ON HOANTRA
FOR EACH ROW
DECLARE
    v_invoice_amount NUMBER;
BEGIN
    IF :NEW.MAHOADON IS NOT NULL THEN
        -- Lay tong tien cua hoa don goc
        SELECT TONGTIEN INTO v_invoice_amount
        FROM HOADONHOCPHI
        WHERE MAHOADON = :NEW.MAHOADON;

        -- So tien hoan tra khong duoc vuot qua tong tien hoa don
        IF :NEW.SOTIEN > v_invoice_amount THEN
            RAISE_APPLICATION_ERROR(-20601,
                'Loi han muc hoan tra: So tien hoan tra (' || :NEW.SOTIEN ||
                ') vuot qua tong tien hoa don goc ID ' || :NEW.MAHOADON ||
                ' (' || v_invoice_amount || '). Khong duoc phep hoan tra vuot muc!');
        END IF;
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 11: Kiem tra dieu kien chuyen lop
-- Muc dich: Lop hoc cu (MALOPCU) phai la lop ma hoc vien do da dang ky
--   thanh cong va dang co trang thai hoat dong thuc te
--   (DANGKY.TRANGTHAIDKY = 'Dang hoc')
-- Tham chieu: Muc 2.2 - Rang buoc lien thuoc tinh lien quan he
-- Bang: YEUCAUCHUYENLOP (BEFORE INSERT OR UPDATE)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_YCCL_ENROLLMENT_CHECK
BEFORE INSERT OR UPDATE ON YEUCAUCHUYENLOP
FOR EACH ROW
DECLARE
    v_is_enrolled NUMBER;
BEGIN
    -- Kiem tra hoc vien co dang hoc tai lop cu hay khong
    SELECT COUNT(*) INTO v_is_enrolled
    FROM DANGKY
    WHERE MAHOCVIEN = :NEW.MAHOCVIEN
      AND MALOPHOC = :NEW.MALOPCU
      AND TRANGTHAIDKY = 'Dang hoc';

    IF v_is_enrolled = 0 THEN
        RAISE_APPLICATION_ERROR(-20602,
            'Loi dieu kien chuyen lop: Hoc vien ID ' || :NEW.MAHOCVIEN ||
            ' khong co dang ky hoat dong (Dang hoc) tai lop cu ID ' || :NEW.MALOPCU ||
            '. Chi cho phep chuyen tu lop dang hoc thuc te!');
    END IF;
END;
/

------------------------------------------------------------
-- TRIGGER 12: Kiem soat si so ghi danh khong vuot qua SISO lop
-- Muc dich: So luong hoc vien dang ky hoat dong (TRANGTHAIDKY = 'Dang hoc')
--   trong mot lop hoc khong duoc vuot qua si so tran (LOPHOC.SISO)
-- Ky thuat: COMPOUND TRIGGER de tranh loi mutating table
--   - Pha 1 (BEFORE EACH ROW): Thu thap ma lop bi anh huong
--   - Pha 2 (AFTER STATEMENT): Doi chieu du lieu khi bang da o trang thai tinh
-- Tham chieu: Muc 2.3 - Rang buoc lien bo lien quan he
-- Bang: DANGKY (FOR INSERT OR UPDATE OF TRANGTHAIDKY)
------------------------------------------------------------
CREATE OR REPLACE TRIGGER TRG_DANGKY_ENROLLMENT_LIMIT
FOR INSERT OR UPDATE OF TRANGTHAIDKY ON DANGKY
COMPOUND TRIGGER

    -- Khai bao mang dong luu tru cac ma lop bi anh huong trong phien giao dich
    TYPE t_class_list IS TABLE OF DANGKY.MALOPHOC%TYPE INDEX BY PLS_INTEGER;
    g_affected_classes t_class_list;

    -- PHA 1: Truoc khi chay dong, thu thap ma lop de xu ly sau
    BEFORE EACH ROW IS
    BEGIN
        -- Chi kiem tra khi trang thai moi la 'Dang hoc' (them moi hoac cap nhat sang 'Dang hoc')
        IF :NEW.TRANGTHAIDKY = 'Dang hoc' THEN
            g_affected_classes(g_affected_classes.COUNT + 1) := :NEW.MALOPHOC;
        END IF;
    END BEFORE EACH ROW;

    -- PHA 2: Sau khi lenh hoan tat, dem so hoc vien dang hoc va doi chieu voi SISO
    AFTER STATEMENT IS
        v_current_enrollment NUMBER;
        v_max_siso           NUMBER;
    BEGIN
        IF g_affected_classes.COUNT > 0 THEN
            FOR i IN 1..g_affected_classes.COUNT LOOP
                -- Lay si so toi da quy dinh cua lop
                SELECT SISO INTO v_max_siso
                FROM LOPHOC
                WHERE MALOPHOC = g_affected_classes(i);

                -- Dem tong so hoc vien dang ky hoat dong ('Dang hoc') trong lop
                SELECT COUNT(*) INTO v_current_enrollment
                FROM DANGKY
                WHERE MALOPHOC = g_affected_classes(i)
                  AND TRANGTHAIDKY = 'Dang hoc';

                -- Chan hanh dong neu so hoc vien ghi danh vuot qua si so tran
                IF v_current_enrollment > v_max_siso THEN
                    RAISE_APPLICATION_ERROR(-20502,
                        'Loi vi pham si so ghi danh: Lop hoc ID ' || g_affected_classes(i) ||
                        ' chi cho phep toi da ' || v_max_siso ||
                        ' hoc vien ghi danh (hien tai: ' || v_current_enrollment || ').');
                END IF;
            END LOOP;
        END IF;
    END AFTER STATEMENT;

END TRG_DANGKY_ENROLLMENT_LIMIT;
/

------------------------------------------------------------
-- HOAN TAT CAI DAT TRIGGER - END OF PART 2
------------------------------------------------------------
-- Kiem tra tat ca trigger da duoc tao thanh cong
SELECT TRIGGER_NAME, TABLE_NAME, STATUS
FROM USER_TRIGGERS
WHERE TRIGGER_NAME IN (
    'TRG_DANGKY_CLASS_STATUS',
    'TRG_LOPHOC_TEACHER_STATUS',
    'TRG_LOPHOC_STATUS_CASCADE',
    'TRG_HOCBONG_MARK_USED',
    'TRG_HOADON_CORRECTION_VALIDATE',
    'TRG_DIEMDANH_LIMIT_CHECK',
    'TRG_BUOIHOC_RESOURCE_VAL',
    'TRG_THAMGIABKT_SCORE_VAL',
    'TRG_INVOICE_LOCK',
    'TRG_HOANTRA_AMOUNT_CHECK',
    'TRG_YCCL_ENROLLMENT_CHECK',
    'TRG_DANGKY_ENROLLMENT_LIMIT'
)
ORDER BY TRIGGER_NAME;
