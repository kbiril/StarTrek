package be.vdab.startrek.repositories;

import be.vdab.startrek.domain.Werknemer;
import be.vdab.startrek.dto.WerknemerVoornaamFamilienaam;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class WerknemerRepository {
    private final JdbcTemplate template;

    public WerknemerRepository(JdbcTemplate template) {
        this.template = template;
    }

    private final RowMapper<Werknemer> rowMapper =
            (result, rowNum) -> new Werknemer(
                    result.getLong("id"),
                    result.getString("voornaam"),
                    result.getString("familienaam"),
                    result.getBigDecimal("budget")
            );


    public List<Werknemer> findAllWerknemers() {
        var sql = """
                    select id, voornaam, familienaam, budget
                    from werknemers
                    order by voornaam
                  """;

        return template.query(sql, rowMapper);
    }

    public Optional<Werknemer> findAndLockById (long id) {
        try {
            var sql = """
                        select id, voornaam, familienaam, budget
                        from werknemers
                        where id = ?
                        for update
                      """;
            return Optional.of(template.queryForObject(sql, rowMapper, id));
        } catch (IncorrectResultSizeDataAccessException ex) {
            return Optional.empty();
        }
    }

    public boolean update (Werknemer werknemer) {
        var sql = """
                    update werknemers
                    set budget = ?
                    where id = ?
                  """;
        return template.update(sql, werknemer.getBudget(), werknemer.getId()) == 1;
    }
}
