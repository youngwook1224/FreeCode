package scv.DevOpsunity.reservations.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import scv.DevOpsunity.reservations.dto.ReservationDTO;
import scv.DevOpsunity.reservations.dto.ReservationRequest;
import scv.DevOpsunity.reservations.dto.ReservationSlot;
import scv.DevOpsunity.reservations.service.ReservationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ReservationViewController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/reservations")
    public String showReservationPage(@RequestParam(required = false) String cafeLocation, Model model) {
        model.addAttribute("currentDate", LocalDate.now());
        if (cafeLocation != null) {
            model.addAttribute("cafeLocation", cafeLocation);
        }
        return "reservations/reservations";
    }

    @GetMapping("/api/available-slots")
    @ResponseBody
    public ResponseEntity<?> getAvailableSlots(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            List<ReservationSlot> slots = new ArrayList<>();
            LocalDateTime startTime = date.atTime(LocalTime.of(10, 0));
            LocalDateTime endTime = date.atTime(LocalTime.of(22, 0));

            while (startTime.isBefore(endTime)) {
                List<Integer> availableSeats = reservationService.getAvailableSeats(startTime);
                slots.add(new ReservationSlot(startTime, availableSeats));
                startTime = startTime.plusHours(2);
            }

            return ResponseEntity.ok(slots);
        } catch (Exception e) {
            // 로그에 오류 기록
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("/api/make-reservation")
    @ResponseBody
    public ResponseEntity<?> makeReservation(@RequestBody ReservationRequest request) {
        ReservationDTO reservation = new ReservationDTO();
        reservation.setMemberId(Long.parseLong(request.getMemberId()));
        reservation.setSeatNumber(request.getSeatNumber());
        reservation.setReservationTime(request.getReservationTime());

        boolean success = reservationService.makeReservation(reservation);
        if (success) {
            return ResponseEntity.ok().body("Reservation successful");
        } else {
            return ResponseEntity.badRequest().body("Reservation failed. The slot might be already taken.");
        }
    }

    @PostMapping("/api/cancel-reservation")
    @ResponseBody
    public ResponseEntity<?> cancelReservation(@RequestBody Map<String, String> request) {
        LocalDateTime reservationTime = LocalDateTime.parse(request.get("reservationTime"));
        boolean success = reservationService.cancelReservationByTime(reservationTime);
        if (success) {
            return ResponseEntity.ok().body("Reservation cancelled successfully");
        } else {
            return ResponseEntity.badRequest().body("Cancellation failed. Reservation not found.");
        }
    }
}