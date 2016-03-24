# delete all dfr_* indexes
curl -XDELETE localhost:9200/dfr_*
javac -cp "./lib/ecj.23.jar" src/es/dfr/paramsearch/MaxOnes.java 
mv src/es/dfr/paramsearch/MaxOnes.class bin/es/dfr/paramsearch/MaxOnes.class 
/usr/bin/time -o dfr.time java -cp "./lib/ecj.23.jar:./bin" ec.Evolve -file dfr.params -p breed.elite.0=2
