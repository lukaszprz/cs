package com.creditsuisse.main.service;

import java.util.List;
import java.util.Optional;

import com.creditsuisse.main.dto.Log;
import com.creditsuisse.core.model.Event;

public interface EventService {
	
	/** Flag any long events that take longer than 4ms with a column in the database called "alert"
	 * 
	 * @param logs - e collection of log entries
	 * @return optional list of events with alert statuses included
	 */
	List<Event> findLongEvents(List<Log> logs);

}
