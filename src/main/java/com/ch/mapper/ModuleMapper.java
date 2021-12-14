package com.ch.mapper;

import com.ch.base.BaseMapper;
import com.ch.bean.Module;
import com.ch.dto.TreeDto;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    List<TreeDto> queryAllModules();

    List<Module> queryModules();

    Module queryModuleByGradeAndModuleName(Integer grade, String moduleName);

    Module queryModuleByGradeAndUrl(Integer grade, String url);

    Module queryModuleByOptValue(String optValue);

    @MapKey("")
    List<Map<String, Object>> queryAllModulesByGrade(Integer grade);

    int countSubModuleByParentId(Integer mid);
}