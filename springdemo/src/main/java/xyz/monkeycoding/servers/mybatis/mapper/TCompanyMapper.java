package xyz.monkeycoding.servers.mybatis.mapper;

import java.util.List;
import xyz.monkeycoding.servers.mybatis.model.TCompany;

public interface TCompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TCompany record);

    TCompany selectByPrimaryKey(Integer id);

    List<TCompany> selectAll();

    int updateByPrimaryKey(TCompany record);
}