

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
- Response
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
- UserDAO dao = new MemoryUserDao(); -- hide that it's a memoryuserDAO
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



Phase 3
- Spark passes info to handler (everything related to HTTP and JSON), use GSON to create a Request object (could write as a record - one line of code or a pojo)
- service has the login in it
- static instance method, getInstance, you 
  - singletons
  - static: don't get a new copy everytime you call an instance
    - from different objects share the same variable
    - declare var as static
    - class.variable or instance.var returns the same thing
- Variation designs
  - write handlers into server lambdas
- spark.staticfilelocation
- cURL

Quality code
- 


### 
- Untested Code is broken code
  - F22,
  - write a little code and test it, over and over
  - Large pieces consist of smaller pieces
    - Unit is a generic term for these smaller pieces
    - Unit testing - test units in isolation
      - developer written
    - Integration Testing - test integrated unites
      - odd for developer to right
    - System Testing - test entire system that is fully inegrated
      - developer seldom rights these
  - regression testing
    - fix a but but create 3 more bugs
  - Test driver program
    - litte or no labor
    - easy to add more tests
    - JUnit Testing Framework
      - @BeforeEach
      - @BeforeAll
      - @AfterEach
      - @AfterAll
      - Group Tests with package level
      - Before, During, After???
    - Code Coverage
      - enough tests? can only tell you the minimum hurdle
  - Function Coverage
    - No universally accepted right answer on coverage percentage?
      - most things need to be, getters and setters don't need to be
  - tools
    - Propriety / built in to IDE's like Intellij
    - Cobertura: Open-source, command line
    - JaCoCo: open-source command line but has intellij plugin
    - Parasoft JTest: Nice, but expensive
    - Many others
    - 