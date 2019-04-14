/**
 * 
 */
package com.creditsuisse.core.repository;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creditsuisse.core.model.Event;

/**
 * @author lukasz
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class EventRepositoryTest {

	private static Logger logger = LoggerFactory.getLogger(EventRepositoryTest.class);

	private static final String DUMMY_EVENT_ID = "testid";

	@Mock
	private static EventRepository eventRepositoryMock;
	
	private static Event eventMock;

	@BeforeClass
	public static void setUp() throws Exception {
		MockitoAnnotations.initMocks(EventRepositoryTest.class);

		Event event = new Event();
		event.setId(DUMMY_EVENT_ID);
		event.setAlert(true);
		event.setDuration(5L);
		event.setHost("localhost");
		event.setType("DUMMY_LOG");

		eventMock = event;

		
		
		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		logger.debug("Cleaning...");
		eventRepositoryMock.deleteById(DUMMY_EVENT_ID);
	}

	public final void testThatEventIsSaved() {
		logger.debug("Setting the Event record and storing it to db.");
		when(eventRepositoryMock.save(eventMock)).thenReturn(eventMock);
		eventRepositoryMock.save(eventMock);
		logger.debug("done");
	}

	@Test
	public final void testThatEventRecordExists() {
		when(eventRepositoryMock.existsById(DUMMY_EVENT_ID)).thenReturn(true);
		assertTrue(eventRepositoryMock.existsById(DUMMY_EVENT_ID));
	}

	@Test
	public final void testThatEventCountIsOne() {
		when(eventRepositoryMock.count()).thenReturn(1L);
		assertEquals(1, eventRepositoryMock.count());
	}

	@Test
	public final void testThatEventAlertIsTrue() {
		when(eventRepositoryMock.findById(DUMMY_EVENT_ID)).thenReturn(Optional.of(eventMock));
		Event event = eventRepositoryMock.findById(DUMMY_EVENT_ID).get();
		logger.debug("isAlert? " + event.isAlert());
		assertTrue(event.isAlert());
	}

	@Test
	public final void testThatEventDurationIsGraterThan4() {
		when(eventRepositoryMock.findById(DUMMY_EVENT_ID)).thenReturn(Optional.of(eventMock));
		assertTrue(4 < eventRepositoryMock.findById(DUMMY_EVENT_ID).get().getDuration());
	}

}
