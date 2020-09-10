package:clean
	@mvn package

clean:
	@mvn clean

install:clean
	@mvn install

test:package
	@mvn test -Ptest

coverage:test
	@mvn clean verify -Ptest
	@open cp-ddd-test/target/site/jacoco-aggregate/index.html

javadoc:install
	@mvn javadoc:javadoc
	@open target/site/apidocs/index.html

release-javadoc:install
	@git checkout gh-pages
	@mvn javadoc:javadoc
	@mv -f target/site/apidocs/ doc/
	@git add doc/apidocs
	@git commit -m 'Javadoc updated' doc/apidocs
	@git push
