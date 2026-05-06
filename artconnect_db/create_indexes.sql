USE artconnect_db;

CREATE INDEX idx_artworks_artist_id
    ON Artworks (artist_id);

CREATE INDEX idx_artworks_status
    ON Artworks (status);

CREATE INDEX idx_artworks_type_status
    ON Artworks (type, status);

CREATE INDEX idx_exhibitions_dates
    ON Exhibitions (start_date, end_date);

CREATE INDEX idx_exhibitions_gallery_id
    ON Exhibitions (gallery_id);

CREATE INDEX idx_reviews_artwork_id
    ON Reviews (artwork_id);

CREATE INDEX idx_reviews_member_id
    ON Reviews (member_id);

CREATE INDEX idx_bookings_workshop_member
    ON Bookings (workshop_id, member_id);

CREATE INDEX idx_possedes_tag_id
    ON Possedes (tag_id);

CREATE INDEX idx_workshops_date
    ON Workshops (date_);