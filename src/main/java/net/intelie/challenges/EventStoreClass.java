package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EventStoreClass implements EventStore {
	
	private final EventRepository repository;
	
	/*
	 * Constructor requiring an EventRepository.
	 * @param repository.
	 */
	EventStoreClass(EventRepository repository){
		this.repository = repository;
	}
	
	/*
	 * Stores an Event.
	 * @param event.
	 */
	@Override
	public void insert(Event event) {
		repository.save(event);
	}

	/*
	 * Removes all Events of specific type.
	 * @param type.
	 */
	@Override
	public void removeAll(String type) {
		List<Event> foundEvents = repository.findByType(type); //Using a custom query generated to find Objects by type.
		
		repository.deleteAll(foundEvents);
	}
	
	/*
	 * Returns an EventIterator based on Events of given type and timestamp.
	 * @param type      The type we are querying for.
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return An iterator where all its events have same type as
     * {@param type} and timestamp between {@param startTime}
     * (inclusive) and {@param endTime} (exclusive).
	 */
	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		List<Event> foundEventsByType = repository.findByType(type); //Finds all elements by type
		List<Event> foundEvents = new ArrayList<Event>();		
		
		foundEventsByType.stream()
				   .filter(x -> x.getTimestamp() >= startTime && x.getTimestamp() < endTime) //Filter Events by TimeStamp
				   .forEach(x -> foundEvents.add(x)); //Adds them to the foundEvents list.

		EventIteratorClass ei = new EventIteratorClass(foundEvents, repository);
		
		return ei;
	}
}
