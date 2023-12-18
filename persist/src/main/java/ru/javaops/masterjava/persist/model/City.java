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
public class City extends BaseEntity {
    private @NonNull String name;

    public City(Integer id, @NonNull String name) {
        this(name);
        this.id = id;
    }

}
