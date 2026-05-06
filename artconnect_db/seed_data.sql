USE artconnect_db;

INSERT INTO Artists (name, city, contact_email, phone, birth_year, bio, website, social_media, is_active) VALUES
('Sophie Marceau', 'Paris', 'sophie.marceau@art.fr', '0612345678', 1990, 'Peintre expressionniste spécialisée dans les portraits urbains. Exposée dans plus de 15 galeries en Europe.', 'https://sophiemarceau.art', '@sophieart_insta', TRUE),
('Lucas Durand', 'Lyon', 'lucas.durand@studio.fr', '0623456789', 1985, 'Sculpteur contemporain travaillant principalement le bronze et le béton. Lauréat du Prix de la Sculpture 2019.', 'https://lucasdurand.com', '@lucas_sculpture', TRUE),
('Amina Benali', 'Marseille', 'amina.benali@creative.fr', '0634567890', 1992, 'Photographe documentaire et artiste numérique. Ses œuvres explorent l identité et la diaspora.', 'https://aminabenali.fr', '@aminaphotography', TRUE),
('Thomas Leclerc', 'Bordeaux', 'thomas.leclerc@arts.fr', '0645678901', 1978, 'Peintre abstrait influencé par le mouvement Fluxus. Professeur aux Beaux-Arts de Bordeaux.', 'https://thomasleclerc.art', '@thomasabstract', TRUE),
('Chloé Petit', 'Nantes', 'chloe.petit@design.fr', '0656789012', 1995, 'Illustratrice et aquarelliste. Collabore régulièrement avec des maisons d\'édition parisiennes.', 'https://chloepetit.fr', '@chloe_watercolor', TRUE),
('Marc Fontaine', 'Toulouse', 'marc.fontaine@atelier.fr', '0667890123', 1982, 'Céramiste et artisan d\'art. Fondateur de l\'atelier collectif "Terre & Feu" à Toulouse.', 'https://terreetfeu.fr', '@marc_ceramique', TRUE),
('Isabelle Moreau', 'Strasbourg', 'isabelle.moreau@galerie.fr', '0678901234', 1975, 'Graveuse et lithographe reconnue. Ses estampes figurent dans plusieurs collections nationales.', 'https://isabellemoreau.eu', '@isabelle_gravure', FALSE),
('Karim El Fassi', 'Lille', 'karim.elfassi@studio.fr', '0689012345', 1988, 'Artiste multimédia et vidéaste. Ses installations ont été présentées à la Biennale de Venise 2022.', 'https://karimelfassi.com', '@karim_multimedia', TRUE),
('Elena Vasquez', 'Nice', 'elena.vasquez@artstudio.fr', '0690123456', 1993, 'Peintre surréaliste d\'origine espagnole. Ses toiles mêlent onirisme et mythologie.', 'https://elenavasquez.art', '@elena_surrealisme', TRUE),
('Pierre Guillot', 'Rennes', 'pierre.guillot@craft.fr', '0601234567', 1970, 'Maître verrier et artiste plasticien. Plus de 30 ans d\'expérience dans la création de vitraux contemporains.', 'https://pierreguillot.fr', '@pierre_verre', FALSE);

INSERT INTO Artworks (title, creation_year, medium, type, description, dimensions, price, status, artist_id) VALUES
('Lumières de la Ville', 2021, 'Huile sur toile', 'Peinture', 'Portrait nocturne d\'une rue parisienne sous la pluie, jeu de reflets et de lumières artificielles.', '100x80 cm', 3500.00, TRUE, 1),
('Fragments Urbains', 2020, 'Huile sur toile', 'Peinture', 'Série de visages fragmentés évoquant la solitude dans la foule.', '120x90 cm', 4200.00, TRUE, 1),
('L\'Éveil', 2019, 'Bronze', 'Sculpture', 'Figure humaine en mouvement, symbolisant l\'éveil de la conscience.', '60x30x25 cm', 8500.00, FALSE, 2),
('Méandres', 2022, 'Béton et acier', 'Sculpture', 'Installation abstraite explorant les flux et tensions urbaines.', '200x50x50 cm', 15000.00, TRUE, 2),
('Racines', 2021, 'Photographie numérique', 'Photographie', 'Série de portraits de femmes issues de la diaspora africaine.', '80x60 cm', 1200.00, TRUE, 3),
('Babel Moderne', 2022, 'Photographie argentique', 'Photographie', 'Dyptique représentant des marchés populaires à Marseille et Alger.', '100x70 cm', 2100.00, TRUE, 3),
('Vibrations #3', 2020, 'Acrylique sur toile', 'Peinture', 'Composition abstraite chromatique et rythmique.', '150x150 cm', 5600.00, FALSE, 4),
('Sans Titre XII', 2023, 'Technique mixte', 'Peinture', 'Œuvre expérimentale mêlant collage et peinture.', '90x70 cm', 3100.00, TRUE, 4),
('Jardins Secrets', 2022, 'Aquarelle', 'Illustration', 'Série de 5 illustrations botaniques imaginaires.', '42x30 cm', 850.00, TRUE, 5),
('Contes d\'Été', 2023, 'Encre et aquarelle', 'Illustration', 'Illustration narrative pour album jeunesse.', '50x35 cm', 1100.00, TRUE, 5),
('Bol Céladon n°7', 2021, 'Grès émaillé', 'Céramique', 'Bol tourné à la main, cuisson réduction.', '12x20 cm', 320.00, FALSE, 6),
('Vase Flamme', 2022, 'Porcelaine', 'Céramique', 'Vase en porcelaine aux formes organiques.', '35x15 cm', 780.00, TRUE, 6),
('Mémoire Gravée', 2018, 'Eau-forte', 'Gravure', 'Série de 12 estampes sur la mémoire.', '40x30 cm', 2400.00, TRUE, 7),
('Boucle', 2023, 'Vidéo installation', 'Numérique', 'Installation vidéo en boucle.', '4K, 12 min', 6000.00, TRUE, 8),
('Miroirs Brisés', 2022, 'Technique mixte numérique', 'Numérique', 'Installation interactive fragmentant le reflet.', 'Variable', 9500.00, FALSE, 8),
('Songe d\'Icare', 2023, 'Huile sur toile', 'Peinture', 'Composition surréaliste contemporaine.', '180x140 cm', 7200.00, TRUE, 9),
('Animus', 2021, 'Acrylique sur bois', 'Peinture', 'Portraits oniriques hybrides.', '60x60 cm', 2800.00, TRUE, 9),
('Rose Cathédrale', 2015, 'Vitrail', 'Vitrail', 'Vitrail contemporain inspiré des rosaces gothiques.', '80x80 cm', 12000.00, TRUE, 10);

INSERT INTO Workshops (title, date_, price, level, duration_minutes, max_participants, location, description, instructor_id) VALUES
('Introduction à la peinture à l\'huile', '2025-02-10 10:00:00', 75.00, 'Débutant', 180, 12, 'Atelier Sophie Marceau - Paris', 'Bases de la peinture à l\'huile.', 1),
('Sculpture sur argile - niveau avancé', '2025-03-05 14:00:00', 120.00, 'Avancé', 240, 8, 'Studio Lucas Durand - Lyon', 'Techniques avancées de modelage.', 2),
('Photographie de rue et reportage', '2025-02-20 09:00:00', 90.00, 'Intermédiaire', 300, 15, 'Marseille Centre', 'Atelier photo en extérieur.', 3),
('Aquarelle botanique', '2025-04-12 10:00:00', 65.00, 'Débutant', 150, 10, 'Atelier Chloé Petit - Nantes', 'Peinture botanique.', 5),
('Initiation à la céramique au tour', '2025-03-22 11:00:00', 85.00, 'Débutant', 210, 8, 'Atelier Terre & Feu - Toulouse', 'Tournage de l\'argile.', 6),
('Gravure sur linogravure', '2025-05-08 15:00:00', 70.00, 'Intermédiaire', 180, 10, 'Atelier Isabelle Moreau - Strasbourg', 'Technique de gravure.', 7),
('Art numérique et vidéo mapping', '2025-04-25 16:00:00', 150.00, 'Avancé', 360, 6, 'Studio Karim El Fassi - Lille', 'Installation interactive.', 8),
('Peinture abstraite expressive', '2025-03-15 10:00:00', 80.00, 'Intermédiaire', 180, 12, 'Galerie des Arts - Bordeaux', 'Expression abstraite.', 4),
('Vitrail contemporain - initiation', '2025-06-07 13:00:00', 95.00, 'Débutant', 240, 6, 'Atelier Pierre Guillot - Rennes', 'Création de vitraux.', 10),
('Portrait photographique en studio', '2025-05-17 14:00:00', 110.00, 'Intermédiaire', 270, 8, 'Studio Photo Lumière - Nice', 'Portrait en studio.', 3);

INSERT INTO Galleries (name, address, owner_name, opening_hours, contact_phone, rating, website) VALUES
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

INSERT INTO Artwork_Tags (name) VALUES
('Abstrait'),
('Portrait'),
('Urbain'),
('Nature'),
('Surréalisme'),
('Identité'),
('Lumière'),
('Mémoire'),
('Corps'),
('Numérique'),
('Contemporain'),
('Traditionnel'),
('Politique'),
('Onirisme'),
('Minimalisme');

INSERT INTO Community_Members (name, email, birth_year, city, membership_type, phone) VALUES
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

INSERT INTO Disciplines (name) VALUES
('Peinture'),
('Sculpture'),
('Photographie'),
('Gravure'),
('Céramique'),
('Art numérique'),
('Illustration'),
('Vitrail'),
('Installation'),
('Dessin');

INSERT INTO Exhibitions (title, end_date, description, curator_name, start_date, theme, gallery_id) VALUES
('Visages de la Ville', '2025-03-30', 'Exposition collective autour du portrait urbain contemporain.', 'Marie Cuvier', '2025-02-01', 'Portrait Urbain', 1),
('Matières Vivantes', '2025-04-20', 'Exposition sculpture et céramique.', 'Jean-Paul Tessier', '2025-03-01', 'Sculpture & Matière', 2);

INSERT INTO Bookings (booking_date, payment_status, workshop_id, member_id) VALUES
('2025-01-15', 'Payé', 1, 1),
('2025-01-20', 'Payé', 2, 2),
('2025-01-22', 'En attente', 3, 3),
('2025-02-01', 'Payé', 4, 4),
('2025-02-05', 'Payé', 5, 5),
('2025-02-10', 'Annulé', 6, 6),
('2025-02-15', 'Payé', 7, 7),
('2025-02-18', 'Payé', 8, 8),
('2025-03-01', 'En attente', 9, 9),
('2025-03-05', 'Payé', 10, 10),
('2025-03-10', 'Payé', 1, 11),
('2025-03-12', 'En attente', 2, 12);

INSERT INTO Reviews (rating, comment, review_date, artwork_id, member_id) VALUES
(5, 'Très belle œuvre.', '2025-01-20', 1, 1),
(4, 'Bonne sculpture.', '2025-01-25', 3, 2),
(5, 'Magnifique série.', '2025-02-01', 5, 3);