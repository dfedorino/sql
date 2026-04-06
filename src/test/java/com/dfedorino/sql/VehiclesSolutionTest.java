package com.dfedorino.sql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class VehiclesSolutionTest {

    public static final String DB_URL = "jdbc:h2:mem:testdb;MODE=MySQL";
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() throws Exception {
        var conn = DriverManager.getConnection(DB_URL, "sa", "");
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));

        ScriptUtils.executeSqlScript(conn, new ClassPathResource("vehicles/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("vehicles/data.sql"));
    }

    @Test
    void task1() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/vehicles/task1-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results)
                .hasSize(1);
        assertThat(results.get(0))
                .containsEntry("maker", "Yamaha")
                .containsEntry("model", "YZF-R1");
    }

    @Test
    void task2() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/vehicles/task2-solution.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results)
                .hasSize(5);
        assertThat(results.get(0))
                .containsEntry("maker", "Toyota")
                .containsEntry("model", "Camry")
                .containsEntry("vehicle_type", "Car");
        assertThat(results.get(1))
                .containsEntry("maker", "Yamaha")
                .containsEntry("model", "YZF-R1")
                .containsEntry("vehicle_type", "Motorcycle");
        assertThat(results.get(2))
                .containsEntry("maker", "Honda")
                .containsEntry("model", "Civic")
                .containsEntry("vehicle_type", "Car");
    }
}
