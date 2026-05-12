package com.project.artconnect.service;

import com.project.artconnect.model.Discipline;

import java.util.List;
import java.util.Optional;

public interface DisciplineService {
    List<Discipline> getAllDisciplines();
    Optional<Discipline> getDisciplineByName(String name);
}