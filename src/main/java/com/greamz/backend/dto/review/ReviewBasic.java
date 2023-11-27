package com.greamz.backend.dto.review;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewBasic {
    private Long id;
    private String text;
    private int rating;
    private int like;
    private int dislike;

}
