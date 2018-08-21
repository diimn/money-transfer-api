package com.revolut.moneytransfer.util;


import com.revolut.moneytransfer.model.Deletable;

/**
 * The type Utils.
 */
public class Utils {
    /**
     * Check deletable boolean.
     *
     * @param deletable the deletable
     * @return the boolean
     */
    public static boolean checkDeletable(Deletable deletable) {
        return (deletable != null && !deletable.isDeleted());
    }
}
