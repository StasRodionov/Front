package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.ProjectDto;

import java.util.List;

public interface ProjectService {

    List<ProjectDto> getAll();

    ProjectDto getById(Long id);

    void create(ProjectDto projectDto);

    void update(ProjectDto projectDto);

    void deleteById(Long id);
}
