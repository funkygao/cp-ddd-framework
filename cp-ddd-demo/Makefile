package:clean
	@mvn package

clean:
	@mvn clean

plugin-loading:clean
	@mvn package -Pplugin

depgraph:
	@mvn com.github.ferstl:depgraph-maven-plugin:aggregate -DcreateImage=true -DreduceEdges=false -Dscope=compile "-Dincludes=*:*"
	@open target/dependency-graph.png
