# Design Document 


Authors: Marcelo Coronel, Mostafa Asadollahy, Tommaso Natta, Zissis Tabouras 

Date: 28 april 2021

Version: #1


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

The <u><b>MVC</b></u> Design Pattern is used to perform separation of concers between the GUI and the model

The <u><b>3 tier</b></u> architecture is used to better separate the GUI from the Application Logic and the DB data (i.e. the Presentation should never interact directly with the DB but only throught the application logic in the middle.)
<report package diagram>
```plantuml
@startuml
left to right direction
package GUI
package Model_and_Application_Logic as MC
GUI ..> MC
```



# Low level design

<for each package, report class diagram>

MC package:

TODO: ~~FR1~~ (maybe user class), ~~FR3~~, ~~FR4~~, ~~FR5~~, <b>FR6</b>, ~~FR7~~, FR8,  (Ticket class)

<u><b>FR6</b></u>:  ~~FR6.1~~, ~~FR6.2~~, ~~FR6.3~~, ~~FR6.4~~, ~~FR6.5~~, ~~FR6.6~~, FR6.7,  ~~FR6.8~~, ~~FR6.9~~, ~~FR6.10~~, ~~FR6.11~~, ~~FR6.12~~, ~~FR6.13~~, ~~FR6.14~~, ~~FR6.15~~

```plantuml
left to right direction


class Shop{
    accountBalance
    productTypes
    saleTransactions
    ()_FR1
    -boolean : defineUser()
    -boolean : UpdateUser()
    -boolean : DeleteUser()
    -List    : getAllUser()
    -User    : getUser()
    -boolean : setRights(UserID,right)
    ()_FR3
    Integer : createProductType()
    boolean : updateProduct()
    boolean : deleteProductType()
    List : getAllProductTypes()
    ProductType : getProductTypeByBarCode()
    List : getProductTypesByDescription()
    ()_FR4
    boolean : updateQuantity() <of product in store>
    boolean : updatePosition() <of product in store>
    Integer : issueReorder()
    Integer : payOrderFor()
    boolean : payOrder()
    boolean : recordOrderArrival()
    List : getAllOrders()
    ()_FR6
    Integer   : startSaleTransaction() '1 <creates instance>
    boolean   : addProductToSale() '2
    boolean   : deleteProductFromSale() '3
    boolean   : applyDiscountRateToSale() '4
    boolean   : applyDiscountRateToProduct() '5 
    int       : computePointsForSale() '6
    -product   : searchProductViaBarCode() '7
    -boolean   : printSaleTicket() '8
    Ticket    : getTicketByNumber() '9
    boolean   : closeSaleTransaction() '10
    Integer   : startReturnTransaction() '12
    boolean   : returnProduct() '13
    boolean   : endReturnTransaction() '14
    boolean   : deleteReturnTransaction() '15
    boolean   : deleteSaleTicket()
    ticket    : getSaleTicket()
    int       : computePointsForSale()
    boolean   : closeSaleTransaction()
    ()_FR7
    double  : receiveCashPayment()
    boolean : receiveCreditCardPayment()
    double  : returnCashPayment()
    double  : returnCreditCardPayment()

}

class AccountBook{
    ()_FR8
    boolean: recordBalanceUpdate() '1,'2
    List: getCreditsAndDebits() '3
    double: computeBalance() '4
}
AccountBook - Shop

class FinancialTransaction {
    description
    amount
    date
}
AccountBook -- "*" FinancialTransaction

class Credit 
class Debit

Credit --|> FinancialTransaction
Debit --|> FinancialTransaction

class Order
class Sale
class Return

Order --|> Debit
Sale --|> Credit
Return --|> Debit


class ProductType{
    barCode
    description
    sellPrice
    quantity
    discountRate
    notes
}

Shop - "*" ProductType

class SaleTransaction {
    ID 
    date
    time
    cost
    paymentType
    discount rate
    loyalityCardCodes
    customerIds
    ()_FR5
    Integer : defineCustomer() ' <returns customer id>
    boolean : modifyCustomer()
    boolean : deleteCustomer()
    Customer : getCustomer()
    List : getAllCustomers()
    String : createCard()  ' <returns card code>
    boolean : attachCardToCustomer()
    Integer  : modifyPointsOnCard()



}
Shop --"*" SaleTransaction
SaleTransaction - "*" ProductType

class SaleTicket{
    ticketNumber
}
SaleTransaction -- SaleTicket

class Quantity {
    quantity
}
(SaleTransaction, ProductType)  .. Quantity

class LoyaltyCard {
    ID
    points
}

class Customer {
    name
    surname
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" -- "0..1" LoyaltyCard

class Product {
    
}

class Position {
    aisleID
    rackID
    levelID
}

ProductType - "0..1" Position

ProductType -- "*" Product : describes

class Order {
  supplier
  pricePerUnit
  quantity
  status
}

Order "*" - ProductType

class ReturnTransaction {
  quantity
  returnedValue
}

ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType

note "ID is a number on 10 digits " as N1  
N1 .. LoyaltyCard
note "bar code is a number on 12 to 14  digits, compliant to GTIN specifications, see  https://www.gs1.org/services/how-calculate-check-digit-manually " as N2  
N2 .. ProductType
note "ID is a unique identifier of a transaction,  printed on the receipt (ticket number) " as N3
N3 .. SaleTransaction

```







# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>

||Shop|Account<br/>Book|Account<br/>Book|Financial<br/>Transaction|Credit|Debit|Order|Sale|Return|Product<br/>Type|Sale<br/>Transaction|Quantity|Loyalty<br/>Card|Customer| Product | Position  | Order | Return<br/>Transaction | Sale<br/>Ticket
|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|-------|------|-----|----|----|
| FR.1 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |
| FR.3 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |
| FR.4 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |
| FR.5 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |
| FR.6 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |
| FR.7 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |
| FR.8 | X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |X |









# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

