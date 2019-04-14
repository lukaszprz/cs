/**
 * 
 */
package com.creditsuisse.core.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.creditsuisse.core.model.Event;

/**
 * @author lukasz
 *
 */
@Repository
public interface EventRepository extends CrudRepository<Event, String> {

}
