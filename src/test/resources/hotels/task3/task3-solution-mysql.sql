-- MySQL variant (GROUP_CONCAT instead of LISTAGG)
SELECT c.ID_customer,
       c.name,
       CASE
           WHEN MAX(CASE WHEN hc.category = 'Дорогой' THEN 1 ELSE 0 END) = 1 THEN 'Дорогой'
           WHEN MAX(CASE WHEN hc.category = 'Средний' THEN 1 ELSE 0 END) = 1 THEN 'Средний'
           ELSE 'Дешевый'
           END AS preferred_hotel_type,
       GROUP_CONCAT(DISTINCT h.name ORDER BY h.name SEPARATOR ', ') AS visited_hotels
FROM Customer c
         JOIN Booking b ON c.ID_customer = b.ID_customer
         JOIN Room r ON b.ID_room = r.ID_room
         JOIN Hotel h ON r.ID_hotel = h.ID_hotel
         JOIN (SELECT ID_hotel,
                      CASE WHEN AVG(price) < 175 THEN 'Дешевый'
                           WHEN AVG(price) <= 300 THEN 'Средний'
                           ELSE 'Дорогой' END AS category
               FROM Room
               GROUP BY ID_hotel) hc ON h.ID_hotel = hc.ID_hotel
GROUP BY c.ID_customer, c.name
ORDER BY MAX(CASE hc.category WHEN 'Дешевый' THEN 1 WHEN 'Средний' THEN 2 ELSE 3 END);
