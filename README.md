# ![HtmlUnit Logo](https://github.com/HtmlUnit/htmlunit/blob/master/src/site/resources/images/htmlunit.png)

Version 4.10.0 / February 22, 2025

:heart: [Sponsor](https://github.com/sponsors/rbri)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit)
[![OpenSSF Scorecard](https://api.securityscorecards.dev/projects/github.com/HtmlUnit/htmlunit/badge)](https://securityscorecards.dev/viewer/?uri=github.com/HtmlUnit/htmlunit)

### Homepage

[htmlunit.org][4]

### News

**[Developer Blog](https://htmlunit.github.io/htmlunit-blog/)**

[HtmlUnit@mastodon](https://fosstodon.org/@HtmlUnit) | [HtmlUnit@bsky](https://bsky.app/profile/htmlunit.bsky.social) | [HtmlUnit@Twitter](https://twitter.com/HtmlUnit)

> **Check out HtmlUnit [satellite projects](https://github.com/orgs/HtmlUnit/repositories)**,
such as:
> * [HtmlUnit on android](https://github.com/HtmlUnit/htmlunit-android)
> * [HtmlUnit for .Net](https://github.com/HtmlUnit/NHtmlUnit)
> * or [our Rhino fork](https://github.com/HtmlUnit/htmlunit-rhino-fork) (the JS engine)
>
> Note as well that you can use HtmlUnit with [Selenium](https://www.selenium.dev/) via
> their [htmlunit-driver](https://github.com/SeleniumHQ/htmlunit-driver)!


## Sponsoring

Constantly updating and maintaining the HtmlUnit code base already takes a lot of time.

I would like to make 2 major extensions in the next few months
* [Add HTTP/2 support](https://github.com/HtmlUnit/htmlunit/issues/370)
* [Replace the Rhino based JavaScript engine](https://github.com/HtmlUnit/htmlunit/issues/755)

For doing this I need your [sponsoring](https://github.com/sponsors/rbri).

## Get it!

* [Download from GitHub][12]
* [Download from Sourceforge][1]
* [Release History](https://www.htmlunit.org/changes-report.html)

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.htmlunit</groupId>
    <artifactId>htmlunit</artifactId>
    <version>4.10.0</version>
</dependency>
```

### Gradle

Add to your `build.gradle`:

```groovy
implementation group: 'org.htmlunit', name: 'htmlunit', version: '4.10.0'
```

## Vulnerabilities

[List of Vulnerabilities](https://github.com/HtmlUnit/htmlunit/blob/master/CVE.md)

[Security Policy](https://github.com/HtmlUnit/htmlunit/blob/master/SECURITY.md)

## Overview
HtmlUnit is a "GUI-less browser for Java programs". It models HTML documents and provides an API that allows you to invoke pages, fill out forms, click links, etc... just like you do in your "normal" browser.

It has fairly good JavaScript support (which is constantly improving) and is able to work even with quite complex AJAX libraries, simulating Chrome, Firefox or Internet Explorer depending on the configuration used.

HtmlUnit is typically used for testing purposes or to retrieve information from web sites.

## Features
* Support for the HTTP and HTTPS protocols
* Support for cookies
* Ability to specify whether failing responses from the server should throw exceptions or should be returned as pages of the appropriate type (based on content type)
* Support for submit methods POST and GET (as well as HEAD, DELETE, ...)
* Ability to customize the request headers being sent to the server
* Support for HTML responses
  * Wrapper for HTML pages that provides easy access to all information contained inside them
  * Support for submitting forms
  * Support for clicking links
  * Support for walking the DOM model of the HTML document
* Proxy server support
* Support for basic and NTLM authentication
* Excellent JavaScript support

HtmlUnit is used as the underlying "browser" by different Open Source tools like
 * [WebDriver](https://github.com/SeleniumHQ/selenium)
 * [Arquillian Drone](https://arquillian.org/arquillian-extension-drone)
 * [Serenity BDD](https://serenity-bdd.info)
 * [FluentLenium](https://github.com/FluentLenium/FluentLenium)
 * [WETATOR](https://www.wetator.org/)
 * [Selenium Foundation](https://github.com/sbabcoc/Selenium-Foundation)
 * [Spring Testing](https://docs.spring.io/spring/docs/current/spring-framework-reference/testing.html#spring-mvc-test-server-htmlunit)
 * [Selenide](https://selenide.org/)
 * [JWebUnit](https://jwebunit.github.io/jwebunit/)
 * [JSFUnit](http://www.jboss.org/jsfunit/)
 * ...

HtmlUnit is used by many projects for automated web testing
 * [jenkins-test-harness](https://github.com/jenkinsci/jenkins-test-harness)
 * [Apache Shiro](https://shiro.apache.org/)
 * [Apache Struts](https://struts.apache.org/)
 * [Quarkus](https://quarkus.io/)
 * [Togglz](https://www.togglz.org/)
 * [Dataverse](https://dataverse.org/)
 * [Janssen Project](https://github.com/JanssenProject/jans)
 * [Apache TomEE](https://github.com/apache/tomee)
 * [Apache Maven Surefire](https://maven.apache.org/surefire/)
 * [JSCover](http://tntim96.github.io/JSCover/)
 * [Apache Jackrabbit](https://jackrabbit.apache.org/jcr/index.html)
 * [Apache MyFaces](https://myfaces.apache.org/)
 * [JakartaEE TCK](https://github.com/jakartaee/platform-tck)
 * [Jakarta Security](https://github.com/jakartaee/security)
 * [OpenXava](https://github.com/openxava/openxava)
 * [Cargo](https://github.com/codehaus-cargo/cargo)
 * [piranha cloud](https://github.com/piranhacloud/piranha)
 * ...

## Getting Started
You can start here:
* [Getting Started][7]
* [Introduction to HtmlUnit - Baeldung](https://www.baeldung.com/htmlunit)
* [The Java Web Scraping Handbook][8] A nice tutorial about webscraping with a lot of background information and details about HtmlUnit.
* [Web Scraping][9] Examples how to implement web scraping using HtmlUnit, Selenium or jaunt and compares them.
* [The Complete Guide to Web Scraping with Java][10] A small straightforward guide to web scraping with Java.
* [How to test Jakarta Faces with HtmlUnit and Arquillian][11]
* [WebScraping.AI HtmlUnit FAQ][13]: 

## Contributing
Pull Requests and all other Community Contributions are essential for open source software.
Every contribution - from bug reports to feature requests, typos to full new features - are greatly appreciated.

Please try to keep your pull requests small (don't bundle unrelated changes) and try to include test cases.

## Last CI build
The latest builds are available from our
[Jenkins CI build server][2]

[![Build Status](https://jenkins.wetator.org/buildStatus/icon?job=HtmlUnit+-+Headless)](https://jenkins.wetator.org/job/HtmlUnit%20-%20Headless/)

Read on if you want to try the latest bleeding-edge snapshot.

### Maven

Add the snapshot repository and dependency to your `pom.xml`:

```xml
    <!-- ... -->
    <repository>
      <id>OSS Sonatype snapshots</id>
      <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
      <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
      </snapshots>
      <releases>
        <enabled>false</enabled>
      </releases>
    </repository>

    <!-- ... -->
    <dependencies>
      <dependency>
          <groupId>org.htmlunit</groupId>
          <artifactId>htmlunit</artifactId>
          <version>4.11.0-SNAPSHOT</version>
      </dependency>
      <!-- ... -->
    </dependencies>

    <!-- ... -->
```

### Gradle

Add the snapshot repository and dependency to your `build.gradle`:

```groovy
repositories {
  maven { url "https://s01.oss.sonatype.org/content/repositories/snapshots" }
  // ...
}
// ...
dependencies {
    implementation group: 'org.htmlunit', name: 'htmlunit', version: '4.11.0-SNAPSHOT'
  // ...
}
```


## License

This project is licensed under the Apache 2.0 License


## Development

### useful mvn command lines
setup as or refresh the eclipse project

```
mvn eclipse:eclipse -DdownloadSources=true
```

run the whole core test suite (no huge tests, no libary tests)

```
mvn test -U -P without-library-and-huge-tests -Dgpg.skip -Djava.awt.headless=true
```

check dependencies for known security problems

```
mvn dependency-check:check
```


## Some insights
[HtmlUnit at openhub][5]

### Stargazers
[![Stargazers](https://starchart.cc/HtmlUnit/htmlunit.svg)](https://starchart.cc/HtmlUnit/htmlunit)


[1]: https://sourceforge.net/projects/htmlunit/files/htmlunit/4.1.0/ "HtmlUnit on sourceforge"
[2]: https://jenkins.wetator.org/view/HtmlUnit/ "HtmlUnit CI"
[4]: https://www.htmlunit.org "https://www.htmlunit.org"
[5]: https://www.openhub.net/p/HtmlUnit "https://www.openhub.net/p/HtmlUnit"
[7]: https://www.htmlunit.org/gettingStarted.html
[8]: https://www.scrapingbee.com/java-webscraping-book/
[9]: https://www.innoq.com/en/blog/webscraping/
[10]: https://www.webscrapingapi.com/java-web-scraping/
[11]: http://www.mastertheboss.com/java-ee/jsf/how-to-test-jakarta-faces-with-htmlunit-and-arquillian
[12]: https://github.com/HtmlUnit/htmlunit/releases
[13]: https://webscraping.ai/faq/htmlunit