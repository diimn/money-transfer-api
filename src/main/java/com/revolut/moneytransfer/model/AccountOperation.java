package com.revolut.moneytransfer.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * The type Account operation.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class AccountOperation {

    private long operationId;

    private long fromAccountId;

    private long toAccountId;

    private BigDecimal sum;

    /**
     * Instantiates a new Account operation.
     *
     * @param fromAccountId the from account id
     * @param toAccountId   the to account id
     * @param sum           the sum
     */
    public AccountOperation(long fromAccountId, long toAccountId, BigDecimal sum) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.sum = sum;
    }

    /**
     * Instantiates a new Account operation.
     *
     * @param operationId   the operation id
     * @param fromAccountId the from account id
     * @param toAccountId   the to account id
     * @param sum           the sum
     */
    public AccountOperation(long operationId, long fromAccountId, long toAccountId, BigDecimal sum) {
        this.operationId = operationId;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.sum = sum;
    }
}
