javac -cp "./lib/ecj.23.jar" src/es/dfr/paramsearch/MaxOnes.java 
mv src/es/dfr/paramsearch/MaxOnes.class bin/es/dfr/paramsearch/MaxOnes.class 
java -cp "./lib/ecj.23.jar:./bin" ec.Evolve -file dfr.params
