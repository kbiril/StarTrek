package be.vdab.startrek.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
class WerknemerControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final static String WERKNEMERS = "werknemers";
    private final MockMvc mockMvc;

    WerknemerControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void findAllWerknemers() throws Exception{
        mockMvc.perform(get("/werknemers"))
                .andExpectAll(status().isOk(),
                              jsonPath("length()").value(countRowsInTable(WERKNEMERS)));
    }
}