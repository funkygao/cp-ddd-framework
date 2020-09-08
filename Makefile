default:clean
	@mvn package

clean:
	@mvn clean

install:clean
	@mvn install

test:clean
	@mvn test -Ptest

javadoc:install
	@mvn javadoc:javadoc
	@open target/site/apidocs/index.html

