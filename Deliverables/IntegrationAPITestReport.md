# Integration and API Test Documentation

Authors: Marcelo Coronel, Mostafa Asadollahy, Tommaso Natta, Zissis Tabouras 

Date: 27/05/2021

Version:

# Contents

- [Dependency graph](#dependency-graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>

<img src="./images/IntegrationTesting/dependencyGraph.png" alt="Login Manager" width="1200"/>

    
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>



#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

     Approach : Bottom-up

## Step 1
| Classes  | JUnit test cases |
|--|--|
|DAObalanceOperation|TestDAObalanceOperation|
|DAOcustomer|TestDAOcustomer|
|DAOloyaltyCard|TestDAOloyaltyCard|
|DAOorder|TestDAOorder|
|DAOproductType|TestDAOproductType|
|DAOlocation||
|DAOreturnEntry|testDAOReturnTransaction|
|DAOreturnTransaction|testDAOReturnTransaction|
|DAOsaleEntry||
|DAOsaleTransaction||
|DAOuser||
|DAOcc||
|||


## Step 2
| Classes  | JUnit test cases |
|--|--|
|EzShop, DAObalanceOperation| testBalanceOperationApi|
|EzShop, DAOorder|testOrderApi|
|EzShop, DAOReturnTramsaction, DAOreturnEntry| testReturnTransactionApi|
|||
|||


## Step n 

   

| Classes  | JUnit test cases |
|--|--|
|||




# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |  
|  2     |  ... |



# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|  ..         | FRx                             |             |             
|  ..         | FRy                             |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|                            |           |


