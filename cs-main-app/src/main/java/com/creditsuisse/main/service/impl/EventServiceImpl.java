/**
 * 
 */
package com.creditsuisse.main.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.creditsuisse.main.dto.Log;
import com.creditsuisse.main.dto.Log.State;
import com.creditsuisse.core.model.Event;
import com.creditsuisse.core.repository.EventRepository;
import com.creditsuisse.main.service.EventService;

/**
 * @author lukasz
 *
 */
@Service
public class EventServiceImpl implements EventService {

	private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

	@Autowired
	private EventRepository eventRepository;

	@Value("${cs.duration.limit}")
	private Long duration;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.creditsuisse.main.service.EventService#findLongEvents(java.util.List)
	 */
	@Override
	public List<Event> findLongEvents(List<Log> logs) {
		Long startTime = 0L;
		Long endTime = 0L;
		List<Event> events = new ArrayList<>();
		logger.info("logs size: {}", logs.size());
		for (Log logEntry : logs) {
			logger.info("log entry: {}", logEntry);
			String logId = logEntry.getId();
			if (logEntry.getState().equals(State.STARTED)) {
				logger.debug("STARTED FOUND");
				startTime = logEntry.getTimestamp();
			} else {
				logger.debug("FINISHED FOUND");
				Log endLog = findEndTimeForLogId(logId, logs);

				if (endLog != null) {
					endTime = endLog.getTimestamp();
					boolean isLonger = isLongerThanPriod(startTime, endTime, duration);
					Event event = new Event();
					event.setId(logId);
					event.setType(logEntry.getType());
					event.setHost(logEntry.getHost());
					event.setDuration(endTime - startTime);
					event.setAlert(isLonger);
					if (isLonger) {
						Event storedEvent = eventRepository.save(event);
						events.add(storedEvent);
						logger.info("Created Event object ({}): {}", events.size(), storedEvent);
					}
				}
			}
		}

		return events;
	}

	private Log findEndTimeForLogId(String logId, List<Log> logs) {
		logger.debug("Given logs size: {}. Searching for ID {}", logs.size(), logId);
		for (Log logEntry : logs) {

			if (logId.equals(logEntry.getId()) && State.FINISHED.name().equals(logEntry.getState().name())) {
				return logEntry;
			}
		}
		return null;
	}

	private boolean isLongerThanPriod(Long startTime, Long endTime, Long duration) {
		return (endTime - startTime) > duration;
	}

}
