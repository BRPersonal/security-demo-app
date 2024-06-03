# fs-labs-security-demo-app
A sample spring boot app that shows how to use security component

Use Postman to copy the Jwt token that received at SignIn and then 
access these urls with the token

#Access user resource
GET http://localhost:8081/demo

#Access Admin resource
GET http://localhost:8081/admin_only

#Access seeker resource
GET http://localhost:8081/seeker

#If security server in local machine port 8080
gradle bootRun

#if security server is running in different server port 9000
gradle bootRun -PjvmArgs="-DSECURITY_SERVER_HOST=66.175.211.212 -DSECURITY_SERVER_PORT=9000"