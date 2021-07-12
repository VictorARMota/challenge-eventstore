package net.intelie.challenges;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * The @RestContoller annotation indicates that the data from each method will be returned straight to the response body.
 */
@RestController
public class EventController {
	private final EventRepository repository;
	
	private EventIterator ei;
	
	/*
	 * Constructor requiring EventRepository.
	 */
	EventController(EventRepository repository){
		this.repository = repository;
	}
	
	/*
	 * GET HTTP method to return All Events in the Repository.
	 * 
	 * @return all events at repository.
	 */
	@GetMapping("/event")
	List<Event> all(){
		return repository.findAll();
	}
	
	/*
	 * POST HTTP method to store an Event.
	 * 
	 * Body Sample:
	 *	{
     *		"type" : "Test",
     *		"timestamp" : 999999
     *	}
     * @param newEvent.
	 * @return "Event Stored" if successful, Exception message if otherwise.
	 */
	@PostMapping("/event")
	String newEvent(@RequestBody Event newEvent) {
		EventStoreClass store = new EventStoreClass(repository);
		try {
			store.insert(newEvent);
			return "Event Stored";
		}
		catch(Exception e){
			return e.getMessage();
		}		
	}
	
	/*
	 * DELETE HTTP method to delete Events by their type.
	 * 
	 * @param type 	Retrieved from path variable.
	 * @return "Event Removed" if successful, Exception message if otherwise.
	 */
	@DeleteMapping("/event/{type}")
	String deleteEventsByType(@PathVariable String type) {
		EventStoreClass store = new EventStoreClass(repository);
		try {
			store.removeAll(type);
			return "Event Removed";
		}
		catch(Exception e){
			return e.getMessage();
		}		
	}
	
	
	/*
	 * GET HTTP method to generate an EventIterator.
	 * 
	 * @param type		Type we are querying for. Retrieved from request parameter "type".
	 * @param startTime	Start timestamp. Retrieved from request parameter "startTime".
	 * @param endTime	End timestamp. Retrieved from request parameter "endTime".
	 * @return the EventIterator itself as a JSON if successful, Exception message if otherwise.
	 */
	@GetMapping("/query")
	String query(@RequestParam("type") String type, @RequestParam("startTime") long startTime, @RequestParam("endTime") long endTime) {
		try {
			EventStoreClass store = new EventStoreClass(repository);
			this.ei = store.query(type, startTime, endTime);
			
			return "Query Generated";
		}
		catch (Exception e){
			return e.getMessage();
		}
	}
	
	/*
	 * GET HTTP method to move to the next Event from the EventIterator.
	 * 
	 * @return true if the current Event was changed, false otherwise. 
	 * 			Also returns false if the Query was never created.
	 */
	@GetMapping("/query/next")
	boolean moveNext() {
		if(ei != null) 
			return this.ei.moveNext();	
		else
			return false;
	}
	
	/*
	 * GET HTTP method to return the current selected Event from the EventIterator.
	 * 
	 * @return the Event itself as a JSON. Also returns "Query not found. Please generate a Query." 
	 * 							if the Query was never created.
	 */
	@GetMapping("/query/current")
	String currentEvent() {
		if(ei != null) {
			if(this.ei.current() != null) {
				try {
					return new ObjectMapper().writeValueAsString(this.ei.current());
				}
				catch(Exception  e){
					return "Failed to convert Current Event! " + e.getMessage();
				}
			}
			else
				return "Current Event Not Found.";
		}
		else
			return "Query not found. Please generate a Query.";
	}
	
	/*
	 * DELETE HTTP method to remove the current Event selected by the EventIterator
	 * 
	 * @return "Event Removed" if successful, Exception message if otherwise.
	 */
	@DeleteMapping("/query/current")
	String removeEvent() {
		try {
			this.ei.remove();
			return "Event Removed";
		}
		catch (Exception e){
			return "Failed to Remove Event! " + e.getMessage();
		}
	}
}
