

## 2/13
Implementing a Web API with spark
- Spark Server, make endpoints available
- Specify URLs that map to lambda expressions
- send HTTP back and forth
- NOT apache spark
- import spark.Spark;
- public class SimpleHelloBYUServer {
  - public static void main(String[] args) {
    - Spark.get("/hello", (req, res) -> Hello BYU)}}
  - Default port is 4567
    - can specify it
- Request
  - body - retreive the request body
  - headers - retrieve all headers () or specefic header -- auth token, what data format, how long
- Reespone
  - body - st the response body -- JSON string
  - status(404) - set the status code to 404
Serve Static Files/Web App
- set up server to serve static files
- spark.staticFiles.location("/resources/web") -- has to be on the class path of your project
  - resources directory (name whatever you want) want to mark as resources root
    - web subdirectory (match 2 lines above what is in the parenthesis)
Filters
- map certain things that happen before or after a route
- in create routes
  - before specify some lambda
Make Spark Java Available
  - set up a Maven project
    - package manager
  - Add the dependency from File/Project Structure

PHASE 3
- Server, class, call it what you want and then call spark
- User dao = new MemoryUserDao();
- dao.insertuser
- in phase 4, just change MemoryUserDao() to SQLUserDao
- Interface for UserDAO, MemoryUserDAO implements UserDAO
- Models
  - simple basic classes to represent data
  - record classes
- Tips
  - handler can vaildate the authtoken
  - Desearlize JSO request to java request object
  - call service class to perform the requested funciton, passing in the java reqeust object
  - receive java esponse object from service
  - Serialize Java response object to JSon
  - Send HTTP ((FINISH LINE))
  - don't have to reinstantiate RegisterHandler every time
    - use static method(difficult to avoid code duplication accross handlers)
    - create a static getInstance() method that always returns the same instance
  - Handler logic
    - gson.from Json(reqData, LoginRequest.class);
    - view slides
  - avoid code duplication
    - use inheritance from handlers and services
  - Server implemntation approach (SLIDES)
    - review info on writing test (Dr. Wilkerson'll talk about this soon)
  - UUID.randomUUID().toString() -- generate AuthTokoen
-  