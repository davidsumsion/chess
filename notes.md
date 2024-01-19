# CS 240
#### Dr. Wilkerson


## 1/10

Online Chess Server
Write good tests
Do the readings
- Deeper

Core Java for the Impatient, Third Edition
O’Reilly Books Online Subscription from the library
- Canvas/Syllabus

Read Policies

Some of the phases take 40+ hours
- Given a couple weeks to do it
- Structured course for right when you need it

Course Github
Policies Canvas page
Complete Github project assignment
Readings for the first two days of class

About 12 hours a week w/ lecture

Intelli J
- IDE
- Community edition

Chess Github
- 10 commits per phase spread evenly out (maybe even every day)

mkdir gitTest && cd gitTest
git init
ls -la
mate t1.txt
ls
git add t1.txt
git status
git commit -m “First Commit”
Add stages it, commit puts it locally in git, push puts it in remotely
git log
mate t1.txt
git commit -m ‘2nd commit’
Git add, commit
git diff HEAD~1 HEAD
git checkout 845395
git branch
git checkout main
git branch
git log
git branch myNewBranch 8bcd
git checkout myNewBranch
mate t1.txt
git checkout main
git branch -d myNewBranch
git branch
git checkout -b anotherBranch 8bcd -created and switched to new branch
git branch
rm t1.txt
git commit -m “removed file”




## 1/16 


### Javadoc
Documentation
java 21 api

/**
Javadoc comment (format w/ HTML)
*/
public class myclass {
    /**
    Summary (everything up to the first .)
    everything else is included.
    @param describes parameters
    @return describes what's returned
    */
    public void mymethod(){

    }
}

### primitive datatypes
byte 8 bits, short 16 bits, int df 32 bits, long 64 bits, float, double df, char, boolean

Java fundementals, see code

System.out.println() -- convert non strings to strings
System.out.printf() -- %d, fill ins. strings

int length()
char charAt()
String trim()
boolean startsWith(String)
int indexOf(int) (normally put a char instead)
int indexOf (String)
String substring(int)
String substring(int, int) chunk of string

string concatenation is really slow
StringBuilder = new StringBuilder();
builder.append("words");
String str = builder.toString();


## 1/18

### Command-Line Arguments
Packages
- organizes packages into logical grouping
- dots are folder seperaters
- java.lang is implicityly imported (contains String)

reading a Text File of Words
- [skipped]

### Classes and Objects
- hour of videos to watch before next class period
- string and array constructor are the only 2 exceptions, all other objects need new keyword
  - Date dt;
  - dt = new Date();
- override the .equals method -- og for equals same memory location
  - code, generate (equals and hash method)
  - if (!(o instanceof Person)) return false
  - Person person = (Person) o;
  - Objects.equals(firstName, person.firstName) && ...

### Instance vs static variables
Instance Vars
- each object gets its own copy of the variable
- Allows two date objects to represent different dates
Static Vars
- being in the class not the instance
- objects don't get their own copy, stored in class

Instance Methods
- each object gets its own copy of the code theoretically
- not really tho
Static Methods
- can't access instance variables
- 

### Getters/Setters
Should make variables private instance vars only available from instance 
- create get/set methods if needed outside of that
- IDE generates them
  - code, generate, getter and setter, specify which ones
  - public

### Constructor methods
much match the class name, including case
CamelCase (classes)
methods are notCamelCase
no return type (can't specify void)
- don't return void
All classes have at least one (default constructor, no parametes on defualt), can have multiple
Constructors invoke each other with this(...)
- invoke parent class with super(...)
- if you do this(...) or super(...) it needs to be the first statement in the constructor class
  - needs to be one or the other every time
 



References
Directory names, packages
static


























