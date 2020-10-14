package:clean
	@mvn package

clean:
	@mvn clean

install:clean test
	@mvn install -Pinstall

test:clean
	@mvn test -Ptest

coverage:test
	@mvn clean verify -Ptest
	@open dddplus-test/target/site/jacoco-aggregate/index.html

javadoc:install
	@mvn javadoc:javadoc -Pinstall
	@open target/site/apidocs/index.html

deploy:
	@mvn clean deploy verify -Possrh

release-javadoc:install
	@git checkout gh-pages
	@git pull
	@git merge -m 'Merge master to gh-pages' master
	@git checkout master
	@mvn javadoc:javadoc
	@git checkout gh-pages
	@rm -rf doc/apidocs
	@mv -f target/site/apidocs/ doc/
	@git add doc/apidocs
	@git commit -m 'Javadoc released' doc/apidocs
	@git push
	@git checkout master

all:release-javadoc deploy
	@git push origin
	@git push dddplus
