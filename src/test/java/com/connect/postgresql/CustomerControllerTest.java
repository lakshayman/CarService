package com.connect.postgresql;

import com.connect.postgresql.Entity.Customer;
import com.connect.postgresql.Repository.CustomerRepository;
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
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCachedCustomer() throws Exception {
        Customer customer = new Customer(1L, "Lakshay", "8485631685", "afcsdfgdfgd");

        when(customerRepository.findByContact("8485631685")).thenReturn(Optional.of(customer));

        // First request should fetch from the repository
        mockMvc.perform(get("/api/customers/lookup").param("contact", "8485631685"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lakshay"))
                .andExpect(jsonPath("$.contact").value("8485631685"))
                .andExpect(jsonPath("$.address").value("afcsdfgdfgd"));

        // Second request should fetch from the cache
        mockMvc.perform(get("/api/customers/lookup").param("contact", "8485631685"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lakshay"))
                .andExpect(jsonPath("$.contact").value("8485631685"))
                .andExpect(jsonPath("$.address").value("afcsdfgdfgd"));
    }

    @Test
    public void testNotFoundCustomer() throws Exception {
        when(customerRepository.findByContact(anyString())).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/customers/lookup").param("contact", "unknown"))
                .andExpect(status().isNotFound())
                .andExpect(status().reason("Customer not found"));
    }
}