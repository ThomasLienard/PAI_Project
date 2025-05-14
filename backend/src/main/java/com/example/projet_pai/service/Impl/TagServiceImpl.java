package com.example.projet_pai.service.Impl;

import com.example.projet_pai.repository.TagRepository;
import com.example.projet_pai.service.TagServiceItf;
import com.example.projet_pai.entite.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagServiceImpl implements TagServiceItf {
    @Autowired
    private TagRepository tagRepository;

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }
}
