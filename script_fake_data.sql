-- Fake seed data for 6_8_TMDT_backend (PostgreSQL)
-- Re-runnable script. All demo users share the same password hash:
-- $2y$10$3.EDPxNy2qmVfUa9nKW8C.Ot1NLl2D5wqnfZH2.YE2YzqqS8k722i

-- Create tables if they do not exist (DDL derived from entity classes)
-- This ensures the seed script can run on a fresh database.

BEGIN;

-- Users table (id is UUID string)
CREATE TABLE IF NOT EXISTS users (
  id varchar(36) PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  avatar_id varchar(100),
  email varchar(100) UNIQUE NOT NULL,
  phone varchar(20) UNIQUE NOT NULL,
  full_name varchar(255) NOT NULL,
  is_lock boolean DEFAULT false,
  password_hash varchar(255) NOT NULL,
  role varchar(50) NOT NULL
);

-- Cinemas
CREATE TABLE IF NOT EXISTS cinemas (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  name varchar(255) NOT NULL,
  address text NOT NULL,
  city varchar(100) NOT NULL
);

-- Rooms
CREATE TABLE IF NOT EXISTS rooms (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  cinema_id integer NOT NULL REFERENCES cinemas(id) ON DELETE CASCADE,
  name varchar(100) NOT NULL,
  total_seats integer NOT NULL,
  type varchar(50) NOT NULL
);

-- Seats
CREATE TABLE IF NOT EXISTS seats (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  room_id integer NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  seat_number varchar(10) NOT NULL,
  row_label varchar(5) NOT NULL,
  type varchar(50) NOT NULL,
  base_price numeric(10,2) NOT NULL
);

-- Media
CREATE TABLE IF NOT EXISTS media (
  id varchar(64) PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  name varchar(255),
  url varchar(1000),
  content_type varchar(100),
  size bigint,
  file_key varchar(500),
  status boolean
);

-- Movies
CREATE TABLE IF NOT EXISTS movies (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  title varchar(500) NOT NULL,
  genre varchar(255) NOT NULL,
  duration integer NOT NULL,
  director varchar(255) NOT NULL,
  movie_cast text,
  description text,
  poster_media_id varchar(64) REFERENCES media(id),
  release_date date NOT NULL,
  status varchar(50) NOT NULL,
  teaser_url varchar(500),
  review_url varchar(500)
);

-- Showtimes
CREATE TABLE IF NOT EXISTS showtimes (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  movie_id integer NOT NULL REFERENCES movies(id) ON DELETE CASCADE,
  room_id integer NOT NULL REFERENCES rooms(id) ON DELETE CASCADE,
  start_time timestamptz NOT NULL,
  end_time timestamptz NOT NULL,
  base_price numeric(10,2) NOT NULL,
  available_seats integer NOT NULL,
  status varchar(50) NOT NULL
);

-- Promotions
CREATE TABLE IF NOT EXISTS promotions (
  id serial PRIMARY KEY,
  code varchar(50) UNIQUE NOT NULL,
  created_at timestamptz NOT NULL,
  discount_type varchar(50) NOT NULL,
  discount_value numeric(10,2) NOT NULL,
  start_date date NOT NULL,
  end_date date NOT NULL,
  max_usage integer NOT NULL,
  used_count integer NOT NULL DEFAULT 0,
  status varchar(50) NOT NULL
);

-- Bookings
CREATE TABLE IF NOT EXISTS bookings (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  user_id varchar(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  showtime_id integer NOT NULL REFERENCES showtimes(id) ON DELETE CASCADE,
  promotion_id integer REFERENCES promotions(id),
  total_amount numeric(10,2) NOT NULL,
  discount_amount numeric(10,2) NOT NULL DEFAULT 0,
  final_amount numeric(10,2) NOT NULL,
  status varchar(50) NOT NULL,
  qr_code varchar(500)
);

-- Booking details
CREATE TABLE IF NOT EXISTS booking_details (
  id serial PRIMARY KEY,
  created_at timestamptz NOT NULL,
  created_by uuid,
  updated_at timestamptz,
  updated_by uuid,
  booking_id integer NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
  seat_id integer NOT NULL REFERENCES seats(id) ON DELETE CASCADE,
  seat_number varchar(10) NOT NULL,
  price_at_time numeric(10,2) NOT NULL
);

-- Payments
CREATE TABLE IF NOT EXISTS payments (
  id serial PRIMARY KEY,
  booking_id integer NOT NULL REFERENCES bookings(id) ON DELETE CASCADE,
  method varchar(30) NOT NULL,
  amount numeric(10,2) NOT NULL,
  transaction_id varchar(100) UNIQUE,
  status varchar(50) NOT NULL,
  paid_at timestamptz,
  created_at timestamptz NOT NULL
);

-- Notifications
CREATE TABLE IF NOT EXISTS notifications (
  id serial PRIMARY KEY,
  user_id varchar(36) NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  booking_id integer REFERENCES bookings(id),
  type varchar(20) NOT NULL,
  subject varchar(255) NOT NULL,
  body text NOT NULL,
  status varchar(20) NOT NULL,
  sent_at timestamptz,
  created_at timestamptz NOT NULL
);


-- =========================
-- 0) Cleanup old demo data
-- =========================
DELETE FROM notifications WHERE id IN (9501, 9502, 9503, 9504) OR user_id IN ('usr_demo_admin', 'usr_demo_001', 'usr_demo_002');
DELETE FROM payments WHERE id IN (9001, 9002, 9003) OR transaction_id IN ('TX-DEMO-7001', 'TX-DEMO-7002', 'TX-DEMO-7003');
DELETE FROM booking_details WHERE id IN (8001, 8002, 8003, 8004, 8005) OR booking_id IN (7001, 7002, 7003);
DELETE FROM bookings WHERE id IN (7001, 7002, 7003) OR qr_code IN ('QR-DEMO-7001', 'QR-DEMO-7002', 'QR-DEMO-7003');
DELETE FROM showtimes WHERE id IN (6001, 6002, 6003, 6004);
DELETE FROM movies WHERE id IN (5001, 5002, 5003);
DELETE FROM media WHERE id IN ('media_demo_movie_001', 'media_demo_movie_002', 'media_demo_movie_003');
DELETE FROM seats WHERE id IN (4001, 4002, 4003, 4004, 4005, 4006, 4007, 4008);
DELETE FROM rooms WHERE id IN (3001, 3002, 3003);
DELETE FROM promotions WHERE id IN (2001, 2002) OR code IN ('PROMO-DEMO-10', 'PROMO-DEMO-50000');
DELETE FROM cinemas WHERE id IN (1001, 1002);
DELETE FROM users WHERE id IN ('usr_demo_admin', 'usr_demo_001', 'usr_demo_002')
   OR email IN ('admin_demo@mail.com', 'booking_demo_001@mail.com', 'booking_demo_002@mail.com')
   OR phone IN ('0900000009', '0900000001', '0900000002');

-- =========================
-- 1) Users
-- =========================
INSERT INTO users (
    id, created_at, created_by, updated_at, updated_by,
    avatar_id, email, full_name, is_lock, password_hash, phone, role
) VALUES
(
    'usr_demo_admin',
    NOW(), NULL, NULL, NULL,
    NULL,
    'admin_demo@mail.com',
    'Demo Admin',
    FALSE,
    '$2y$10$3.EDPxNy2qmVfUa9nKW8C.Ot1NLl2D5wqnfZH2.YE2YzqqS8k722i',
    '0900000009',
    'ADMIN'
),
(
    'usr_demo_001',
    NOW(), NULL, NULL, NULL,
    NULL,
    'booking_demo_001@mail.com',
    'Booking Demo User 1',
    FALSE,
    '$2y$10$3.EDPxNy2qmVfUa9nKW8C.Ot1NLl2D5wqnfZH2.YE2YzqqS8k722i',
    '0900000001',
    'USER'
),
(
    'usr_demo_002',
    NOW(), NULL, NULL, NULL,
    NULL,
    'booking_demo_002@mail.com',
    'Booking Demo User 2',
    FALSE,
    '$2y$10$3.EDPxNy2qmVfUa9nKW8C.Ot1NLl2D5wqnfZH2.YE2YzqqS8k722i',
    '0900000002',
    'USER'
);

-- =========================
-- 2) Cinema / Room / Seat
-- =========================
INSERT INTO cinemas (id, created_at, created_by, updated_at, updated_by, address, city, name) VALUES
(1001, NOW(), NULL, NULL, NULL, '123 Nguyen Hue, District 1, Ho Chi Minh City', 'HCM', 'CGV Demo D1'),
(1002, NOW(), NULL, NULL, NULL, '02 Vo Van Ngan, Thu Duc City, Ho Chi Minh City', 'HCM', 'Lotte Demo Thu Duc');

INSERT INTO rooms (id, created_at, created_by, updated_at, updated_by, name, total_seats, type, cinema_id) VALUES
(3001, NOW(), NULL, NULL, NULL, 'Room A', 120, 'STANDARD', 1001),
(3002, NOW(), NULL, NULL, NULL, 'Room VIP 1', 60, 'VIP', 1001),
(3003, NOW(), NULL, NULL, NULL, 'IMAX 01', 150, 'IMAX', 1002);

INSERT INTO seats (id, created_at, created_by, updated_at, updated_by, base_price, row_label, seat_number, type, room_id) VALUES
(4001, NOW(), NULL, NULL, NULL, 90000.00, 'A', 'A1', 'STANDARD', 3001),
(4002, NOW(), NULL, NULL, NULL, 90000.00, 'A', 'A2', 'STANDARD', 3001),
(4003, NOW(), NULL, NULL, NULL, 120000.00, 'B', 'B1', 'VIP', 3001),
(4004, NOW(), NULL, NULL, NULL, 120000.00, 'B', 'B2', 'VIP', 3001),
(4005, NOW(), NULL, NULL, NULL, 150000.00, 'C', 'C1', 'VIP', 3002),
(4006, NOW(), NULL, NULL, NULL, 150000.00, 'C', 'C2', 'VIP', 3002),
(4007, NOW(), NULL, NULL, NULL, 180000.00, 'D', 'D1', 'COUPLE', 3003),
(4008, NOW(), NULL, NULL, NULL, 180000.00, 'D', 'D2', 'COUPLE', 3003);

-- =========================
-- 3) Media / Movie
-- =========================
INSERT INTO media (
    id, created_at, created_by, updated_at, updated_by,
    content_type, file_key, name, size, status, url
) VALUES
(
    'media_demo_movie_001',
    NOW(), NULL, NULL, NULL,
    'image/jpeg', 'posters/poster-demo-1.jpg', 'Poster Demo 1', 123456, TRUE, 'https://example.com/media/poster-demo-1.jpg'
),
(
    'media_demo_movie_002',
    NOW(), NULL, NULL, NULL,
    'image/jpeg', 'posters/poster-demo-2.jpg', 'Poster Demo 2', 135790, TRUE, 'https://example.com/media/poster-demo-2.jpg'
),
(
    'media_demo_movie_003',
    NOW(), NULL, NULL, NULL,
    'image/jpeg', 'posters/poster-demo-3.jpg', 'Poster Demo 3', 148800, TRUE, 'https://example.com/media/poster-demo-3.jpg'
);

INSERT INTO movies (
    id, created_at, created_by, updated_at, updated_by,
    description, director, duration, genre, movie_cast,
    release_date, review_url, status, teaser_url, title, poster_media_id
) VALUES
(
    5001,
    NOW(), NULL, NULL, NULL,
    'Action movie used for demo booking flow',
    'Director Demo 1',
    120,
    'Action',
    'Actor A, Actor B',
    DATE '2026-04-19',
    'https://example.com/review/1',
    'NOW_SHOWING',
    'https://example.com/teaser/1',
    'The First Booking',
    'media_demo_movie_001'
),
(
    5002,
    NOW(), NULL, NULL, NULL,
    'Comedy movie for weekend schedule',
    'Director Demo 2',
    105,
    'Comedy',
    'Actor C, Actor D',
    DATE '2026-05-01',
    'https://example.com/review/2',
    'COMING_SOON',
    'https://example.com/teaser/2',
    'Laugh Night',
    'media_demo_movie_002'
),
(
    5003,
    NOW(), NULL, NULL, NULL,
    'Family adventure for IMAX demo slot',
    'Director Demo 3',
    135,
    'Adventure',
    'Actor E, Actor F',
    DATE '2026-03-12',
    'https://example.com/review/3',
    'ENDED',
    'https://example.com/teaser/3',
    'Journey Home',
    'media_demo_movie_003'
);

-- =========================
-- 4) Showtime / Promotion
-- =========================
INSERT INTO showtimes (
    id, created_at, created_by, updated_at, updated_by,
    available_seats, base_price, end_time, start_time, status, movie_id, room_id
) VALUES
(6001, NOW(), NULL, NULL, NULL, 118, 90000.00, TIMESTAMPTZ '2026-06-01 20:00:00+00', TIMESTAMPTZ '2026-06-01 18:00:00+00', 'SCHEDULED', 5001, 3001),
(6002, NOW(), NULL, NULL, NULL, 116, 95000.00, TIMESTAMPTZ '2026-06-01 23:00:00+00', TIMESTAMPTZ '2026-06-01 21:00:00+00', 'ONGOING', 5001, 3001),
(6003, NOW(), NULL, NULL, NULL, 58, 150000.00, TIMESTAMPTZ '2026-06-02 22:30:00+00', TIMESTAMPTZ '2026-06-02 20:30:00+00', 'SCHEDULED', 5002, 3002),
(6004, NOW(), NULL, NULL, NULL, 148, 180000.00, TIMESTAMPTZ '2026-06-03 22:00:00+00', TIMESTAMPTZ '2026-06-03 19:30:00+00', 'SCHEDULED', 5003, 3003);

INSERT INTO promotions (
    id, code, created_at, discount_type, discount_value,
    end_date, max_usage, start_date, status, used_count
) VALUES
(2001, 'PROMO-DEMO-10', NOW(), 'PERCENT', 10.00, DATE '2026-12-31', 1000, DATE '2026-01-01', 'ACTIVE', 12),
(2002, 'PROMO-DEMO-50000', NOW(), 'FIXED', 50000.00, DATE '2026-12-31', 500, DATE '2026-01-01', 'ACTIVE', 4);

-- =========================
-- 5) Booking flow data
-- =========================
INSERT INTO bookings (
    id, created_at, created_by, updated_at, updated_by,
    discount_amount, final_amount, qr_code, status, total_amount,
    promotion_id, showtime_id, user_id
) VALUES
(7001, NOW(), NULL, NULL, NULL, 9000.00, 81000.00, 'QR-DEMO-7001', 'PENDING', 90000.00, 2001, 6001, 'usr_demo_001'),
(7002, NOW(), NULL, NULL, NULL, 50000.00, 100000.00, 'QR-DEMO-7002', 'CONFIRMED', 150000.00, 2002, 6003, 'usr_demo_002'),
(7003, NOW(), NULL, NULL, NULL, 0.00, 180000.00, 'QR-DEMO-7003', 'CANCELLED', 180000.00, NULL, 6004, 'usr_demo_001');

INSERT INTO booking_details (
    id, created_at, created_by, updated_at, updated_by,
    price_at_time, seat_number, booking_id, seat_id
) VALUES
(8001, NOW(), NULL, NULL, NULL, 90000.00, 'A1', 7001, 4001),
(8002, NOW(), NULL, NULL, NULL, 150000.00, 'C1', 7002, 4005),
(8003, NOW(), NULL, NULL, NULL, 150000.00, 'C2', 7002, 4006),
(8004, NOW(), NULL, NULL, NULL, 180000.00, 'D1', 7003, 4007),
(8005, NOW(), NULL, NULL, NULL, 180000.00, 'D2', 7003, 4008);

INSERT INTO payments (
    id, amount, created_at, method, paid_at, status, transaction_id, booking_id
) VALUES
(9001, 81000.00, NOW(), 'MOMO', NOW(), 'PENDING', 'TX-DEMO-7001', 7001),
(9002, 100000.00, NOW(), 'VNPAY', NOW(), 'SUCCESS', 'TX-DEMO-7002', 7002),
(9003, 180000.00, NOW(), 'CASH', NULL, 'FAILED', 'TX-DEMO-7003', 7003);

INSERT INTO notifications (
    id, body, created_at, sent_at, status, subject, type, booking_id, user_id
) VALUES
(9501, 'Dat ve dang xu ly. Ma booking: QR-DEMO-7001', NOW(), NULL, 'PENDING', 'Trang thai dat ve', 'EMAIL', 7001, 'usr_demo_001'),
(9502, 'Dat ve thanh cong. Ma booking: QR-DEMO-7002', NOW(), NOW(), 'SENT', 'Xac nhan dat ve', 'EMAIL', 7002, 'usr_demo_002'),
(9503, 'Lich chieu san sang cho bo phim Journey Home', NOW(), NOW(), 'SENT', 'Thong bao lich chieu', 'PUSH', 7003, 'usr_demo_001'),
(9504, 'Khuyen mai moi PROMO-DEMO-10', NOW(), NOW(), 'SENT', 'Khuyen mai', 'PUSH', NULL, 'usr_demo_admin');

-- =========================
-- 6) Sync identity sequences
-- =========================
SELECT setval(pg_get_serial_sequence('cinemas', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM cinemas), 1002));
SELECT setval(pg_get_serial_sequence('rooms', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM rooms), 3003));
SELECT setval(pg_get_serial_sequence('seats', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM seats), 4008));
SELECT setval(pg_get_serial_sequence('promotions', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM promotions), 2002));
SELECT setval(pg_get_serial_sequence('movies', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM movies), 5003));
SELECT setval(pg_get_serial_sequence('showtimes', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM showtimes), 6004));
SELECT setval(pg_get_serial_sequence('bookings', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM bookings), 7003));
SELECT setval(pg_get_serial_sequence('booking_details', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM booking_details), 8005));
SELECT setval(pg_get_serial_sequence('payments', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM payments), 9003));
SELECT setval(pg_get_serial_sequence('notifications', 'id'), GREATEST((SELECT COALESCE(MAX(id), 1) FROM notifications), 9504));

COMMIT;
