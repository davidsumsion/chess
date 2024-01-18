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

























