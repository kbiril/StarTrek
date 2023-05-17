package be.vdab.startrek.controllers;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.dto.WerknemerVoornaamFamilienaam;
import be.vdab.startrek.services.WerknemerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("werknemers")
@RestController
class WerknemerController {
    private final WerknemerService werknemerService;

    public WerknemerController(WerknemerService werknemerService) {
        this.werknemerService = werknemerService;
    }

    @GetMapping
    List<Werknemer> findAllWerknemers () {
        return werknemerService.findAllWerknemers();
    }
}
