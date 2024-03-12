package com.greamz.backend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewFromUser {
    private String text;
    private int rating;
    private Long appid;
    private Long orderDetailId;
    private Integer platformId;
    private Integer accountId;
}
