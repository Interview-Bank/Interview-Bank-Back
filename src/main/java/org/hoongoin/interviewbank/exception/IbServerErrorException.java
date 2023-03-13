package org.hoongoin.interviewbank.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class IbServerErrorException extends RuntimeException{

    public IbServerErrorException(String message){
        super(message + " Internal Server Error");
        log.info(message + " Internal Server Error");
    }
}
