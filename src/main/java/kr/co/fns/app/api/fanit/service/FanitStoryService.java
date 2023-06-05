package kr.co.fns.app.api.fanit.service;


import kr.co.fns.app.api.country.entity.CountryData;
import kr.co.fns.app.api.country.mapper.CountryMapper;
import kr.co.fns.app.api.fanit.entity.FanitStory;
import kr.co.fns.app.api.fanit.mapper.FanitStoryMapper;
import kr.co.fns.app.api.fanit.model.response.FanitMessageResponse;
import kr.co.fns.app.api.fanit.model.response.FanitStoryResponse;
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
public class FanitStoryService extends CheckBaseService {

    @Autowired
    MessageSourceService me;

    private FanitStoryMapper fanitStoryMapper;

    private CountryMapper countryMapper;

    @Transactional(readOnly = true)
    public FanitStoryResponse getFanitStoryList(int page, int fanitId) {

        int pageSize = 100;
        page = (ObjectUtils.isEmpty(page) || (page < 1)) ? 1 : page;
        int startRow = ((page - 1) * pageSize);
        List<FanitStory> fanitStoryList = fanitStoryMapper.getFanitStoryList(pageSize,startRow,fanitId);

        return FanitStoryResponse.builder()
                .fanitStoryList(fanitStoryList)
                .build();
    }

    @Transactional
    public FanitMessageResponse saveFanitStory(Account account, String integUid, int fanitId, String comment) {
        this.uidCheck(account.getIntegUid(),integUid);
        Boolean isFanitList = fanitStoryMapper.isFanitList(fanitId);
        if(!isFanitList){
            throw new FantooException(ErrorCode.ERROR_FE5014);
        }
        String userIp = account.getUserIp();
        String ip = this.extractIpFromRequest(userIp);
        CountryData countryData = countryMapper.getIpNation(ip);
        String country = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountry();
        String countryCode = (ObjectUtils.isEmpty(countryData)) ? "" : countryData.getCountryCode();
        fanitStoryMapper.insertFanitStory(fanitId, integUid, comment, userIp, country, countryCode);
        int idx = fanitStoryMapper.getFanitStoryId(integUid);
        return FanitMessageResponse.builder()
                .fanitStoryId(idx)
                .build();
    }

    @Transactional
    public FanitMessageResponse deleteFanitStory(Account account, String integUid,int storyId, int fanitId) {
        this.uidCheck(account.getIntegUid(),integUid);
        Boolean isFanitStory = fanitStoryMapper.isFanitStory(storyId,integUid);
        if(!isFanitStory){
            throw new FantooException(ErrorCode.ERROR_FE5014);
        }
        int resultCnt = fanitStoryMapper.deleteFanitStory(storyId,fanitId);
        return FanitMessageResponse.builder()
                .resultCnt(resultCnt)
                .build();
    }

}
