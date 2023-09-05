BRANCH := $(shell git rev-parse --abbrev-ref HEAD)

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

mutation:install
	@mvn eu.stamp-project:pitmp-maven-plugin:run
	@open dddplus-test/target/pit-reports/

jdepend:
	@mvn site
	@find . -name jdepend-report.html

doxygen:
	@doxygen Doxyfile
	@open target/callgraph/index.html

deploy:
	@mvn clean deploy verify -Possrh -e

deploy-snapshot:
	@mvn clean deploy verify -Dskip.dddplus.plugin.module=false -Possrh -e

setver:
	@echo mvn versions:set -DnewVersion=VER
	@echo mvn versions:commit

visualize-showcase:
	@mvn io.github.dddplus:dddplus-maven-plugin:model -DrootDir=./dddplus-test/src/test/java/ddd/plus/showcase/wms/ -DplantUml=./doc/showcase/wms.svg -Dencapsulation=./doc/showcase/encapsulation.txt -DtextModel=./doc/showcase/wms.txt
	@dot -Tsvg doc/showcase/callgraph.dot -o doc/showcase/callgraph.svg

enforce-showcase:
	@mvn io.github.dddplus:dddplus-maven-plugin:enforce -DrootPackage=ddd.plus.showcase.wms -DrootDir=./dddplus-test/src/test/java/ddd/plus/showcase/wms/

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

all:release-javadoc deploy-snapshot
	@git push origin
	@git push dddplus
