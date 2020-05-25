# Read Me

## 🙉 Fixed issues in this update
* Issue: System may lag when loading many clients
*     use lazy initialisation in ClientImpl class to mitigate lag.

* Issue: Handing client contact methods is messy
*     use Chain of Responsibility to make the contact order explicit.

## 🙉 Issues to be fixed
* Issue: Report class stored too much data and used too much RAM.
*     use Flyweight pattern to reduce data storage 

* Issue: Too much Order classes.
*     Use Bridge pattern

* Issue: Complex comparison process of Report objects
*     Design indexing (primary key) to compare，HashMap, Value Object??

* Issue: Slow database operations in Order creation
*     Simplify the process, use Prototype, UOW??

* Issue: Single threading is not useful.
*     database processes and other functions. Thread Pool.

## Speical Notes for scope
1. Changes are in-scope as they are only made in feaa package excluding ReportDatabase class.
2. Changes made out of scope are mainly for testing purposes to record the RAM and time used for comparisons. They are written outside the in-scope classes temporarily for intance the CLI class, and they are not meant to be permanent and should be commented out or deleted afterwards.
3. The source code maintain a green status to pass all tests in AllowedScopeTest class.

## Style Guide

Code style: Google Java Style Guide https://google.github.io/styleguide/javaguide.html

Code style tooling: https://github.com/google/google-java-format

The style guide is only used for coding style and documentation is applied to methods and classes at the discretion of the developer.