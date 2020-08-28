# dataGen

A python script to generate fake pseudo-random data to ingest, for testing purposes.

To start, it generates some fake "words" of different lengths, numbers of different sizes, and one coordinate point.  I save to a file and ingest with the spec.  

I'll be adding to it and refining it over time.  Hopefully, I'll add new kinds of data, and also have it automatically save to a file and use the API to load to a specified datasource.

usage: python data.py n1 n2 > ingest.data

n1 is number of entries for each set of dimensions, for testing rollups
n2 is the number of times to generate a set of dimensions and its n1 input rows

Eg, if you use:
	python data.py 2 10 > ingest.data

you'll get a file with 20 rows - 2 rows for each of 10 dimension sets

If you then go to the Ingestion tab of the druid console, and copy ingest.spec into "submit a json task", and submit, you should end up with 10 rows in the datasource.  You can inspect ingest.data and the datasource to see that rollups worked as expected.  (Be sure to edit ingest.spec to set baseDir to the right path first.)
