package com.revolut.moneytransfer.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * The type Client.
 */
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class Client implements Deletable {

    private long clientId;

    private String name;

    private String info;

    private boolean isDeleted;

    /**
     * Instantiates a new Client.
     *
     * @param clientId  the client id
     * @param name      the name
     * @param info      the info
     * @param isDeleted the is deleted
     */
    public Client(long clientId, String name, String info, boolean isDeleted) {
        this.clientId = clientId;
        this.name = name;
        this.info = info;
        this.isDeleted = isDeleted;
    }

    /**
     * Instantiates a new Client.
     *
     * @param name the name
     * @param info the info
     */
    public Client(String name, String info) {
        this.name = name;
        this.info = info;
    }
}
