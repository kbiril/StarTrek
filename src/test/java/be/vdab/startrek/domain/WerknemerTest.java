package be.vdab.startrek.domain;

import be.vdab.startrek.exceptions.OnvoldoendeBudgetException;
import com.mysql.cj.jdbc.exceptions.OperationNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;



class WerknemerTest {
    private Werknemer test;
    @BeforeEach
    void beforeEach() {test = new Werknemer("test", "test", BigDecimal.TEN); }

    @ParameterizedTest
    @ValueSource (ints = {7, 10})
    void bestel(int bedrag) {
        test.bestel(BigDecimal.valueOf(bedrag));
        assertThat(test.getBudget()).isEqualTo(BigDecimal.valueOf(10 - bedrag));
    }

    @Test
    void bestelMisluktBijOnvoldoendeBudget () {
        assertThatExceptionOfType(OnvoldoendeBudgetException.class).isThrownBy(
                () -> test.bestel(BigDecimal.valueOf(11))
        );
    }

}