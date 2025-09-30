package com.scenicairways.service;

import com.scenicairways.model.Flight;
import com.scenicairways.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AIService aiService;

    @PostConstruct
    public void initializeFlights() {
        // Create sample flights if none exist
        if (flightRepository.count() == 0) {
            createSampleFlights();
        }
    }

    private void createSampleFlights() {
        // Mumbai to Goa flight
        Flight flight1 = new Flight();
        flight1.setFlightNumber("SA101");
        flight1.setDeparture("Mumbai");
        flight1.setArrival("Goa");
        flight1.setDepartureTime(LocalTime.of(6, 0));
        flight1.setArrivalTime(LocalTime.of(7, 30));
        flight1.setDate(LocalDate.now().plusDays(5));
        flight1.setAircraft("Boeing 737");
        flight1.setScenicSide(Flight.ScenicSide.LEFT);
        flight1.setScenicDescription("Experience breathtaking sunrise views over the Western Ghats and Arabian Sea coastline");
        flight1.setPrice(8500);
        flight1.setTotalSeats(150);
        flight1.setAvailableSeats(120);
        flight1.setSunPosition(Flight.SunPosition.SUNRISE);
        
        // Generate seat layout
        flight1.setSeatLayout(generateSampleSeatLayout());
        flight1.setRoute(generateSampleRoute("Mumbai", "Goa"));
        
        String aiDescription = aiService.generateScenicDescription(flight1);
        flight1.setAiGeneratedDescription(aiDescription);
        
        flightRepository.save(flight1);
        
        // Delhi to Leh flight
        Flight flight2 = new Flight();
        flight2.setFlightNumber("SA202");
        flight2.setDeparture("Delhi");
        flight2.setArrival("Leh");
        flight2.setDepartureTime(LocalTime.of(8, 0));
        flight2.setArrivalTime(LocalTime.of(9, 30));
        flight2.setDate(LocalDate.now().plusDays(6));
        flight2.setAircraft("Airbus A320");
        flight2.setScenicSide(Flight.ScenicSide.RIGHT);
        flight2.setScenicDescription("Marvel at the majestic Himalayan peaks and pristine valleys from your window");
        flight2.setPrice(15000);
        flight2.setTotalSeats(180);
        flight2.setAvailableSeats(145);
        flight2.setSunPosition(Flight.SunPosition.DAYLIGHT);
        
        flight2.setSeatLayout(generateSampleSeatLayout());
        flight2.setRoute(generateSampleRoute("Delhi", "Leh"));
        
        String aiDescription2 = aiService.generateScenicDescription(flight2);
        flight2.setAiGeneratedDescription(aiDescription2);
        
        flightRepository.save(flight2);
        
        System.out.println("Sample flights created successfully");
    }
    
    private List<Flight.SeatRow> generateSampleSeatLayout() {
        // This is a simplified version - in real implementation, this would be more complex
        return Arrays.asList();
    }
    
    private List<Flight.RoutePoint> generateSampleRoute(String departure, String arrival) {
        // This is a simplified version - in real implementation, this would use actual coordinates
        return Arrays.asList();
    }
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public List<Flight> getAvailableFlights() {
        return flightRepository.findAvailableFlights(LocalDate.now());
    }

    public Optional<Flight> getFlightById(String id) {
        return flightRepository.findById(id);
    }

    public List<Flight> searchFlights(String departure, String arrival, LocalDate date) {
        if (date != null) {
            return flightRepository.findByDepartureAndArrivalAndDate(departure, arrival, date);
        } else {
            return flightRepository.findByDepartureContainingIgnoreCaseAndArrivalContainingIgnoreCase(departure, arrival);
        }
    }

    public Flight createFlight(Flight flight) {
        flight.setUpdatedAt(LocalDateTime.now());
        
        // Generate AI description for scenic views
        String aiDescription = aiService.generateScenicDescription(flight);
        flight.setAiGeneratedDescription(aiDescription);
        
        return flightRepository.save(flight);
    }

    public Flight updateFlight(String id, Flight flightDetails) {
        Optional<Flight> optionalFlight = flightRepository.findById(id);
        if (optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            flight.setFlightNumber(flightDetails.getFlightNumber());
            flight.setDeparture(flightDetails.getDeparture());
            flight.setArrival(flightDetails.getArrival());
            flight.setDepartureTime(flightDetails.getDepartureTime());
            flight.setArrivalTime(flightDetails.getArrivalTime());
            flight.setDate(flightDetails.getDate());
            flight.setAircraft(flightDetails.getAircraft());
            flight.setScenicSide(flightDetails.getScenicSide());
            flight.setScenicDescription(flightDetails.getScenicDescription());
            flight.setPrice(flightDetails.getPrice());
            flight.setTotalSeats(flightDetails.getTotalSeats());
            flight.setAvailableSeats(flightDetails.getAvailableSeats());
            flight.setSeatLayout(flightDetails.getSeatLayout());
            flight.setRoute(flightDetails.getRoute());
            flight.setSunPosition(flightDetails.getSunPosition());
            flight.setUpdatedAt(LocalDateTime.now());

            // Regenerate AI description if scenic details changed
            String aiDescription = aiService.generateScenicDescription(flight);
            flight.setAiGeneratedDescription(aiDescription);

            return flightRepository.save(flight);
        }
        return null;
    }

    public boolean deleteFlight(String id) {
        if (flightRepository.existsById(id)) {
            flightRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean updateSeatAvailability(String flightId, List<String> seatNumbers, boolean available) {
        Optional<Flight> optionalFlight = flightRepository.findById(flightId);
        if (optionalFlight.isPresent()) {
            Flight flight = optionalFlight.get();
            
            for (Flight.SeatRow row : flight.getSeatLayout()) {
                for (Flight.Seat seat : row.getSeats()) {
                    if (seatNumbers.contains(seat.getSeatNumber())) {
                        seat.setAvailable(available);
                    }
                }
            }
            
            // Update available seats count
            long availableCount = flight.getSeatLayout().stream()
                .flatMap(row -> row.getSeats().stream())
                .mapToLong(seat -> seat.isAvailable() ? 1 : 0)
                .sum();
            
            flight.setAvailableSeats((int) availableCount);
            flight.setUpdatedAt(LocalDateTime.now());
            
            flightRepository.save(flight);
            return true;
        }
        return false;
    }
}