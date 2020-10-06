# druidTools/dataGen

A tool to generate fake pseudo-random data to ingest, for testing purposes, under dataGen.

invoke with:

java DataGen

(The python version is the original, older version, no longer being updated.)

You'll be prompted for the total number of rows to generate, and how many rows per dimension group.  (Multiple rows per dimension group are for testing rollup.)
(You'll also be prompted for the output file format you want - right now, only csv is supported, so just hit enter.)

The program will output two files - ingest.data (the data) and ingest.spec (the ingestion spec).  Currently the data is output in .csv format.

The program will also try to ingest the data to a local instance, without authentication, to a datasource named dataTest, with appendToExisting true.  (True so I can load more rows than my JVM can handle at once.)  If you don't have a local quickstart instance without auth, it will silently fail this part, but still output the datafile and spec for you.

(If you're using the spec to load the datafile, be sure to update baseDir if necessary.)

Note that based on the time it takes the program to run, and the query granularity, all rows will be ingested with the same timestamp.
If you would like other times, to test queries with time intervals, re-run the program after some minutes, and it will appendToExisting with a new timestamp.
