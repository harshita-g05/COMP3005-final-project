
DROP TABLE IF EXISTS Bookings CASCADE;
DROP TABLE IF EXISTS HealthMetrics CASCADE;
DROP TABLE IF EXISTS Members CASCADE;
DROP TABLE IF EXISTS FitnessClasses CASCADE;
DROP TABLE IF EXISTS Trainers CASCADE;
DROP TABLE IF EXISTS Rooms CASCADE;
DROP TRIGGER IF EXISTS trigger_prevent_room_conflict ON FitnessClasses;
DROP FUNCTION IF EXISTS check_room_availability;

CREATE TABLE Rooms (
	room_number VARCHAR(10) PRIMARY KEY,
	room_name VARCHAR(50) NOT NULL,
	capacity INT NOT NULL
);


CREATE TABLE Trainers(
	trainer_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE FitnessClasses(
	class_id SERIAL PRIMARY KEY,
	room_number VARCHAR(10) REFERENCES Rooms(room_number),
    trainer_id INT REFERENCES Trainers(trainer_id) ON DELETE SET NULL,
	class_type VARCHAR(50) NOT NULL,
	class_date DATE NOT NULL,
	start_time TIME NOT NULL,
	end_time TIME NOT NULL	
);


CREATE TABLE Members (
    member_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(20),
    gender VARCHAR(10),
    date_of_birth DATE,
    fitness_goal TEXT 
);

CREATE TABLE HealthMetrics (
	member_id INT NOT NULL REFERENCES Members(member_id) ON DELETE CASCADE,
    date_recorded DATE DEFAULT CURRENT_DATE,
    type VARCHAR(50) NOT NULL,
    value DECIMAL(5, 2),
    PRIMARY KEY (member_id, date_recorded, type)
);

CREATE TABLE Bookings (
    booking_id SERIAL PRIMARY KEY,
    member_id INT NOT NULL REFERENCES Members(member_id) ON DELETE CASCADE,
    class_id INT NOT NULL REFERENCES FitnessClasses(class_id) ON DELETE CASCADE,
	payment_amount DECIMAL(10, 2) DEFAULT 0.00,
    billing_status VARCHAR(20) DEFAULT 'Pending'
);



CREATE OR REPLACE FUNCTION check_room_availability() 
RETURNS TRIGGER AS $$
BEGIN
    IF EXISTS (
        SELECT 1 FROM FitnessClasses
        WHERE room_number = NEW.room_number
        AND class_date = NEW.class_date
		AND (start_time < NEW.end_time AND end_time > NEW.start_time)
        AND class_id != NEW.class_id
    ) THEN
        RAISE EXCEPTION 'Room Conflict: Room % is already booked at this time.', NEW.room_number;
    END IF;
    RETURN NEW;
END;

$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_prevent_room_conflict
BEFORE INSERT OR UPDATE ON FitnessClasses
FOR EACH ROW
EXECUTE FUNCTION check_room_availability();


CREATE INDEX idx_member_last_name ON Members(last_name);

CREATE VIEW PublicSchedule AS
SELECT 
    fc.class_date,
    fc.start_time,
    fc.class_type,
    r.room_name,
    t.first_name || ' ' || t.last_name AS trainer_name
FROM FitnessClasses fc
JOIN Rooms r ON fc.room_number = r.room_number
JOIN Trainers t ON fc.trainer_id = t.trainer_id;



