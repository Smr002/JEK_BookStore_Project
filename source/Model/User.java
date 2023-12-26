package source.Model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;


public  class User {

    private String username;
    private String password;
    private String role;

    public User(String username, String password) {

        this.username = username;
        this.password = password;
       
    }

   
    public User(String role,String username, String password) {

        this.username = username;
        this.password = password;
        this.role = role;
    }

    public   String getUsername() {
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
     public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public  String Login() {
        try (BufferedReader reader = new BufferedReader(new FileReader("files/User.txt"))) {

            String header = reader.readLine();
            String[] columns = header.split(",");

            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                if (values.length == columns.length) {
                    String type1 = values[0].trim();
                    String username1= values[1].trim();
                    String password1= values[2].trim();

                    if(username.equals(username1) && password.equals(password1)){
                     

                        return type1;
                         

                    }
                } else {
                    System.err.println("Invalid data: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Try Again";

    };

    public void Logout() {

    };

}
