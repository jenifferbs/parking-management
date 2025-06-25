CREATE TABLE vehicles
(
    license_plate VARCHAR(20) PRIMARY KEY,
    entry_time    TIMESTAMP   NOT NULL,
    exit_time     TIMESTAMP,
    parked_time   TIMESTAMP,
    sector_name   VARCHAR(10),
    spot_id       BIGINT,
    status        VARCHAR(20) NOT NULL CHECK (status IN ('ENTERED', 'PARKED', 'EXITED')),
    FOREIGN KEY (sector_name) REFERENCES sectors (name),
    FOREIGN KEY (spot_id) REFERENCES spots (id)
);

CREATE INDEX idx_vehicles_status ON vehicles (status);
CREATE INDEX idx_vehicles_sector ON vehicles (sector_name);
CREATE INDEX idx_vehicles_entry_time ON vehicles (entry_time);