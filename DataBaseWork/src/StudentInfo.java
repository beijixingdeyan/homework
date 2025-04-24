public class StudentInfo {
    private String studentId;
    private String name;
    private String gender;
    private String birthday;
    private String nativePlace;
    private String department;
    private String className;

    public StudentInfo(String studentId, String name, String gender, String birthday, String nativePlace, String department, String className) {
        this.studentId = studentId;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.nativePlace = nativePlace;
        this.department = department;
        this.className = className;
    }

    // Getter 和 Setter 方法
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public String getNativePlace() { return nativePlace; }
    public void setNativePlace(String nativePlace) { this.nativePlace = nativePlace; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getClassName() { return className; }
    public void setClassName(String className) { this.className = className; }
}