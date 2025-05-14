package com.example.projet_pai.controller;

import com.example.projet_pai.entite.Tag;
import com.example.projet_pai.service.TagServiceItf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    @Autowired
    private TagServiceItf tagService;

    @GetMapping
    public List<Tag> getAllTags() {
        return tagService.findAll();
    }
}