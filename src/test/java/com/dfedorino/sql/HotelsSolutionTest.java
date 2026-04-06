package com.dfedorino.sql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class HotelsSolutionTest {

    public static final String DB_URL = "jdbc:h2:mem:hotelsdb;MODE=MySQL";
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() throws Exception {
        var conn = DriverManager.getConnection(DB_URL, "sa", "");
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));

        ScriptUtils.executeSqlScript(conn, new ClassPathResource("hotels/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("hotels/data.sql"));
    }

    @Test
    void task1() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/hotels/task1/task1-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(
                        m -> m.get("name"),
                        m -> m.get("email"),
                        m -> m.get("phone"),
                        m -> m.get("total_bookings"),
                        m -> m.get("hotels_visited"),
                        m -> m.get("avg_stay_duration"))
                .containsExactly(
                        tuple("Bob Brown", "bob.brown@example.com", "+2233445566", 3L, "Grand Hotel, Ocean View Resort", new BigDecimal("3.0000")),
                        tuple("Ethan Hunt", "ethan.hunt@example.com", "+5566778899", 3L, "Mountain Retreat, Ocean View Resort", new BigDecimal("3.0000"))
                );
    }

    @Test
    void task2() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/hotels/task2/task2-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(
                        m -> m.get("ID_customer"),
                        m -> m.get("name"),
                        m -> m.get("total_bookings"),
                        m -> m.get("total_spent"),
                        m -> m.get("unique_hotels"))
                .containsExactly(
                        tuple(4, "Bob Brown", 3L, new BigDecimal("820.00"), 2L),
                        tuple(7, "Ethan Hunt", 3L, new BigDecimal("850.00"), 2L)
                );
    }

    @Test
    void task3() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/hotels/task3/task3-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(10);
        assertThat(results)
                .extracting(
                        m -> m.get("ID_customer"),
                        m -> m.get("name"),
                        m -> m.get("preferred_hotel_type"),
                        m -> m.get("visited_hotels"))
                .containsExactly(
                        tuple(10, "Hannah Montana", "Дешевый", "City Center Inn"),
                        tuple(1, "John Doe", "Средний", "City Center Inn, Grand Hotel"),
                        tuple(2, "Jane Smith", "Средний", "Grand Hotel"),
                        tuple(3, "Alice Johnson", "Средний", "Grand Hotel"),
                        tuple(4, "Bob Brown", "Средний", "Grand Hotel, Ocean View Resort"),
                        tuple(5, "Charlie White", "Средний", "Ocean View Resort"),
                        tuple(6, "Diana Prince", "Средний", "Ocean View Resort"),
                        tuple(7, "Ethan Hunt", "Дорогой", "Mountain Retreat, Ocean View Resort"),
                        tuple(8, "Fiona Apple", "Дорогой", "Mountain Retreat"),
                        tuple(9, "George Washington", "Дорогой", "City Center Inn, Mountain Retreat")
                );
    }
}
