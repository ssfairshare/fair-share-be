package me.shaneslone.fairshare.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userroles")
@IdClass(UserRolesId.class)
public class UserRoles extends Auditable implements Serializable {
    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "userId")
    @JsonIgnoreProperties(value = "roles", allowSetters = true)
    private User user;

    @Id
    @ManyToOne
    @NotNull
    @JoinColumn(name = "roleId")
    @JsonIgnoreProperties(value = "users", allowSetters = true)
    private Role role;

    public UserRoles() {
    }

    public UserRoles(User user, Role role) {
        this.user = user;
        this.role = role;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if (!(o instanceof UserRoles))
        {
            return false;
        }
        UserRoles that = (UserRoles) o;
        return ((user == null) ? 0 : user.getUserId()) == ((that.user == null) ? 0 : that.user.getUserId()) &&
                ((role == null) ? 0 : role.getRoleId()) == ((that.role == null) ? 0 : that.role.getRoleId());
    }
    @Override
    public int hashCode()
    {
        return 37;
    }
}
