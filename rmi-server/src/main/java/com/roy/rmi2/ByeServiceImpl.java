package com.roy.rmi2;

/**
 * @author Roy
 */
public class ByeServiceImpl implements IByeService {
    public String bye(String msg) {
        return "bye,"+msg;
    }
}
