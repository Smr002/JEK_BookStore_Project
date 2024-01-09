package source.Model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class User {

    private String username;
    private String password;
    private String name;
    private String birthday;
    private String phone;
    private String email;
    private String role;

    public User(String role, String username, String password, String name, String birthday, String phone, String email,
            String salary, String accessLevel) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.name = name;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.salary = salary;
        this.accessLevel = accessLevel;

    }

    public User(String username, String password) {

        this.username = username;
        this.password = password;
    }

    public User(String role, String username, String password) {
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String Login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/User.txt"))) {

            String header = reader.readLine();
            String[] columns = header.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length == columns.length) {
                    String type1 = values[0].trim();
                    String username1 = values[1].trim();
                    String password1 = values[2].trim();
                    String name = values[3].trim();
                    String birthday = values[4].trim();
                    String phone = values[5].trim();
                    String email = values[6].trim();
                    String salary = values[7].trim();
                    String option = values[8].trim();

                    if (username.equals(username1) && password.equals(password1)) {
                        return type1;
                    }
                }
            }
        } catch (FileNotFoundException e) {
          
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Doesn't work";
    }

    public void Logout() {

    };

}
