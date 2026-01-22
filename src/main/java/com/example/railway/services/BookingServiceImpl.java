package com.example.railway.services;

import com.example.railway.dto.AddBookingDto;
import com.example.railway.dto.ShowBookingDto;
import com.example.railway.model.entities.Booking;
import com.example.railway.model.entities.Hall;
import com.example.railway.model.entities.Trip;
import com.example.railway.model.entities.User;
import com.example.railway.repository.BookingRepository;
import com.example.railway.repository.HallRepository;
import com.example.railway.repository.TripRepository;
import com.example.railway.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final HallRepository hallRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              HallRepository hallRepository,
                              TripRepository tripRepository,
                              UserRepository userRepository,
                              ModelMapper mapper) {
        this.bookingRepository = bookingRepository;
        this.hallRepository = hallRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void addBooking(AddBookingDto bookingDto, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " не найден"));

        Trip trip = tripRepository.findById(bookingDto.getTripId())
                .orElseThrow(() -> new RuntimeException("Рейс не найден"));

        Hall hall = hallRepository.findById(bookingDto.getHallId()).orElseGet(() -> {
            Hall newHall = new Hall("Автоматически созданный зал", "STANDARD", 500.0, "Базовые услуги", 100);
            newHall.setId(bookingDto.getHallId());
            newHall.setActive(true);
            return hallRepository.save(newHall);
        });

        if (hall.getAvailableSeats() < bookingDto.getSeats()) {
            throw new RuntimeException("Недостаточно свободных мест в зале");
        }

        Double totalPrice = hall.getPrice() * bookingDto.getSeats();
        Booking booking = new Booking(user, trip, hall, bookingDto.getSeats(), totalPrice);

        hall.setAvailableSeats(hall.getAvailableSeats() - bookingDto.getSeats());

        bookingRepository.save(booking);
        hallRepository.save(hall);
    }

    @Override
    public List<ShowBookingDto> allBookings() {
        return bookingRepository.findAll().stream()
                .map(this::convertToShowBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowBookingDto> findByUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " не найден"));

        return bookingRepository.findByUser(user).stream()
                .map(this::convertToShowBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShowBookingDto getBookingById(String bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        return convertToShowBookingDto(booking);
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " не найден"));

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Бронирование не найдено"));

        if (!booking.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Вы можете отменять только свои бронирования");
        }

        if (!"CONFIRMED".equals(booking.getStatus())) {
            throw new RuntimeException("Можно отменять только подтвержденные бронирования");
        }

        booking.setStatus("CANCELLED");

        Hall hall = booking.getHall();
        hall.setAvailableSeats(hall.getAvailableSeats() + booking.getSeats());

        bookingRepository.save(booking);
        hallRepository.save(hall);
    }

    private ShowBookingDto convertToShowBookingDto(Booking booking) {
        ShowBookingDto dto = mapper.map(booking, ShowBookingDto.class);

        dto.setTripId(booking.getTrip().getId());
        dto.setHallId(booking.getHall().getId());
        dto.setHallName(booking.getHall().getName());
        dto.setHallType(booking.getHall().getType());
        dto.setDepartureCity(booking.getTrip().getDepartureCity());
        dto.setArrivalCity(booking.getTrip().getArrivalCity());
        dto.setDepartureTime(booking.getTrip().getDepartureTime());

        return dto;
    }
}