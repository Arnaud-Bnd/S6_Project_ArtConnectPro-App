USE  artconnect_db;

CALL sp_create_booking('2026-04-07', 'paid', 1, 2);
CALL sp_create_booking('2026-04-07', 'paid', 1, 3);	


INSERT INTO Review(rating, comment, review_date, artwork_id, member_id)
VALUES (5, 'Excellent', '2026-04-07', 1, 1);

INSERT INTO Exhibitions(title, endDate, description, curatorName, start_date, theme, gallery_id)
VALUES ('Expo test', '2026-04-01', 'desc', 'Curator X', '2026-04-10', 'Modern Art', 1);

CALL sp_assign_artist_to_workshop(1, 1);