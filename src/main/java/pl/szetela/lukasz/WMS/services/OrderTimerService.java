package pl.szetela.lukasz.WMS.services;

import org.springframework.stereotype.Service;
import pl.szetela.lukasz.WMS.models.Order;

import java.time.Instant;

@Service
public class OrderTimerService {

    public void run(Order order) {
        order.setCurrentRealizeTimestamp(Instant.now().getEpochSecond());
    }

    public void stop(Order order) {
        Long realizeTimestamp = order.getCurrentRealizeTimestamp();
        if (realizeTimestamp != null) {
            Long currentTimestamp = Instant.now().getEpochSecond();
            Long resultInSeconds = currentTimestamp - realizeTimestamp;
            String pickingTime = order.getPickingTime();
            pickingTime = pickingTime == null ? initPickingTime(resultInSeconds) : computePickingTime(resultInSeconds, pickingTime);
            order.setPickingTime(pickingTime);
            order.setCurrentRealizeTimestamp(null);
        }
    }

    private String initPickingTime(Long resultInSeconds) {
        return resultInSeconds < 3600 ? initMinutes(resultInSeconds) : initMinutesAndHours(resultInSeconds);
    }

    private String computePickingTime(Long resultInSeconds, String pickingTime) {
        String currentPickingTime = initPickingTime(resultInSeconds);
        return computeNewPickingTime(pickingTime, currentPickingTime);
    }

    private String initMinutes(Long resultInSeconds) {
        Long minutes = resultInSeconds / 60;
        return minutes + " min.";
    }

    private String initMinutesAndHours(Long resultInSeconds) {
        Long hours = resultInSeconds / 3600;
        Long minutes = (resultInSeconds - (hours * 3600)) / 60;
        return convertToStringPickingTime(hours, minutes);
    }

    private Long[] computePreviousPickingTime(String pickingTime) {
        String[] splits = pickingTime.split(" ");
        return splits.length == 2 ? new Long[]{0L, Long.valueOf(splits[0])} : new Long[]{Long.valueOf(splits[0]), Long.valueOf(splits[2])};
    }

    private String computeNewPickingTime(String previousPickingTime, String currentPickingTime) {
        Long[] previousTimes = computePreviousPickingTime(previousPickingTime);
        Long previousHours = previousTimes[0];
        Long previousMinutes = previousTimes[1];
        Long[] currentTimes = computePreviousPickingTime(currentPickingTime);
        Long currentHours = currentTimes[0];
        Long currentMinutes = currentTimes[1];
        Long newHours = previousHours + currentHours;
        Long newMinutes = previousMinutes + currentMinutes;
        return computeNewPickingTime(newMinutes, newHours);
    }

    private String computeNewPickingTime(Long newMinutes, Long newHours) {
        return newMinutes > 60 ? processMinutesAndHours(newHours, newMinutes) : convertToStringPickingTime(newHours, newMinutes);
    }

    private String convertToStringPickingTime(Long hours, Long minutes) {
        return hours + " h " + minutes + " min.";
    }

    private String processMinutesAndHours(Long hours, Long minutes) {
        Long hoursFromMinutes = minutes / 60;
        Long remainingMinutes = minutes - (hoursFromMinutes * 60);
        return convertToStringPickingTime(hours + hoursFromMinutes, remainingMinutes);
    }
}
