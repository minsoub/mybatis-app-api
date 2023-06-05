package kr.co.fns.app.api.hashtag.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;
import kr.co.fns.app.api.hashtag.entity.Hashtag;
import lombok.*;

import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HashtagDto {

    @JsonIgnore
    private int hashtagId;

    @Expose
    @Pattern(regexp="^.{2,15}$",
            message = "ERROR_FE2023")
    private String tag;


    public HashtagDto(Hashtag hashtag) {
        this.hashtagId = hashtag.getHashtagId();
        this.tag = hashtag.getTag();
    }
}
