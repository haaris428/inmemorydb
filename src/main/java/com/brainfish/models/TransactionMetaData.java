package com.brainfish.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TransactionMetaData
{
    private TransactionStateCommand stateCommand;
    private String key;
    private String value;
}
