package com.example.projet_pai.dto;

import com.example.projet_pai.entite.Tag;

public class TagDTO {
    private Long id;
    private String name; // Nom du tag
    private String icon; // Icône associée au tag

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
    public static TagDTO fromEntity(Tag tag) {
        if (tag == null) {
            return null;
        }
        TagDTO dto = new TagDTO();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}