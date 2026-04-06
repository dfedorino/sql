WITH CarAvg AS (SELECT c.name,
                       c.class,
                       AVG(r.position) AS avg_pos,
                       COUNT(r.race)   AS race_count
                FROM Cars c
                         JOIN Results r ON c.name = r.car
                GROUP BY c.name, c.class),
     ClassLowCount AS (SELECT class,
                              COUNT(*) AS low_position_count
                       FROM CarAvg
                       WHERE avg_pos >= 3.0
                       GROUP BY class),
     TotalRaces AS (SELECT ca.class,
                           SUM(ca.race_count) AS total_races
                    FROM CarAvg ca
                    WHERE ca.class IN (SELECT class FROM ClassLowCount)
                    GROUP BY ca.class)
SELECT ca.name                                      AS car_name,
       cl.class                                     AS car_class,
       CAST(ROUND(ca.avg_pos, 4) AS DECIMAL(10, 4)) AS average_position,
       CAST(ca.race_count AS INT)                   AS race_count,
       cl.country                                   AS car_country,
       tr.total_races,
       clc.low_position_count
FROM CarAvg ca
         JOIN ClassLowCount clc ON ca.class = clc.class
         JOIN TotalRaces tr ON ca.class = tr.class
         JOIN Classes cl ON ca.class = cl.class
WHERE ca.avg_pos > 3.0
ORDER BY clc.low_position_count DESC, LOWER(cl.class);
