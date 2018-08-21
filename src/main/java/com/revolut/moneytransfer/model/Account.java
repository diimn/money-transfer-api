package com.revolut.moneytransfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * The type Account.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Account implements Deletable {

    private long accountId;

    private BigDecimal amount;

    /**
     * The Client id.
     */
    private long clientId;

    private boolean isDeleted;

    /**
     * Instantiates a new Account.
     *
     * @param amount   the amount
     * @param clientId the client id
     */
    public Account(BigDecimal amount, long clientId) {
        this.amount = amount;
        this.clientId = clientId;
    }

    /**
     * Instantiates a new Account.
     *
     * @param accountId the account id
     * @param amount    the amount
     * @param clientId  the client id
     */
    public Account(long accountId, BigDecimal amount, long clientId) {
        this.accountId = accountId;
        this.amount = amount;
        this.clientId = clientId;
    }

    /**
     * Instantiates a new Account.
     *
     * @param accountId the account id
     * @param amount    the amount
     * @param clientId  the client id
     * @param isDeleted the is deleted
     */
    public Account(long accountId, BigDecimal amount, long clientId, boolean isDeleted) {
        this.accountId = accountId;
        this.amount = amount;
        this.clientId = clientId;
        this.isDeleted = isDeleted;
    }
}
