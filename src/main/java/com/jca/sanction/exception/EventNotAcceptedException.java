package com.jca.sanction.exception;

public class EventNotAcceptedException extends RuntimeException {

  public EventNotAcceptedException(Object event, Object state) {
    super("Event " + event + " is not accepted for sanction in state " + state);
  }
}
