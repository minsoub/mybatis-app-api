package kr.co.fns.app.api.fanit.service;


import kr.co.fns.app.api.fanit.mapper.FanitMapper;
import kr.co.fns.app.api.fanit.model.request.FanitPointInsertReq;
import kr.co.fns.app.base.service.CheckBaseService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@AllArgsConstructor
public class FanitOCIService extends CheckBaseService{

    private FanitMapper fanitMapper;

    @Transactional
    public void getFanitPaymentPolicy(FanitPointInsertReq fanitPointInsertReq) {
        String rewardType = fanitPointInsertReq.getRewardType();
        if(rewardType.equals("CLUB_JOIN_REWARD")){
            //클럽 인원 달성 적립
            this.saveClubJoinFanit(fanitPointInsertReq);
        }else if(rewardType.equals("CODE_USE")){
            //누군가가 추천해줘서 받는 팬잇
            this.saveCodeUseFanit(fanitPointInsertReq);
        }else if(rewardType.equals("COMMUNITY_REPLY_REWARD") || rewardType.equals("COMMUNITY_LIKE_REWARD")
                || rewardType.equals("MINUTE_REPLY_REWARD") || rewardType.equals("MINUTE_LIKE_REWARD")
                || rewardType.equals("FANTOO_TV_REPLY_REWARD") || rewardType.equals("FANTOO_TV_LIKE_REWARD")){
            // 댓글에 대한 확인
            this.saveCommunityReplyRewardFanit(fanitPointInsertReq);
        }else{
            Boolean isFanitPayment = (fanitPointInsertReq.getPossibleDayCnt()>0) ? fanitMapper.isTodayFanitPayment(fanitPointInsertReq) : fanitMapper.isRewardFanitPayment(fanitPointInsertReq);
            if(!isFanitPayment){
                fanitMapper.insertFanitPoint(fanitPointInsertReq);
            }
        }
    }

    /**
     * 클럽인원 달성 적립
     */
    @Transactional
    public void saveClubJoinFanit(FanitPointInsertReq fanitPointInsertReq){
        String cludManage = fanitMapper.getClubManage(fanitPointInsertReq);
        if(!ObjectUtils.isEmpty(cludManage)){
            fanitPointInsertReq.setIntegUid(cludManage);
            int joinRewardCnt = fanitMapper.getJoinRewardCnt(fanitPointInsertReq);
            for(int i=0; i<joinRewardCnt; i++){
                Boolean isFanitPayment = fanitMapper.isRewardFanitPayment(fanitPointInsertReq);
                if(!isFanitPayment){
                    fanitMapper.insertFanitPoint(fanitPointInsertReq);
                }
            }
        }
    }

    /**
     * 누군가가 추천해줘서 받는 팬잇
     */
    @Transactional
    public void saveCodeUseFanit(FanitPointInsertReq fanitPointInsertReq){
        //누군가가 추천해줘서 받는 팬잇
        String referralUser = fanitMapper.getReferralUser(fanitPointInsertReq);
        if(!ObjectUtils.isEmpty(referralUser)){
            fanitPointInsertReq.setIntegUid(referralUser);
            fanitMapper.insertFanitPoint(fanitPointInsertReq);
        }
    }

    /**
     * 커뮤니티 활동보상
     * 댓글작성
     */
    @Transactional
    public void saveCommunityReplyRewardFanit(FanitPointInsertReq fanitPointInsertReq){
        //커뮤니티 활동보상 - 댓글 작성
        Boolean isCommunityFanitPayment = fanitMapper.isTodayCommunityFanitPayment(fanitPointInsertReq);
        if(!isCommunityFanitPayment){
            Boolean isFanitPayment = (fanitPointInsertReq.getPossibleDayCnt()>0) ? fanitMapper.isTodayFanitPayment(fanitPointInsertReq) : fanitMapper.isRewardFanitPayment(fanitPointInsertReq);
            if(!isFanitPayment){
                fanitMapper.insertFanitPoint(fanitPointInsertReq);
            }
        }
    }
}
