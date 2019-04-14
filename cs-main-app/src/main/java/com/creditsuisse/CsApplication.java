package com.creditsuisse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.context.annotation.ComponentScan;

import com.creditsuisse.core.model.Event;
import com.creditsuisse.core.repository.EventRepository;
import com.creditsuisse.main.dto.Log;
import com.creditsuisse.main.service.EventService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
@ComponentScan("com.creditsuisse")
public class CsApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(CsApplication.class);
	
	@Autowired
	private EventService eventService;
	
	@Autowired
	private EventRepository repo;
		
	public static void main(String[] args) {
		SpringApplication.run(CsApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws Exception {
		
		List<Log> logs = new ArrayList<>();
		
		File file = new File(args[0]);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			while (line != null) {
				logger.info("Reading JSON entry: {}", line);
				
				JSONObject obj = new JSONObject(line);				
				Log log = mapper.readValue(obj.toString(), Log.class);
				logs.add(log);
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		List<Event> events = eventService.findLongEvents(logs);
		if (!events.isEmpty()) {
			for (Event event :events) {
				logger.info("Stored event: {}", event);
			}
		}
		
		List<Event> evs = (List<Event>) repo.findAll();
		for (Event ev : evs) {
			logger.debug("Checked event from DB: {}", ev);
		}
	}

}
