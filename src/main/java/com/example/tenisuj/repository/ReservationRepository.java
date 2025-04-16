package com.example.tenisuj.repository;

import com.example.tenisuj.model.Reservation;
import com.example.tenisuj.model.enums.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    @Query("select r from reservations r where r.place = :place " +
            "and r.date = :date " +
            "and ((r.startTime between :startTime and :endTime) " +
            "or (r.endTime between :startTime and :endTime) " +
            "or (r.startTime <= :startTime and r.endTime >= :endTime))")
    List<Reservation> findConflictingReservations(@Param("place") Location place,
                                                  @Param("date") LocalDate date,
                                                  @Param("startTime") LocalTime startTime,
                                                  @Param("endTime") LocalTime endTime);

    @Query("select r from reservations r where r.customer= :playerFullName")
    List<Reservation> findAllPlayerReservations(String playerFullName);

    @Query("select r from reservations r where r.customer= :userName")
    List<Reservation> findAllUserReservations(String userName);

    @Query("select r from reservations r where r.place =:place and r.date =:date")
    List<Reservation> findAllReservationsByDateAndPlace(@Param("date") LocalDate date,
                                                        @Param("place") Location place);

    @Query("select r from reservations r where r.status =:status")
    List<Reservation> findAllReservationsByStatus(@Param("status") String status);

    @Query("select r from reservations r where r.status='approved' and r.place =:place and r.date =:date")
    List<Reservation> findApprovedReservationsByDateAndPlace(@Param("date") LocalDate date,
                                                             @Param("place") Location place);

    @Query("select r from reservations r where r.status='pending' and r.place =:place and r.date =:date")
    List<Reservation> findPendingReservationsByDateAndPlace(@Param("date") LocalDate date,
                                                             @Param("place") Location place);

    @Query("select r from reservations r order by r.date,r.place,r.startTime")
    List<Reservation> getAllReservationsSortedByPlaceAndDateAndStartTime();
}
