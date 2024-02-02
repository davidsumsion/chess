## 1/23
Interface is like a contract
Static vs nonstatic, public private (236, haven't cared), final
interface in different file?
C++
read board, copy from powerpoints
checked/vs unchecked exceptions
mutable
static
try w/ resources

### Java Records
POJOs
- significant amount of code, a few lines of code, the ide can generate the rest of it
- boiler flake code
- Lombok library
  - records built off it

public record Pet(int id, String name, String type) {}
  - 52 lines of code reduced to one 
  - final -- no setter, getter is p.name() would return name for pet. not p.getName()!
  - immutable

### Programming Exam
- study for the exam
  - look at your code, see if there's a way to simplify and shorten it
  - don't memorize all of it
- practice zipping your files!!
- anything linked from phase 0 assignment
- no pseudo code
- no chess code

### Exceptions & Exception Handling in Java
- Errors are one thing that they are (not necessarially errors)
- abnormal conditions that occur in java programming
- Seperate errors from exceptions
- [INSERT PARTIAL CLASS HIERARCHY]
- Throwable
  - Error (computer can't recover)
    - VirtualMachineError
      - OutOfMemoryError
    - LinkageError
      - NoClassDefFoundError
    - Many Others
  - Exception
    - RuntimeException (bugs in code) -- fix your code if you have one
      - NullPointerException
      - IndexOutOfBoundsExceptions
      - Many Others
    - (everthing else checked exceptions) (yellow)
      - IOException 
        - try to read a file and the file doesn't exist, shouldn't crash your program
          - want to handle it, tell user that something abnormal happened -- can't open it or the file is corrupted
      - Many Others
- Syntax
  - try
    - code that could throw an exception
    - if i can't do y if x doesn't work, then include x and y in the same try block
  - catch
    - handle the code if exception thrown
    - make the problem go away if theres a catch block
    - continue on ofter catch block
    - pops off the stack if it can't find the catch, crashes your program
    - catch (IOException ex){
    - } catch (otherExcpetion ex){
    - }
  - Handle (try & catch)
  - Declare (throws exception)
    - Say throws if you don't want to write a catch block
    - have to have throws if you call code that includes an exception
  - Finally
    - always implemented 
    - after try/catch block on the same block

## 1/25

### Try with Resources
- try (stuff in parenthesis) {}
- open things that need to be closed later, automatically closed, don't need to write a finally block
- shows the exception you want to see (like file opening), if you you had a normal try and finally then it would give you the close file exception because there wasn't a fopen file and you want th eopen on  to debug

### Exceptions
- throw new myExcetpion("Message");
  - new exception
  - want to create it when you have an error, allocate and construct it on the same line
- Don't swallow Runtime Exceptions
  - make sure to catch RuntimeExceptions and rethrow them so then you can catch (Exception ex)
  - only one catch block will run unless you throw it again
- overiding methods can throw
  - fewer exeptions than the overriden method
  - the same exceptionsas the overridden method
  - sublasses of excetpitons thrown by the overriden method
  - Runtime exceptions and any runtime excpetion sublasses
  - errors and any error subclasses
- Creating own Exception Classes

### Java Collections API
- List interface
  - sequence of elements accessed by index
    - get(index), set(index, value)
  - ArrayList (resizable array implementation)
  - LinkedList (doubly linked list implmementation)
  - lists support a more powerful iterator called a ListIterator
    - can loop through and change data and won't run into any undefineds
  - Depricated: Vector, problems with it
- Set
  - Collection w/ no duplicates, no order
    add(value), contains(value), remove(value)
  - can't add something that's already implmented (throws an exception)
  - HashSet - gen purpose
  - TreeSet - sorted set
  - LinkedHashSet - linked hash
- Queue Interface
  - storing things that we plan to process in the future
  - add(value), remove() top element, peek() tells you what the top element is that would be removed
  - ArrayDeque(fifo, resizable array impl) most common
  - LinkedList
  - PriorityQueue
- Reference the interface! Performance, what sections
- Deque Interface
  - AddFirst(value), addLast(value), peekFirst(), peekLast(), removeFirst(), removeLast()
- Stack Class in Java, sublclass of Vector
  - Don't use it, depricated!!
  - threadsafe, multiple threads in programs, becomes slow
  - based on the old class Vector -- 
  - use deque instead push, addfirst; pop, deque.removefirst(), deque.peekFirst()
- Map Interface
  - put(k,v), get(key), contains(key), remove(key), keySet(), values(), entrySet()
  - HashMap
    - HashTable based on vector -- don't use
  - TreeMap inherent order
  - linkedHashMap - hashtable + linked list
- Iterable Interface
  - for each, no index
    - Set<String> words;
    - for (String w : words) { ... }

### How to use Collections properly
- Equality Checking
  - equals matters, contains calls equals, override the equals method or be ok with reference equality
  - JDK has already overwritten them, date class
- Hashing-based collections
  - override the hashcode method, it will just be very inefficient, might as well as have an array if you don't overwrite it
  - don't change info in objects that are used as keys or you'll lose it, remove it and the value change the key and readd it so you don't lose it
- Sorted Collections
  - TreeSet, TreeMap PiorityQueue
    - make sure items are comparable
      - implement comparable or comparator interface
    - implement comparable interface
    - Override comparteTo class

###Copying Objects
- Support undo capability in a program
- Shallow copy
- Deep Copy
  - copy the object and all of its references, recursively
  - Immutable objects don't need to be copied and can be safely shared
    - strings
    - integer, boolean, double (object versions of the primitives)
    - etc
- Writing classes that support copying
  - copy constructors
  - clone method on each class
    - if you want to make an object cloneable, impelment Cloneable on class
      - override clone() method to make it public because its not public 
        - return super.clone()
    - create shallow copy first
    - then change mutable variables inside of clone
  - Person clone = (Person) person.clone() -- shallow copy
  - 
    

## 1/30
### Inner Classes
- Iterator, whilehasnext
  - Iterator interface
  - Iterator class located inside of datatypes (such as array)
- Inner Classes
  - can (should) be declared as private
  - static: dont have special powers
    - you can call the functions w/out making an instance of the class
    - nonstatic members are members of the class
  - method in class that has a class in it and returns an instance of that class
    - can access local variables
    - creates a copy of the variables, only copy if it's declared as final or never changed
  - anonymous inner classes
    - public Iterator iterator(int increment){
      - return new Iterator(){
    - for GUI 
### Phase 1, Chess Game
- lots of methods
- isInCheck, checkmate, stalemate
- validMoves- can't put your own king in check!
- makeMove
- isInCheck
- isInCheckMate
  - are you in check
  - look at all the moves that you can leagally make, check each one to see if you're in check
  - clone the board, apply the moves, iterate the moves, 
    - expensive
  - apply move and unapply move
    - move the piece, then unapply it
    - more complicated
- Stalemate
  - no valid moves, you're in stalemate
### Design Principles
- Great systems
  - work
  - easy to understand, debug and maintain
  - hold up well under changes
  - have reusable components
- Design is inherently Iterative
  - building all the blueprints works for bridges and buildings, but not for software
  - enough design to get started
    - implement to see if it worked
    - do more design
    - keep doing those iterations, never done
- BDUF - big design up front, doesn't work
- can't just start coding, enough design before
- DITD, Design, implemenet, test, deploy
- Abstraction: how we deal with complex things
  - create higher-level, domain-specefic abstractions and write software in terms of those
  - ChessPiece
  - sometimes Bank, customer, account, loan, broker
  - other times HttpServer, DataBase, HashTable
  - abstractions represented as a class
    - someone can call your methods without knowing exactly how it was made, by the description or by the name
  - Person class
    - pulse, heart-rate, fingerprint -- do we care for chess? do we care for FBI system or HealthCare System
  - Naming matters
    - Class and var names are usually nouns
    - method names are usually verbs
  - Single-Responsibility/Cohesion
    - Each class/method should have 1 reason for existing and should do one thing
  - Decomposition (thursday)


## 2/1
### Abstraction
- Decomposition
  - tames complexity
  - Large problems subdivided into smaller sub-problems
  - levels
    - system
    - substystem
    - packages
    - classes
    - methods
  - Trie Data Structure

### Streams and Files
- when you write to your server
- phases 5&6 you'll need to have user input
- Streams (general, from server, from file)
  - read bytes from source of bytes
  - InputStreams, OutputStreams
    - reading/writing bytes
    - fileInputStream
    - PipedInputStream
    - URLConnection.getInputStream()
    - HttpExchange.getRequestBody() -- use something else now
    - ResultSet.getBinaryStream(int column index)
    - more, plus corresponding output stream
  - Filter Input Streams
    - attach different streams
    - can read a zipped file 
  - Output Streams
    - similar to input streams
  - Data input/output streams
  - Reader, Writer
    - reading/writing characters
    - inputstreamreader -- copper/pvc pipe connecter
- Scanner Class, tokenized input by anything you tokenize by
  - default: whitespace
  - scanner + system.in waits for user input
- Files class, ready, copy, whole files
  - represents a file but it doesn't read the file
  - pases as a parameter
  - [insert photo!]
  - Path path = Paths.get(file.getPath());
  - List<String> filecontents = Files.readAllLines(path);
  - return fileContents
  - Java.nio
- Random AccessFile Class
  - seek(long), skipBytes(int)

### Information Hiding
- public
- private
  - implementation details 
  - you can change it
- protected

### DRY principle
- don't repeat yourself
- don't use duplicate code
  - 2 points of maintainence when you have 2 copies
  - 