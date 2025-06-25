package com.estapar.parking.repository;

import com.estapar.parking.model.entity.ParkingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ParkingSessionRepository extends JpaRepository<ParkingSession, Long> {

    Optional<ParkingSession> findByLicensePlateAndStatus(String licensePlate, ParkingSession.SessionStatus status);

    @Query("SELECT ps FROM ParkingSession ps WHERE ps.sectorName = :sectorName " +
            "AND ps.status = 'COMPLETED' " +
            "AND ps.exitTime BETWEEN :startDate AND :endDate")
    List<ParkingSession> findCompletedSessionsBySectorAndDateRange(
            @Param("sectorName") String sectorName,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}