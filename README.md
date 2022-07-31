# Invoice microservice
## This service is used for generating billing invoices for given clients based on client's billing cycle, transaction status ,fees etc.
### Project Structure

![img.png](img.png)

### Pre requisite (Java 8 +,maven 3+ ,mysql )

### Steps
#### Clone https://github.com/ybdahe/invoice.git
#### Import as maven project
#### run mvn clean Install -DskipTests or mvn clean Install command
#### run SpringBootInvoiceApplication.java will start spring boot application on 8080 port
#### Schedulers can be enabled by uncommenting @EnableScheduling annotation in SpringBootInvoiceApplication.java
##### Schedulers cron as below
![img_12.png](img_12.png)
#### Test Actuators after application up http://localhost:8080/actuator/health
![img_1.png](img_1.png)

#### Test Swagger link http://localhost:8080/swagger-ui/

![img_2.png](img_2.png)

## Create Data
###1:- Create Client
#### Api :- http://localhost:8080/api/clients

![img_3.png](img_3.png)

###2:- Get Clients
#### Api :- http://localhost:8080/api/clients

![img_4.png](img_4.png)

###3:- Create Transaction
#### Api :- http://localhost:8080/api/transactions

![img_5.png](img_5.png)

###4:- Get Transactions
#### Api :- http://localhost:8080/api/transactions

![img_6.png](img_6.png)

## generate Invoice
###1:- Call http://localhost:8080/api/invoice?clientName=Pizza House 1

![img_7.png](img_7.png)

###1:- Call again same api with same client will give duplicate invoice error with 204 http://localhost:8080/api/invoice?clientName=Pizza House 1

![img_8.png](img_8.png)

![img_9.png](img_9.png)

### Sonar Scan result :- Zero issue left , all resolved

![img_10.png](img_10.png)

### Unit Test case and Code Coverage result
Note:- 61% current coverage can extend >80 %
(Due to less time not able to cover all code).

![img_11.png](img_11.png)

### Dockerized (see Docker file in classpath) :- Can be improved
#### docker build -t invoice.
#### docker run -p 8080:8080 -t invoice

### Postman Collection attached(see in classpath)
File Invoices.postman_collection.json


