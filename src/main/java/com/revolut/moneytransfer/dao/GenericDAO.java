package com.revolut.moneytransfer.dao;

import java.util.List;

/**
 * The interface Generic dao.
 *
 * @param <T> the type parameter
 */
public interface GenericDAO<T>
{
    /**
     * Find t.
     *
     * @param id the id
     * @return the t
     */
    public T find(long id);

    /**
     * Find list.
     *
     * @return the list
     */
    public List<T> find();

    /**
     * Create long.
     *
     * @param value the value
     * @return the long
     */
    public long create(T value);

    /**
     * Update.
     *
     * @param value the value
     */
    public void update(T value);

    /**
     * Delete.
     *
     * @param value the value
     */
    public void delete(T value);
}