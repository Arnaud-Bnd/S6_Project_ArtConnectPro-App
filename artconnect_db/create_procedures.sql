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

    -- Vérifie le membre
    SELECT COUNT(*)
    INTO v_exists
    FROM Community_members
    WHERE member_id = p_member_id;

    IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Membre inexistant';
    END IF;

    -- Vérifie le workshop
    SELECT COUNT(*)
    INTO v_exists
    FROM Workshops
    WHERE workshop_id = p_workshop_id;

    IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Workshop inexistant';
    END IF;

    -- Vérifie si déjà inscrit
    SELECT COUNT(*)
    INTO v_exists
    FROM Bookings
    WHERE workshop_id = p_workshop_id
      AND member_id = p_member_id;

    IF v_exists > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ce membre est déjà inscrit à ce workshop';
    END IF;

    -- Insertion réservation
    INSERT INTO Bookings (
        booking_date,
        payment_status,
        workshop_id,
        member_id
    )
    VALUES (
        p_booking_date,
        p_payment_status,
        p_workshop_id,
        p_member_id
    );
END //

CREATE PROCEDURE sp_assign_artist_to_workshop(
    IN p_artist_id INT,
    IN p_workshop_id INT
)
BEGIN
    DECLARE v_exists INT;

    -- Vérifie artiste
    SELECT COUNT(*)
    INTO v_exists
    FROM Artists
    WHERE artist_id = p_artist_id;

    IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Artiste inexistant';
    END IF;

    -- Vérifie workshop
    SELECT COUNT(*)
    INTO v_exists
    FROM Workshops
    WHERE workshop_id = p_workshop_id;

    IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Workshop inexistant';
    END IF;

    -- Assigne l'artiste comme instructeur
    UPDATE Workshops
    SET instructor_id = p_artist_id
    WHERE workshop_id = p_workshop_id;

END //


CREATE PROCEDURE sp_create_booking(
    IN p_booking_date DATE,
    IN p_payment_status VARCHAR(50),
    IN p_workshop_id INT,
    IN p_member_id INT
)
BEGIN
    DECLARE v_exists INT;
    DECLARE v_places_restantes INT;

    -- 1. LE FILET DE SÉCURITÉ : En cas d'erreur, on annule tout (ROLLBACK)
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        ROLLBACK;
        RESIGNAL;
    END;

    -- 2. DÉBUT DE LA TRANSACTION ACID
    START TRANSACTION;

    -- Vérifie le membre
    SELECT COUNT(*) INTO v_exists FROM Community_members WHERE member_id = p_member_id;
    IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Membre inexistant';
    END IF;

    -- Vérifie le workshop
    SELECT COUNT(*) INTO v_exists FROM Workshops WHERE workshop_id = p_workshop_id;
    IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Workshop inexistant';
    END IF;

    -- Vérifie les places disponibles
    SELECT max_participants - (SELECT COUNT(*) FROM Bookings WHERE workshop_id = p_workshop_id)
    INTO v_places_restantes
    FROM Workshops WHERE workshop_id = p_workshop_id;

    IF v_places_restantes <= 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Plus de places disponibles pour cet atelier';
    END IF;

    -- Vérifie si déjà inscrit
    SELECT COUNT(*) INTO v_exists FROM Bookings WHERE workshop_id = p_workshop_id AND member_id = p_member_id;
    IF v_exists > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Ce membre est déjà inscrit à ce workshop';
    END IF;

    -- Insertion de la réservation
    INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id)
    VALUES (p_booking_date, p_payment_status, p_workshop_id, p_member_id);

    -- 3. TOUT S'EST BIEN PASSÉ : ON VALIDE LA SAUVEGARDE (COMMIT)
    COMMIT;
END //

DELIMITER ;

DELIMITER ;