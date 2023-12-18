package ru.javaops.masterjava.persist.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Project extends BaseEntity {
    private @NonNull String name;
    private String description;

    public Project(Integer id, @NonNull String name, String description) {
        this(name);
        this.id = id;
        this.description = description;
    }

    public Project(@NonNull String name, String description) {
        this.name = name;
        this.description = description;
    }
}
