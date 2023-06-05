package kr.co.fns.app.api.hashtag.service;


import kr.co.fns.app.api.hashtag.entity.Hashtag;
import kr.co.fns.app.api.hashtag.mapper.HashtagMapper;
import kr.co.fns.app.api.hashtag.dto.HashtagDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service("HashtagService")
public class HashtagService {

    @Autowired
    HashtagMapper hashtagMapper;


    @Transactional
    public List<HashtagDto> insertHashtag(List<HashtagDto> hashtagDtoList) {

        /**
         * insert후 select 하여 해시태그 정보 return
         */

        // Hashtag insert
        hashtagMapper.insertHashtag(hashtagDtoList);

        List<HashtagDto> dtoList = this.getHashtag(hashtagDtoList);

        return dtoList;
    }


    // TODO 23/04/16, SLAVE 로 변경해야 될듯 좀다 추가작업 진행.-complete(위에서 transactional 적용)
    private List<HashtagDto> getHashtag(List<HashtagDto> hashtagDtoList) {

        /**
         * private, insert후 select
         * 해시태그 조회, 데이터 입력 순서대로 return
         */
        List<HashtagDto> dtoList = new ArrayList<>();

        // Hashtag select
        List<Hashtag> hashtagList = hashtagMapper.getHashtag(hashtagDtoList);
        hashtagList.forEach(s -> dtoList.add(new HashtagDto(s)) );

        return dtoList;
    }


}
