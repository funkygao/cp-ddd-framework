package:clean
	@mvn package

clean:
	@mvn clean

install:clean
	@mvn install -Pinstall

test:clean
	@mvn test -Ptest

coverage:test
	@mvn clean verify -Ptest
	@open cp-ddd-test/target/site/jacoco-aggregate/index.html

javadoc:install
	@mvn javadoc:javadoc -Pinstall
	@open target/site/apidocs/index.html

deploy-github:test
	@mvn deploy -Prelease

release-javadoc:install
	@git checkout gh-pages
	@git pull
	@git checkout master
	@mvn javadoc:javadoc
	@git checkout gh-pages
	@rm -rf doc/apidocs
	@mv -f target/site/apidocs/ doc/
	@git add doc/apidocs
	@git commit -m 'Javadoc updated' doc/apidocs
	@git push
	@git checkout master
