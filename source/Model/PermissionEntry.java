package source.Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class PermissionEntry {
    private final SimpleStringProperty permission;
    private final SimpleStringProperty username;
    private final SimpleBooleanProperty verify;

    public PermissionEntry(String permission, String username, boolean verify) {
        this.permission = new SimpleStringProperty(permission);
        this.username = new SimpleStringProperty(username);
        this.verify = new SimpleBooleanProperty(verify);
    }

    public String getPermission() {
        return permission.get();
    }

    public String getUsername() {
        return username.get();
    }

    public boolean isVerify() {
        return verify.get();
    }
}
