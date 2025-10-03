# ![HtmlUnit Logo](https://github.com/HtmlUnit/htmlunit/blob/master/src/site/resources/images/htmlunit.png)

Version 4.16.0 / August 29, 2025

:heart: [Sponsor](https://github.com/sponsors/rbri)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.htmlunit/htmlunit)
[![OpenSSF Scorecard](https://api.securityscorecards.dev/projects/github.com/HtmlUnit/htmlunit/badge)](https://securityscorecards.dev/viewer/?uri=github.com/HtmlUnit/htmlunit)


### Homepage

[htmlunit.org](https://www.htmlunit.org)

### News

**[Developer Blog](https://htmlunit.github.io/htmlunit-blog/)**

[HtmlUnit@mastodon](https://fosstodon.org/@HtmlUnit) | [HtmlUnit@bsky](https://bsky.app/profile/htmlunit.bsky.social) | [HtmlUnit@Twitter](https://twitter.com/HtmlUnit)


## Table of Contents
- [Overview](#overview)
- [Get it!](#get-it)
  - [Maven](#maven)
  - [Gradle](#gradle)
- [Getting Started](#getting-started)
- [Features](#features)
  - [Selenium Integration](#selenium-integration)
  - [jsoup Bridge](#jsoup-bridge)
  - [HtmlUnit satellite projects](#htmlunit-satellite-projects)
  - [Built on HtmlUnit](#built-on-htmlunit)
- [Vulnerabilities](#vulnerabilities)
- [Sponsoring](#sponsoring)
- [Contributing](#contributing)
- [Last CI build](#last-ci-build)
  - [Maven](#maven-1)
  - [Gradle](#gradle-1)
- [License](#license)
- [Development](#development)
- [Some insights](#some-insights)

## Overview
HtmlUnit is a "GUI-less browser for Java programs". It models HTML documents and provides an API that allows you to invoke pages, fill out forms, click links, etc... just like you do in your "normal" browser.

It has fairly good JavaScript support (which is constantly improving) and is able to work even with quite complex AJAX libraries, simulating Chrome, Firefox or Edge depending on the configuration used.

HtmlUnit is typically used for testing purposes or to retrieve information from web sites.


## Get it!

* [Download from GitHub](https://github.com/HtmlUnit/htmlunit/releases)
* [Download from Sourceforge](https://sourceforge.net/projects/htmlunit/files/htmlunit/)
* [Release History](https://www.htmlunit.org/changes-report.html)

### Maven

Add to your `pom.xml`:

```xml
<dependency>
    <groupId>org.htmlunit</groupId>
    <artifactId>htmlunit</artifactId>
    <version>4.16.0</version>
</dependency>
```

### Gradle

Add to your `build.gradle`:

```groovy
implementation group: 'org.htmlunit', name: 'htmlunit', version: '4.16.0'
```

## Getting Started
You can start here:
* [Getting Started](https://www.htmlunit.org/gettingStarted.html)
* [Introduction to HtmlUnit - Baeldung](https://www.baeldung.com/htmlunit)
* [The Java Web Scraping Handbook](https://www.scrapingbee.com/java-webscraping-book/) A nice tutorial about webscraping with a lot of background information and details about HtmlUnit.
* [Web Scraping](https://www.innoq.com/en/blog/webscraping/) Examples how to implement web scraping using HtmlUnit, Selenium or jaunt and compares them.
* [The Complete Guide to Web Scraping with Java](https://www.webscrapingapi.com/java-web-scraping/) A small straightforward guide to web scraping with Java.
* [How to test Jakarta Faces with HtmlUnit and Arquillian](http://www.mastertheboss.com/java-ee/jsf/how-to-test-jakarta-faces-with-htmlunit-and-arquillian)
* [WebScraping.AI HtmlUnit FAQ](https://webscraping.ai/faq/htmlunit)


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

### Selenium Integration

HtmlUnit can be used as a [Selenium](https://www.selenium.dev/) 
[WebDriver](https://www.selenium.dev/documentation/webdriver/)-compatible browser through the 
[htmlunit-driver](https://github.com/SeleniumHQ/htmlunit-driver).
This integration allows you to use HtmlUnit as a headless browser option within Selenium test suites, 
providing fast execution without the overhead of launching a full browser instance.

Please have a look at the [HtmlUnit Remote](https://github.com/HtmlUnit/htmlunit-remote) project 
if you like to use this driver from Selenium 4 Grid.


### jsoup Bridge

The [htmlunit-jsoup](https://github.com/HtmlUnit/htmlunit-jsoup) library provides utilities 
to bridge the gap between [HtmlUnit](https://htmlunit.org) and [jsoup](https://jsoup.org/).
The `HtmlUnitDOMToJsoupConverter` enables seamless integration between HtmlUnit's comprehensive
browser simulation capabilities and all the jsoup-based libraries,
allowing you to leverage the full ecosystem of jsoup tools 
while maintaining HtmlUnit's JavaScript execution and dynamic content handling.


### HtmlUnit [satellite projects](https://github.com/orgs/HtmlUnit/repositories)

* [HtmlUnit on android](https://github.com/HtmlUnit/htmlunit-android)
* [Htmlunit - NekoHtml Parser](https://github.com/HtmlUnit/htmlunit-neko)
* [HtmlUnit - CSSParser](https://github.com/HtmlUnit/htmlunit-cssparser)
* [HtmlUnit - CSP](https://github.com/HtmlUnit/htmlunit-csp)
* or [core-js](https://github.com/HtmlUnit/htmlunit-core-js) our [Rhino](https://github.com/mozilla/rhino) fork


### Built on HtmlUnit

HtmlUnit is used as the underlying "browser" by different Open Source tools like
 * [WebDriver](https://github.com/SeleniumHQ/selenium)
 * [Arquillian Drone](https://arquillian.org/arquillian-extension-drone)
 * [Serenity BDD](https://serenity-bdd.info)
 * [XLT](https://www.xceptance.com/en/products/xlt/)
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

## Vulnerabilities

[List of Vulnerabilities](https://github.com/HtmlUnit/htmlunit/blob/master/CVE.md)

[Security Policy](https://github.com/HtmlUnit/htmlunit/blob/master/SECURITY.md)


## Sponsoring

Constantly updating and maintaining the HtmlUnit code base already takes a lot of time.

I would like to make 2 major extensions in the next few months
* [Add HTTP/2 support](https://github.com/HtmlUnit/htmlunit/issues/370)
* [Replace the Rhino based JavaScript engine](https://github.com/HtmlUnit/htmlunit/issues/755)

For doing this I need your [sponsoring](https://github.com/sponsors/rbri).

## Contributing
Pull Requests and all other Community Contributions are essential for open source software.
Every contribution - from bug reports to feature requests, typos to full new features - are greatly appreciated.

Please try to keep your pull requests small (don't bundle unrelated changes) and try to include test cases.

## Last CI build
The latest builds are available from our
[Jenkins CI build server](https://jenkins.wetator.org/view/HtmlUnit/)

[![Build Status](https://jenkins.wetator.org/buildStatus/icon?job=HtmlUnit+-+Headless)](https://jenkins.wetator.org/job/HtmlUnit%20-%20Headless/)

Read on if you want to try the latest bleeding-edge snapshot.

### Maven

Add the snapshot repository and dependency to your `pom.xml`:

```xml
    <!-- ... -->
    <repository>
        <name>Central Portal Snapshots</name>
        <id>central-portal-snapshots</id>
        <url>https://central.sonatype.com/repository/maven-snapshots/</url>
        <releases>
            <enabled>false</enabled>
        </releases>
        <snapshots>
            <enabled>true</enabled>
        </snapshots>
    </repository>

    <!-- ... -->
    <dependencies>
      <dependency>
          <groupId>org.htmlunit</groupId>
          <artifactId>htmlunit</artifactId>
          <version>4.17.0-SNAPSHOT</version>
      </dependency>
      <!-- ... -->
    </dependencies>

    <!-- ... -->
```

### Gradle

Add the snapshot repository and dependency to your `build.gradle`:

```groovy
repositories {
  maven { url "https://central.sonatype.com/repository/maven-snapshots/" }
  // ...
}
// ...
dependencies {
    implementation group: 'org.htmlunit', name: 'htmlunit', version: '4.17.0-SNAPSHOT'
  // ...
}
```


## License

This project is licensed under the Apache 2.0 License


## Development

Checkout these pages on our website for detailed hints about starting with the development:
 * [HtmlUnit Development](https://www.htmlunit.org/development.html) 
 * [Coding Conventions](https://www.htmlunit.org/codingConventions.html)



## Some insights
[HtmlUnit at openhub](https://www.openhub.net/p/HtmlUnit)

### Stargazers
[![Stargazers](https://starchart.cc/HtmlUnit/htmlunit.svg)](https://starchart.cc/HtmlUnit/htmlunit)
