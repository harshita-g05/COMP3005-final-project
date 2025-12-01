
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.*;
import java.util.Scanner;

/**
 * FINAL PROJECT FOR COMP3005
 * NAME: HARSHITA GUPTA
 * STUDENT ID: 101298606
 * Simple Command Line Interface Simulation for a Health and Fitness
 * Club Database Management System
 */

public class fitnessCenter {
    // Database Setup -------
    static String url = "jdbc:postgresql://localhost:5432/Health and Fitness Club";
    static String user = "postgres";
    static String password = "harshita9098";
    private static Connection conn = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        try {
            // Connect to Database
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to Database Successfully!");

            // Main Menu Loop
            while (true) {
                System.out.println("\n Welcome to the Health and Fitness Club!\n");
                System.out.println("1. Sign-up (New Users)");
                System.out.println("2. Login (Existing Users)");
                System.out.println("3. Exit");
                System.out.print("Enter Options: ");

                String choice = scanner.nextLine();

                switch (choice) {
                    // This is for new members (sign-up logic)
                    case "1":
                        System.out.println("\n Welcome new member!\n Choose your sign-up method:");
                        System.out.println("1. Member Signup");
                        System.out.println("2. Trainer Signup");
                        System.out.println("3. Exit");
                        System.out.print("Select: ");
                        String signUpOption = scanner.nextLine();
                        if (signUpOption.equals("1")) {
                            memberSignup();
                        } else if (signUpOption.equals("2")) {
                            trainerSignup();
                        }
                        break;

                    // This is for old/existing members (login logic)
                    case "2":
                        System.out.println("\n Welcome back!\n Choose your login method:");
                        System.out.println("1. Member Login");
                        System.out.println("2. Trainer Login");
                        System.out.println("3. Admin Staff Login");
                        System.out.println("4. Exit");
                        System.out.print("Select: ");
                        String loginOption = scanner.nextLine();

                        if (loginOption.equals("1")) {
                            memberLogin();
                        } else if (loginOption.equals("2")) {
                            trainerLogin();
                        } else if (loginOption.equals("3")) {
                            adminLogin();
                        }
                        break;

                    case "3":
                        System.out.println("Bye bye!");
                        return;

                    default:
                        System.out.println("Invalid selection.");
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

////////////////////// SIGNUP METHODS /////////////////////////

    /**
     * This function registers a new member in the database
     * @throws SQLException
     */
    private static void memberSignup() throws SQLException {
        System.out.print("First Name: ");
        String fName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Phone Number: ");
        String phone = scanner.nextLine();
        System.out.print("Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Date of Birth (YYYY-MM-DD): ");
        String dob = scanner.nextLine();
        System.out.print("Fitness Goal: ");
        String goal = scanner.nextLine();

        String sql = "INSERT INTO members (first_name, last_name, email, phone, gender, date_of_birth, fitness_goal) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, email);
            stmt.setString(4, phone);
            stmt.setString(5, gender);
            // have to convert the string to date format so sql accepts
            stmt.setDate(6, Date.valueOf(dob));
            stmt.setString(7, goal);
            stmt.executeUpdate();
            System.out.println("Successful! Please go back and Login.");
        } catch (SQLException e) {
            System.out.println("Registration Failed: " + e.getMessage());
        }
    }

    /**
     * This function registers a new trainer in the database
     * @throws SQLException
     */
    private static void trainerSignup() throws SQLException {
        System.out.print("First Name: ");
        String fName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        String sql = "INSERT INTO Trainers (first_name, last_name, email) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, fName);
            stmt.setString(2, lName);
            stmt.setString(3, email);
            stmt.executeUpdate();
            System.out.println("Successful! Please go back and Login.");
        } catch (SQLException e) {
            System.out.println("Registration Failed: " + e.getMessage());
        }
    }


////////////////// LOGIN HELPER METHODS ///////////////////////////////////

    /**
     * The function authenticates a member by thier email and allows them
     * to login and have access to the member dashboard (a seperate function).
     * @throws SQLException
     */
    private static void memberLogin() throws SQLException {
        System.out.print("Enter Your Email: ");
        String email = scanner.nextLine();
        String sql = "SELECT * FROM Members WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            // We still need the ID for the dashboard, so we grab it from the result
            int id = rs.getInt("member_id");
            System.out.println("Login Successful! Welcome, " + rs.getString("first_name"));
            memberDashboard(id); // show member dashboard according to thier id
        } else {
            System.out.println("Email not found. Please Sign Up first.");
        }
    }

    /**
     * This function authenticates a trainer by thier email and allows them
     * to login and have access to the trainer dashboard (a seperate function).
     * @throws SQLException
     */
    private static void trainerLogin() throws SQLException {
        System.out.print("Enter Your Email: ");
        String email = scanner.nextLine();
        String sql = "SELECT * FROM Trainers WHERE email = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            int id = rs.getInt("trainer_id");
            System.out.println("Login Successful! Welcome, " + rs.getString("first_name"));
            trainerDashboard(id); // pass the id to the dashboard
        } else {
            System.out.println("Email not found. Please Sign Up first.");
        }
    }


    /**
     * This function authenticates the admin by thier password ( which is admin123) and allows them
     * to login and have access to the admin dashboard (a seperate function).
     * @throws SQLException
     */
    private static void adminLogin() throws SQLException {
        System.out.print("Enter Admin Password: ");
        String passcode = scanner.nextLine();
        if (passcode.equals("admin123")) {
            System.out.println("Welcome Admin!");
            adminDashboard(); // GO TO DASHBOARD
        } else {
            System.out.println("Incorrect Password.");
        }
    }

//////////////////// DASHBOARD METHODS ///////////////////////////////////////////////////////


    /**
     * MEMBER DASHBOARD FUNCTION
     * Includes features available to the member such as:
     * View Profile/Schedule,Book Class, Log Metrics, Cancel Booking
     * @param memberId
     * @throws SQLException
     */
    private static void memberDashboard(int memberId) throws SQLException {
        while (true) {
            // menu for member
            System.out.println("\nMEMBER DASHBOARD\n");
            System.out.println("1. View Profile & Stats");
            System.out.println("2. Book a Session");
            System.out.println("3. Log new Health Metric");
            System.out.println("4. Cancel a Booking");
            System.out.println("5. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            // VIEW PROFILE/SCHEDULE LOGIC ////////////
            if (choice.equals("1")) {
                String sql = "SELECT * FROM Members WHERE member_id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, memberId);
                ResultSet rs = stmt.executeQuery();
                if(rs.next()) {
                    // show fitness goals:
                    System.out.println("------My Profile--------\nGoal: " + rs.getString("fitness_goal"));
                }
                // show health metrics:
                System.out.println("Health History:");
                String hSql = "SELECT * FROM HealthMetrics WHERE member_id = ?";
                PreparedStatement hStmt = conn.prepareStatement(hSql);
                hStmt.setInt(1, memberId);
                ResultSet hRs = hStmt.executeQuery();
                while(hRs.next()) {
                    System.out.println("- " + hRs.getString("type") + ": " + hRs.getDouble("value") + " (" + hRs.getDate("date_recorded") + ")");
                }
                // show upcoming fitness classes:
                System.out.println("Upcoming Bookings:");
                // get any current booking information and display on profile
                String bookSql = "SELECT class_type, class_date, start_time, FitnessClasses.room_number " +
                        "FROM Bookings, FitnessClasses, Rooms " +
                        "WHERE Bookings.class_id = FitnessClasses.class_id " +
                        "AND FitnessClasses.room_number = Rooms.room_number " + "AND member_id = ? AND class_date >= CURRENT_DATE";
                PreparedStatement bStmt = conn.prepareStatement(bookSql);
                bStmt.setInt(1, memberId);
                ResultSet bRs = bStmt.executeQuery();
                while(bRs.next()) {
                    System.out.println("- " + bRs.getString("class_type") +
                            " on " + bRs.getDate("class_date") +
                            " at " + bRs.getTime("start_time") +
                            " in room: " + bRs.getString("room_number"));
                }

            /// BOOKING CODE LOGIC
            } else if (choice.equals("2")) {
                // show available classes to book
                System.out.println("\nAvailable Classes: \n");
                String cSql = "SELECT * FROM FitnessClasses ORDER BY class_date";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(cSql);
                System.out.println("ID   | Class Type      | Date        | Time");
                System.out.println("------------------------------------------------");
                while(rs.next()) {
                    int id = rs.getInt("class_id");
                    String type = rs.getString("class_type");
                    String date = rs.getDate("class_date").toString();
                    String time = rs.getTime("start_time").toString();
                    System.out.println(id + "    | " + type + "   | " + date + "  | " + time);
                }
                // get users input for which class they'd like to book
                System.out.print("\nEnter Class ID to book: ");
                try {
                    int classId = Integer.parseInt(scanner.nextLine());
                    // insert Booking
                    String bookSql = "INSERT INTO Bookings (member_id, class_id, payment_amount, billing_status) VALUES (?, ?, 20.00, 'Pending')";
                    PreparedStatement bookStmt = conn.prepareStatement(bookSql);
                    bookStmt.setInt(1, memberId);
                    bookStmt.setInt(2, classId);
                    bookStmt.executeUpdate();
                    System.out.println("Class Booked! See you soon");
                } catch (SQLException e) {
                    System.out.println("Booking Failed: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid ID");
                }

            // LOG HEALTH METRIC CODE
            } else if (choice.equals("3")) {
                try {
                    System.out.println("\nLog Health Metric");
                    System.out.print("Metric Type (Weight, Heart Rate, etc...): ");
                    String type = scanner.nextLine();
                    System.out.print("Value: ");
                    double value = Double.parseDouble(scanner.nextLine());
                    String metricSql = "INSERT INTO HealthMetrics (member_id, type, value) VALUES (?, ?, ?)";
                    PreparedStatement mStmt = conn.prepareStatement(metricSql);
                    mStmt.setInt(1, memberId);
                    mStmt.setString(2, type);
                    mStmt.setDouble(3, value);
                    mStmt.executeUpdate();
                    System.out.println("New Metric Recorded!");
                } catch (SQLException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid Number.");
                }

            // CANCEL BOOKING LOGIC
            } else if (choice.equals("4")) {
                System.out.println("\n--- Current Bookings ---");
                // Show the user thier current bookings so they know the ID
                String bookSql = "SELECT b.booking_id, fc.class_type, fc.class_date " +
                        "FROM Bookings b " +
                        "JOIN FitnessClasses fc ON b.class_id = fc.class_id " +
                        "WHERE b.member_id = ? AND fc.class_date >= CURRENT_DATE";
                PreparedStatement bStmt = conn.prepareStatement(bookSql);
                bStmt.setInt(1, memberId);
                ResultSet bRs = bStmt.executeQuery();
                boolean hasBookings = false;
                while(bRs.next()) {
                    hasBookings = true;
                    System.out.println("Booking #" + bRs.getInt("booking_id") +
                            ": " + bRs.getString("class_type") + " on " + bRs.getDate("class_date"));
                }
                // if the user has no bookings show that otherwise get the booking they want to cancel and delete that row
                if (!hasBookings) {
                    System.out.println("You have no upcoming bookings to cancel.");
                } else {
                    System.out.print("\nEnter Booking ID to Cancel: ");
                    try {
                        int bid = Integer.parseInt(scanner.nextLine());
                        String delSql = "DELETE FROM Bookings WHERE booking_id = ? AND member_id = ?";
                        PreparedStatement delStmt = conn.prepareStatement(delSql);
                        delStmt.setInt(1, bid);
                        delStmt.setInt(2, memberId);
                        int rows = delStmt.executeUpdate();
                        if (rows > 0) {
                            System.out.println("Booking Cancelled Successfully.");
                        } else {
                            System.out.println("Invalid Booking ID");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Input.");
                    }
                }
            } else if (choice.equals("5")) {
                break;
            }
        }
    }

    /**
     * TRAINER DASHBOARD FUNCTION
     * Includes features available to the trainer such as:
     *  View Schedule and Member Lookup.
     * @param trainerId
     * @throws SQLException
     */
    private static void trainerDashboard(int trainerId) throws SQLException {
        while (true) {
            System.out.println("\nTRAINER DASHBOARD\n");
            System.out.println("1. View Schedule");
            System.out.println("2. Search Member");
            System.out.println("3. Logout");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            // VIEW SCHEDULE LOGIC
            if (choice.equals("1")) {
                // get the schedule for the trainer by checking
                // which fitness classes are held by that trainer
                String sql = "SELECT fc.class_type, fc.class_date, fc.start_time, fc.end_time, r.room_number " +
                        "FROM FitnessClasses fc " +
                        "JOIN Rooms r ON fc.room_number = r.room_number " +
                        "WHERE fc.trainer_id = ? " +
                        "ORDER BY fc.class_date, fc.start_time";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, trainerId);
                ResultSet rs = stmt.executeQuery();
                // show upcoming classes
                System.out.println("\n----My Classes----\n");
                while(rs.next()) {
                    System.out.println("- " + rs.getString("class_type") + " on " + rs.getDate("class_date") + " from " + rs.getTime("start_time") + " to " + rs.getTime("end_time") + " in Room: " + rs.getString("room_number"));
                }


            // SEARCH MEMBER LOGIC
            } else if (choice.equals("2")) {
                System.out.print("Enter Member First Name: ");
                String name = scanner.nextLine();
                String sql = "SELECT * FROM Members WHERE LOWER(first_name) = LOWER(?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                ResultSet rs = stmt.executeQuery();
                boolean found = false;
                while(rs.next()) {
                    found = true;
                    int memId = rs.getInt("member_id");
                    String fName = rs.getString("first_name");
                    String lName = rs.getString("last_name");
                    String goal = rs.getString("fitness_goal");

                    // get last metric
                    String metricSql = "SELECT type, value FROM HealthMetrics WHERE member_id = ? ORDER BY date_recorded DESC LIMIT 1";
                    PreparedStatement mStmt = conn.prepareStatement(metricSql);
                    mStmt.setInt(1, memId);
                    ResultSet mRs = mStmt.executeQuery();

                    String lastMetric = "No metrics recorded";
                    if (mRs.next()) {
                        lastMetric = mRs.getString("type") + ": " + mRs.getDouble("value");
                    }
                    // print full details
                    System.out.println("-------------------------------");
                    System.out.println("Found: " + fName + " " + lName);
                    System.out.println("Goal: " + goal);
                    System.out.println("Last Metric: " + lastMetric);
                    System.out.println("-------------------------------");
                }

                if (!found) {
                    System.out.println("No member found.");
                }

            } else if (choice.equals("3")) {
                break;
            }
        }
    }


    /**
     * ADMIN DASHBOARD FUNCTION
     * Includes features available to the admin such as:
     * Class Management (Add Class), Billing (Process Payments).
     * @throws SQLException
     */
    private static void adminDashboard() throws SQLException {
        while (true) {
            System.out.println("\nADMIN DASHBOARD\n");
            System.out.println("1. Add a Class");
            System.out.println("2. Process Billing Payments");
            System.out.println("3. Exit");
            System.out.print("Choose: ");
            String choice = scanner.nextLine();

            // ADD CLASS LOGIC
            if (choice.equals("1")) {
                try {
                    System.out.print("Room Number: ");
                    String room = scanner.nextLine();
                    System.out.print("Trainer ID: ");
                    int tid = Integer.parseInt(scanner.nextLine());
                    System.out.print("Type (Yoga, HIIT, Zumba...): ");
                    String type = scanner.nextLine();
                    System.out.print("Date (YYYY-MM-DD): ");
                    Date date = Date.valueOf(scanner.nextLine());
                    System.out.print("Start Time (HH:MM:00): ");
                    Time start = Time.valueOf(scanner.nextLine());
                    System.out.print("End Time (HH:MM:00): ");
                    Time end = Time.valueOf(scanner.nextLine());

                    // This Insert will fail if the SQL Trigger detects a room conflict such as overlapping times and same rooms
                    String sql = "INSERT INTO FitnessClasses (room_number, trainer_id, class_type, class_date, start_time, end_time) VALUES (?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, room);
                    stmt.setInt(2, tid);
                    stmt.setString(3, type);
                    stmt.setDate(4, date);
                    stmt.setTime(5, start);
                    stmt.setTime(6, end);
                    stmt.executeUpdate();
                    System.out.println("Class Added!");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }

            // BILLING PROCESSING LOGIC
            } else if (choice.equals("2")) {
                System.out.println("\nPending Payments:\n");
                // Show all unpaid bookings to the admin first
                String sql = "SELECT b.booking_id, m.first_name, m.last_name, b.payment_amount " +
                        "FROM Bookings b " +
                        "JOIN Members m ON b.member_id = m.member_id " +
                        "WHERE b.billing_status = 'Pending'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                // Iterate through the database results to display each pending
                // bill and track if any records were found.
                boolean hasPending = false;
                while(rs.next()) {
                    hasPending = true;
                    System.out.println("Booking #" + rs.getInt("booking_id") + " | Member: " + rs.getString("first_name") + " " + rs.getString("last_name") + " | Amount Due: $" + rs.getDouble("payment_amount"));
                }
                // is there's no pending payments found:
                if (!hasPending) {
                    System.out.println("No pending payments found.");
                // else process a payment (pending -> paid)
                } else {
                    System.out.print("\nEnter Booking ID to mark as PAID (or 0 to cancel): ");
                    try {
                        int bid = Integer.parseInt(scanner.nextLine());
                        if (bid != 0) {
                            String paySql = "UPDATE Bookings SET billing_status = 'Paid' WHERE booking_id = ?";
                            PreparedStatement pStmt = conn.prepareStatement(paySql);
                            pStmt.setInt(1, bid);
                            int rows = pStmt.executeUpdate();
                            if (rows > 0) System.out.println("Payment Processed!");
                            else System.out.println("Booking ID not found.");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid Input.");
                    }
                }
            } else if (choice.equals("3")) {
                break;
            }
        }
    }
}