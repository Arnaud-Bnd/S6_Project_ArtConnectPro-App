USE artconnect_db;

START TRANSACTION;
	-- 1. Créer une réservation
	INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id)
	VALUES (CURDATE(), 'Payé', 1, 1);

	-- 2. Vérifier la capacité (exemple logique)
	UPDATE Workshops
	SET max_participants = max_participants - 1
	WHERE workshop_id = 1;
    
-- Si tout est OK
COMMIT;



START TRANSACTION;
	-- 1. Mettre à jour le statut
	UPDATE Bookings
	SET payment_status = 'Annulé'
	WHERE workshop_id = 2 AND member_id = 2;

	-- 2. Réaugmenter la capacité
	UPDATE Workshops
	SET max_participants = max_participants + 1
	WHERE workshop_id = 2;
COMMIT;



START TRANSACTION;
	-- 1. Ajouter une œuvre
	INSERT INTO Artworks (title, creation_year, medium, type, description, dimensions, price, status, artist_id)
	VALUES ('Nouvelle œuvre test', 2025, 'Huile', 'Peinture', 'Œuvre contemporaine', '100x100 cm', 5000, 'FOR_SALE', 1);

	-- 2. Récupérer ID (en pratique via LAST_INSERT_ID)
	SET @artwork_id = LAST_INSERT_ID();

	-- 3. Lier à une exposition
	INSERT INTO Exposes (artwork_id, exhibition_id)
	VALUES (@artwork_id, 1);
COMMIT;



START TRANSACTION;
	-- 1. Vérifier que l'œuvre est disponible
	UPDATE Artworks
	SET status = 'SOLD'
	WHERE artwork_id = 1 AND status = 'FOR_SALE';

	-- 2. Vérification simple (si aucune ligne modifiée → rollback côté appli)
	-- (MySQL pur ne gère pas IF ROWCOUNT sans procédure)
COMMIT;



START TRANSACTION;
	-- 1. Réserver workshop
	INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id)
	VALUES (CURDATE(), 'Payé', 3, 4);

	-- 2. Ajouter discipline préférée automatiquement
	INSERT INTO Prefers (member_id, discipline_id)
	VALUES (4, 5); -- ex: cinéma
COMMIT;



START TRANSACTION;
	INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id)
	VALUES (CURDATE(), 'Payé', 1, 1);

	-- ERREUR volontaire (ID workshop inexistant)
	INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id)
	VALUES (CURDATE(), 'Payé', 999, 1);
ROLLBACK;

