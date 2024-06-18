package org.acme;

public class OperatorDto {

    private Integer id;
    private String name;

    public OperatorDto(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters e setters
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
}
