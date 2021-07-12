package net.intelie.challenges;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.transaction.annotation.Transactional;

@Transactional
public class EventIteratorClass implements EventIterator {
	
	private List<Event> eventList = new ArrayList<Event>();
	private final EventRepository repository;
		
	/*
	 * Constructor requires a List<Event> and an EventRepository
	 * 
	 * @param eventList		The list of events after the type and timestamp filter.
	 * @param repository	The Event storage.
	 */
	EventIteratorClass(List<Event> eventList, EventRepository repository){
		this.eventList = eventList;
		this.repository = repository;
	}
		
	/*
	 * Counter created to store List's current position. It starts on -1 so that the moveNext function, as required, should always be called.
	 */
	private final AtomicInteger counter = new AtomicInteger(-1);
	
	/*
	 * movedCursor created to work as a switch for the moveNext function
	 */
	private final AtomicBoolean movedCursor = new AtomicBoolean(false);
	
	/*
	 * Moves the iterator to the next Event. If it is the first time the function is called, returns the first element (Index 0).
	 * 
	 * @return true after changing to the next Event, false if the List has reached its end.
	 */
	@Override
	public boolean moveNext() {
		counter.incrementAndGet();

		if(eventList.size() > counter.get()) //Checks if the counter is over the List size.
			movedCursor.compareAndSet(false, true);
		else {
			counter.decrementAndGet(); //Returns the counter to the previous value to allow repetition.
			movedCursor.compareAndSet(true, false);
		}
				
		return movedCursor.get();
	}
	
	/*
	 * Returns the current selected Event by this iterator.
	 * 
	 * @return the event itself
	 * @throw IllegalStateException if moveNext was never called or its last result was false.
	 */
	@Override
	public Event current() {
		if(!movedCursor.get()) //Checks if moveNext was called.
			throw new IllegalStateException("Current Event Not Found.");
		else 
			return eventList.get(counter.get());		
	}

	/*
	 * Removes the current event from the store. Removing from eventList and repository.
	 * Since removing the Event from the list and repository shifts any subsequent elements to the left,
	 * index will be updated to the previous Event to wait for the moveNext function to update the current Event.
	 * This was implemented since the moveNext function, as required, needs to be called. 
	 * 
	 * @throw IllegalStateException if moveNext was never called of its last result was false.
	 */
	@Override
	public void remove() {
		if(!movedCursor.get()) //Checks if moveNext was called.
			throw new IllegalStateException("Current Event Not Found.");
		else {
			movedCursor.compareAndSet(true, false);	//Sets movedCursor as false so the moveNext function is required to return the current Event.	
			repository.delete(eventList.get(counter.get())); //Removes the Event from Repository.
			eventList.remove(counter.get()); //Removes the Event from the eventList.
			counter.decrementAndGet(); //Decreases the counter to match the moveNext requirement.
		}		
	}

	/*
	 * Overriding close method because the EventIterator Interface extends AutoCloseable
	 */
	@Override
	public void close() throws Exception {
		System.out.println("Closing EventIterator!");
	}
}
