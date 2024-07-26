package com.connect.postgresql;

import com.connect.postgresql.Entity.Customer;
import com.connect.postgresql.Entity.Vehicle;
import com.connect.postgresql.Repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class VehicleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCachedVehicle() throws Exception {
        Vehicle vehicle = new Vehicle(1L, "ABC123", "Toyota", "Camry", 2024, new Customer(Long.valueOf(1), "Lakshay", "8485631685", "afcsdfgdfgd"));

        when(vehicleRepository.findByRegNumber("ABC123")).thenReturn(Optional.of(vehicle));

        // First request should fetch from the repository
        mockMvc.perform(get("/api/vehicles/lookup").param("regNumber", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.regNumber").value("ABC123"))
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Camry"))
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.customer.id").value(1));

        // Second request should fetch from the cache
        mockMvc.perform(get("/api/vehicles/lookup").param("regNumber", "ABC123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.regNumber").value("ABC123"))
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Camry"))
                .andExpect(jsonPath("$.year").value(2024))
                .andExpect(jsonPath("$.customer.id").value(1));
    }

    @Test
    public void testNotFoundVehicle() throws Exception {
        when(vehicleRepository.findByRegNumber(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/vehicles/lookup").param("regNumber", "unknown"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Vehicle not found"));
    }
}
