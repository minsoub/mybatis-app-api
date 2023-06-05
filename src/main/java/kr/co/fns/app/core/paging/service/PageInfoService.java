package kr.co.fns.app.core.paging.service;


import kr.co.fns.app.core.paging.dto.PageInfo;
import kr.co.fns.app.base.dto.BaseSearchDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service("PageInfoService")
public class PageInfoService {

    // HashMap을 받아 put Name 까지 공통적으로 처리.
    public HashMap<String, Object> convertPageInfo(PageInfo pageInfo, HashMap<String, Object> hashMapResult) {

        if(pageInfo.getPagingStatus()){
            BaseSearchDto result = new BaseSearchDto();

            result.setPage(pageInfo.getPage());
            result.setSize(pageInfo.getSize());
            result.setTotalCount(pageInfo.getTotalCount());

            hashMapResult.put("pageInfo",result);
        }
        return hashMapResult;
    }


}
