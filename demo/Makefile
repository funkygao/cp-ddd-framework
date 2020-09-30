package:clean
	@mvn package

clean:
	@mvn clean

run:package
	@java -jar order-center-cp/cp-oc-main/target/cp-ddd-framework-demo.jar

order:
	@curl -d '{"source":"ISV","customerNo":"home","externalNo":"2020"}' -H 'Content-type: application/json' http://localhost:9090/order

plugin-loading:clean
	@mvn package -Pplugin

depgraph:
	@mvn com.github.ferstl:depgraph-maven-plugin:aggregate -DcreateImage=true -DreduceEdges=false -Dscope=compile "-Dincludes=*:*"
	@open target/dependency-graph.png
