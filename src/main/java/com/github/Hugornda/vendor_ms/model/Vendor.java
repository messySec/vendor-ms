package com.github.Hugornda.vendor_ms.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("VENDORS")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Vendor {
    @Id
    public long id;
    public String name;
    @Column(value = "n_employees")
    private Integer numberOfEmployees;
    private String country;

    public Vendor(String name, Integer numberOfEmployees, String country) {
        this.name = name;
        this.numberOfEmployees = numberOfEmployees;
        this.country = country;
    }
}
