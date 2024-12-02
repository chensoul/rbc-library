package com.productdock.data.provider.out.sql;

import com.productdock.adapter.out.sql.entity.ReviewJpaEntity;

import java.util.Calendar;

public class ReviewJpaEntityMother {

    private static final Long defaultBookId = 1L;
    private static final String defaultUserId = "::userId::";
    private static final String defaultUserFullName = "::userFullName::";
    private static final String defaultComment = "::comment::";
    private static final Short defaultRating = 2;
    private static final Integer defaultRecommendation = 3;

    public static ReviewJpaEntity.ReviewJpaEntityBuilder defaultReviewEntityBuilder() {
        var calendar = Calendar.getInstance();
        calendar.set(2022, Calendar.APRIL, 5);

        return ReviewJpaEntity.builder()
                .reviewCompositeKey(ReviewJpaEntity.ReviewCompositeKey.builder()
                        .bookId(defaultBookId)
                        .userId(defaultUserId)
                        .build())
                .userFullName(defaultUserFullName)
                .comment(defaultComment)
                .rating(defaultRating)
                .date(calendar.getTime())
                .recommendation(defaultRecommendation);
    }

    public static ReviewJpaEntity defaultReviewEntity() {
        return defaultReviewEntityBuilder().build();
    }

}
