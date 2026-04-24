-- Insert some mock expenses
INSERT INTO expenses (id, amount, category, description, date, idempotency_key, created_at) VALUES
('123e4567-e89b-12d3-a456-426614174000', 15000, 'Food', 'Lunch with team', '2023-10-25', 'key1', '2023-10-25T14:30:00'),
('223e4567-e89b-12d3-a456-426614174001', 5000, 'Transport', 'Taxi to airport', '2023-10-26', 'key2', '2023-10-26T10:00:00'),
('323e4567-e89b-12d3-a456-426614174002', 20000, 'Entertainment', 'Movie night', '2023-10-27', 'key3', '2023-10-27T20:00:00');
