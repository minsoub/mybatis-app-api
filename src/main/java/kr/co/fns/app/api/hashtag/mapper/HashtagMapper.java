package kr.co.fns.app.api.hashtag.mapper;

import kr.co.fns.app.api.hashtag.entity.Hashtag;
import kr.co.fns.app.api.hashtag.dto.HashtagDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface HashtagMapper {

//    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.MASTER)
    void insertHashtag(List<HashtagDto> hashtagDtoList);

//    @SetDataSource(dataSourceType = SetDataSource.DataSourceType.SLAVE)
    List<Hashtag> getHashtag(List<HashtagDto> hashtagDtoList);

}
