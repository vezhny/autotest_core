# Autotest Core
This is a Test Automation project, if you need a quick start from scratch.  
Useful for juniors, has examples.  
  
## How to run tests  

Here is the core software you need to run tests:

- [JDK](https://www.oracle.com/java/technologies/downloads/#java17-linux)
- [Maven](https://maven.apache.org/download.cgi)

After installation of these two, you can run tests by entering maven commands into project terminal.

#### Commands:
Simple test run for profile stand1  
` mvn clean test -Pstand1 allure:report `  

Run tests from package  
` mvn clean test -Pstand1 -Dtest="com.vezh.lab.api.**"" allure:report `  

Run tests with specific tags  
` mvn clean test -Pstand1 -Dgroups=API allure:report `  

Allure report location: _target/site/allure-maven-plugin/index.html_

## About

### Maven profiles  
By default, the project has two _Maven profiles_, which you can change\add\remove as you wish.
But also, the project works on _Spring Boot_. Which means, that all the changes in _Maven Profiles_ must correspond to .yml files.  
#### Example
If there is a **_stand1_** profile, then there is a **application-_stand1_.yml** file

### Allure links
Except simple links, _Allure_ has a kind of smart ones. There are few types of them:
- wiki
- issue
- tms

They all described in _src/main/resources/allure.properties_  

#### Example
You have a bug in _Jira_ with id JB-512
If you want to mark some test in _Allure_ report with a link to that bug, you write:  
` @Link(type = "issue", value = "JB-512") `  
_Allure_ will fetch all the _URL_ to your _Jira_ from `allure.link.issue.pattern` and append there your bug id. 
So, you will open this ticker from _Allure_ report right away.

#### Bug annotation
For bugs there is a shortcut — `@Bug` annotation (_com.vezh.lab.core.annotation.Bug_).  
` @Bug("JB-512") `  
It does the same as ` @Link(type = "issue", value = "JB-512") ` and also adds a tag _JB-512_ to the test in _Allure_ report.  
It can be put on a test method or on a test class.

### Screenshots
If UI test fails, there will be taken a **screenshot** and attached into _Allure_ report. 
They also being saved in _/build/reports/tests/_  
If you want to configure that, see UiTestListener.java