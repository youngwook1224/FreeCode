package scv.DevOpsunity.reservations.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import scv.DevOpsunity.reservations.dao.ReservationDAO;
import scv.DevOpsunity.reservations.dto.ReservationDTO;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationDAO reservationDAO;

    private static final LocalTime OPENING_TIME = LocalTime.of(10, 0);
    private static final LocalTime CLOSING_TIME = LocalTime.of(22, 0);
    private static final int RESERVATION_DURATION_HOURS = 2;
    private static final int TOTAL_SEATS = 5;

    @Transactional
    public boolean makeReservation(ReservationDTO reservation) {
        if (!isValidReservationTime(reservation.getReservationTime())) {
            return false;
        }

        LocalDateTime endTime = reservation.getReservationTime().plusHours(RESERVATION_DURATION_HOURS);
        reservation.setEndTime(endTime);

        List<ReservationDTO> existingReservations = reservationDAO.getReservationsBySeatAndTimeRange(
                reservation.getSeatNumber(),
                reservation.getReservationTime(),
                endTime
        );

        if (existingReservations.isEmpty()) {
            reservationDAO.createReservation(reservation);
            return true;
        }
        return false;
    }

    public ReservationDTO getReservationById(Long id) {
        return reservationDAO.getReservationById(id);
    }

    public List<ReservationDTO> getReservationsForDate(LocalDateTime date) {
        LocalDateTime startOfDay = date.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = startOfDay.plusDays(1);
        return reservationDAO.getReservationsForDateRange(startOfDay, endOfDay);
    }

    @Transactional
    public boolean cancelReservation(Long id) {
        ReservationDTO reservation = reservationDAO.getReservationById(id);
        if (reservation != null && reservation.getReservationTime().isAfter(LocalDateTime.now())) {
            reservationDAO.deleteReservation(id);
            return true;
        }
        return false;
    }

    public List<Integer> getAvailableSeats(LocalDateTime dateTime) {
        List<Integer> occupiedSeats = reservationDAO.getOccupiedSeatsAtTime(dateTime);
        List<Integer> allSeats = List.of(1, 2, 3, 4, 5);  // Assuming 5 seats
        allSeats.removeAll(occupiedSeats);
        return allSeats;
    }

    private boolean isValidReservationTime(LocalDateTime reservationTime) {
        LocalTime time = reservationTime.toLocalTime();
        return !reservationTime.isBefore(LocalDateTime.now()) &&
                time.isAfter(OPENING_TIME) &&
                time.isBefore(CLOSING_TIME) &&
                time.getMinute() == 0 &&
                (time.getHour() % RESERVATION_DURATION_HOURS == 0);
    }

    @Transactional
    public boolean cancelReservationByTime(LocalDateTime reservationTime) {
        List<ReservationDTO> reservations = reservationDAO.getReservationsForDateRange(reservationTime, reservationTime.plusHours(RESERVATION_DURATION_HOURS));
        if (!reservations.isEmpty()) {
            reservationDAO.deleteReservation(reservations.get(0).getId());
            return true;
        }
        return false;
    }
}