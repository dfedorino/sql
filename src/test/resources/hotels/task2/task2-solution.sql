SELECT c.ID_customer,
       c.name,
       COUNT(b.ID_booking) AS total_bookings,
       SUM(r.price) AS total_spent,
       COUNT(DISTINCT r.ID_hotel) AS unique_hotels
FROM Customer c
JOIN Booking b ON c.ID_customer = b.ID_customer
JOIN Room r ON b.ID_room = r.ID_room
GROUP BY c.ID_customer, c.name
HAVING COUNT(b.ID_booking) > 2
   AND COUNT(DISTINCT r.ID_hotel) > 1
   AND SUM(r.price) > 500
ORDER BY total_spent;
