package dao;

import models.Student;
import project1.studentenrollmentsystem.DatabaseConnection;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * StudentDAO Data Access Object (DAO) handles communication between the Student objects
 * and the database.
 *
 * It allows the application to register students and retrieve
 * students based on their academic major.
 *
 * @author Abbas Mohammed
 *
 */
public class StudentDAO {

    // Logger is used to record database errors and other important events.
    private static final Logger LOGGER =
            Logger.getLogger(StudentDAO.class.getName());

    /**
     * Registers a new student in the database.
     *
     * First, the student is saved in the students table.
     * Then, a user account is created in the users table.
     *
     * Both operations use one transaction, so either both succeed
     * or the database changes are rolled back.
     *
     * @param student The Student object containing the student's details.
     * @return "SUCCESS" if registration succeeds, or an error message.
     */
    public String registerStudent(Student student) {

        // SQL query to save the student's personal information.
        // The question marks are placeholders for values added later.
        String sqlStudent = "INSERT INTO students " +
                "(first_name, middle_name, last_name, email, sex, dob) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        // SQL query to create a user account linked to the new student.
        String sqlUser = "INSERT INTO users (email, role, student_id) " +
                "VALUES (?, 'student', ?)";

        // Stores the database connection so it can be closed later.
        Connection conn = null;

        try {
            // Open a connection to the database.
            conn = DatabaseConnection.getConnection();

            // Turn off automatic commits so we can control the transaction.
            conn.setAutoCommit(false);

            // This variable will store the ID generated for the new student.
            int newStudentId = 0;

            // STEP 1: Insert the student's details into the students table.
            // RETURN_GENERATED_KEYS allows us to retrieve the new student ID.
            try (PreparedStatement pstmtStudent =
                    conn.prepareStatement(
                            sqlStudent,
                            Statement.RETURN_GENERATED_KEYS)) {

                // Fill in the question marks with the student's details.
                pstmtStudent.setString(1, student.getFirstName());
                pstmtStudent.setString(2, student.getMiddleName());
                pstmtStudent.setString(3, student.getLastName());
                pstmtStudent.setString(4, student.getEmail());
                pstmtStudent.setString(5, student.getSex());
                pstmtStudent.setDate(6, student.getDob());

                // Execute the INSERT query.
                // The returned number tells us how many rows were inserted.
                int affectedRows = pstmtStudent.executeUpdate();

                // If no row was inserted, cancel the transaction.
                if (affectedRows == 0) {
                    conn.rollback();
                    return "Error: Failed to insert student data.";
                }

                // Retrieve the automatically generated student ID.
                try (ResultSet generatedKeys =
                        pstmtStudent.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        // Save the generated ID for use in the users table.
                        newStudentId = generatedKeys.getInt(1);
                    } else {
                        // Cancel the transaction if no ID was generated.
                        conn.rollback();
                        return "Error: Failed to obtain student ID.";
                    }
                }
            }

            // STEP 2: Create a user account for the new student.
            // The student ID links this account to the students table.
            try (PreparedStatement pstmtUser =
                    conn.prepareStatement(sqlUser)) {

                // Set the email for the new user account.
                pstmtUser.setString(1, student.getEmail());

                // Link the user account to the newly created student.
                pstmtUser.setInt(2, newStudentId);

                // Execute the INSERT query for the users table.
                pstmtUser.executeUpdate();
            }

            // Both database operations succeeded, so save the changes.
            conn.commit();

            // Tell the calling code that registration was successful.
            return "SUCCESS";

        } catch (SQLException e) {

            // If a database error occurs, undo the transaction's changes.
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    // Record an error if the rollback itself fails.
                    LOGGER.log(
                            Level.SEVERE,
                            "Failed to roll back transaction",
                            ex);
                }
            }

            // Return a more helpful message if the email already exists.
            if (e.getMessage() != null &&
                    e.getMessage().contains("Duplicate entry")) {
                return "Error: Email already exists!";
            }

            // Return the database error message for other SQL errors.
            return "Error: " + e.getMessage();

        } finally {

            // Always attempt to restore the default connection setting
            // and close the connection, even if an error occurs.
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException ex) {
                    // Record an error if closing the connection fails.
                    LOGGER.log(
                            Level.SEVERE,
                            "Failed to close database connection",
                            ex);
                }
            }
        }
    }

    /**
     * Retrieves all active students who belong to a specific major.
     *
     * The database records are converted into Student objects
     * and returned as a list.
     *
     * @param majorId The ID of the academic major.
     * @return A list containing the matching active students.
     * @throws SQLException if a database error occurs.
     */
    public List<Student> getStudentsByMajor(int majorId)
            throws SQLException {

        // Create an empty list to store the students retrieved from the DB.
        List<Student> students = new ArrayList<>();

        // Select active students for the requested major.
        // ORDER BY sorts the results alphabetically by first name.
        String sql = "SELECT * FROM students " +
                "WHERE major_id = ? AND status = 'Active' " +
                "ORDER BY first_name";

        // Open the connection and prepare the SQL query.
        // try-with-resources automatically closes these resources.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Replace the question mark with the requested major ID.
            pstmt.setInt(1, majorId);

            // Execute the SELECT query and receive the database results.
            try (ResultSet rs = pstmt.executeQuery()) {

                // Process each student record returned by the query.
                while (rs.next()) {

                    // Create a new Student object for the current row.
                    Student s = new Student();

                    // Copy personal and contact information from the DB.
                    s.setStudentId(rs.getInt("student_id"));
                    s.setStudentCode(rs.getString("student_code"));
                    s.setFirstName(rs.getString("first_name"));
                    s.setMiddleName(rs.getString("middle_name"));
                    s.setLastName(rs.getString("last_name"));
                    s.setEmail(rs.getString("email"));
                    s.setPhone(rs.getString("phone"));
                    s.setSex(rs.getString("sex"));
                    s.setDob(rs.getDate("dob"));

                    // Copy academic information and current status.
                    s.setMajorId(rs.getInt("major_id"));
                    s.setFinancialStatus(
                            rs.getString("financial_status"));
                    s.setStatus(rs.getString("status"));

                    // Add the completed Student object to the list.
                    students.add(s);
                }
            }
        }

        // Return all matching students to the calling code.
        return students;
    }
}
