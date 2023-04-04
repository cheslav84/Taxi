package com.havryliuk.util.google.map;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DistanceAndDuration {

    private Duration duration;
    private Distance distance;
}
