USE artconnect_db;

DELIMITER //

CREATE FUNCTION fn_booking_count(p_workshop_id INT)
RETURNS INT
DETERMINISTIC
READS SQL DATA
BEGIN
    DECLARE v_count INT;

    SELECT COUNT(*)
    INTO v_count
    FROM Bookings
    WHERE workshop_id = p_workshop_id;

    RETURN v_count;
END//

DELIMITER ;