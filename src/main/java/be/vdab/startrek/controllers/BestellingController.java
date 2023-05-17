package be.vdab.startrek.controllers;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.dto.NieuweBestelling;
import be.vdab.startrek.services.BestellingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("bestellingen")
@RestController
class BestellingController {

    private final BestellingService bestellingService;

    public BestellingController(BestellingService bestellingService) {
        this.bestellingService = bestellingService;
    }

    @GetMapping ("{id}")
    List<Bestelling> getBestellingenById(@PathVariable long id) {
        return bestellingService.getBestellingenById(id);
    }

    @PatchMapping ("{id}")
    void bestel (@PathVariable long id, @RequestBody @Valid NieuweBestelling nieuweBestelling) {
        bestellingService.bestel(id, nieuweBestelling);
    }


}
