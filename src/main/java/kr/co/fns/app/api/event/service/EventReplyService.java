package kr.co.fns.app.api.event.service;


import kr.co.fns.app.api.country.entity.CountryData;
import kr.co.fns.app.api.country.mapper.CountryMapper;
import kr.co.fns.app.api.event.entity.EventStoryReply;
import kr.co.fns.app.api.event.mapper.EventReplyMapper;
import kr.co.fns.app.api.event.response.EventMessageResponse;
import kr.co.fns.app.api.event.response.EventReplyResponse;
import kr.co.fns.app.base.service.CheckBaseService;
import kr.co.fns.app.base.service.MessageSourceService;
import kr.co.fns.app.config.resolver.Account;
import kr.co.fns.app.core.aop.exception.ErrorCode;
import kr.co.fns.app.core.aop.exception.custom.FantooException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventReplyService extends CheckBaseService {

    @Autowired
    MessageSourceService me;

    private EventReplyMapper eventReplyMapper;

    private CountryMapper countryMapper;

    @Transactional(readOnly = true)
    public EventReplyResponse getEventStoryReplyList(Integer page, int storyId) {

        int pageSize = 10;
        page = (ObjectUtils.isEmpty(page) || (page < 1)) ? 1 : page;
        int startRow = ((page - 1) * pageSize);
        List<EventStoryReply> eventStoryReplyList = eventReplyMapper.getEventStoryReplyList(pageSize,startRow,storyId);

        return EventReplyResponse.builder()
                .eventStoryReplyList(eventStoryReplyList)
                .build();
    }

    @Transactional
    public EventMessageResponse saveEventStoryReply(Account account, int storyId, String integUid, String comment) {
        this.uidCheck(account.getIntegUid(),integUid);
        Boolean isEventStory = eventReplyMapper.isEventStory(storyId);
        if(!isEventStory){
            throw new FantooException(ErrorCode.ERROR_FE5014);
        }
        var userIp = account.getUserIp();
        String ip = this.extractIpFromRequest(userIp);
        CountryData countryData = countryMapper.getIpNation(ip);
        String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
        String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
        eventReplyMapper.insertEventStoryReply(storyId,integUid,comment,userIp,country,countryCode);
        int idx = eventReplyMapper.getEventStoryReplyId(integUid);
        return EventMessageResponse.builder()
                .eventStoryReplyId(idx)
                .build();
    }

    @Transactional
    public EventMessageResponse deleteEventStoryReply(Account account, String integUid,int replyStoryId, int storyId) {
        this.uidCheck(account.getIntegUid(), integUid);
        Boolean isEventReply = eventReplyMapper.isEventReply(replyStoryId,integUid);
        if(!isEventReply){
            throw new FantooException(ErrorCode.ERROR_FE5014);
        }
        int resultCnt = eventReplyMapper.deleteEventStoryReply(replyStoryId,storyId);
        return EventMessageResponse.builder()
                .resultCnt(resultCnt)
                .build();
    }
}
