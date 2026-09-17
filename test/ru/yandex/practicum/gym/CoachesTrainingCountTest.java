package ru.yandex.practicum.gym;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CoachesTrainingCountTest {

    private Timetable timetable;
    private Coach coachVasilyev;
    private Coach coachSemenov;
    private Coach coachIzmaylov;
    private Group childGroup;

    @BeforeEach
    void setUp() {
        timetable = new Timetable();
        coachVasilyev = new Coach("Васильев", "Николай", "Сергеевич");
        coachSemenov = new Coach("Семёнов", "Владимир", "Константинович");
        coachIzmaylov = new Coach("Измайлов", "Кирилл", "Валентинович");
        childGroup = new Group("Акробатика для детей", Age.CHILD, 60);
    }

    @Test
    void testGetCountByCoachesWhenTimetableIsEmpty() {
        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить, что возвращается пустой список, а не null
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetCountByCoachesSingleCoachMultipleSessions() {
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachVasilyev, DayOfWeek.MONDAY, new TimeOfDay(13, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachVasilyev, DayOfWeek.WEDNESDAY, new TimeOfDay(15, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить корректность подсчета для одного тренера
        assertEquals(1, result.size());
        assertEquals(coachVasilyev, result.get(0).getCoach());
        assertEquals(2, result.get(0).getCount());
    }

    @Test
    void testGetCountByCoachesSortedDescending() {

        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachIzmaylov, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachIzmaylov, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachIzmaylov, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));

        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachVasilyev, DayOfWeek.THURSDAY, new TimeOfDay(14, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachVasilyev, DayOfWeek.FRIDAY, new TimeOfDay(15, 0)));

        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachSemenov, DayOfWeek.SATURDAY, new TimeOfDay(16, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить размерность и очередность сортировки по убыванию количества тренировок
        assertEquals(3, result.size());

        assertEquals(coachIzmaylov, result.get(0).getCoach());
        assertEquals(3, result.get(0).getCount());

        assertEquals(coachVasilyev, result.get(1).getCoach());
        assertEquals(2, result.get(1).getCount());

        assertEquals(coachSemenov, result.get(2).getCoach());
        assertEquals(1, result.get(2).getCount());
    }

    @Test
    void testGetCountByCoachesWithEqualCounts() {
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachVasilyev, DayOfWeek.MONDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(childGroup, coachSemenov, DayOfWeek.TUESDAY, new TimeOfDay(14, 0)));

        List<CounterOfTrainings> result = timetable.getCountByCoaches();

        // Проверить, что оба тренера присутствуют и у каждого по 1 занятию
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getCount());
        assertEquals(1, result.get(1).getCount());
    }
}
