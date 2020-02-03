package map.graphics;

public class Time {

  private int hours, minutes, seconds;

  public Time (int hour, int minute, int second) {
    hours = hour; minutes = minute; seconds = second;
  }

  public Time () {
    hours = 0; minutes = 0; seconds = 0;
  }

  public int get_hours () {
    return hours;
  }

  public int get_minutes () {
    return minutes;
  }

  public int get_seconds () {
    return seconds;
  }

  public static double calc_raw_time (int distance) {
    return (distance/100)/1.1;
  }

  public static Time calc_time (int distance) {
    double time = calc_raw_time(distance);
    double hour = (time/3600);
    double minute;
    double second;
    if (hour >= 1) {
      minute = (hour%1)*60;
      hour = (hour-(hour%1));
    } else {
      minute = (time/60);
    }
    if (minute >= 1) {
      second = (minute%1)*60;
      minute = (minute-(minute%1));
      second = (second-(second%1));
    } else {
      second = (time-(time%1));
    }
    return new Time((int)hour, (int)minute, (int)second);
  }
}
