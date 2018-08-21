package com.revolut.moneytransfer.util.db;

import org.apache.commons.dbutils.DbUtils;
import org.h2.jdbcx.JdbcConnectionPool;

import javax.sql.DataSource;
import java.io.InputStream;

import java.util.Properties;

/**
 * The enum H 2 connection pool.
 */
public enum H2ConnectionPool implements IConnectionPool {

    /**
     * Instance h 2 connection pool.
     */
    INSTANCE;

    /**
     * The constant PORERTY_FILE_NAME.
     */
    public static final String PORERTY_FILE_NAME = "/config.properties";

    private JdbcConnectionPool cp;

    H2ConnectionPool(){
        Properties properties = new Properties();
        try (InputStream is = H2ConnectionPool.class.getResourceAsStream(PORERTY_FILE_NAME)) {
            properties.load(is);
        } catch (Exception e) {
            throw new RuntimeException("Properties file is missing", e);
        }
        DbUtils.loadDriver(properties.getProperty("db.driver"));
        cp = JdbcConnectionPool.create(properties.getProperty("db.url"), properties.getProperty("db.user"),
                properties.getProperty("db.password"));
    }

    public DataSource getConnectionPool() {
        return INSTANCE.cp;
    }



}
