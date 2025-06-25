CREATE TABLE parking_sessions
(
    id                  BIGSERIAL PRIMARY KEY,
    license_plate       VARCHAR(20) NOT NULL,
    sector_name         VARCHAR(10),
    spot_id             BIGINT,
    entry_time          TIMESTAMP   NOT NULL,
    parked_time         TIMESTAMP,
    exit_time           TIMESTAMP,
    base_price          DECIMAL(10, 2),
    final_price         DECIMAL(10, 2),
    discount_percentage DECIMAL(5, 2)        DEFAULT 0.00,
    status              VARCHAR(20) NOT NULL CHECK (status IN ('ACTIVE', 'COMPLETED')),
    created_at          TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP            DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (license_plate) REFERENCES vehicles (license_plate),
    FOREIGN KEY (sector_name) REFERENCES sectors (name),
    FOREIGN KEY (spot_id) REFERENCES spots (id)
);

CREATE INDEX idx_parking_sessions_license_plate ON parking_sessions (license_plate);
CREATE INDEX idx_parking_sessions_sector ON parking_sessions (sector_name);
CREATE INDEX idx_parking_sessions_status ON parking_sessions (status);
CREATE INDEX idx_parking_sessions_exit_time ON parking_sessions (exit_time);