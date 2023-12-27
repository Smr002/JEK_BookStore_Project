package source.Model;

public class employees {
    private String name;
    private String birthday;
    private String phone;
    private String email;
    private String role;
    

    public employees(String name, String birthday, String phone, String email, String salary, String accessLevel,String role) {
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.accessLevel = accessLevel;
        this.role = role;
    }

    private String salary;

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    private String accessLevel;

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
       public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
