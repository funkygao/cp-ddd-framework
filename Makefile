package:clean
	@mvn package

clean:
	@mvn clean

install:clean
	@mvn install

test:clean
	@mvn test -Ptest

coverage:test
	@mvn clean verify -Pjacoco
	@open cp-ddd-test/target/site/jacoco-aggregate/index.html

publish:package
	@cp -f cp-ddd-example/order-center-bp-isv/target/order-center-bp-isv-0.0.1.jar doc/assets/jar
	@cp -f cp-ddd-example/order-center-bp-ka/target/order-center-bp-ka-0.0.1.jar doc/assets/jar
	@git commit -m 'bp jar published to github'

javadoc:install
	@mvn javadoc:javadoc
	@open target/site/apidocs/index.html

