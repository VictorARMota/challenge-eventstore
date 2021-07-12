package net.intelie.challenges;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/*
 * Marked the Event Class as an Entity to mark it as an JPA-based data store.
 */
@Entity
public class Event {
	private @Id @GeneratedValue long id; //Primary Key of the Entity.
    private String type;
    private long timestamp;

    public Event(String type, long timestamp) {
        this.type = type;
        this.timestamp = timestamp;
    }
    
    /*
     * Removed:
     *	public String type() {
     *  	return type;
     *	}
     *
     *	public long timestamp() {
     *		return timestamp;
     *	}
     */

    
    /*
     * Added
     */
    
    public Event() {}
    
    public void setId(long id) {
    	this.id = id;
    }
    
    public long getId() {
    	return this.id;
    }
    
    public String getType() {
    	return this.type;
    }
      
    public long getTimestamp() {
    	return this.timestamp;
    }
}
