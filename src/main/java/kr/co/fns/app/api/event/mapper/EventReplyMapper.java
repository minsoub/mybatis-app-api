package kr.co.fns.app.api.event.mapper;

import kr.co.fns.app.api.event.entity.EventStory;
import kr.co.fns.app.api.event.entity.EventStoryReply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface EventReplyMapper {

    List<EventStoryReply> getEventStoryReplyList(int pageSize, int startRow, int storyId);

    Boolean isEventStory(int storyId);

    void insertEventStoryReply(int storyId, String integUid, String comment, String userIp, String country, String countryCode);

    int getEventStoryReplyId(String integUid);

    Boolean isEventReply(int replyStoryId, String integUid);
    int deleteEventStoryReply(int replyStoryId, int storyId);

}