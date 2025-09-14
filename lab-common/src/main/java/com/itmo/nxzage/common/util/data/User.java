package com.itmo.nxzage.common.util.data;

import java.io.Serializable;

public class User implements Comparable<User>, Serializable {
    private Integer id;
    private String name;

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(User other) {
        return this.id.compareTo(other.id);
    }

    @Override
    public String toString() {
        return "USER[id: %d, name: '%s']".formatted(id, name);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other)
            return true;
        if (!(other instanceof User))
            return false;
        User user = (User) other;
        return id.equals(user.id) && name.equals(user.name);
    }

    @Override
    public int hashCode() {
        final int mod = 31;
        int hash = mod;
        hash = hash * mod + (id != null ? id.hashCode() : 0);
        hash = hash * mod + (name != null ? name.hashCode() : 0);
        return hash;
    }
}
