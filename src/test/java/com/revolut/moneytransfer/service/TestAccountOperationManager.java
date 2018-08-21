package com.revolut.moneytransfer.service;

import com.revolut.moneytransfer.model.Account;
import com.revolut.moneytransfer.service.impl.AccountManager;
import com.revolut.moneytransfer.service.impl.ClientManager;
import com.revolut.moneytransfer.service.impl.AccountOperationManager;
import com.revolut.moneytransfer.util.db.H2ConnectionPool;
import junit.framework.TestCase;
import org.junit.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.sleep;

/**
 * The type Test account operation manager.
 */
public class TestAccountOperationManager {
    /**
     * The Account operation manager.
     */
    AccountOperationManager accountOperationManager = new AccountOperationManager();
    /**
     * The Account manager.
     */
    AccountManager accountManager = new AccountManager();

    /**
     * Test refill money.
     */
    @Test
    public void testRefillMoney() {
        Account accBefore = accountManager.getActualAccounts().get(0);
        accountOperationManager.refillMoney(accBefore.getAccountId(), BigDecimal.valueOf(100));
        System.out.println(accBefore.getAmount());
        Account accAfter = accountManager.getAccById(accBefore.getAccountId());
        TestCase.assertTrue(accBefore.getAmount()
                .compareTo(accAfter.getAmount()) < 0);
        System.out.println(accAfter.getAmount());
    }


    /**
     * Test withdraw money.
     */
    @Test
    public void testWithdrawMoney() {
        Account accBefore = accountManager.getActualAccounts().get(0);
        accountOperationManager.withdrawMoney(accBefore.getAccountId(), BigDecimal.valueOf(100));
        System.out.println(accBefore.getAmount());
        Account accAfter = accountManager.getAccById(accBefore.getAccountId());
        TestCase.assertTrue(accBefore.getAmount()
                .compareTo(accAfter.getAmount()) > 0);
        System.out.println(accAfter.getAmount());
    }

    /**
     * Test transfer.
     *
     * @throws SQLException         the sql exception
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void testTransfer() throws SQLException, InterruptedException {
        ClientManager clientManager = new ClientManager();
        long idAccFrom = accountManager
                .createAccount(clientManager.getActualClients().get(0).getClientId(), BigDecimal.valueOf(500));
        long idAccTo = accountManager
                .createAccount(clientManager.getActualClients().get(0).getClientId(), BigDecimal.valueOf(0));
        Account accFromBefore = accountManager.getAccById(idAccFrom);
        Account accToBefore = accountManager.getAccById(idAccTo);
        System.out.println("accFromBefore " + accFromBefore);
        System.out.println("accToBefore " + accToBefore);
        accountOperationManager
                .transferMoney(accFromBefore.getAccountId(), accToBefore.getAccountId(), BigDecimal.valueOf(500));
        Account accFromAfter = accountManager.getAccById(accFromBefore.getAccountId());
        Account accToAfter = accountManager.getAccById(accToBefore.getAccountId());
        System.out.println("accFromAfter " + accFromAfter);
        System.out.println("accToAfter " + accToAfter);
        TestCase.assertTrue((accFromAfter.getAmount().compareTo(accFromBefore.getAmount()) < 0)
                && (accToAfter.getAmount().compareTo(accToBefore.getAmount()) > 0));
    }


    /**
     * Test refill fail on block.
     *
     * @throws SQLException the sql exception
     */
    @Test
    public void testRefillFailOnBlock() throws SQLException {
        long idAccFrom = 0;
        try (Connection connection = H2ConnectionPool.INSTANCE.getConnectionPool().getConnection();
             PreparedStatement lockStatement
                     = connection.prepareStatement("SELECT * FROM Account WHERE AccountId = ? FOR UPDATE")) {
            ClientManager clientManager = new ClientManager();
            idAccFrom = accountManager
                    .createAccount(clientManager.getActualClients().get(0).getClientId(), BigDecimal.valueOf(500));
            System.out.println("acc1: " + accountManager.getAccById(idAccFrom));

            System.out.println("acc11: " + accountManager.getAccById(idAccFrom));
            try {
                connection.setAutoCommit(false);
                lockStatement.setLong(1, idAccFrom);
                ResultSet resultSet = lockStatement.executeQuery();
                if (resultSet.next()) {
                    Account account = new Account(resultSet.getLong(1), resultSet.getBigDecimal(2),
                            resultSet.getLong(3));
                    System.out.println(account);
                }
                accountOperationManager.refillMoney(idAccFrom, BigDecimal.valueOf(500));
                System.out.println("acc1: " + accountManager.getAccById(idAccFrom));
                connection.commit();
                connection.setAutoCommit(true);
            } catch (Exception e) {
                connection.rollback();
                connection.setAutoCommit(true);
                e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        TestCase.assertTrue(accountManager.getAccById(idAccFrom).getAmount()
                .compareTo(BigDecimal.valueOf(500)) == 0);
    }

    /**
     * Multi thread transfer test.
     *
     * @throws InterruptedException the interrupted exception
     */
    @Test
    public void multiThreadTransferTest() throws InterruptedException {
        final long idFrom = 21111111111111111L;
        final long idTo = 21111111111111112L;
//        //deadlock test
//        final long idFrom1 = 21111111111111112L;
//        final long idTo1 = 21111111111111111L;

        final long idFrom1 = 21111111111111113L;
        final long idTo1 = 21111111111111114L;


        long timeStart = System.currentTimeMillis();
        BigDecimal sumFromBefore = accountManager.getAccById(idFrom).getAmount();
        BigDecimal sumToBefore = accountManager.getAccById(idTo).getAmount();

        System.out.println("start balance idFrom1: " + sumFromBefore);
        System.out.println("start balance idTo1: " + sumToBefore);

        BigDecimal sumFromBefore1 = accountManager.getAccById(idFrom1).getAmount();
        BigDecimal sumToBefore1 = accountManager.getAccById(idTo1).getAmount();

        System.out.println("start balance idFrom1: " + sumFromBefore1);
        System.out.println("start balance idTo1: " + sumToBefore1);

        final int THREADS_NUM = 1000;

        CountDownLatch countDownLatch = new CountDownLatch(THREADS_NUM * 2);

        for (int i = 0; i < THREADS_NUM * 2; i++) {

            int finalI = i;
            new Thread(() -> {
                try {

                    AccountOperationManager accountOperationManager = new AccountOperationManager();
                    if (finalI % 2 == 0) {
                        accountOperationManager
                                .transferMoney(idFrom, idTo, BigDecimal.valueOf(1));
                    } else {
                        accountOperationManager
                                .transferMoney(idFrom1, idTo1, BigDecimal.valueOf(1));
                    }

                } catch (Exception e) {
                    System.out.println(e);
                } finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();

        System.out.println("-----------------------------");

        BigDecimal sumFromAfter = new AccountManager().getAccById(idFrom).getAmount();
        BigDecimal sumToAfter = new AccountManager().getAccById(idTo).getAmount();

        System.out.println("sumFromAfter: " + sumFromAfter);
        System.out.println("sumToAfter: " + sumToAfter);

        BigDecimal sumFromAfter1 = new AccountManager().getAccById(idFrom1).getAmount();
        BigDecimal sumToAfter1 = new AccountManager().getAccById(idTo1).getAmount();

        System.out.println("sumFromAfter1: " + sumFromAfter1);
        System.out.println("sumToAfter1: " + sumToAfter1);

        System.out.println("TIME: " + (System.currentTimeMillis() - timeStart));
        TestCase.assertTrue(sumFromBefore.add(sumToBefore).compareTo(sumFromAfter.add(sumToAfter)) == 0);

    }


}
