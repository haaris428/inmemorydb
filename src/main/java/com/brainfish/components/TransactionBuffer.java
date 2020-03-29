package com.brainfish.components;

import com.brainfish.models.Transaction;
import com.brainfish.models.TransactionMetaData;
import com.brainfish.models.TransactionStateCommand;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Stack;


@NoArgsConstructor
@Data
public class TransactionBuffer
{
    private Stack<TransactionMetaData> commandsBuffer;

    public void init()
    {
        if(commandsBuffer == null) {
            commandsBuffer = new Stack<>();
        }

    }

    public void addToBuffer(Transaction transaction){
        try
        {
            commandsBuffer.push(new TransactionMetaData(TransactionStateCommand.SET,
                    transaction.getKey(),
                    transaction.getValue()));

        } catch (OutOfMemoryError outOfMemoryError){
            System.out.println("Run out of memory, please commit transaction");
        }
    }

    public void removeFromBuffer(Transaction transaction){
        try {
            commandsBuffer.push(new TransactionMetaData(TransactionStateCommand.DELETE,
                    transaction.getKey(),
                    transaction.getValue()));

        } catch (OutOfMemoryError outOfMemoryError){
            System.out.println("Run out of memory, please commit transaction");
        }
    }

    public void updateBuffer(Transaction transaction, String oldValue)
    {
        commandsBuffer.push(new TransactionMetaData(TransactionStateCommand.UPDATE,
                    transaction.getKey(),
                    oldValue));
    }
}
