package com.revolut.moneytransfer.db;


import com.revolut.moneytransfer.util.db.H2ConnectionPool;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * The type Abstract dao test.
 */
public abstract class AbstractDAOTest {

    /**
     * The Connection pool.
     */
    DataSource connectionPool = H2ConnectionPool.INSTANCE.getConnectionPool();

    /**
     * Test find all.
     *
     * @throws SQLException the sql exception
     */
    public abstract void testFindAll() throws SQLException;

    /**
     * Test find by id.
     *
     * @throws SQLException the sql exception
     */
    public abstract void testFindById() throws SQLException;


    /**
     * Test find null by id.
     *
     * @throws SQLException the sql exception
     */
    public abstract void testFindNullById() throws SQLException;

    /**
     * Test create.
     *
     * @throws SQLException the sql exception
     */
    public abstract void testCreate() throws SQLException;


    /**
     * Test update.
     *
     * @throws SQLException the sql exception
     */
    public abstract void testUpdate() throws SQLException;


    /**
     * Test delete.
     *
     * @throws SQLException the sql exception
     */
    public abstract void testDelete() throws SQLException;


}
