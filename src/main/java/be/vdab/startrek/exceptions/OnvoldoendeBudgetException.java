package be.vdab.startrek.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;

@ResponseStatus(HttpStatus.CONFLICT)
public class OnvoldoendeBudgetException extends RuntimeException {
    public OnvoldoendeBudgetException(BigDecimal budget) {
        super("Het bedrag is te groot, beschikbaar budget: " + budget);
    }
}
