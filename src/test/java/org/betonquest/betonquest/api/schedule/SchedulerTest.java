package org.betonquest.betonquest.api.schedule;

import org.betonquest.betonquest.BetonQuest;
import org.betonquest.betonquest.api.logger.BetonQuestLogger;
import org.betonquest.betonquest.id.EventID;
import org.betonquest.betonquest.modules.schedule.ScheduleID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Test Scheduler class.
 */
@ExtendWith(MockitoExtension.class)
class SchedulerTest {
    /**
     * The current time used in the tests.
     */
    private final Instant now = Instant.now();

    @Mock
    private BetonQuestLogger logger;

    @Test
    void testAddSchedule() {
        final Scheduler<Schedule> scheduler = new MockedScheduler(logger);
        final ScheduleID scheduleID = mock(ScheduleID.class);
        final Schedule schedule = mock(Schedule.class);
        when(schedule.getId()).thenReturn(scheduleID);
        scheduler.addSchedule(schedule);
        assertTrue(scheduler.schedules.containsValue(schedule), "Schedules map should contain schedule");
        assertEquals(schedule, scheduler.schedules.get(scheduleID), "ScheduleID should be key of schedule");
    }

    @Test
    void testStart() {
        final Scheduler<Schedule> scheduler = new MockedScheduler(logger);
        assertFalse(scheduler.isRunning(), "isRunning should be false before start is called");
        scheduler.start(now);
        assertTrue(scheduler.isRunning(), "isRunning should be true after start is called");
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestContainsTooManyAsserts")
    void testStop() {
        final Scheduler<Schedule> scheduler = new MockedScheduler(logger);
        final ScheduleID scheduleID = mock(ScheduleID.class);
        final Schedule schedule = mock(Schedule.class);
        scheduler.schedules.put(scheduleID, schedule);
        scheduler.start(now);
        assertTrue(scheduler.isRunning(), "isRunning should be true after start is called");
        scheduler.stop();
        assertFalse(scheduler.isRunning(), "isRunning should be false before stop is called");
        assertTrue(scheduler.schedules.isEmpty(), "Schedules map should be empty");
    }

    @Test
    @SuppressWarnings("PMD.JUnitTestsShouldIncludeAssert")
    void testExecuteEvents() {
        try (MockedStatic<BetonQuest> betonQuest = mockStatic(BetonQuest.class)) {
            final Scheduler<Schedule> scheduler = new MockedScheduler(logger);
            final Schedule schedule = mock(Schedule.class);
            when(schedule.getId()).thenReturn(mock(ScheduleID.class));
            final EventID eventA = mock(EventID.class);
            final EventID eventB = mock(EventID.class);
            when(schedule.getEvents()).thenReturn(List.of(eventA, eventB));
            scheduler.executeEvents(schedule);
            betonQuest.verify(() -> BetonQuest.event(null, eventA));
            betonQuest.verify(() -> BetonQuest.event(null, eventB));
        }
    }

    /**
     * Class extending a scheduler without any changes.
     */
    private static final class MockedScheduler extends Scheduler<Schedule> {
        /**
         * Default constructor.
         *
         * @param logger the logger that will be used for logging
         */
        public MockedScheduler(final BetonQuestLogger logger) {
            super(logger);
        }
    }
}
