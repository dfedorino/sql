-- H2 / in-memory DB variant (uses LISTAGG)
-- For MySQL, see task1-solution-mysql.sql
SELECT c.name,
       c.email,
       c.phone,
       COUNT(b.ID_booking) AS         total_bookings,
       LISTAGG(DISTINCT h.name, ', ') WITHIN GROUP (ORDER BY h.name) AS hotels_visited,
       CAST(ROUND(AVG(DATEDIFF('DAY', b.check_in_date, b.check_out_date)), 4) AS DECIMAL(10,4)) AS avg_stay_duration
FROM Customer c
    JOIN Booking b
ON c.ID_customer = b.ID_customer
    JOIN Room r ON b.ID_room = r.ID_room
    JOIN Hotel h ON r.ID_hotel = h.ID_hotel
GROUP BY c.ID_customer, c.name, c.email, c.phone
HAVING COUNT(b.ID_booking)
     > 2
   AND COUNT(DISTINCT h.ID_hotel)
     > 1
ORDER BY total_bookings DESC;
