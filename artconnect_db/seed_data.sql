USE artconnect_db;

/* =========================
   ARTISTS
========================= */
INSERT INTO Artists (name, city, contact_email, phone, birth_year, bio, website, social_media, is_active) VALUES
('Auguste Rodin', 'Paris', 'rodin@art.fr', NULL, 1840, 'Sculpteur majeur du XIXe siècle, pionnier de la sculpture moderne.', 'https://musee-rodin.fr', NULL, FALSE),
('Claude Monet', 'Giverny', 'monet@art.fr', NULL, 1840, 'Fondateur de l’impressionnisme.', 'https://claudemonet.fr', NULL, FALSE),
('Victor Hugo', 'Paris', 'hugo@art.fr', NULL, 1802, 'Écrivain romantique majeur.', 'https://victorhugo.fr', NULL, FALSE),
('Jean-Luc Godard', 'Paris', 'godard@cinema.fr', NULL, 1930, 'Pionnier de la Nouvelle Vague.', 'https://godard-cinema.fr', NULL, FALSE),
('Ludwig van Beethoven', 'Vienne', 'beethoven@music.fr', NULL, 1770, 'Compositeur classique majeur.', 'https://beethoven-music.fr', NULL, FALSE),
('Pablo Picasso', 'Mougins', 'picasso@art.fr', NULL, 1881, 'Fondateur du cubisme.', 'https://picasso.fr', NULL, FALSE),
('Frida Kahlo', 'Mexico', 'kahlo@art.fr', NULL, 1907, 'Peintre symbolique et surréaliste.', 'https://fridakahlo.mx', NULL, FALSE),
('Albert Camus', 'Paris', 'camus@art.fr', NULL, 1913, 'Écrivain existentialiste.', 'https://camus.fr', NULL, FALSE),
('Hayao Miyazaki', 'Tokyo', 'miyazaki@anime.fr', NULL, 1941, 'Maître de l’animation japonaise.', 'https://ghibli.jp', '@ghibli_official', TRUE),
('Édith Piaf', 'Paris', 'piaf@music.fr', NULL, 1915, 'Chanteuse emblématique française.', 'https://edithpiaf.fr', NULL, FALSE),
('Andy Warhol', 'New York', 'warhol@art.fr', NULL, 1928, 'Figure du pop art.', NULL, '@warhol', FALSE),
('Banksy', NULL, 'banksy@street.art', NULL, 1974, 'Artiste street art anonyme.', NULL, '@banksy', TRUE),
('Christopher Nolan', 'London', 'nolan@cinema.com', NULL, 1970, 'Réalisateur contemporain majeur.', NULL, '@nolan', TRUE),
('Hans Zimmer', NULL, 'zimmer@music.com', NULL, 1957, 'Compositeur de musiques de films.', NULL, '@zimmer', TRUE);


/* =========================
   ARTWORKS
========================= */
INSERT INTO Artworks (title, creation_year, medium, type, description, dimensions, price, status, artist_id) VALUES
('Le Penseur', 1904, 'Bronze', 'Sculpture', 'Figure philosophique.', '180 cm', 2000000, 'SOLD', 1),
('Nymphéas', 1916, 'Huile sur toile', 'Peinture', 'Jardin aquatique.', NULL, 5000000, 'SOLD', 2),
('Les Contemplations', 1856, 'Texte', 'Littérature', 'Recueil poétique.', NULL, NULL, 'SOLD', 3),
('À bout de souffle', 1960, 'Film', 'Cinéma', 'Nouvelle Vague.', '90 min', NULL, 'SOLD', 4),
('Symphonie n°9', 1824, 'Partition', 'Musique', 'Ode à la joie.', '70 min', NULL, 'SOLD', 5),
('Guernica', 1937, 'Huile sur toile', 'Peinture', 'Guerre civile espagnole.', NULL, NULL, 'SOLD', 6),
('Les Deux Fridas', 1939, 'Huile sur toile', 'Peinture', 'Identité double.', NULL, NULL, 'SOLD', 7),
('L’Étranger', 1942, 'Roman', 'Littérature', 'Absurdie.', NULL, NULL, 'SOLD', 8),
('Le Voyage de Chihiro', 2001, 'Animation', 'Cinéma', 'Conte initiatique.', '125 min', NULL, 'SOLD', 9),
('La Vie en rose', 1947, 'Chanson', 'Musique', 'Chanson iconique.', '3 min', NULL, 'SOLD', 10),
('Marilyn Diptych', 1962, 'Sérigraphie', 'Peinture', 'Pop art iconique.', NULL, NULL, 'SOLD', 11),
('Girl with Balloon', 2002, 'Spray paint', 'Street Art', 'Art urbain symbolique.', NULL, NULL, 'FOR_SALE', 12),
('Inception', 2010, 'Film', 'Cinéma', 'Rêves imbriqués.', '148 min', NULL, 'SOLD', 13),
('Interstellar OST', 2014, 'Musique', 'Musique', 'Bande originale du film.', '2h', NULL, 'SOLD', 14);


/* =========================
   WORKSHOPS
========================= */
INSERT INTO Workshops (title, date_, price, level, duration_minutes, max_participants, location, description, instructor_id) VALUES
('Introduction sculpture moderne', '2025-03-10 10:00:00', 120, 'Débutant', 240, 10, 'Paris', 'Sculpture contemporaine.', 1),
('Cinéma contemporain', '2025-05-05 09:00:00', 150, 'Avancé', 300, 8, 'London Studio', 'Analyse filmique.', 13),
('Musique de film', '2025-06-01 14:00:00', 200, 'Avancé', 360, 6, 'Studio Berlin', 'Composition cinéma.', 14);


/* =========================
   GALLERIES
========================= */
INSERT INTO Galleries (name, address, owner_name, opening_hours, contact_phone, rating, website) VALUES
('Musée Rodin', 'Paris', 'État', '10h-18h', NULL, 4.9, 'https://musee-rodin.fr'),
('MoMA', 'New York', 'MoMA Org', '10h-18h', NULL, 4.8, 'https://moma.org'),
('Street Art Gallery', NULL, 'Collectif urbain', '24/7', NULL, 4.5, NULL),
('Studio Ghibli Museum', 'Tokyo', 'Ghibli', '10h-18h', NULL, 4.9, 'https://ghibli.jp'),
('Galerie du Marais', '15 Rue de Bretagne, 75003 Paris', 'Hélène Rousseau', 'Mar-Sam 11h-19h', '0142781234', 4.70, 'https://galeriedumaris.fr'),
('Espace Art Lyon', '8 Place Bellecour, 69002 Lyon', 'Antoine Garnier', 'Mer-Dim 10h-18h', '0472345678', 4.20, 'https://espaceartlyon.com'),
('La Friche Belle de Mai', '41 Rue Jobin, 13003 Marseille', 'Collectif FBM', 'Mar-Dim 9h-20h', '0491678901', 4.80, 'https://lafriche.org'),
('Galerie Confluence', '112 Quai Perrache, 69002 Lyon', 'Marie-Claire Noel', 'Lun-Sam 10h-19h', '0478901234', 4.10, 'https://galerieconfluence.fr'),
('Studio Galerie Nantes', '3 Rue du Calvaire, 44000 Nantes', 'Bertrand Leroy', 'Mar-Sam 12h-19h', '0240123456', 3.90, 'https://studiogalerienantes.fr'),
('Maison des Arts Bordeaux', '22 Cours Victor Hugo, 33000 Bordeaux', 'Sophie Dumas', 'Mer-Dim 11h-18h', '0556234567', 4.50, 'https://maisondesartsbdx.fr'),
('Galerie Strasbourg Art', '5 Place Gutenberg, 67000 Strasbourg', 'Klaus Müller', 'Mar-Sam 10h-18h30', '0388345678', 4.30, 'https://galeriestrasbourg.eu'),
('Art ik Gallery', '78 Rue Nationale, 59000 Lille', 'Fatima Ouedraogo', 'Mar-Sam 11h-19h', '0320456789', 4.60, 'https://artikgallery.fr'),
('Galerie Azur', '12 Promenade des Anglais, 06000 Nice', 'Marco Ferretti', 'Lun-Sam 10h-19h', '0493567890', 4.00, 'https://galerieazur.com'),
('Espace Créatif Rennes', '6 Rue Saint-Melaine, 35000 Rennes', 'Anne-Sophie Riou', 'Jeu-Dim 13h-19h', '0299678901', 3.80, 'https://espacecreatifrennes.fr');


/* =========================
   TAGS
========================= */
INSERT INTO Artwork_Tags (name) VALUES
('Sculpture'),
('Peinture'),
('Cinéma'),
('Musique'),
('Littérature'),
('Street Art'),
('Pop Art'),
('Animation');


/* =========================
   COMMUNITY MEMBERS
========================= */
INSERT INTO Community_members (name, email, birth_year, city, membership_type, phone) VALUES
('Alice Martin', 'alice.martin@gmail.com', 1988, 'Paris', 'Premium', '0611111111'),
('Bruno Lefevre', 'bruno.lefevre@hotmail.fr', 1975, 'Lyon', 'Standard', '0622222222'),
('Camille Bertrand', 'camille.bertrand@yahoo.fr', 1995, 'Bordeaux', 'Premium', '0633333333'),
('David Chen', 'david.chen@outlook.com', 1990, 'Paris', 'Étudiant', '0644444444'),
('Estelle Morin', 'estelle.morin@free.fr', 1982, 'Nantes', 'Standard', '0655555555'),
('Fabrice Dupont', 'fabrice.dupont@gmail.com', 1968, 'Toulouse', 'Premium', '0666666666'),
('Gabrielle Noir', 'gabrielle.noir@laposte.net', 1999, 'Lille', 'Étudiant', '0677777777'),
('Hassan Aït Kaci', 'hassan.aitkaci@gmail.com', 1985, 'Marseille', 'Standard', '0688888888'),
('Ines Lacroix', 'ines.lacroix@sfr.fr', 1993, 'Nice', 'Premium', '0699999999'),
('Julien Barbier', 'julien.barbier@gmail.com', 1979, 'Rennes', 'Standard', '0610101010'),
('Karine Vidal', 'karine.vidal@orange.fr', 1970, 'Strasbourg', 'Premium', '0621212121'),
('Laurent Schmitt', 'laurent.schmitt@gmail.com', 2000, 'Paris', 'Étudiant', '0632323232');


/* =========================
   DISCIPLINES
========================= */
INSERT INTO Disciplines (name) VALUES
('Peinture'),
('Sculpture'),
('Photographie'),
('Gravure'),
('Cinéma'),
('Littérature'),
('Musique'),
('Street Art'),
('Animation'),
('Pop Art');


/* =========================
   EXHIBITIONS
========================= */
INSERT INTO Exhibitions (title, end_date, description, curator_name, start_date, theme, gallery_id) VALUES
('Les Maîtres de la Sculpture', '2025-06-30', 'Exposition consacrée aux grandes œuvres sculpturales du XIXe siècle.', 'Claire Dumont', '2025-03-01', 'Sculpture classique', 1),
('Lumières Impressionnistes', '2025-07-15', 'Immersion dans les paysages et jeux de lumière impressionnistes.', 'Paul Rivière', '2025-04-01', 'Impressionnisme', 1),
('Cubisme et Modernité', '2025-09-01', 'Exploration des formes et perspectives du cubisme.', 'Sophie Lambert', '2025-05-10', 'Cubisme', 2),
('Voix de la Littérature Française', '2025-08-20', 'Exposition autour des manuscrits et œuvres littéraires françaises.', 'Marie Valois', '2025-05-01', 'Littérature', 2),
('Cinéma et Nouvelle Vague', '2025-10-10', 'Projection et analyse des films majeurs de la Nouvelle Vague.', 'Julien Moreau', '2025-06-01', 'Cinéma', 3),
('Musique et Émotions', '2025-11-01', 'Exposition immersive autour des grands compositeurs et interprètes.', 'Antoine Berger', '2025-07-01', 'Musique', 2),
('Le Monde de Miyazaki', '2025-12-20', 'Univers poétique et écologique des films de Miyazaki.', 'Yuki Tanaka', '2025-09-01', 'Animation japonaise', 4),
('Street Art : Banksy et Après', '2025-08-30', 'Regards contemporains sur le street art engagé.', 'Nina Rodriguez', '2025-05-20', 'Street Art', 3),
('Pop Art Forever', '2025-09-15', 'Retour sur les icônes du Pop Art américain.', 'Emma Collins', '2025-06-10', 'Pop Art', 2),
('Femmes et Art Moderne', '2025-10-30', 'Exposition dédiée aux artistes féminines majeures du XXe siècle.', 'Camille Renard', '2025-07-15', 'Art moderne', 1);


/* =========================
   BOOKINGS
========================= */
INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id) VALUES
('2025-01-15', 'Payé', 1, 1),
('2025-01-20', 'Payé', 2, 2),
('2025-01-22', 'En attente', 3, 3),
('2025-02-01', 'Payé', 1, 4),
('2025-02-05', 'Payé', 2, 5),
('2025-02-10', 'Annulé', 3, 6),
('2025-02-15', 'Payé', 1, 7),
('2025-02-18', 'Payé', 2, 8),
('2025-03-01', 'En attente', 3, 9),
('2025-03-05', 'Payé', 1, 10),
('2025-03-10', 'Payé', 2, 11),
('2025-03-12', 'En attente', 3, 12);


/* =========================
   REVIEWS
========================= */
INSERT INTO Reviews (rating, comment, review_date, artwork_id, member_id) VALUES
(5, 'Très belle œuvre.', '2025-01-20', 1, 1),
(4, 'Livre profondément touchant.', '2025-01-25', 3, 2),
(5, 'Composition musicale incroyable.', '2025-02-01', 5, 3);


/* =========================
   PRATIQUES
========================= */
INSERT INTO Pratiques (artist_id, discipline_id) VALUES
(1, 2), -- Rodin → Sculpture
(2, 1), -- Monet → Peinture
(3, 6), -- Victor Hugo → Littérature
(4, 5), -- Godard → Cinéma
(5, 7), -- Beethoven → Musique
(6, 1), -- Picasso → Peinture
(7, 1), -- Frida Kahlo → Peinture
(8, 6), -- Camus → Littérature
(9, 9),  -- Miyazaki → Animation
(10, 7), -- Édith Piaf → Musique
(11, 10), -- Andy Warhol → Pop Art
(12, 8), -- Banksy → Street Art
(13, 5), -- Christopher Nolan → Cinéma
(14, 7); -- Hans Zimmer → Musique