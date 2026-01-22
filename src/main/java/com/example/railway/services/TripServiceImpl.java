    package com.example.railway.services;

    import com.example.railway.dto.AddTripDto;
    import com.example.railway.dto.ShowTripDto;
    import com.example.railway.dto.ShowDetailedTripDto;
    import com.example.railway.model.entities.Trip;
    import com.example.railway.repository.TripRepository;
    import org.modelmapper.ModelMapper;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.List;
    import java.util.stream.Collectors;

    @Service
    @Transactional(readOnly = true)
    public class TripServiceImpl implements TripService {

        private final TripRepository tripRepository;
        private final ModelMapper mapper;

        public TripServiceImpl(TripRepository tripRepository, ModelMapper mapper) {
            this.tripRepository = tripRepository;
            this.mapper = mapper;
        }

        @Override
        @Transactional
        public void addTrip(AddTripDto tripDto) {
            Trip trip = mapper.map(tripDto, Trip.class);
            tripRepository.save(trip);
        }

        @Override
        public List<ShowTripDto> allTrips() {
            return tripRepository.findAll().stream()
                    .map(trip -> mapper.map(trip, ShowTripDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public List<ShowTripDto> searchByDepartureCity(String city) {
            return tripRepository.findByDepartureCity(city).stream()
                    .map(trip -> mapper.map(trip, ShowTripDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public List<ShowTripDto> searchByArrivalCity(String city) {
            return tripRepository.findByArrivalCity(city).stream()
                    .map(trip -> mapper.map(trip, ShowTripDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public List<ShowTripDto> searchByStationName(String stationName) {
            return tripRepository.findByStationName(stationName).stream()
                    .map(trip -> mapper.map(trip, ShowTripDto.class))
                    .collect(Collectors.toList());
        }

        @Override
        public ShowDetailedTripDto tripDetails(String tripId) {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new RuntimeException("Рейс не найден"));
            return mapper.map(trip, ShowDetailedTripDto.class);
        }

        @Override
        @Transactional
        public void removeTrip(String tripId) {
            if (!tripRepository.existsById(tripId)) {
                throw new RuntimeException("Попытка удалить несуществующий рейс");
            }
            tripRepository.deleteById(tripId);
        }

        @Override
        public AddTripDto getTripById(String tripId) {
            Trip trip = tripRepository.findById(tripId)
                    .orElseThrow(() -> new RuntimeException("Рейс не найден"));

            return mapper.map(trip, AddTripDto.class);
        }

        @Override
        @Transactional
        public void updateTrip(AddTripDto tripDto) {
            Trip trip = tripRepository.findById(tripDto.getId())
                    .orElseThrow(() -> new RuntimeException("Рейс не найден"));

            mapper.map(tripDto, trip);

            tripRepository.save(trip);
        }
    }