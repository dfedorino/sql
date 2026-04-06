package com.example.sql;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.datasource.init.ScriptUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class VehicleTasksTest {

    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() throws Exception {
        var url = "jdbc:h2:mem:testdb;MODE=MySQL";
        var conn = DriverManager.getConnection(url, "sa", "");
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));

        ScriptUtils.executeSqlScript(conn, new ClassPathResource("schema-h2.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("data-h2.sql"));
    }

    @Test
    void task1() {
        String sql = """
                SELECT v.maker, m.model
                FROM Motorcycle m
                JOIN Vehicle v ON m.model = v.model
                WHERE m.horsepower > 150
                  AND m.price < 20000
                  AND m.type = 'Sport'
                ORDER BY m.horsepower DESC
                """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).containsEntry("maker", "Yamaha")
                .containsEntry("model", "YZF-R1");
    }

    @Test
    void task2() {
        String sql = """
                SELECT v.maker, c.model, c.horsepower, c.engine_capacity, 'Car' AS vehicle_type
                FROM Car c
                JOIN Vehicle v ON c.model = v.model
                WHERE c.horsepower > 150
                  AND c.engine_capacity < 3
                  AND c.price < 35000

                UNION ALL

                SELECT v.maker, m.model, m.horsepower, m.engine_capacity, 'Motorcycle' AS vehicle_type
                FROM Motorcycle m
                JOIN Vehicle v ON m.model = v.model
                WHERE m.horsepower > 150
                  AND m.engine_capacity < 1.5
                  AND m.price < 20000

                UNION ALL

                SELECT v.maker, b.model, NULL, NULL, 'Bicycle' AS vehicle_type
                FROM Bicycle b
                JOIN Vehicle v ON b.model = v.model
                WHERE b.gear_count > 18
                  AND b.price < 4000

                ORDER BY horsepower DESC
                """;

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(5);
        assertThat(results.get(0)).containsEntry("maker", "Toyota")
                .containsEntry("model", "Camry")
                .containsEntry("vehicle_type", "Car");
        assertThat(results.get(1)).containsEntry("maker", "Yamaha")
                .containsEntry("model", "YZF-R1")
                .containsEntry("vehicle_type", "Motorcycle");
        assertThat(results.get(2)).containsEntry("maker", "Honda")
                .containsEntry("model", "Civic")
                .containsEntry("vehicle_type", "Car");
    }
}
