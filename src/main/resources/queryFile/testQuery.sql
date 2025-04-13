--a. 월별 주문 회원 수, 주문 수, 주문 금액, 할인 금액, 배송비
--mysql
SELECT
    DATE_FORMAT(ORDER_DATE, '%Y-%m') AS month,
    COUNT(DISTINCT USER_ID) AS user_count,
    COUNT(*) AS order_count,
    SUM(TOTAL_PRICE) AS total_price,
    SUM(DISCOUNT_AMOUNT) AS total_discount,
    SUM(DELIVERY_FEE) AS total_delivery_fee
FROM `ORDER`
GROUP BY DATE_FORMAT(ORDER_DATE, '%Y-%m')
ORDER BY month;

--oracle
SELECT
    TO_CHAR(ORDER_DATE, 'YYYY-MM') AS month,
    COUNT(DISTINCT USER_ID) AS user_count,
    COUNT(*) AS order_count,
    SUM(TOTAL_PRICE) AS total_price,
    SUM(DISCOUNT_AMOUNT) AS total_discount,
    SUM(DELIVERY_FEE) AS total_delivery_fee
FROM "ORDER"
GROUP BY TO_CHAR(ORDER_DATE, 'YYYY-MM')
ORDER BY month;


