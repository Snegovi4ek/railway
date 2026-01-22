package com.example.railway.web;

import com.example.railway.dto.AddTripDto;
import com.example.railway.services.TripService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/trips")
public class TripController {

    private final TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
        log.info("TripController инициализирован");
    }

    @GetMapping("/add")
    public String addTripForm() {
        log.debug("Отображение формы добавления рейса");
        return "trip-add";
    }

    @ModelAttribute("tripModel")
    public AddTripDto initTrip() {
        return new AddTripDto();
    }

    @PostMapping("/add")
    public String addTrip(@Valid AddTripDto tripModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        log.debug("Обработка POST запроса на создание рейса");

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("tripModel", tripModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.tripModel",
                    bindingResult
            );
            return "redirect:/trips/add";
        }

        tripService.addTrip(tripModel);
        redirectAttributes.addFlashAttribute("successMessage", "Рейс успешно добавлен!");

        return "redirect:/trips/all";
    }

    @GetMapping("/all")
    public String showAllTrips(@RequestParam(required = false) String search,
                               @RequestParam(required = false) String type,
                               @RequestParam(required = false) String station,
                               Model model) {

        log.debug("Отображение списка рейсов: поиск='{}', тип='{}', станция='{}'", search, type, station);

        if (station != null && !station.trim().isEmpty()) {
            model.addAttribute("trips", tripService.searchByStationName(station));
            model.addAttribute("station", station);
        } else if (search != null && !search.trim().isEmpty()) {
            if ("arrival".equals(type)) {
                model.addAttribute("trips", tripService.searchByArrivalCity(search));
            } else {
                model.addAttribute("trips", tripService.searchByDepartureCity(search));
            }
            model.addAttribute("search", search);
            model.addAttribute("type", type);
        } else {
            model.addAttribute("trips", tripService.allTrips());
        }

        return "trips-all";
    }

    @GetMapping("/details/{id}")
    public String tripDetails(@PathVariable("id") String tripId, Model model) {
        log.debug("Запрос деталей рейса ID={}", tripId);

        model.addAttribute("tripDetails", tripService.tripDetails(tripId));
        return "trip-details";
    }

    @GetMapping("/delete/{id}")
    public String deleteTrip(@PathVariable("id") String tripId,
                             RedirectAttributes redirectAttributes) {

        log.debug("Запрос на удаление рейса ID={}", tripId);

        tripService.removeTrip(tripId);

        redirectAttributes.addFlashAttribute("successMessage",
                "Рейс успешно удалён!");

        return "redirect:/trips/all";
    }

    @GetMapping("/edit/{id}")
    public String editTripForm(@PathVariable("id") String tripId, Model model) {
        log.debug("Отображение формы редактирования рейса ID={}", tripId);

        AddTripDto tripDto = tripService.getTripById(tripId);
        model.addAttribute("tripModel", tripDto);

        return "trip-edit";
    }

    @PostMapping("/edit")
    public String editTrip(@Valid AddTripDto tripModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        log.debug("Обработка POST запроса на обновление рейса ID={}", tripModel.getId());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при редактировании: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("tripModel", tripModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.tripModel",
                    bindingResult
            );
            return "redirect:/trips/edit/" + tripModel.getId();
        }

        tripService.updateTrip(tripModel);
        redirectAttributes.addFlashAttribute("successMessage", "Рейс успешно обновлён!");

        return "redirect:/trips/details/" + tripModel.getId();
    }
}