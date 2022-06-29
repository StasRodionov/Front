package com.trade_accounting.services.interfaces.util;

import com.trade_accounting.models.dto.util.ProjectDto;

import java.util.List;
import java.util.Map;

public interface ProjectService {

    ProjectDto getById(Long id);

    void create(ProjectDto projectDto);

    void update(ProjectDto projectDto);

    void deleteById(Long id);

    List<ProjectDto> getAll();

    List<ProjectDto> findBySearch(String search);

    List<ProjectDto> search(Map<String, String> query);

}
