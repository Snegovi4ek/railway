package com.example.railway.web;

import com.example.railway.dto.AddHallDto;
import com.example.railway.services.HallService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/halls")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
        log.info("HallController инициализирован");
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addHallForm() {
        log.debug("Отображение формы добавления зала");
        return "hall-add";
    }

    @ModelAttribute("hallModel")
    public AddHallDto initHall() {
        return new AddHallDto();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public String addHall(@Valid AddHallDto hallModel,
                          BindingResult bindingResult,
                          RedirectAttributes redirectAttributes) {

        log.debug("Обработка POST запроса на создание зала");

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("hallModel", hallModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.hallModel",
                    bindingResult
            );
            return "redirect:/halls/add";
        }

        hallService.addHall(hallModel);
        redirectAttributes.addFlashAttribute("successMessage", "Зал успешно добавлен!");

        return "redirect:/halls/all";
    }

    @GetMapping("/all")
    public String showAllHalls(@RequestParam(required = false) String type,
                               Model model) {

        log.debug("Отображение списка залов: тип='{}'", type);

        if (type != null && !type.trim().isEmpty()) {
            model.addAttribute("halls", hallService.findByType(type));
            model.addAttribute("type", type);
        } else {
            model.addAttribute("halls", hallService.allHalls());
        }

        return "halls-all";
    }

    @GetMapping("/active")
    public String showActiveHalls(Model model) {
        log.debug("Отображение списка активных залов");

        model.addAttribute("halls", hallService.findActiveHalls());
        return "halls-all";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String editHallForm(@PathVariable("id") String hallId, Model model) {
        log.debug("Отображение формы редактирования зала ID={}", hallId);

        AddHallDto hallDto = hallService.getHallById(hallId);
        model.addAttribute("hallModel", hallDto);

        return "hall-edit";
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String editHall(@Valid AddHallDto hallModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        log.debug("Обработка POST запроса на обновление зала ID={}", hallModel.getId());

        if (bindingResult.hasErrors()) {
            log.warn("Ошибки валидации при редактировании: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute("hallModel", hallModel);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.hallModel",
                    bindingResult
            );
            return "redirect:/halls/edit/" + hallModel.getId();
        }

        hallService.updateHall(hallModel);
        redirectAttributes.addFlashAttribute("successMessage", "Зал успешно обновлён!");

        return "redirect:/halls/all";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteHall(@PathVariable("id") String hallId,
                             RedirectAttributes redirectAttributes) {

        log.debug("Запрос на удаление зала ID={}", hallId);

        hallService.removeHall(hallId);

        redirectAttributes.addFlashAttribute("successMessage",
                "Зал успешно удалён!");

        return "redirect:/halls/all";
    }
}