package com.revolut.moneytransfer.util.db;

import javax.sql.DataSource;

/**
 * The interface Connection pool.
 */
public interface IConnectionPool {

    /**
     * Gets connection pool.
     *
     * @return the connection pool
     */
    DataSource getConnectionPool();
}
