## SANCTION API

<p>This is an excersise to practice the integration with Spring state machine. This service will simulate a service that can create multiples types of sanctions as follows: </p>

1. Political
2. Economical
3. Military
4. Legal

<p>Sanctions could be applied from to :</p>

1. Institution
2. Person
3. Country

## STATE MACHINE

Here are the possible states for a sanction:

![stateMachine](/documentation/stateMachine.jpg)

## FEATURES

- CRUD for sanctions
- Actions
    - Activate
    - Approve
    - Reject
    - Inactivate
- Open API contracts TBD
- Security layer with Auth0 and spring security JWT Tokens TBD

## TECH STACK

- Java 17
- Spring boot 3
- Mysql
- State Machine 3.2.0