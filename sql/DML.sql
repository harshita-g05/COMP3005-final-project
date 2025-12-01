INSERT INTO Rooms (room_number, room_name, capacity) VALUES 
('101', 'Cardio Studio', 20),
('102', 'Weight Room', 10),
('201', 'Yoga Studio', 15);


INSERT INTO Trainers (first_name, last_name, email) VALUES 
('Alice', 'Parker', 'aliceparker@gmail.com'),
('Jane', 'Smith', 'janesmith@gmail.com'),
('Caden', 'Scott', 'cadenscott@gmail.com');


INSERT INTO FitnessClasses (room_number, trainer_id, class_type, class_date, start_time, end_time) VALUES 
('201', 2, 'Yoga', '2025-12-05', '09:00:00', '10:00:00'),
('102', 1, 'HIIT', '2025-12-05', '14:00:00', '15:00:00'),
('101', 3, 'Cardio Blast', '2025-12-06', '10:00:00', '11:00:00'),
('102', 2, 'Personal Training', '2025-12-07', '08:00:00', '09:00:00');


INSERT INTO Members (first_name, last_name, email, phone, gender, date_of_birth, fitness_goal) VALUES 
('Rachel', 'Green', 'rachelg@gmail.com', '431-555-0101', 'Female', '1996-04-12', 'Lose 10lbs'),
('Raina', 'Sanchez', 'rainasanchez@hotmail.com', '204-555-0102', 'Female', '2005-11-23', 'Build Muscle'),
('Charlie', 'Brown', 'charliebrown21@gmail.com', '133-555-0103', 'Male', '2000-01-15', 'Run a Marathon');

INSERT INTO HealthMetrics (member_id, date_recorded, type, value) VALUES 
(1, '2025-11-01', 'Weight', 140.0),
(1, '2025-11-15', 'Weight', 138.5),
(1, '2025-11-29', 'Weight', 135.0),
(1, '2025-11-29', 'Heart Rate', 72.0),
(2, '2025-11-20', 'Weight', 150.0);


INSERT INTO Bookings (member_id, class_id, payment_amount, billing_status) VALUES 
(1, 1, 20.00, 'Paid'),       
(2, 2, 15.00, 'Pending'),    
(3, 3, 20.00, 'Paid'),      
(1, 4, 50.00, 'Pending');   