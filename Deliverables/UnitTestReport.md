# Unit Testing Documentation

Authors:

Date:

Version:

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

 ### **Class *DAOproductType* - method *Create(ProductType prod)***


**Criteria for method *Create(ProductType prod)*:**
 - Insert product in DB 


**Predicates for method *Create(ProductType prod)*:**

| Criteria | Predicate |
| -------- | --------- |
| Insert product in DB    |    use unique barcode       |
|          |    use not unique barcode       |
|          |    use non negative quanity       |
|          |    use negative quantity       |

**Combination of predicates**:


| Criteria 1 | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|*|Valid|prod5 = new ezProductType(1, "testProd5", "123456789055", 5.0, 5, "testNote5", "5-5-5");|testCreateProduct()
||Invalid|prod5 = new ezProductType(0, "testProd5", "123456789011", 5.0, 5, "testNote5", "5-5-5");|testCreateProductWithDuplicateBarCode()
||Invalid|prod5 = new ezProductType(0, "testProd5", "123456789055", 5.0, -5, "testNote5", "5-5-5");|testCreateProductWithNegativeQuantity()
|||

 ### **Class *DAOproductType* - method *read(Integer prodId)***


**Criteria for method *read(Integer prodId)*:**
 - Read product from DB 


**Predicates for method *read(Integer prodId)*:**

| Criteria | Predicate |
| -------- | --------- |
| Read product from DB     |    product present in DB       |
||product missing from DB|

**Combination of predicates**:


| Criteria 1 | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|
|*|Valid|prod = DAOproductType.read(1)|testReadProductId()
||Invalid|prod = DAOproductType.read(10);|testReadMissingProductId()
|||

### **Class *DAObalanceOperation* - method *Create(BalanceOperation bo)***


**Criteria for method *Create(BalanceOperation bo)*:**
 - Insert BalanceOperation into DB 


**Predicates for method *read(Integer prodId)*:**

| Criteria | Predicate |
| -------- | --------- |
| balance Id     |    duplicate balance id       |
||unique balance id|
| money sign     |    money >= 0       |
||money <0|


**Combination of predicates**:


| balance id | money sign | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
|duplicate|*|Invalid|DAObalanceOperation.Create(credit)|testCreateBOWithWrongId()
|unique|>= 0|Valid|DAObalanceOperation.Create(credit)|testCreateBOWith0Money(), testCreateCredit()
|unique|< 0|Valid|DAObalanceOperation.Create(debit)|testCreateDebit()
|||


### **Class *DAObalanceOperation* - method *readAll()***


**Criteria for method *readAll()*:**
 - read all Balance operations from DB


**Predicates for method *read(Integer prodId)*:**

| Criteria | Predicate |
| -------- | --------- |

<b>

**Combination of predicates**:


| Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|
|Valid|DAObalanceOperation.readAll()|testReadAllBalanceOperation()
|||

 ### **Class *class_name* - method *name***



**Criteria for method *name*:**
	

 - 
 - 





**Predicates for method *name*:**

| Criteria | Predicate |
| -------- | --------- |
|          |           |
|          |           |
|          |           |
|          |           |





**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |
|          |                 |



**Combination of predicates**:


| Criteria 1 | Criteria 2 | ... | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|||||||
|||||||
|||||||
|||||||
|||||||


# White Box Unit Tests

### Test cases definition
    
    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|||
|||
||||

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



