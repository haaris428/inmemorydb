package com.brainfish.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction
{
    private String key;
    private String value;
    public Transaction(String key){
        this.key = key;
    }
}
