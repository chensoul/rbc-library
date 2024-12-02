package com.productdock.domain;

import lombok.*;

import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Book {

    private Long id;
    private String title;
    private String author;
    private String cover;
    private String description;
    @Singular
    private List<Topic> topics;
    @Singular
    private List<Review> reviews;

    public Rating getRating() {
        var reviewsCount = (int) getRatedReviews(reviews).count();
        if (reviewsCount == 0) {
            return new Rating();
        }
        var score = getRatedReviews(reviews).mapToDouble(Review::getRating).average().orElse(0.0);
        return new Rating(score, reviewsCount);
    }

    private Stream<Review> getRatedReviews(List<Review> reviews) {
        return reviews.stream().filter(review -> review.getRating() != null);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Review {
        private ReviewCompositeKey reviewCompositeKey;
        private String userFullName;
        private String comment;
        private Short rating;
        private Integer recommendation;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class ReviewCompositeKey {

            protected Long bookId;
            protected String userId;

        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Rating {
        private Double score;
        private int count;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @Builder
    public static class Topic {
        private Long id;
        private String name;
    }

}
