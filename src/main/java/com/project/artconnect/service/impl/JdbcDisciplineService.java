package com.project.artconnect.service.impl;

import com.project.artconnect.dao.impl.DisciplineDaoImpl;
import com.project.artconnect.model.Discipline;
import com.project.artconnect.service.DisciplineService;

import java.util.List;
import java.util.Optional;

public class JdbcDisciplineService implements DisciplineService {

    private final DisciplineDaoImpl disciplineDao;

    public JdbcDisciplineService(DisciplineDaoImpl disciplineDao) {
        this.disciplineDao = disciplineDao;
    }

    @Override
    public List<Discipline> getAllDisciplines() {
        return disciplineDao.findAll();
    }

    @Override
    public Optional<Discipline> getDisciplineByName(String name) {
        return disciplineDao.findAll()
                .stream()
                .filter(d -> d.getName().equalsIgnoreCase(name))
                .findFirst();
    }
}