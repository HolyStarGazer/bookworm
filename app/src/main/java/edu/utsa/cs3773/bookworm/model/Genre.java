package edu.utsa.cs3773.bookworm.model;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Genre {
    private final int id;
    private final String name;
    private final String description;

    public Genre (int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Genre (int id, String name) {
        this.id = id;
        this.name = name;
        this.description = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof Genre))
            return false;

        Genre genre = (Genre) o;

        return id == genre.id && Objects.equals(name, genre.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @NonNull
    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", name'" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
