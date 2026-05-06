DROP DATABASE IF EXISTS artconnect_db;
CREATE DATABASE artconnect_db;
USE artconnect_db;

CREATE TABLE IF NOT EXISTS Artists (
    artist_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    city VARCHAR(50),
    contact_email VARCHAR(50) UNIQUE NOT NULL,
    phone VARCHAR(10),
    birth_year YEAR,
    bio TEXT,
    website VARCHAR(255),
    social_media VARCHAR(255),
    is_active BOOLEAN
);

CREATE TABLE IF NOT EXISTS Artworks (
    artwork_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    creation_year YEAR,
    medium VARCHAR(50),
    type VARCHAR(50),
    description TEXT,
    dimensions VARCHAR(50),
    price DECIMAL(10,2),
    status BOOLEAN,
    artist_id INT,
    CONSTRAINT fk_artwork_artist FOREIGN KEY (artist_id) REFERENCES Artists(artist_id)
);

CREATE TABLE IF NOT EXISTS Workshops (
    workshop_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    date_ DATETIME NOT NULL,
    price DECIMAL(10,2),
    level VARCHAR(50),
    duration_minutes INT,
    max_participants INT,
    location VARCHAR(50),
    description TEXT,

    -- 🔥 AJOUT IMPORTANT
    instructor_id INT,

    CONSTRAINT fk_workshop_artist
        FOREIGN KEY (instructor_id)
        REFERENCES Artists(artist_id)
);

CREATE TABLE IF NOT EXISTS Galleries (
    gallery_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    owner_name VARCHAR(50),
    opening_hours VARCHAR(50),
    contact_phone VARCHAR(10),
    rating DECIMAL(15,2),
    website VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS Artwork_Tags (
    tag_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Community_members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    birth_year YEAR,
    city VARCHAR(50),
    membership_type VARCHAR(50),
    phone VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS Disciplines (
    discipline_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Exhibitions (
    exhibition_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    end_date DATE,
    description TEXT,
    curator_name VARCHAR(100),
    start_date DATE,
    theme VARCHAR(100),
    gallery_id INT,
    CONSTRAINT fk_exhibition_gallery FOREIGN KEY (gallery_id) REFERENCES Galleries(gallery_id)
);

CREATE TABLE IF NOT EXISTS Bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_date DATE NOT NULL,
    payment_status VARCHAR(50),
    workshop_id INT,
    member_id INT,
    CONSTRAINT uq_booking UNIQUE(workshop_id, member_id),
    CONSTRAINT fk_booking_workshop FOREIGN KEY (workshop_id) REFERENCES Workshops(workshop_id),
    CONSTRAINT fk_booking_member FOREIGN KEY (member_id) REFERENCES Community_members(member_id)
);

CREATE TABLE IF NOT EXISTS Reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    rating INT,
    CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5),
    comment TEXT,
    review_date DATE,
    artwork_id INT,
    member_id INT,
    CONSTRAINT fk_review_artwork FOREIGN KEY (artwork_id) REFERENCES Artworks(artwork_id),
    CONSTRAINT fk_review_member FOREIGN KEY (member_id) REFERENCES Community_members(member_id)
);

CREATE TABLE IF NOT EXISTS Pratiques (
    artist_id INT NOT NULL,
    discipline_id INT NOT NULL,
    PRIMARY KEY (artist_id, discipline_id),
    CONSTRAINT fk_pratique_artist FOREIGN KEY (artist_id) REFERENCES Artists(artist_id),
    CONSTRAINT fk_pratique_discipline FOREIGN KEY (discipline_id) REFERENCES Disciplines(discipline_id)
);

CREATE TABLE IF NOT EXISTS Possedes (
    artwork_id INT NOT NULL,
    tag_id INT NOT NULL,
    PRIMARY KEY (artwork_id, tag_id),
    CONSTRAINT fk_possede_artwork FOREIGN KEY (artwork_id) REFERENCES Artworks(artwork_id),
    CONSTRAINT fk_possede_tag FOREIGN KEY (tag_id) REFERENCES Artwork_Tags(tag_id)
);

CREATE TABLE IF NOT EXISTS Exposes (
    artwork_id INT NOT NULL,
    exhibition_id INT NOT NULL,
    PRIMARY KEY (artwork_id, exhibition_id),
    CONSTRAINT fk_expose_artwork FOREIGN KEY (artwork_id) REFERENCES Artworks(artwork_id),
    CONSTRAINT fk_expose_exhibition FOREIGN KEY (exhibition_id) REFERENCES Exhibitions(exhibition_id)
);

CREATE TABLE IF NOT EXISTS Prefers (
    member_id INT NOT NULL,
    discipline_id INT NOT NULL,
    PRIMARY KEY (member_id, discipline_id),
    CONSTRAINT fk_prefere_member FOREIGN KEY (member_id) REFERENCES Community_members(member_id),
    CONSTRAINT fk_prefere_discipline FOREIGN KEY (discipline_id) REFERENCES Disciplines(discipline_id)
);
