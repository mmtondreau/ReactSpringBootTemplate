package org.tonberry.calories.calorieserver.persistence;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "version", schema = "stocks")
@AllArgsConstructor
@NoArgsConstructor
public class VersionEntity {
    @Id
    @Column(name = "version_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long version_id;

    @Column(name = "name")
    private String name;

    public long getVersion_id() {
        return version_id;
    }

    public void setVersion_id(long version_id) {
        this.version_id = version_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
