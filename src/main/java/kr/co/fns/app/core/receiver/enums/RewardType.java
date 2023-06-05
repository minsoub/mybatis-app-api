package kr.co.fns.app.core.receiver.enums;

import lombok.Getter;

@Getter
public enum RewardType {
    INTEREST_COMPLETE("1", 500, 0,1,"커뮤니티 관심사 설정 최초 1회 지급"),
    CODE_REWARD("1", 500, 0,1,"친구 초대 - 가입 후 추천인 코드 입력"),
    CODE_USE("1", 150, 0,0,"추천자 - 추천할 때마다 획득 (다른 사람이 추천한 경우)"),
    COMMUNITY_POST_REWARD("1", 500,1,0,"커뮤니티 - 게시물작성(이미지첨부 필수)"),
    COMMUNITY_REPLY_REWARD("1", 50,5,0,"커뮤니티 - 댓글작성"),
    COMMUNITY_LIKE_REWARD("1", 10,10,0,"커뮤니티 - 좋아요"),
    CLUB_CREATE_REWARD("1", 1000,0,3,"클럽 - 클럽 개설시 ID당 3회"),
    CLUB_POST_REWARD("1", 500,5,0,"클럽 내 게시물 작성(이미지첨부 필수)"),
    CLUB_JOIN_REWARD("1", 3000, 0,20,"클럽 인원 50명 달성시 마다, 1000명 달성 까지만 지급"),
    MINUTE_POST_REWARD("1", 1000, 1,0,"미닛 동영상 업로드 1일 1회"),
    MINUTE_REPLY_REWARD("1", 50, 5,0,"미닛 댓글 작성 1일 5회"),
    MINUTE_LIKE_REWARD("1", 10, 10,0,"미닛 좋아요 1일 10회"),
    FANTOO_TV_VIEW_REWARD("1", 500, 0,1,"Fantoo TV - 영상 신청"),
    FANTOO_TV_REPLY_REWARD("1", 50, 0,1,"Fantoo TV - 댓글 작성"),
    FANTOO_TV_LIKE_REWARD("1", 10, 0,1,"Fantoo TV - 좋아요"),
    AMOFE2023_REWARD("1", 50, 12,0,"AMOFE 2023")
    ;

    private final String type;
    private final Integer point;
    private final int possibleDayCnt;
    private final int possibleCnt;
    private final String description;
    RewardType(String type, Integer point, int possibleDayCnt, int possibleCnt, String description) {
        this.type = type;
        this.point = point;
        this.possibleDayCnt = possibleDayCnt;
        this.possibleCnt = possibleCnt;
        this.description = description;
    }
}
