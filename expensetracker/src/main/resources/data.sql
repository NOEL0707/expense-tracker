MERGE INTO users (id, name, email, created_at) KEY(id) VALUES
('11111111-1111-1111-1111-111111111111', 'Alex Johnson', 'alex@example.com', '2024-01-05T09:00:00'),
('22222222-2222-2222-2222-222222222222', 'Priya Sharma', 'priya@example.com', '2024-01-06T10:15:00'),
('33333333-3333-3333-3333-333333333333', 'Marcus Lee', 'marcus@example.com', '2024-01-07T08:45:00');

MERGE INTO expenses (id, amount, category, description, date, idempotency_key, created_at, user_id) KEY(id) VALUES
('123e4567-e89b-12d3-a456-426614174000', 15000, 'Food', 'Lunch with team', '2023-10-25', 'key1', '2023-10-25T14:30:00', '11111111-1111-1111-1111-111111111111'),
('223e4567-e89b-12d3-a456-426614174001', 5000, 'Transport', 'Taxi to airport', '2023-10-26', 'key2', '2023-10-26T10:00:00', '11111111-1111-1111-1111-111111111111'),
('323e4567-e89b-12d3-a456-426614174002', 20000, 'Entertainment', 'Movie night', '2023-10-27', 'key3', '2023-10-27T20:00:00', '22222222-2222-2222-2222-222222222222'),
('423e4567-e89b-12d3-a456-426614174003', 8900, 'Food', 'Coffee beans', '2023-10-28', 'key4', '2023-10-28T08:10:00', '33333333-3333-3333-3333-333333333333');
