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
import static org.assertj.core.api.Assertions.tuple;

class OrganizationSolutionTest {

    public static final String DB_URL = "jdbc:h2:mem:organizationdb;MODE=MySQL";
    static JdbcTemplate jdbcTemplate;

    @BeforeAll
    static void setUp() throws Exception {
        var conn = DriverManager.getConnection(DB_URL, "sa", "");
        jdbcTemplate = new JdbcTemplate(new SingleConnectionDataSource(conn, true));

        ScriptUtils.executeSqlScript(conn, new ClassPathResource("organization/schema.sql"));
        ScriptUtils.executeSqlScript(conn, new ClassPathResource("organization/data.sql"));
    }

    @Test
    void task1() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/organization/task1/task1-solution-h2.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(30);
        assertThat(results)
                .extracting(
                        m -> m.get("EmployeeID"),
                        m -> m.get("EmployeeName"),
                        m -> m.get("ManagerID"),
                        m -> m.get("DepartmentName"),
                        m -> m.get("RoleName"),
                        m -> m.get("ProjectNames"),
                        m -> m.get("TaskNames"))
                .containsExactly(
                        tuple(20, "Александр Александров", 3, "Отдел маркетинга", "Менеджер", "Проект B", null),
                        tuple(4, "Алексей Алексеев", 2, "Отдел продаж", "Менеджер", "Проект A", "Задача 14: Создание презентации для клиентов, Задача 1: Подготовка отчета по продажам"),
                        tuple(16, "Анастасия Анастасиева", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null),
                        tuple(29, "Анастасия Анастасиевна", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null),
                        tuple(6, "Андрей Андреев", 1, "Отдел разработки", "Разработчик", "Проект C", "Задача 15: Обновление сайта, Задача 6: Обновление документации"),
                        tuple(30, "Валентин Валентинов", 6, "Отдел разработки", "Разработчик", "Проект C", null),
                        tuple(15, "Виктор Викторов", 4, "Отдел продаж", "Менеджер", "Проект A", null),
                        tuple(21, "Галина Галина", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null),
                        tuple(26, "Денис Денисов", 6, "Отдел разработки", "Разработчик", "Проект C", null),
                        tuple(14, "Дмитрий Дмитриев", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null),
                        tuple(25, "Екатерина Екатеринина", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null),
                        tuple(7, "Елена Еленова", 1, "Отдел поддержки", "Специалист по поддержке", "Проект D", "Задача 12: Настройка системы поддержки"),
                        tuple(1, "Иван Иванов", null, "Отдел продаж", "Генеральный директор", "Проект A", null),
                        tuple(28, "Игорь Игорев", 2, "Отдел продаж", "Менеджер", "Проект A", null),
                        tuple(11, "Ирина Иринина", 6, "Отдел разработки", "Разработчик", "Проект C", "Задача 8: Тестирование нового продукта"),
                        tuple(13, "Кристина Кристинина", 4, "Отдел продаж", "Менеджер", "Проект A", null),
                        tuple(18, "Людмила Людмилова", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null),
                        tuple(17, "Максим Максимов", 6, "Отдел разработки", "Разработчик", "Проект C", null),
                        tuple(23, "Марина Маринина", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null),
                        tuple(5, "Мария Мариева", 3, "Отдел маркетинга", "Менеджер", "Проект B", "Задача 5: Создание рекламной кампании"),
                        tuple(19, "Наталья Натальева", 4, "Отдел продаж", "Менеджер", "Проект A", null),
                        tuple(10, "Николай Николаев", 6, "Отдел разработки", "Разработчик", "Проект C", "Задача 11: Интеграция с новым API, Задача 3: Разработка нового функционала"),
                        tuple(8, "Олег Олегов", 2, "Отдел продаж", "Менеджер", "Проект A", "Задача 7: Проведение тренинга для сотрудников"),
                        tuple(27, "Ольга Ольгина", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null),
                        tuple(22, "Павел Павлов", 6, "Отдел разработки", "Разработчик", "Проект C", null),
                        tuple(2, "Петр Петров", 1, "Отдел продаж", "Директор", "Проект A", null),
                        tuple(3, "Светлана Светлова", 1, "Отдел маркетинга", "Директор", "Проект B", null),
                        tuple(12, "Сергей Сергеев", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", "Задача 4: Поддержка клиентов, Задача 9: Ответы на запросы клиентов"),
                        tuple(24, "Станислав Станиславов", 4, "Отдел продаж", "Менеджер", "Проект A", null),
                        tuple(9, "Татьяна Татеева", 3, "Отдел маркетинга", "Маркетолог", "Проект B", "Задача 10: Подготовка маркетинговых материалов, Задача 13: Проведение анализа конкурентов, Задача 2: Анализ рынка")
                );
    }

    @Test
    void task2() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/organization/task2/task2-solution-h2.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(30);
        assertThat(results)
                .extracting(
                        m -> m.get("EmployeeID"),
                        m -> m.get("EmployeeName"),
                        m -> m.get("ManagerID"),
                        m -> m.get("DepartmentName"),
                        m -> m.get("RoleName"),
                        m -> m.get("ProjectNames"),
                        m -> m.get("TaskNames"),
                        m -> m.get("TotalTasks"),
                        m -> m.get("TotalSubordinates"))
                .containsExactly(
                        tuple(20, "Александр Александров", 3, "Отдел маркетинга", "Менеджер", "Проект B", null, 0L, 0L),
                        tuple(4, "Алексей Алексеев", 2, "Отдел продаж", "Менеджер", "Проект A", "Задача 14: Создание презентации для клиентов, Задача 1: Подготовка отчета по продажам", 2L, 4L),
                        tuple(16, "Анастасия Анастасиева", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null, 0L, 0L),
                        tuple(29, "Анастасия Анастасиевна", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null, 0L, 0L),
                        tuple(6, "Андрей Андреев", 1, "Отдел разработки", "Разработчик", "Проект C", "Задача 15: Обновление сайта, Задача 6: Обновление документации", 2L, 6L),
                        tuple(30, "Валентин Валентинов", 6, "Отдел разработки", "Разработчик", "Проект C", null, 0L, 0L),
                        tuple(15, "Виктор Викторов", 4, "Отдел продаж", "Менеджер", "Проект A", null, 0L, 0L),
                        tuple(21, "Галина Галина", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null, 0L, 0L),
                        tuple(26, "Денис Денисов", 6, "Отдел разработки", "Разработчик", "Проект C", null, 0L, 0L),
                        tuple(14, "Дмитрий Дмитриев", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null, 0L, 0L),
                        tuple(25, "Екатерина Екатеринина", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", null, 0L, 0L),
                        tuple(7, "Елена Еленова", 1, "Отдел поддержки", "Специалист по поддержке", "Проект D", "Задача 12: Настройка системы поддержки", 1L, 5L),
                        tuple(1, "Иван Иванов", null, "Отдел продаж", "Генеральный директор", "Проект A", null, 0L, 4L),
                        tuple(28, "Игорь Игорев", 2, "Отдел продаж", "Менеджер", "Проект A", null, 0L, 0L),
                        tuple(11, "Ирина Иринина", 6, "Отдел разработки", "Разработчик", "Проект C", "Задача 8: Тестирование нового продукта", 1L, 0L),
                        tuple(13, "Кристина Кристинина", 4, "Отдел продаж", "Менеджер", "Проект A", null, 0L, 0L),
                        tuple(18, "Людмила Людмилова", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null, 0L, 0L),
                        tuple(17, "Максим Максимов", 6, "Отдел разработки", "Разработчик", "Проект C", null, 0L, 0L),
                        tuple(23, "Марина Маринина", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null, 0L, 0L),
                        tuple(5, "Мария Мариева", 3, "Отдел маркетинга", "Менеджер", "Проект B", "Задача 5: Создание рекламной кампании", 1L, 0L),
                        tuple(19, "Наталья Натальева", 4, "Отдел продаж", "Менеджер", "Проект A", null, 0L, 0L),
                        tuple(10, "Николай Николаев", 6, "Отдел разработки", "Разработчик", "Проект C", "Задача 11: Интеграция с новым API, Задача 3: Разработка нового функционала", 2L, 0L),
                        tuple(8, "Олег Олегов", 2, "Отдел продаж", "Менеджер", "Проект A", "Задача 7: Проведение тренинга для сотрудников", 1L, 0L),
                        tuple(27, "Ольга Ольгина", 3, "Отдел маркетинга", "Маркетолог", "Проект B", null, 0L, 0L),
                        tuple(22, "Павел Павлов", 6, "Отдел разработки", "Разработчик", "Проект C", null, 0L, 0L),
                        tuple(2, "Петр Петров", 1, "Отдел продаж", "Директор", "Проект A", null, 0L, 3L),
                        tuple(3, "Светлана Светлова", 1, "Отдел маркетинга", "Директор", "Проект B", null, 0L, 7L),
                        tuple(12, "Сергей Сергеев", 7, "Отдел поддержки", "Специалист по поддержке", "Проект D", "Задача 4: Поддержка клиентов, Задача 9: Ответы на запросы клиентов", 2L, 0L),
                        tuple(24, "Станислав Станиславов", 4, "Отдел продаж", "Менеджер", "Проект A", null, 0L, 0L),
                        tuple(9, "Татьяна Татеева", 3, "Отдел маркетинга", "Маркетолог", "Проект B", "Задача 10: Подготовка маркетинговых материалов, Задача 13: Проведение анализа конкурентов, Задача 2: Анализ рынка", 3L, 0L)
                );
    }

    @Test
    void task3() throws Exception {
        String sql = Files.readString(Path.of("src/test/resources/organization/task3/task3-solution-h2.sql"));

        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        assertThat(results).hasSize(1);
        assertThat(results)
                .extracting(
                        m -> m.get("EmployeeID"),
                        m -> m.get("EmployeeName"),
                        m -> m.get("ManagerID"),
                        m -> m.get("DepartmentName"),
                        m -> m.get("RoleName"),
                        m -> m.get("ProjectNames"),
                        m -> m.get("TaskNames"),
                        m -> m.get("TotalSubordinates"))
                .containsExactly(
                        tuple(4, "Алексей Алексеев", 2, "Отдел продаж", "Менеджер", "Проект A", "Задача 14: Создание презентации для клиентов, Задача 1: Подготовка отчета по продажам", 4L)
                );
    }
}
