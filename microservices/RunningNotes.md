Microservices:
it was an Architecture

Monolithic vs microservices

Advantages of Microservices:
Scalability
Independent
Fault Tolerance
Faster Development
Ease in Understanding code
Technology Feasability
loose Coupled

Disadvatnages:
Testing/Logging is Difficult
Maintanance is high
Cost
management id Difficult
Data Inconsistency
deployment

Food Delivery Application

	Restaurants
		id
		name
		address
		phone num
		Items
		rating
		
		
		address: 
			id
			landmark
			city
			pincode
			state
			country
			
		Item:
			id 
			name
			restaurent id
			price
			is available
			category
			type
			rating
Orders
Order
id
user id
restaurent id
status
List of Order Item
Order price
Order Item
id
item id
quantity


	Users
		Users
			id
			name
			phone num
			list address
			list orders
			email
			password
			
	
	Delivery Partners
	
		delivery person
			id
			name
			aadhar
			phone
			email
			isavailble
			list<deliveryAssignmnets>
			rating
			
		Devivery Assignment
			assignmentID
			Order_id
			DeliveryPerson ids
			status
			assigned Time



Dependencies:
spring web
data JPA
sql driver
Lambok
devtools
security(for Security)


onetomany or manytoone should be identifed and added as per it


in properties file
need to add server port
and db config details

Builder pattern(we can use from @Builder coming from Lambok)

Inter Service Communication:

Used to call other service using api calls

	Syncronous
		RestTemplate
		FiegnClient
		
		Order->restaurent
		order need to wait till restaurent give the response then it will go to next line
		
	Asynchronous
		WebClient
		
		order -> NotificationManagement
		order not dependent on NotificationManagement
		it can be done by RabbitMQ/Kafka


RestTemplate:
used to talk with other service with Syncronous api call

	getForObject vs GetForEntity
	here we will pass URL and response Return Data Type
	
	postForObject/postForEntity
	here we need to pass URL,object/body to save, return type
	
	Exchange/Put

Discovery Services(spring cloud)

Eureka - used to monitor all services at one place

need spring web and eurka server/eurka web client dependecies
Eureka server
its spring boot application without any code
add @EnableEurekaServer in main application
in properties file:
add eurka.client.rigister.with.eurka=false
eurka.client.fetch.rigisty=false
server.port=8761

Eureka Cleint
in properties file:
add eurka.client.rigister.with.eurka=true
eurka.client.fetch.rigisty=true
eureka.client.service-url.defaultZone=http://localhost:8761/eurka/

we can access from
localhost:8761/eurka/

eureka will collect heartbeats for every 60/90 seconds and  retry again then if service not found it will show as service down

============
Spring Cloud Load Balancing
It will follow Round Robin by Default
we have other Techniques also like
Availabilty Zone
Random

order -> Restaurant(8002,8003,8004,8005)

	Cleint Lb-
		RestTemplate, FiegnClient, gRPc Cleint
		
		RestTemplate.getForObject(URL);
		
	Server LB-
		AWS, Azure, Kubernate pods
		it was costlier

when you set server.port=0
it will run on random port

eureka.instance.instance-id=${spring.application.name}:${spring.instance.instance-id:${random.value}}
to set the name in eureka server

@LoadBalanced in config class at bean RestTemplate
to enable load balancing

in impl we can remove localhost:8002
only give "http"//RestaurantManagement/Order

API Gateway LB is handles by server LB

RestTemplate Disadvatnages:
manual load balancing
exchange -> method
return type

Open FiegnClient

we have Advantced RestTemplate called OPen FiegnClient
it internally handles load balancing
url

we need not to call exchange

Add dependecy OpenFiegn
add @EnableFeignClients in main application
by default it was desabled

add an interface for service client you want to call like Restaurant
and add @FiegnClient(name="service Name") then copy the end point method declaration from controller of that service

in current serivice impl autowire the interface service you created and call the method it will get the data

===========================================
Profiles

is used to connect with diff environments

application.yml
in main yml spring.profiles.active=qa
used to swich b/w ymls

application-dev.yml->8000, h2
application-prod.yml ->9000, mysql
application-qa.yml->8500, postgres

===========================================
Config - Server

having all yml/properties files related to project

it was fetch the details from external source and give them to clients

we store them in
file system
AWS
Git Hub
yml files

	we generally store in github

Config - Cleints
connect to config servers

Git Hub -> config server-> config clients

for example applicationName-dev.yml

Config-Server
add dependecies

	spring web
	actuator
	config server
add @EnableConfigServer

and in properties

server.port=8888

spring.cloud.config.server.git.uri=git url(yml files location)
spring.cloud.config.server.git.clone-on-start=true
it was Eager initialization if we wont add it will be lazy
spring.cloud.config.server.git.default-lable=main

to check it
http://localhost:8888/Restaurant/dev

Config -client
in application like restaurent management
add config client and actuator dependecies

and
spring.config.import=optional:configserver:http://localhost:8888


managenemt.endpoints.exposure.include=refresh
used to fetch dynamic values from external source

like flipkart->discount

http://localhost:8000/actuator/refresh

===================================

API Gateway

we have many services in microservices
it is a spring boot application
Gateway-> it routes the request to underlying service

from front end every request we call with 8000 port it will redirect to underlying serivice
it automatically supports load balancing

NetFlix Zuul(Old)-> non reactive

Spring Cloud Gateway-> reactive

spring can be done in two ways
reactive -> asyc
mono-> single
flux-> List
non reactive -> synchronous

we will enable jwt verification at gateway

create api Gateway project and add dependecies
Erueka discovery client
gateway from spring cloud

in properties
port=8500
add eruka properties
gateway routing details
spring.cloud.gateway.server.webflux.routes[0].id= RestaurantManagement
spring.cloud.gateway.server.webflux.routes[0].uri=lb://RESTAURANTMANAGEMENT(Name From Eureka)
spring.cloud.gateway.server.webflux.routes[0].predicate[0]=path=/Restaurants/**

=========================================

Circuit Breaker

Hystrix(OLD)

Resliance 4j

when one service is goes down while other service is calling this we open circuit braker

we have 3 states in circuit braker
closed -> every thing running smoth
we can configure threadshold(suppose 50%) to open the circuit
5/10 calls failed it will move to open state
we set some time to when to move half open -10s
it will test few calls in half open state and move states to closed/opened
half open
open -> sent as fall back messages
like pament service is down please retry after sometime


in application add
spring cloud circuit braker
actuator to check the circuit braker state
add add properties
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.register-health-indicator=true
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.sliding-window-size=5
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.failure-rate-thresholf=50
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.wait-duration-in-open-state=10s
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.permitted-number-of-calls-in-half-open-state=2
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.automatic-transition-from-open-to-half-open-enabled=true
resilance4j.circuitbreaker.instances.restaurentmanagenetCB.minimun-number-of-call=3

		management.endpoints.web.exposure.include=*

in service impl class at method level
add @CircuitBreaker(name="circuitbreakerName",fallbackMethod="fallbackForRestaurentName")
public String fallbackForRestaurentName(long restaurentID, Throwable throwable){
return "Restaurant Service is down";
}
note:
method return type should match with the annotated method return type and add throwable in method arguments


================================
Unit Testing vs Integration Testing
Unit Testing
junit5
Mockito
Integration Testing
end to end testing

@SpringBootTest

@Test

@ExtendWith(MockitoExtension.class)
@Mock

@BeforeEach
@AfterEach
@BeforeAll
@AfterALL
@Disabled
@injectMocks



	
		
		

