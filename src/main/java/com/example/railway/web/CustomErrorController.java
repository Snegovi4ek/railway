package com.example.railway.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    @ResponseBody
    public String handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            log.error("Ошибка HTTP: {}", statusCode);

            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "404 - Страница не найдена";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                return "500 - Внутренняя ошибка сервера";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "403 - Доступ запрещен";
            }
        }

        log.error("Неизвестная ошибка");
        return "Произошла неизвестная ошибка";
    }
}
