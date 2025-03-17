package com.example.tenisuj.model.dto;

import com.example.tenisuj.model.enums.Location;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class UpdateMatchLocationDateAndTimeDto {
    private String matchId;
    private Location location;
    private LocalDateTime dateTime;
}
