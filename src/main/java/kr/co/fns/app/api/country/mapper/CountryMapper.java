package kr.co.fns.app.api.country.mapper;

import kr.co.fns.app.api.country.entity.CountryData;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CountryMapper {

    public CountryData getIpNation(String ip);

}