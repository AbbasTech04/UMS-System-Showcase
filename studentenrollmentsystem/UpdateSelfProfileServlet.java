/*

UpdateSelfProfileServlet
Handles self-service profile updates for students, lecturers, and administrative users.
The servlet processes submitted profile information, manages optional profile image uploads,
and persists the updated data to the corresponding database table based on the authenticated user's role.
Profile images are organized into year-based directories, while relative file paths
are stored in the database to maintain separation between application data and file storage.
Upon completion, users are redirected to their respective profile pages with
an appropriate success or error status. */

package controllers;

import project1.studentenrollmentsystem.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

@WebServlet("/UpdateSelfProfileServlet")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 1,
    maxFileSize = 1024 * 1024 * 5,
    maxRequestSize = 1024 * 1024 * 10
)
public class UpdateSelfProfileServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession();

        String sessionEmail = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");

        // Profile updates are restricted to authenticated sessions, while role-specific access is handled below.
        if (sessionEmail == null || role == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // Resolve storage, persistence, and navigation targets from the authenticated user's role.
        String baseUploadPath = "";
        String tableName = "";
        String idColumn = "";
        String redirectPage = "";

        if (role.equals("student")) {
            baseUploadPath = "D:/UMSuploadsStudents";
            tableName = "students";
            idColumn = "student_id";
            redirectPage = "StudentProfileServlet";
        } else if (role.equals("lecturer")) {
            baseUploadPath = "D:/UMSuploadsLecturers";
            tableName = "lecturers";
            idColumn = "lecturer_id";
            redirectPage = "LecturerProfileServlet";
        } else if (role.equals("superadmin") || role.equals("admin")) {
            baseUploadPath = "D:/UMSuploadsAdmins";
            tableName = "users";
            idColumn = "user_id";
            redirectPage = "AdminProfileServlet";
        } else {
            // Reject unsupported roles instead of allowing them to select an unintended update target.
            response.sendRedirect("login.jsp");
            return;
        }

        // Extract editable profile attributes independently of the user's authentication identity.
        String personalEmail = request.getParameter("personalEmail");
        String phone = request.getParameter("phone");
        String addressLine = request.getParameter("addressLine");
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String postcode = request.getParameter("postcode");
        String maritalStatus = request.getParameter("maritalStatus");

        String dbImagePath = null;
        boolean hasImage = false;

        try {
            // Treat the profile image as an optional update so existing images remain unchanged when no file is submitted.
            Part filePart = request.getPart("profileImage");

            if (filePart != null && filePart.getSize() > 0) {

                // Extract the submitted filename and retain its extension for the stored copy.
                String originalName = Paths.get(filePart.getSubmittedFileName())
                        .getFileName().toString();
                String ext = originalName.contains(".")
                        ? originalName.substring(originalName.lastIndexOf("."))
                        : "";

                // Partition uploads by year to avoid accumulating every profile image in a single directory.
                String currentYear = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
                String relativeFolder = "Images/" + currentYear;

                File uploadDir = new File(baseUploadPath + "/" + relativeFolder);

                // Create the year-specific directory on demand.
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Use a timestamp-based filename to minimize collisions between concurrent uploads.
                String fileName = role + "_" + System.currentTimeMillis() + ext;

                // Persist a relative path rather than a machine-specific absolute path, keeping database references independent of the storage root.
                dbImagePath = relativeFolder + "/" + fileName;

                // Store the uploaded content and replace any existing file with the same generated name.
                Files.copy(
                    filePart.getInputStream(),
                    Paths.get(baseUploadPath + "/" + dbImagePath),
                    StandardCopyOption.REPLACE_EXISTING
                );

                hasImage = true;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {

                // Resolve the role-specific record ID through the shared users table using the authenticated session email.
                int specificId = -1;
                String queryId = "SELECT " + idColumn + " FROM users WHERE email = ?";

                try (PreparedStatement psUser = conn.prepareStatement(queryId)) {
                    psUser.setString(1, sessionEmail);

                    try (ResultSet rsUser = psUser.executeQuery()) {
                        if (rsUser.next()) {
                            specificId = rsUser.getInt(idColumn);
                        }
                    }
                }

                // Prefer the resolved primary key; retain the email-based condition as a fallback when no ID is found.
                // The image column is included only when a new image was successfully stored.
                String sql = (specificId > 0)
                    ? "UPDATE " + tableName
                        + " SET personal_email=?, phone=?, address_line=?, city=?, state=?, postcode=?, marital_status=? "
                        + (hasImage ? ", profile_image=?" : "")
                        + " WHERE " + idColumn + "=?"
                    : "UPDATE " + tableName
                        + " SET personal_email=?, phone=?, address_line=?, city=?, state=?, postcode=?, marital_status=? "
                        + (hasImage ? ", profile_image=?" : "")
                        + " WHERE email=?";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {

                    // Bind profile attributes in the same order as their corresponding SQL placeholders.
                    ps.setString(1, personalEmail);
                    ps.setString(2, phone);
                    ps.setString(3, addressLine);
                    ps.setString(4, city);
                    ps.setString(5, state);
                    ps.setString(6, postcode);
                    ps.setString(7, maritalStatus);

                    // Advance the parameter index conditionally to keep the WHERE parameter aligned with the generated SQL.
                    int pIndex = 8;

                    if (hasImage) {
                        ps.setString(pIndex++, dbImagePath);
                    }

                    // Use the primary key when available; otherwise, identify the target record by its email.
                    if (specificId > 0) {
                        ps.setInt(pIndex, specificId);
                    } else {
                        ps.setString(pIndex, sessionEmail);
                    }

                    ps.executeUpdate();
                }

                // Return the user to the profile endpoint responsible for loading their role-specific data.
                response.sendRedirect(redirectPage + "?msg=Profile+Updated+Successfully");
            }

        } catch (Exception e) {
            // Preserve the exception details for server-side diagnosis without exposing internal errors to the client.
            e.printStackTrace();

            response.sendRedirect(redirectPage + "?error=Database+Error+While+Updating");
        }
    }
}
