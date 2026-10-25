package models;

import java.sql.Date;

/**
 * Represents a Student entity within the University Management System (UMS).
 * Encapsulates personal demographics, academic history, guardian information, 
 * qualification metrics, document references, and financial status.
 * 
 * @author Abbas Mohammed
 * @version 1.0
 */
public class Student {
    
    // Clear core fields... (Your existing getters, setters, and getFullName() method remain pristine and fully optimized)
  
    // 1. Basic database fields
    private int studentId;
    private String studentCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String icPassportNo;
    private String personalEmail;
    private String phone;
    private String sex;
    private Date dob;
    
    // 2. Demographics & Location
    private String race;
    private String religion;
    private String maritalStatus;
    private String nationality;
    private String countryOfOrigin;
    private String addressLine;
    private String postcode;
    private String city;
    private String state;
    private String country;
    
    // 3. Academic Details
    private String campus;
    private String studyMode;
    private Date enrollmentDate;
    private int enrollmentYear;
    private Date graduationDate;
    private int majorId;
    private String majorName;
    
    // 4. Advisor Details
    private String advisorName;
    private String advisorEmail;
    private String advisorPhone;
    
    // 5. Guardian Details
    private String guardianName;
    private String guardianEmail;
    private String guardianPhone;
    private String guardianRelation;
    private String guardianAddress;
    
    // 6. Highest Qualification
    private String highestQualification;
    private String hqResultCgpa;
    private String hqInstitution;
    private String hqProgramme;
    private int hqGradYear;
    private boolean hqIsCompleted;
    private String topSubjects;
    
    // 7. Documents
    private String docIcPassport;
    private String docPaymentReceipt;
    private String docEnglishCert;
    private String docQualification;
    private String docOfferLetter;
    private String docConfirmationLetter;
    
    // 8. Financial & Status
    private String financialStatus;
    private String financialNotes;
    private String profileImage;
    private String status;
    private String leaveReason;
    private Date leaveDate;
    private String lastAcademicStage;

    // Default Constructor
    public Student() {
    }

    // --- Getters and Setters ---

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }

    public String getStudentCode() { return studentCode; }
    public void setStudentCode(String studentCode) { this.studentCode = studentCode; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getMiddleName() { return middleName; }
    public void setMiddleName(String middleName) { this.middleName = middleName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFullName() {
        String fullName = firstName;
        if (middleName != null && !middleName.isEmpty()) fullName += " " + middleName;
        if (lastName != null && !lastName.isEmpty()) fullName += " " + lastName;
        return fullName;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getIcPassportNo() { return icPassportNo; }
    public void setIcPassportNo(String icPassportNo) { this.icPassportNo = icPassportNo; }

    public String getPersonalEmail() { return personalEmail; }
    public void setPersonalEmail(String personalEmail) { this.personalEmail = personalEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }

    public Date getDob() { return dob; }
    public void setDob(Date dob) { this.dob = dob; }

    public String getRace() { return race; }
    public void setRace(String race) { this.race = race; }

    public String getReligion() { return religion; }
    public void setReligion(String religion) { this.religion = religion; }

    public String getMaritalStatus() { return maritalStatus; }
    public void setMaritalStatus(String maritalStatus) { this.maritalStatus = maritalStatus; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public String getCountryOfOrigin() { return countryOfOrigin; }
    public void setCountryOfOrigin(String countryOfOrigin) { this.countryOfOrigin = countryOfOrigin; }

    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }

    public String getPostcode() { return postcode; }
    public void setPostcode(String postcode) { this.postcode = postcode; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCampus() { return campus; }
    public void setCampus(String campus) { this.campus = campus; }

    public String getStudyMode() { return studyMode; }
    public void setStudyMode(String studyMode) { this.studyMode = studyMode; }

    public Date getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(Date enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public int getEnrollmentYear() { return enrollmentYear; }
    public void setEnrollmentYear(int enrollmentYear) { this.enrollmentYear = enrollmentYear; }

    public Date getGraduationDate() { return graduationDate; }
    public void setGraduationDate(Date graduationDate) { this.graduationDate = graduationDate; }

    public int getMajorId() { return majorId; }
    public void setMajorId(int majorId) { this.majorId = majorId; }

    public String getMajorName() { return majorName; }
    public void setMajorName(String majorName) { this.majorName = majorName; }

    public String getAdvisorName() { return advisorName; }
    public void setAdvisorName(String advisorName) { this.advisorName = advisorName; }

    public String getAdvisorEmail() { return advisorEmail; }
    public void setAdvisorEmail(String advisorEmail) { this.advisorEmail = advisorEmail; }

    public String getAdvisorPhone() { return advisorPhone; }
    public void setAdvisorPhone(String advisorPhone) { this.advisorPhone = advisorPhone; }

    public String getGuardianName() { return guardianName; }
    public void setGuardianName(String guardianName) { this.guardianName = guardianName; }

    public String getGuardianEmail() { return guardianEmail; }
    public void setGuardianEmail(String guardianEmail) { this.guardianEmail = guardianEmail; }

    public String getGuardianPhone() { return guardianPhone; }
    public void setGuardianPhone(String guardianPhone) { this.guardianPhone = guardianPhone; }

    public String getGuardianRelation() { return guardianRelation; }
    public void setGuardianRelation(String guardianRelation) { this.guardianRelation = guardianRelation; }

    public String getGuardianAddress() { return guardianAddress; }
    public void setGuardianAddress(String guardianAddress) { this.guardianAddress = guardianAddress; }

    public String getHighestQualification() { return highestQualification; }
    public void setHighestQualification(String highestQualification) { this.highestQualification = highestQualification; }

    public String getHqResultCgpa() { return hqResultCgpa; }
    public void setHqResultCgpa(String hqResultCgpa) { this.hqResultCgpa = hqResultCgpa; }

    public String getHqInstitution() { return hqInstitution; }
    public void setHqInstitution(String hqInstitution) { this.hqInstitution = hqInstitution; }

    public String getHqProgramme() { return hqProgramme; }
    public void setHqProgramme(String hqProgramme) { this.hqProgramme = hqProgramme; }

    public int getHqGradYear() { return hqGradYear; }
    public void setHqGradYear(int hqGradYear) { this.hqGradYear = hqGradYear; }

    public boolean isHqIsCompleted() { return hqIsCompleted; }
    public void setHqIsCompleted(boolean hqIsCompleted) { this.hqIsCompleted = hqIsCompleted; }

    public String getTopSubjects() { return topSubjects; }
    public void setTopSubjects(String topSubjects) { this.topSubjects = topSubjects; }

    public String getDocIcPassport() { return docIcPassport; }
    public void setDocIcPassport(String docIcPassport) { this.docIcPassport = docIcPassport; }

    public String getDocPaymentReceipt() { return docPaymentReceipt; }
    public void setDocPaymentReceipt(String docPaymentReceipt) { this.docPaymentReceipt = docPaymentReceipt; }

    public String getDocEnglishCert() { return docEnglishCert; }
    public void setDocEnglishCert(String docEnglishCert) { this.docEnglishCert = docEnglishCert; }

    public String getDocQualification() { return docQualification; }
    public void setDocQualification(String docQualification) { this.docQualification = docQualification; }

    public String getDocOfferLetter() { return docOfferLetter; }
    public void setDocOfferLetter(String docOfferLetter) { this.docOfferLetter = docOfferLetter; }

    public String getDocConfirmationLetter() { return docConfirmationLetter; }
    public void setDocConfirmationLetter(String docConfirmationLetter) { this.docConfirmationLetter = docConfirmationLetter; }

    public String getFinancialStatus() { return financialStatus; }
    public void setFinancialStatus(String financialStatus) { this.financialStatus = financialStatus; }

    public String getFinancialNotes() { return financialNotes; }
    public void setFinancialNotes(String financialNotes) { this.financialNotes = financialNotes; }

    public String getProfileImage() { return profileImage; }
    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getLeaveReason() { return leaveReason; }
    public void setLeaveReason(String leaveReason) { this.leaveReason = leaveReason; }

    public Date getLeaveDate() { return leaveDate; }
    public void setLeaveDate(Date leaveDate) { this.leaveDate = leaveDate; }

    public String getLastAcademicStage() { return lastAcademicStage; }
    public void setLastAcademicStage(String lastAcademicStage) { this.lastAcademicStage = lastAcademicStage; }
}
