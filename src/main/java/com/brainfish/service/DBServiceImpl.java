package com.brainfish.service;

import com.brainfish.components.InMemoryDB;
import com.brainfish.components.TransactionBuffer;
import com.brainfish.models.Transaction;
import com.brainfish.models.TransactionMetaData;
import com.brainfish.models.TransactionStateCommand;
import java.util.Stack;


public class DBServiceImpl implements DBService
{
    private final InMemoryDB inMemoryDB = InMemoryDB.getInstance();
    private Stack<TransactionBuffer> transactionBuffer = new Stack<>();
    private InMemoryDB bufferDB;

    @Override
    public void set(String key, String value)
    {
        if (!transactionBuffer.isEmpty()) {
            if (bufferDB.getInMemoryDB().containsKey(key)){
                //if bufferDb has this key maintain history for state machine
                transactionBuffer.peek().updateBuffer(new Transaction(key, value),
                        bufferDB.getItem(key));

            } else {
                transactionBuffer.peek().addToBuffer(new Transaction(key, value));
            }
            bufferDB.addItem(key,value);
        } else {
            inMemoryDB.addItem(key,value);
        }
    }

    @Override
    public String get(String key)
    {
        if (!transactionBuffer.isEmpty()){
            return bufferDB.getItem(key);
        } else {
            return inMemoryDB.getItem(key);
        }
    }

    @Override
    public Integer count(String value)
    {
        int count = 0;
        if (bufferDB == null) {
            count = Math.max(inMemoryDB.getCountIndex().getOrDefault(value, 0), 0);
        }
        else {
            count = Math.max(bufferDB.getCountIndex().getOrDefault(value, 0), 0);
        }
        return count;
    }

    @Override
    public void delete(String key)
    {
        if (!transactionBuffer.isEmpty()){
            transactionBuffer.peek().removeFromBuffer(new Transaction(key, bufferDB.getItem(key)));
            bufferDB.deleteItem(key);
        }else {
            //if buffer still has
            inMemoryDB.deleteItem(key);
        }
    }

    @Override
    public void beginTransaction()
    {
        TransactionBuffer transactionBuffer = new TransactionBuffer();
        transactionBuffer.init();
        if (bufferDB == null) {
            bufferDB = InMemoryDB.getInstance();
            bufferDB.getInMemoryDB().putAll(inMemoryDB.getInMemoryDB());
            bufferDB.getCountIndex().putAll(inMemoryDB.getCountIndex());
        }

        this.transactionBuffer.push(transactionBuffer);
    }

    @Override
    public void rollbackTransaction()
    {
        if (!transactionBuffer.isEmpty()){
            Stack<TransactionMetaData> meta =this.transactionBuffer.pop().getCommandsBuffer();
            while (!meta.isEmpty()){
                TransactionMetaData transactionMetaData = meta.pop();
                if (transactionMetaData.getStateCommand() == TransactionStateCommand.UPDATE){
                    bufferDB.updateItem(transactionMetaData.getKey(), transactionMetaData.getValue());
                } else if (transactionMetaData.getStateCommand() == TransactionStateCommand.SET){
                    bufferDB.deleteItem(transactionMetaData.getKey());
                } else {
                    bufferDB.addItem(transactionMetaData.getKey(), transactionMetaData.getValue());
                }
            }
        } else {
            System.out.println("TRANSACTION NOT FOUND");
        }
    }

    @Override
    public void commitTransaction()
    {
        inMemoryDB.getInMemoryDB().putAll(bufferDB.getInMemoryDB());
        inMemoryDB.getCountIndex().putAll(bufferDB.getCountIndex());
    }
}
