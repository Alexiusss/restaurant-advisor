package com.example.restaurant_advisor.util;

import com.example.restaurant_advisor.model.Review;
import com.example.restaurant_advisor.model.User;
import com.example.restaurant_advisor.model.dto.ReviewDto;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@UtilityClass
public class ControllerUtils {

    public static Map<String, String> getErrors(BindingResult bindingResult) {
        Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
                fieldError -> fieldError.getField() + "Error",
                FieldError::getDefaultMessage
        );
        return bindingResult.getFieldErrors().stream().collect(collector);
    }

    public static String saveFile(MultipartFile file, String uploadPath) throws IOException {
        String resultFileName = null;
        if (file != null && !file.getOriginalFilename().isEmpty()) {
            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();

            resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));
        }
        return resultFileName;
    }

    // https://stackoverflow.com/a/37771947
    public static <T> Page<T> createPageFromList(Pageable pageable, List<T> list) {
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }

    public static List<ReviewDto> createListReviewTos(Set<Review> reviews, User currentUser) {
        return reviews.stream()
                .map(review ->
                        new ReviewDto(review, (long) review.getLikes().size(),
                                review.getLikes().stream()
                                        .anyMatch(Predicate.isEqual(currentUser))))
                .collect(Collectors.toList());
    }

    public static double getRestaurantRating(List<ReviewDto> reviews) {
        if (reviews == null) return 0;
        return reviews.stream()
                .filter(ReviewDto::isActive)
                .mapToDouble(ReviewDto::getRating)
                .average().orElse(0);
    }

    public static Set<Review> getActiveReviews(Set<Review> reviews) {
        return reviews.stream().filter(Review::isActive).collect(Collectors.toSet());
    }
}