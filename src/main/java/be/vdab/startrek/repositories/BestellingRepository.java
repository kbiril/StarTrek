package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Bestelling;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class BestellingRepository {
    private final JdbcTemplate template;

    public BestellingRepository(JdbcTemplate template) {
        this.template = template;
    }

    private final RowMapper<Bestelling> rowMapper =
            (result, rowNum) -> new Bestelling(
                                                result.getLong("id"),
                                                result.getLong("werknemerId"),
                                                result.getString("omschrijving"),
                                                result.getBigDecimal("bedrag"),
                                                result.getObject("moment", LocalDateTime.class)
                    );


    public List<Bestelling> getBestellingenById(long id) {
        var sql = """
                    select id, werknemerId, omschrijving, bedrag, moment
                    from bestellingen
                    where werknemerId = ?
                    order by id
                  """;

        return template.query(sql, rowMapper, id);
    }

    public long bestel (Bestelling bestelling) {
            var sql = """
                        insert into bestellingen (werknemerId, omschrijving, bedrag, moment) values
                        (?, ?, ?, ?)
                      """;
            var keyHolder = new GeneratedKeyHolder();
            template.update(
                    connection -> {
                        var statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                        statement.setLong(1, bestelling.getWerknemerId());
                        statement.setString(2, bestelling.getOmschrijving());
                        statement.setBigDecimal(3, bestelling.getBedrag());
                        statement.setObject(4, bestelling.getMoment());
                        return statement;
                    }, keyHolder
            );
            return keyHolder.getKey().longValue();
    }
}
