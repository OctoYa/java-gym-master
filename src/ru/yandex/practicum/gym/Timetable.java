package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, TreeMap<TimeOfDay, List<TrainingSession>>> timetable = new HashMap<>();

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);
        if (daySchedule == null) {
            daySchedule = new TreeMap<>();
            timetable.put(day, daySchedule);
        }

        List<TrainingSession> sessions = daySchedule.get(time);
        if (sessions == null) {
            sessions = new ArrayList<>();
            daySchedule.put(time, sessions);
        }

        sessions.add(trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null || daySchedule.isEmpty()) {
            return Collections.emptyList();
        }

        List<TrainingSession> result = new ArrayList<>();
        for (List<TrainingSession> sessions : daySchedule.values()) {
            result.addAll(sessions);
        }
        return result;
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        TreeMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);
        if (daySchedule == null) {
            return Collections.emptyList();
        }

        List<TrainingSession> sessions = daySchedule.get(timeOfDay);
        if (sessions == null) {
            return Collections.emptyList();
        }

        return new ArrayList<>(sessions);
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCounts = new HashMap<>();

        for (TreeMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    Integer count = coachCounts.get(coach);
                    if (count == null) {
                        coachCounts.put(coach, 1);
                    } else {
                        coachCounts.put(coach, count + 1);
                    }
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCounts.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(new Comparator<CounterOfTrainings>() {
            @Override
            public int compare(CounterOfTrainings c1, CounterOfTrainings c2) {
                return Integer.compare(c2.getCount(), c1.getCount());
            }
        });

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Timetable timetable1 = (Timetable) o;
        return Objects.equals(timetable, timetable1.timetable);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(timetable);
    }
}
