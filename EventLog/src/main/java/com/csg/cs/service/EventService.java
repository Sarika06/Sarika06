package com.csg.cs.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.csg.cs.entity.EventEntity;
import com.csg.cs.model.Event;
import com.csg.cs.repository.EventRepository;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EventService {

	@Autowired
	private EventRepository eventRepository;
	
	/*
	 * this could be next step for performance improvement
	 * if the files are too big
	 * partition and parse it in chunks with parallel processing
	 * */

	public void logEventData() throws IOException, ParseException {
		JSONParser jsonParser = new JSONParser();
		ConcurrentHashMap<String, Long> Map = new ConcurrentHashMap<String, Long>();
		ExecutorService executorService = Executors.newFixedThreadPool(2);
		File file = ResourceUtils.getFile("classpath:logfile.txt");
		try (FileReader reader = new FileReader(file)) {
			Object obj = jsonParser.parse(reader);
			Gson gson = new Gson();
			final Event[] eventArray = gson.fromJson(obj.toString(), Event[].class);
			log.info("Json Array extracted successfully from text file");
			executorService.execute(()->{
				  log.info(String.format("starting  task thread %s", Thread.currentThread().getName()));
				  calculateDiff(Map, eventArray);   
				});
		} catch (FileNotFoundException e) {
			log.error("FilenotFoundException occurred while trying to read from input path" + e.getMessage());
			throw e;
		} catch (IOException e) {
			log.error("IOException thrown while trying to parse the input file" + e.getMessage());
			throw e;
		} catch (ParseException e) {
			log.error("ParseException thrown while parsing the json object from input file" + e.getMessage());
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			log.error("Unknown execption thrown while processing" + e.getCause());
			e.printStackTrace();
			throw e;
		}finally{
			  executorService.shutdown(); 
		}
	}

	public void calculateDiff(Map<String, Long> Map, Event[] eventArray) {
		try {
			for (Event event : eventArray) {
				Long diff;
				EventEntity eventEntity = new EventEntity();
				eventEntity.setId(event.getId());
				eventEntity.setType(event.getType());
				eventEntity.setHost(event.getHost());
				if ((Map.containsKey(event.getId() + "STARTED")) || (Map.containsKey(event.getId() + "FINISHED"))) {
					if (event.getState().equals("STARTED")) {
						diff = (Map.get(event.getId() + "FINISHED")) - (event.getTimestamp());
					} else {
						diff = (event.getTimestamp() - (Map.get(event.getId() + "STARTED")));
					}
					eventEntity.setDuration(diff.intValue());
					if (diff > 4) {
						eventEntity.setAlert(true);
					}
					eventRepository.save(eventEntity);
					log.info("entity saved to Event table with id :{}", event.getId());
				}
				Map.put(event.getId() + event.getState(), event.getTimestamp());
			}
		} catch (Exception e) {
			log.error("Unknown exception thrown while processing" + e.getMessage());
			throw e;
		}

	}
	
}
