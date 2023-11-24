package com.greamz.backend.dto.review;

import com.greamz.backend.dto.account.AccountBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewsUserDTO {
    private String text;
    private int rating;
    private int like;
    private int dislike;
    private AccountBasicDTO account;

}
