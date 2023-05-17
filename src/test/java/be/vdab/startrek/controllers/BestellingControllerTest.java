package be.vdab.startrek.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

@SpringBootTest
@Sql({"/werknemers.sql", "/bestellingen.sql"})
@AutoConfigureMockMvc
class BestellingControllerTest extends AbstractTransactionalJUnit4SpringContextTests {
    private final MockMvc mockMvc;
    private final static String WERKNEMERS = "werknemers";
    private final static String BESTELLINGEN = "bestellingen";
    private long idTest1;
    private final static Path TEST_RESOURCES = Path.of("src/test/resources");

    BestellingControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }
    private long idVanWerknemerTest1() {
        return jdbcTemplate.queryForObject(
                "select id from werknemers where voornaam = 'test1' and familienaam = 'test1'", Long.class);

    }

    @BeforeEach
    void beforeEach() { idTest1 = idVanWerknemerTest1(); }

    @Test
    void getBestellingenById() throws Exception {

        mockMvc.perform(get("/bestellingen/{id}", idTest1))
                .andExpectAll(
                        status().isOk(),
                        jsonPath("length()")
                                .value(countRowsInTableWhere(BESTELLINGEN, "werknemerId = " + idTest1))
                );
    }

    @Test
    void bestel() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteNieuweBestelling.json"));

        mockMvc.perform(patch("/bestellingen/{id}", idTest1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isOk());
        assertThat(countRowsInTableWhere(WERKNEMERS, "id = " + idTest1 + " and budget = 490")).isOne();
        assertThat(countRowsInTableWhere(BESTELLINGEN,
                "omschrijving = 'test3' and werknemerId = " + idTest1 + " and bedrag = 10")).isOne();
    }

    @Test
    void bestelMetTeGroteBedragMislukt() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("bestellingMetTeGrootBedrag.json"));

        mockMvc.perform(patch("/bestellingen/{id}", idTest1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isConflict());
    }
    @Test
    void bestelMetVerkeerdeWerknemerIdMislukt() throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve("correcteNieuweBestelling.json"));

        mockMvc.perform(patch("/bestellingen/{id}", Long.MAX_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @ValueSource(strings = {"bestellingMet0Bedrag.json", "bestellingMetLegeOmschrijving.json",
            "bestellingMetNegatiefBedrag.json", "bestellingZonderBedrag.json",
            "bestellingZonderOmschrijving.json"})
    void bestelMetVerkeerdeDataMislukt (String bestandsnaam) throws Exception {
        var jsonData = Files.readString(TEST_RESOURCES.resolve(bestandsnaam));
        mockMvc.perform(patch("/bestellingen/{id}", idTest1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData))
                .andExpect(status().isBadRequest());
    }


 }