This project provides the backend service of the ECNU postgraduate management system.

## Guidence

1. Understand our **[team culture][4]** before starting your development!

2. [yjsy-wiki][1] contains all development, depolyment and automation related documents.
    + **[onboard][2]** contains all **must-read-firstly** information for a new engineer.
    + [skill][3] contains all development skills.  


### Installation

Finish the steps bellow to setup your development environment for the backend service.

1. Install [Git][5], Oracle Java 8, [Apache Maven 3.3.x][6] and Eclipse

2. Register yourself to our [Gitlab][7]

3. [Generate your SSH Key][8]

4. Open shell and run `$ git clone git@58.198.176.57:${your_account}/yjsy.git`

5. Import `yjsy` project into Eclipse:
    + Open Eclipse
    + Select "Import..." -> "Maven" -> "Existing Maven Projects"
    + Click "Next" button and choose the folder which contains our projects
    
6. Open shell and run `$ mvn spring-boot:run` to start our Web Application.

7. Open shell and run `$ curl -X GET http://localhost:8888`

[1]: http://58.198.176.57/xulinhao/yjsy-wiki
[2]: http://58.198.176.57/xulinhao/yjsy-wiki/tree/master/onboard
[3]: http://58.198.176.57/xulinhao/yjsy-wiki/tree/master/skill
[4]: http://58.198.176.57/xulinhao/yjsy-wiki/tree/master/onboard/culture.md
[5]: https://git-scm.com/downloads
[6]: https://maven.apache.org/install.html
[7]: http://58.198.176.57
[8]: http://guides.beanstalkapp.com/version-control/git-on-windows.html


### Welcome and happy coding!

