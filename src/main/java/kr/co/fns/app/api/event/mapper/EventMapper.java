package kr.co.fns.app.api.event.mapper;

import kr.co.fns.app.api.event.entity.EventStory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventMapper {
    int getEventStoryCnt(int eventId);

    List<EventStory> getEventStoryList(int pageSize, int startRow, int eventId);

    void insertEventStory(int eventId, String integUid, String comment, String userIp, String country, String countryCode);

    int getEventStoryId(String integUid);

    Boolean isEvent(int storyId, String integUid);

    int deleteEventStory(int storyId, int eventId);

}