generateTestGraphs.jar: src/webcrawler/*.java src/datastructures/*.java src/executables/* src/algorithms/*
	javac -classpath src -sourcepath src -d ./bin2 src/executables/GenerateTestGraphs.java 
	jar cvfm generateTestGraphs.jar ManifestGenerateTestGraphs -C bin2 .
testAlgorithmComplexity.jar: src/webcrawler/*.java src/datastructures/*.java src/executables/* src/algorithms/*
	javac -classpath src -sourcepath src -d ./bin2 src/executables/TestAlgorithmComplexity.java 
	jar cvfm testAlgorithmComplexity.jar ManifestTestAlgorithmComplexity -C bin2 .
clean: 
	rm generateTestGraphs.jar
	rm testAlgorithmComplexity.jar
