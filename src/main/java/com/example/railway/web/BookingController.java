package com.example.railway.web;

import com.example.railway.dto.AddBookingDto;
import com.example.railway.services.BookingService;
import com.example.railway.services.TripService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final TripService tripService;

    public BookingController(BookingService bookingService, TripService tripService) {
        this.bookingService = bookingService;
        this.tripService = tripService;
        log.info("BookingController инициализирован");
    }

    @GetMapping("/add/{tripId}")
    public String addBookingForm(@PathVariable("tripId") String tripId,
                                 Model model,
                                 Authentication authentication){
        log.debug("Отображение формы бронирования для рейса ID={}", tripId);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login";
        }

        model.addAttribute("tripDetails", tripService.tripDetails(tripId));

        AddBookingDto bookingDto = new AddBookingDto();
        bookingDto.setTripId(tripId);

        model.addAttribute("bookingModel", bookingDto);

        return "booking-add";
    }

    @ModelAttribute("bookingModel")
    public AddBookingDto initBooking() {
        return new AddBookingDto();
    }

    @PostMapping("/add")
    public String addBooking(@Valid AddBookingDto bookingModel,
                             BindingResult bindingResult,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {

        log.debug("Обработка POST запроса на создание бронирования");

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login";
        }

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("bookingModel", bookingModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.bookingModel",
                    bindingResult
            );
            return "redirect:/trips/details/" + bookingModel.getTripId();
        }

        String username = authentication.getName();
        bookingService.addBooking(bookingModel, username);
        redirectAttributes.addFlashAttribute("successMessage", "Бронирование успешно создано!");

        return "redirect:/bookings/my";
    }

    @GetMapping("/my")
    public String showMyBookings(Authentication authentication,
                                 Model model) {
        log.debug("Отображение бронирований пользователя");

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login";
        }

        String username = authentication.getName();
        model.addAttribute("bookings", bookingService.findByUser(username));

        return "bookings-my";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable("id") String bookingId,
                                Authentication authentication,
                                RedirectAttributes redirectAttributes) {

        log.debug("Запрос на отмену бронирования ID={}", bookingId);

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/users/login";
        }

        String username = authentication.getName();
        bookingService.cancelBooking(bookingId, username);

        redirectAttributes.addFlashAttribute("successMessage",
                "Бронирование успешно отменено!");

        return "redirect:/bookings/my";
    }
}