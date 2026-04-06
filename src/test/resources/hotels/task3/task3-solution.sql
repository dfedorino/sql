WITH HotelAvg AS (
    SELECT h.ID_hotel,
           h.name,
           AVG(r.price) AS avg_price
    FROM Hotel h
    JOIN Room r ON h.ID_hotel = r.ID_hotel
    GROUP BY h.ID_hotel, h.name
),
HotelCategory AS (
    SELECT ID_hotel,
           name,
           CASE
               WHEN avg_price < 175 THEN 'Дешевый'
               WHEN avg_price <= 300 THEN 'Средний'
               ELSE 'Дорогой'
           END AS category
    FROM HotelAvg
),
CustomerHotels AS (
    SELECT b.ID_customer,
           hc.name AS hotel_name,
           hc.category
    FROM Booking b
    JOIN Room r ON b.ID_room = r.ID_room
    JOIN HotelCategory hc ON r.ID_hotel = hc.ID_hotel
),
CustomerPreferred AS (
    SELECT ID_customer,
           CASE
               WHEN MAX(CASE WHEN category = 'Дорогой' THEN 1 ELSE 0 END) = 1 THEN 'Дорогой'
               WHEN MAX(CASE WHEN category = 'Средний' THEN 1 ELSE 0 END) = 1 THEN 'Средний'
               ELSE 'Дешевый'
           END AS preferred_hotel_type
    FROM CustomerHotels
    GROUP BY ID_customer
),
CustomerVisited AS (
    SELECT ID_customer,
           LISTAGG(DISTINCT hotel_name, ', ') WITHIN GROUP (ORDER BY hotel_name) AS visited_hotels
    FROM CustomerHotels
    GROUP BY ID_customer
)
SELECT c.ID_customer,
       c.name,
       cp.preferred_hotel_type,
       cv.visited_hotels
FROM Customer c
JOIN CustomerPreferred cp ON c.ID_customer = cp.ID_customer
JOIN CustomerVisited cv ON c.ID_customer = cv.ID_customer
ORDER BY CASE cp.preferred_hotel_type
             WHEN 'Дешевый' THEN 1
             WHEN 'Средний' THEN 2
             WHEN 'Дорогой' THEN 3
         END;
