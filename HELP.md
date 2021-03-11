# Spring Security

* To add a login page add this to pom.xml
    -     <dependency>
    			<groupId>org.springframework.boot</groupId>
    			<artifactId>spring-boot-starter-security</artifactId>
    		</dependency>
    		
* Every thing with security must do inside the bellow class because it defined with
@EnableWebSecurity annotation.

    ApplicationSecurityConfig 
    
    this class must extends WebSecurityConfigurerAdapter class and there are few override
    methods. We can access them by clicking the Ctrl+O. (we can get this methods with alt+insert -> override also)
    
    first we are going to check with basic auth: (configure(HttpSecurity http)) -> this gives a popup login by browser.
    
* If we want to access pages without permissions we can add them by giving antMatchers() inside the upper basic auth
 method. To work this permitAll() also required.
     
      -  .antMatchers("/", "index", "/css/*", "/js/*" )
         .permitAll()
         
* PasswordConfig class encode the passwords

* we add guava maven dependency for permission
 - <dependency>
   			<groupId>com.google.guava</groupId>
   			<artifactId>guava</artifactId>
   			<version>28.1-jre</version>
   		</dependency>
   		
* csrf() used to control un authorized 3rd party accessing our web side like hackers. this works only for the browser
client so if you are not a browser client make sure to disable this inside ApplicationSecurityConfig class. If you are using 
csrf for non browser like postman, make sure to add the cookies for headers. to do this, active postman interceptor in top 
of the right hand side of postman and we can see the cookie as XSRF-TOKEN copy it and paste in headers as key -> X-XSRF-TOKEN
and value -> cookie that we copied.

* themyleaf is a template engine to help do things with HTML files
 
* if you have multiple services it is good to use JWT(jeson web token) otherwise you can use form base authentication