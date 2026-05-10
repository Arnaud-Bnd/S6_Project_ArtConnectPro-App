USE artconnect_db;

DELIMITER //

CREATE TRIGGER trg_booking_check_capacity
BEFORE INSERT ON Bookings
FOR EACH ROW
BEGIN
    DECLARE current_bookings INT;
    DECLARE workshop_capacity INT;

    -- Récupère la capacité du workshop
    SELECT max_participants
    INTO workshop_capacity
    FROM Workshops
    WHERE workshop_id = NEW.workshop_id;

    -- Vérifie existence workshop
    IF workshop_capacity IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Workshop inexistant ou capacité non définie';
    END IF;

    -- Compte les réservations actuelles
    SELECT COUNT(*)
    INTO current_bookings
    FROM Bookings
    WHERE workshop_id = NEW.workshop_id;

    -- Vérifie capacité
    IF current_bookings >= workshop_capacity THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Réservation impossible : workshop complet';
    END IF;

END //

CREATE TRIGGER trg_review_check_rating
BEFORE INSERT ON Reviews
FOR EACH ROW
BEGIN

    IF NEW.rating < 1 OR NEW.rating > 5 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'La note doit être comprise entre 1 et 5';
    END IF;

END //

CREATE TRIGGER trg_exhibition_check_dates
BEFORE INSERT ON Exhibitions
FOR EACH ROW
BEGIN

    IF NEW.end_date < NEW.start_date THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Date de fin invalide : end_date < start_date';
    END IF;

END //

DELIMITER ;