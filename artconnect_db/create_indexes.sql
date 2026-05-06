USE  artconnect_db;

CREATE INDEX idx_artworks_artist_id
    ON Artworks (artist_id);
    
CREATE INDEX idx_artworks_status
    ON Artworks (status);
    
CREATE INDEX idx_artworks_type_status
    ON Artworks (type, status);

CREATE INDEX idx_exhibitions_dates
    ON Exhibitions (start_date, endDate);

CREATE INDEX idx_exhibitions_gallery_id
    ON Exhibitions (gallery_id);

CREATE INDEX idx_review_artwork_id
    ON Review (artwork_id);

CREATE INDEX idx_review_member_id
    ON Review (member_id);
    
CREATE INDEX idx_booking_workshop_member
    ON Booking (workshop_id, member_id);
    
CREATE INDEX idx_possede_tag_id
    ON Possede (tag_id);
    
CREATE INDEX idx_workshops_date
    ON Workshops (date_);
    
