package:clean
	@mvn package

clean:
	@mvn clean

install:clean
	@mvn install

test:package
	@mvn test -Ptest

coverage:test
	@mvn clean verify -Pjacoco
	@open cp-ddd-test/target/site/jacoco-aggregate/index.html

javadoc:install
	@mvn javadoc:javadoc
	@open target/site/apidocs/index.html

