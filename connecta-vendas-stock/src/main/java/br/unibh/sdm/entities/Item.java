package br.unibh.sdm.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @Column(length = 50, unique = true)
    @Size(min = 1, max = 50)
    private String code;

    @NotBlank
    @Column(length = 100)
    @Size(min = 3, max = 100)
    private String name;

    @Column(length = 255)
    @Size(max = 255)
    private String description;

    @NotNull
    private Integer quantity;

    @NotNull
    private Double price;

    public Item() {
        super();
        this.code = UUID.randomUUID().toString();
    }

    public Item(String name, String description, Integer quantity, Double price) {
        this();
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Item [code=" + code + ", name=" + name + ", description=" + description
                + ", quantity=" + quantity + ", price=" + price + "]";
    }
}
