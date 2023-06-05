package kr.co.fns.app.api.fanit.mapper;

import kr.co.fns.app.api.fanit.entity.FanitStory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface FanitStoryMapper {

    List<FanitStory> getFanitStoryList(int pageSize, int startRow, int fanitId);

    Boolean isFanitList(int fanitId);

    void insertFanitStory(int fanitId, String integUid, String comment, String userIp, String country, String countryCode);

    int getFanitStoryId(String integUid);

    Boolean isFanitStory(int storyId, String integUid);

    int deleteFanitStory(int storyId, int fanitId);

}