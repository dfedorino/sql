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

class RacesSolutionTest {

    public static final String DB_URL = "jdbc:h2:mem:racesdb;MODE=MySQL";
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() throws Exception {
        var conn = DriverManager.getConnection(DB_URL, "sa", "");
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));

        ScriptUtils.executeSqlScript(conn, new ClassPathResource("races/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("races/data.sql"));
    }

    @Test
    void task1() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/races/task1/task1-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(8);
        assertThat(results)
                .extracting(
                        m -> m.get("car_name"),
                        m -> m.get("car_class"),
                        m -> m.get("average_position"),
                        m -> m.get("race_count"))
                .containsExactlyInAnyOrder(
                        tuple("Ferrari 488", "Convertible", 1.0, 1L),
                        tuple("Ford Mustang", "SportsCar", 1.0, 1L),
                        tuple("Toyota RAV4", "SUV", 2.0, 1L),
                        tuple("Mercedes-Benz S-Class", "Luxury Sedan", 2.0, 1L),
                        tuple("BMW 3 Series", "Sedan", 3.0, 1L),
                        tuple("Chevrolet Camaro", "Coupe", 4.0, 1L),
                        tuple("Renault Clio", "Hatchback", 5.0, 1L),
                        tuple("Ford F-150", "Pickup", 6.0, 1L)
                );
    }

    @Test
    void task2() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/races/task2/task2-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(1);
        assertThat(results)
                .extracting(
                        m -> m.get("car_name"),
                        m -> m.get("car_class"),
                        m -> m.get("average_position"),
                        m -> m.get("race_count"),
                        m -> m.get("car_country"))
                .containsExactly(
                        tuple("Ferrari 488", "Convertible", new BigDecimal("1.0000"), 1, "Italy")
                );
    }

    @Test
    void task3() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/races/task3/task3-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(
                        m -> m.get("car_name"),
                        m -> m.get("car_class"),
                        m -> m.get("average_position"),
                        m -> m.get("race_count"),
                        m -> m.get("car_country"),
                        m -> m.get("total_races"))
                .containsExactly(
                        tuple("Ferrari 488", "Convertible", new BigDecimal("1.0000"), 1, "Italy", 1L),
                        tuple("Ford Mustang", "SportsCar", new BigDecimal("1.0000"), 1, "USA", 1L)
                );
    }

    @Test
    void task4() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/races/task4/task4-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(2);
        assertThat(results)
                .extracting(
                        m -> m.get("car_name"),
                        m -> m.get("car_class"),
                        m -> m.get("average_position"),
                        m -> m.get("race_count"),
                        m -> m.get("car_country"))
                .containsExactly(
                        tuple("BMW 3 Series", "Sedan", new BigDecimal("3.0000"), 1, "Germany"),
                        tuple("Toyota RAV4", "SUV", new BigDecimal("2.0000"), 1, "Japan")
                );
    }

    @Test
    void task5() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/races/task5/task5-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(4);
        assertThat(results)
                .extracting(
                        m -> m.get("car_name"),
                        m -> m.get("car_class"),
                        m -> m.get("average_position"),
                        m -> m.get("race_count"),
                        m -> m.get("car_country"),
                        m -> m.get("total_races"),
                        m -> m.get("low_position_count"))
                .containsExactly(
                        tuple("Audi A4", "Sedan", new BigDecimal("8.0000"), 1, "Germany", new BigDecimal("2"), 2L),
                        tuple("Chevrolet Camaro", "Coupe", new BigDecimal("4.0000"), 1, "USA", new BigDecimal("1"), 1L),
                        tuple("Renault Clio", "Hatchback", new BigDecimal("5.0000"), 1, "France", new BigDecimal("1"), 1L),
                        tuple("Ford F-150", "Pickup", new BigDecimal("6.0000"), 1, "USA", new BigDecimal("1"), 1L)
                );
    }
}
