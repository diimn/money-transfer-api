package com.revolut.moneytransfer.util.db;

import java.sql.Connection;

/**
 * The type Jdbc support.
 */
public abstract class JDBCSupport {
    /**
     * The Connection.
     */
    protected Connection connection;

    /**
     * Instantiates a new Jdbc support.
     *
     * @param connection the connection
     */
    public JDBCSupport(Connection connection) {
        this.connection = connection;
    }

}
