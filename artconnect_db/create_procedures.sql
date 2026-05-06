USE artconnect_db;

DELIMITER //

CREATE PROCEDURE sp_create_booking(
    IN p_booking_date DATE,
    IN p_payment_status VARCHAR(50),
    IN p_workshop_id INT,
    IN p_member_id INT
)
BEGIN
    DECLARE v_exists INT;

    SELECT COUNT(*)
    INTO v_exists
    FROM Community_members
    WHERE member_id = p_member_id;

    IF v_exists = 0 THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Membre inexistant';
    END IF;

    SELECT COUNT(*)
    INTO v_exists
    FROM Workshops
    WHERE workshop_id = p_workshop_id;

    IF v_exists = 0 THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Workshop inexistant';
    END IF;

    SELECT COUNT(*)
    INTO v_exists
    FROM Bookings
    WHERE workshop_id = p_workshop_id 
      AND member_id = p_member_id;

    IF v_exists > 0 THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ce membre est déjà inscrit à ce workshop';
    END IF;

    INSERT INTO Bookings(booking_date, payment_status, workshop_id, member_id)
    VALUES (p_booking_date, p_payment_status, p_workshop_id, p_member_id);
END //

CREATE PROCEDURE sp_assign_artist_to_workshop(
    IN p_artist_id INT,
    IN p_workshop_id INT
)
BEGIN
    DECLARE v_exists INT;

    SELECT COUNT(*)
    INTO v_exists
    FROM Artists
    WHERE artist_id = p_artist_id;

    IF v_exists = 0 THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Artiste inexistant';
    END IF;

    SELECT COUNT(*)
    INTO v_exists
    FROM Workshops
    WHERE workshop_id = p_workshop_id;

    IF v_exists = 0 THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Workshop inexistant';
    END IF;

    SELECT COUNT(*)
    INTO v_exists
    FROM Bookings
    WHERE workshop_id = p_workshop_id
      AND member_id = p_artist_id;

    IF v_exists > 0 THEN 
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Association artiste-workshop déjà existante';
    END IF;

    INSERT INTO Bookings(workshop_id, member_id, booking_date, payment_status)
    VALUES (p_workshop_id, p_artist_id, CURDATE(), 'ASSIGNED');
END //

DELIMITER ;