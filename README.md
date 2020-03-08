# HtmlUnit

HtmlUnit is a "GUI-Less browser for Java programs". It models HTML documents and provides an API that allows you to invoke pages, fill out forms, click links, etc... just like you do in your "normal" browser.

It has fairly good JavaScript support (which is constantly improving) and is able to work even with quite complex AJAX libraries, simulating Chrome, Firefox or Internet Explorer depending on the configuration used.

It is typically used for testing purposes or to retrieve information from web sites.

### Project Hoepage
[htmlunit.sourceforge.net][4]

### Project News
[HtmlUnit@Twitter][3]

### Latest release Version 2.38.0 / March 8, 2020
[Download from Sourceforge][1]

For maven, you would add:

    <dependency>
        <groupId>net.sourceforge.htmlunit</groupId>
        <artifactId>htmlunit</artifactId>
        <version>2.38.0</version>
    </dependency>

### Latest CI build
The latest builds are available from our
[Jenkins CI build server][2]

If you use maven please add:

    <dependency>
        <groupId>net.sourceforge.htmlunit</groupId>
        <artifactId>htmlunit</artifactId>
        <version>2.38.0-SNAPSHOT</version>
    </dependency>

You have to add the sonatype snapshot repository to your pom distributionManagement section also:

    <snapshotRepository>
        <id>sonatype-nexus-snapshots</id>
        <url>https://oss.sonatype.org/content/repositories/snapshots</url>
    </snapshotRepository>


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
* Excellent JavaScript support (see the JavaScript section below)



## License

This project is licensed under the Apache 2.0 License


[1]: https://sourceforge.net/projects/htmlunit/files/htmlunit/2.38.0/ "HtmlUnit on sourceforge"
[2]: https://jenkins.wetator.org/job/HtmlUnit/ "HtmlUnit CI"
[3]: https://twitter.com/HtmlUnit "https://twitter.com/HtmlUnit"
[4]: http://htmlunit.sourceforge.net/ "http://htmlunit.sourceforge.net/"
