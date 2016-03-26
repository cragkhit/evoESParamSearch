# delete all dfr_* indexes
curl -XDELETE localhost:9200/bm25_*
javac -cp "./lib/ecj.23.jar" src/es/dfr/paramsearch/BM25.java 
mv src/es/dfr/paramsearch/BM25.class bin/es/dfr/paramsearch/BM25.class 
/usr/bin/time -o bm25.time java -cp "./lib/ecj.23.jar:./bin" ec.Evolve -file bm25.params 
