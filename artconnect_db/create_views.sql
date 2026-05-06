USE artconnect_db;

CREATE OR REPLACE VIEW v_artists_public AS
SELECT artist_id,
       name,
       city,
       birth_year,
       bio,
       website,
       social_media,
       is_active
FROM Artists
WHERE is_active = TRUE;

CREATE OR REPLACE VIEW v_artworks_catalog AS
SELECT a.artwork_id,
       a.title,
       a.creation_year,
       a.medium,
       a.type,
       a.description,
       a.dimensions,
       a.price,
       a.status AS available,
       ar.artist_id,
       ar.name AS artist_name,
       ar.city AS artist_city
FROM Artworks a
JOIN Artists ar ON a.artist_id = ar.artist_id;

CREATE OR REPLACE VIEW v_artworks_available AS
SELECT artwork_id,
       title,
       creation_year,
       medium,
       type,
       dimensions,
       price,
       artist_name,
       artist_city
FROM v_artworks_catalog
WHERE available = TRUE;

CREATE OR REPLACE VIEW v_exhibitions_full AS
SELECT e.exhibition_id,
       e.title AS exhibition_title,
       e.start_date,
       e.end_date,
       e.theme,
       e.curator_name,
       e.description AS exhibition_description,
       g.gallery_id,
       g.name AS gallery_name,
       g.address AS gallery_address,
       g.opening_hours,
       g.contact_phone AS gallery_phone,
       g.website AS gallery_website
FROM Exhibitions e
JOIN Galleries g ON e.gallery_id = g.gallery_id;

CREATE OR REPLACE VIEW v_exhibitions_active AS
SELECT *
FROM v_exhibitions_full
WHERE CURDATE() BETWEEN start_date AND end_date;

CREATE OR REPLACE VIEW v_members_safe AS
SELECT member_id,
       name,
       birth_year,
       city,
       membership_type
FROM Community_members;

CREATE OR REPLACE VIEW v_bookings_dashboard AS
SELECT b.booking_id,
       b.booking_date,
       b.payment_status,
       m.member_id,
       m.name AS member_name,
       m.membership_type,
       w.workshop_id,
       w.title AS workshop_title,
       w.date_ AS workshop_date,
       w.price AS workshop_price,
       w.level AS workshop_level,
       w.location AS workshop_location
FROM Bookings b
JOIN Community_members m ON b.member_id = m.member_id
JOIN Workshops w ON b.workshop_id = w.workshop_id;

CREATE OR REPLACE VIEW v_artwork_tags AS
SELECT a.artwork_id,
       a.title,
       a.type,
       GROUP_CONCAT(t.name ORDER BY t.name SEPARATOR ', ') AS tags
FROM Artworks a
JOIN Possedes p ON a.artwork_id = p.artwork_id
JOIN Artwork_Tags t ON p.tag_id = t.tag_id
GROUP BY a.artwork_id, a.title, a.type;

CREATE OR REPLACE VIEW v_artist_disciplines AS
SELECT ar.artist_id,
       ar.name AS artist_name,
       ar.city,
       ar.is_active,
       GROUP_CONCAT(d.name ORDER BY d.name SEPARATOR ', ') AS disciplines
FROM Artists ar
JOIN Pratiques p ON ar.artist_id = p.artist_id
JOIN Disciplines d ON p.discipline_id = d.discipline_id
GROUP BY ar.artist_id, ar.name, ar.city, ar.is_active;

CREATE OR REPLACE VIEW v_reviews_summary AS
SELECT a.artwork_id,
       a.title AS artwork_title,
       a.type AS artwork_type,
       ar.name AS artist_name,
       COUNT(r.review_id) AS review_count,
       ROUND(AVG(r.rating), 2) AS average_rating,
       MAX(r.review_date) AS latest_review
FROM Artworks a
JOIN Artists ar ON a.artist_id = ar.artist_id
LEFT JOIN Reviews r ON a.artwork_id = r.artwork_id
GROUP BY a.artwork_id, a.title, a.type, ar.name;