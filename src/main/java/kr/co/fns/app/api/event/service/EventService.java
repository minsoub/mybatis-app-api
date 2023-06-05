package kr.co.fns.app.api.event.service;


import kr.co.fns.app.api.country.entity.CountryData;
import kr.co.fns.app.api.country.mapper.CountryMapper;
import kr.co.fns.app.api.event.entity.EventStory;
import kr.co.fns.app.api.event.mapper.EventMapper;
import kr.co.fns.app.api.event.response.EventMessageResponse;
import kr.co.fns.app.api.event.response.EventResponse;
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
import java.util.Locale;

@Slf4j
@Service
@AllArgsConstructor
public class EventService extends CheckBaseService {

    @Autowired
    MessageSourceService me;

    private EventMapper eventMapper;

    private CountryMapper countryMapper;

    @Transactional(readOnly = true)
    public EventResponse getEventStoryList(Integer page, int eventId) {

        int pageSize = 10;
        page = (ObjectUtils.isEmpty(page) || (page < 1)) ? 1 : page;
        int startRow = ((page - 1) * pageSize);
        int totalCnt = eventMapper.getEventStoryCnt(eventId);
        int totalPage = (int) Math.ceil((double) totalCnt/pageSize);
        List<EventStory> eventStoryList = eventMapper.getEventStoryList(pageSize,startRow,eventId);

        return EventResponse.builder()
                .eventStoryList(eventStoryList)
                .totalCnt(totalCnt)
                .totalPage(totalPage)
                .build();
    }

    @Transactional
    public EventMessageResponse saveEventStory(Account account, int eventId, String integUid, String comment) {
        this.uidCheck(account.getIntegUid(),integUid);
        String userIp = account.getUserIp();
        String ip = this.extractIpFromRequest(userIp);
        CountryData countryData = countryMapper.getIpNation(ip);
        String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
        String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
        eventMapper.insertEventStory(eventId, integUid, comment, userIp, country, countryCode);
        int idx = eventMapper.getEventStoryId(integUid);
        return EventMessageResponse.builder()
                .eventStoryId(idx)
                .build();
    }

    @Transactional
    public EventMessageResponse deleteEventStory(Account account, String integUid, int storyId, int eventId) {
        this.uidCheck(account.getIntegUid(), integUid);
        Boolean isEvent = eventMapper.isEvent(storyId,integUid);
        if(!isEvent){
            throw new FantooException(ErrorCode.ERROR_FE5014);
        }
        int resultCnt = eventMapper.deleteEventStory(storyId,eventId);
        return EventMessageResponse.builder()
                .resultCnt(resultCnt)
                .build();
    }
}
