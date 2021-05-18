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
|          |    quantity >= 0    |
|          |    quantity < 0    |

**Combination of predicates**:


| Criteria | Predicate | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| * | Unique barcode, quantity>=0 | Valid | DAOproductType.Create(prod); | testCreateProduct() |
|  | not unique barcode | Invalid | DAOproductType.Create(prod); | testCreateProductWithDuplicateBarCode() |
|  | quantity < 0 | Invalid | DAOproductType.Create(prod); | testCreateProductWithNegativeQuantity() |
 ### **Class *DAOproductType* - method *read(Integer prodId)***


**Criteria for method *read(Integer prodId)*:**
 - Read product from DB 


**Predicates for method *read(Integer prodId)*:**

| Criteria | Predicate |
| -------- | --------- |
| Read product from DB     |    product present in DB       |
||product not present in DB|

**Combination of predicates**:


| Criteria | Predicate | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|
| * | product present | Valid | prod = DAOproductType.read(1) | testReadProductId() |
|          | product missing | Invalid | prod = DAOproductType.read(10) | testReadMissingProductId() |
 ### **Class *DAOproductType* - method *Update(prod)***

**Criteria for method *Update(prod)*:**

 - Update product in DB 

**Predicates for method *Update(prod)*:**

| Criteria             | Predicate                 |
| -------------------- | ------------------------- |
| Update product in DB | product present in DB     |
|                      | product not present in DB |

**Combination of predicates**:


| Criteria | Predicate       | Valid / Invalid | Description of the test case | JUnit test case            |
| -------- | --------------- | --------------- | ---------------------------- | -------------------------- |
| *        | product present | Valid           | DAOproductType.Update(prod); | testReadProductId()        |
|          | product missing | Invalid         | DAOproductType.Update(prod); | testReadMissingProductId() |



 ### **Class *DAOproductType* - method *Delete(prod)***

**Criteria for method *Delete(prod)*:**

 - Delete product from DB 

**Predicates for method *Delete(prod)*:**

| Criteria               | Predicate                 |
| ---------------------- | ------------------------- |
| Delete product from DB | product present in DB     |
|                        | product not present in DB |

**Combination of predicates**:


| Criteria | Predicate       | Valid / Invalid | Description of the test case | JUnit test case                |
| -------- | --------------- | --------------- | ---------------------------- | ------------------------------ |
| *        | product present | Valid           | DAOproductType.Delete(prod)  | testDeleteProductById()        |
|          | product missing | Invalid         | DAOproductType.Delete(prod)  | testDeleteProductByMissingId() |

 ### **Class *DAOloyaltyCard* - method *Create(card)***

**Criteria for method *Create(card)*:**

 - Insert loyalty card in DB 

**Predicates for method *Create(card)*:**

| Criteria                  | Predicate       |
| ------------------------- | --------------- |
| Insert loyalty card in DB | card points >=0 |
|                           | card points <0  |
|                           | customer Id >=0 |
|                           | customer Id <0  |

**Combination of predicates**:


| Criteria | Predicate                       | Valid / Invalid | Description of the test case | JUnit test case                                |
| -------- | ------------------------------- | --------------- | ---------------------------- | ---------------------------------------------- |
| *        | card points >=0, customer id >=0 | Valid           | DAOloyaltyCard.Create(card)  | testCreateProduct()                            |
|          | card points <0                  | Invalid         | DAOloyaltyCard.Create(card)  | testCreateLoyaltyaCardWithNegativePoints()     |
|          | customer id <0                 | Invalid         | DAOloyaltyCard.Create(card)  | testCreateLoyaltyaCardWithNegativeCustomerId() |





 ### **Class *DAOloyaltyCard* - method *Read(String cardId)***

**Criteria for method *Read(String cardId)*:**

 - read loyalty card from DB 

**Predicates for method *Read(String cardId)*:**

| Criteria                  | Predicate              |
| ------------------------- | ---------------------- |
| read loyalty card from DB | card present in DB     |
|                           | card not present in DB |

**Combination of predicates**:


| Criteria | Predicate              | Valid / Invalid | Description of the test case | JUnit test case              |
| -------- | ---------------------- | --------------- | ---------------------------- | ---------------------------- |
| *        | card present in DB     | Valid           | DAOloyaltyCard.Read(card_id) | testReadLoyaltyCard()        |
|          | card not present in DB | Invalid         | DAOloyaltyCard.Read(card_id) | testReadMissingLoyaltyCard() |





 ### **Class *DAOloyaltyCard* - method *Update(card)***

**Criteria for method *Update(card)*:**

 - update loyalty card in DB 

**Predicates for method *Update(card)*:**

| Criteria                  | Predicate              |
| ------------------------- | ---------------------- |
| update loyalty card in DB | card present in DB     |
|                           | card not present in DB |

**Combination of predicates**:


| Criteria | Predicate              | Valid / Invalid | Description of the test case | JUnit test case              |
| -------- | ---------------------- | --------------- | ---------------------------- | ---------------------------- |
| *        | card present in DB     | Valid           | DAOloyaltyCard.Update(card)  | testReadLoyaltyCard()        |
|          | card not present in DB | Invalid         | DAOloyaltyCard.Update(card)  | testReadMissingLoyaltyCard() |



 ### **Class *DAOloyaltyCard* - method *Delete(card)***

**Criteria for method *Delete(card)*:**

 - Delete loyalty card from DB 

**Predicates for method *Delete(card)*:**

| Criteria                    | Predicate              |
| --------------------------- | ---------------------- |
| Delete loyalty card from DB | card present in DB     |
|                             | card not present in DB |

**Combination of predicates**:


| Criteria | Predicate              | Valid / Invalid | Description of the test case | JUnit test case         |
| -------- | ---------------------- | --------------- | ---------------------------- | ----------------------- |
| *        | card present in DB     | Valid           | DAOloyaltyCard.Delete(card); | testDeleteLoyaltyCard() |
|          | card not present in DB | Valid           | DAOloyaltyCard.Delete(card); | testDeleteLoyaltyCard() |



















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


**Predicates for method *readAll()*:**

No criteria because there is no input
<br>

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



