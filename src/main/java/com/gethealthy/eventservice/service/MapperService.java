package com.gethealthy.eventservice.service;

/**
 *
 * @param <T> Class of the DTO (data transfer object
 * @param <U> Class of the Entity
 */
public interface MapperService<T,U> {
    /**
     *Converts an entity object to a dto object
     *
     * @param u entity to be converted to a DTO object
     * @return The specified DTO object
     */
    public T toDTO(U u);

    /**
     * converts a dto object to an entity object
     *
     * @param t the DTO object to convert to an entity object
     * @return the specified entity object
     */
    public U toEntity(T t);

    /**
     *Updates the entity with the new information from the dto
     * @param t the DTO object containing the updated entity information
     * @param u the entity object to be updated
     */
    public void updateEntity(T t, U u);
}
