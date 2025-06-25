CREATE TABLE spots
(
    id                    BIGSERIAL PRIMARY KEY,
    sector_name           VARCHAR(10)    NOT NULL,
    latitude              DECIMAL(9, 6)  NOT NULL,
    longitude             DECIMAL(10, 6) NOT NULL,
    is_occupied           BOOLEAN        NOT NULL DEFAULT false,
    occupied_since        TIMESTAMP,
    current_license_plate VARCHAR(20),
    FOREIGN KEY (sector_name) REFERENCES sectors (name)
);

CREATE INDEX idx_spots_sector ON spots (sector_name);
CREATE INDEX idx_spots_coordinates ON spots (latitude, longitude);
CREATE INDEX idx_spots_occupied ON spots (is_occupied);