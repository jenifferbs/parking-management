package com.estapar.parking.repository;

import com.estapar.parking.model.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

    @Query("SELECT s FROM Spot s WHERE s.latitude = :lat AND s.longitude = :lng")
    Optional<Spot> findByCoordinates(@Param("lat") BigDecimal latitude, @Param("lng") BigDecimal longitude);
}