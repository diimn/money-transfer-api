package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.util.db.IConnectionPool;


/**
 * The type Abstract manager.
 */
public class AbstractManager {
    /**
     * The Connection pool.
     */
    IConnectionPool connectionPool;

    /**
     * Instantiates a new Abstract manager.
     *
     * @param connectionPool the connection pool
     */
    public AbstractManager(IConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }
}
