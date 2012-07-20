To configure Hudson to build HtmlUnit using Amazon EC2:

1. Set up the local environment

	set EC2_BASE=C:\path\to\ec2\base
	set EC2_HOME=%EC2_BASE%\ec2-api-tools-<version>
	set PATH=%PATH%;%EC2_HOME%\bin
	set EC2_PRIVATE_KEY=%EC2_BASE%\<pk-pem-file>
	set EC2_CERT=%EC2_BASE%\<cert-pem-file>

2. Start a virgin Fedora 8 instance

	ec2-run-instances ami-2b5fba42 -k <keypair-name>

3. Check the IP of the new EC2 instance

	ec2-describe-instances

4. Configure the EC2 instance

	4.a. SSH into the EC2 instance
		PUTTY.EXE -ssh -i %EC2_BASE%\<ppk-file> root@<server-ip>

	4.b. Install the Sun JDK

		wget http://<sun-java-path>/jdk-6u17-linux-i586-rpm.bin
		chmod 755 jdk-6u17-linux-i586-rpm.bin
		./jdk-6u17-linux-i586-rpm.bin

	4.c. Make sure that the Sun JDK is the default JDK

		updatedb
		locate javac | grep bin
		/usr/sbin/alternatives --install /usr/bin/java java /usr/java/jdk1.6.0_17/bin/java 100
		/usr/sbin/alternatives --install /usr/bin/jar jar /usr/java/jdk1.6.0_17/bin/jar 100
		/usr/sbin/alternatives --install /usr/bin/javac javac /usr/java/jdk1.6.0_17/bin/javac 100
		/usr/sbin/alternatives --config java
		java -version

	4.d. Get an X server going

		yum install xorg-x11-server-Xvfb
		yum install libXtst
		Xvfb :1 &
		export DISPLAY=":1"

	4.e. Get an email server going

		/etc/init.d/sendmail start

	4.f. Install Hudson

		wget http://hudson-ci.org/latest/hudson.war
		nohup java -Xms1000m -Xmx1000m -jar hudson.war --argumentsRealm.roles.admin=admin --argumentsRealm.passwd.admin=foo123 --httpPort=80 > nohup.out 2>&1 &
		navigate to Hudson in your browser, go to Manage Hudson > Configure System
			click "Add Maven", set name = "Maven 2", select the latest version
			set admin email = "build@htmlunit.org"
			click "Save"

	4.g. Configure Hudson to build HtmlUnit

		wget http://htmlunit.svn.sf.net/viewvc/htmlunit/trunk/htmlunit/hudson/job-config-without-libs.xml
		wget http://htmlunit.svn.sf.net/viewvc/htmlunit/trunk/htmlunit/hudson/job-config-only-libs.xml
		curl --user admin:foo123 --header "Content-Type:text/xml" --data @job-config-without-libs.xml http://localhost/createItem?name=htmlunit-without-library-tests
		curl --user admin:foo123 --header "Content-Type:text/xml" --data @job-config-only-libs.xml http://localhost/createItem?name=htmlunit-library-tests

	4.h. Enable Hudson security (doing this before step 4.g might cause that step to fail)

		navigate to Hudson in your browser, go to Manage Hudson > Configure System
			check Enable Security, check delegate to servlet container, check legacy mode
			click "Save"


Miscellaneous Links:

	Hudson Homepage: http://hudson-ci.org/
	CI Server Feature Matrix: http://confluence.public.thoughtworks.org/display/CC/CI+Feature+Matrix
	Hudson Quick and Simple Security Setup: http://wiki.hudson-ci.org/display/HUDSON/Quick+and+Simple+Security
