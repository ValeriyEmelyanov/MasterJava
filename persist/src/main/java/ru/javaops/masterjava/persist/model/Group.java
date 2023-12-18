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
public class Group extends BaseEntity {
    private @NonNull String name;

    public Group(Integer id, @NonNull String name) {
        this(name);
        this.id = id;
    }

}
