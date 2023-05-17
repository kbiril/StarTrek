package be.vdab.startrek.services;

import be.vdab.startrek.domain.Bestelling;
import be.vdab.startrek.dto.NieuweBestelling;
import be.vdab.startrek.exceptions.WerknemerNietGevondenException;
import be.vdab.startrek.repositories.BestellingRepository;
import be.vdab.startrek.repositories.WerknemerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class BestellingService {
    private final BestellingRepository bestellingRepository;
    private final WerknemerRepository werknemerRepository;

    public BestellingService(BestellingRepository bestellingRepository, WerknemerRepository werknemerRepository) {
        this.bestellingRepository = bestellingRepository;
        this.werknemerRepository = werknemerRepository;
    }

    public List<Bestelling> getBestellingenById(long id) {
        return bestellingRepository.getBestellingenById(id);
    }

    @Transactional
    public void bestel(long werknemerId, NieuweBestelling nieuweBestelling) {
        var werknemer = werknemerRepository.findAndLockById(werknemerId)
                .orElseThrow(
                        () -> new WerknemerNietGevondenException(werknemerId)
                );

        werknemer.bestel(nieuweBestelling.bedrag());

        var bestelling = new Bestelling(werknemerId, nieuweBestelling.omschrijving(), nieuweBestelling.bedrag());

        if ( !werknemerRepository.update(werknemer) ) {
            throw new WerknemerNietGevondenException(werknemerId);
        }

        bestellingRepository.bestel(bestelling);

    }
}
