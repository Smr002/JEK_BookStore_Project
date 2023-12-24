package source.Model;
import java.util.ArrayList;
import java.util.List;
public class Users {

    private final List<Permission> permissions;

    public Users() {
        this.permissions = new ArrayList<>();
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean hasPermission(Permission requiredPermission) {
        return permissions.contains(requiredPermission);
    }
}
