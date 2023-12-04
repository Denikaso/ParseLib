package org.example.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Poster {
    private final String title;
    private final String imageUrl;
    private final String date;
    private final String duration;
    private final String ageLimit;
}
