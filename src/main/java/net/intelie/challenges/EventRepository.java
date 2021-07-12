package net.intelie.challenges;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/*
 * JPA Repository to store Events on Memory.
 */
interface EventRepository extends JpaRepository<Event,Long>{ 
	List<Event> findByType (String type); //Custom Query created to find Events by Type.
}
