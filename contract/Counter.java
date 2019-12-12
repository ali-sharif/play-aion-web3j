package org.aion.web3;

import avm.Blockchain;

import org.aion.avm.tooling.abi.Callable;
import org.aion.avm.tooling.abi.Initializable;

import java.math.BigInteger;

public class Counter
{
    @Initializable
    private static int count;


    @Callable
    public static void incrementCounter(int increment){
        count += increment;
        Blockchain.log("CounterIncreased".getBytes(), BigInteger.valueOf(increment).toByteArray());
    }

    @Callable
    public static void decrementCounter(int decrement){
        count -= decrement;
        Blockchain.log("CounterDecreased".getBytes(), BigInteger.valueOf(decrement).toByteArray());
    }

    @Callable
    public static int getCount(){
        return count;
    }

}
