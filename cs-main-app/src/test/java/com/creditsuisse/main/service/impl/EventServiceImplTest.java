/**
 * 
 */
package com.creditsuisse.main.service.impl;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creditsuisse.main.dto.Log;
import com.creditsuisse.main.dto.Log.State;
import com.creditsuisse.core.model.Event;
import com.creditsuisse.core.repository.EventRepository;

/**
 * @author lukasz
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EventServiceImplTest {

	private final static Logger logger = LoggerFactory.getLogger(EventServiceImplTest.class);

	@Mock
	EventServiceImpl service;

	@Mock
	EventRepository eventRepository;

	private List<Log> logs = new ArrayList<>();

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		LocalDateTime now = LocalDateTime.now();
		ZonedDateTime zdt = now.atZone(ZoneId.of("Europe/Warsaw"));
		Log log1Start = new Log();
		log1Start.setId("test");
		log1Start.setState(State.STARTED);
		log1Start.setTimestamp(zdt.toInstant().toEpochMilli());
		logs.add(log1Start);

		Log log1Finish = new Log();
		log1Finish.setId("test");
		log1Finish.setState(State.FINISHED);
		log1Finish.setTimestamp(zdt.toInstant().plusMillis(6).toEpochMilli());
		logs.add(log1Finish);

		Event event = new Event();
		event.setId(logs.get(0).getId());
		event.setAlert(true);
		event.setDuration(logs.get(1).getTimestamp() - logs.get(0).getTimestamp());

		when(service.findLongEvents(any(List.class))).thenReturn(logs);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link com.creditsuisse.main.service.impl.EventServiceImpl#findLongEvents(java.util.List)}.
	 */
	@Test
	public final void testFindLongEvents() {

		List<Event> events = service.findLongEvents(logs);

		assertNotNull(events);
	}

	@Test
	public final void testIfEventsListSizeIsGreaterThanZero() {
		List<Event> events = service.findLongEvents(logs);
		
		assertTrue(events.size() > 0);
	}
	

}
