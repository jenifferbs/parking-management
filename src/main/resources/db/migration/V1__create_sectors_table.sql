CREATE TABLE sectors
(
    name                   VARCHAR(10) PRIMARY KEY,
    base_price             DECIMAL(10, 2) NOT NULL,
    max_capacity           INTEGER        NOT NULL,
    open_hour              TIME           NOT NULL,
    close_hour             TIME           NOT NULL,
    duration_limit_minutes INTEGER        NOT NULL,
    current_capacity       INTEGER        NOT NULL DEFAULT 0,
    is_closed              BOOLEAN        NOT NULL DEFAULT false
);