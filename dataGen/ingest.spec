{
 "type": "index_parallel",
 "spec": { 
    "dataSchema" : {
      "dataSource": "dataTest",
      "timestampSpec": {
        "format": "micro",
        "column": "ts"
      },
      "dimensionsSpec": {
        "dimensions": [
	  {"name": "word2", "type": "string"},
	  {"name": "word4", "type": "string"},
	  {"name": "word8", "type": "string"}
        ],
        "spatialDimensions": [{
          "dimName": "coordinates",
          "dims": ["lat", "lon"]
        }]
      },
      "metricsSpec": [
	  {"type": "longSum", "name": "int1", "fieldName": "int1"},
	  {"type": "longSum", "name": "int3", "fieldName": "int3"},
	  {"type": "longSum", "name": "int5", "fieldName": "int5"}
      ],
      "granularitySpec": {
	  "type": "uniform",
	  "segmentGranularity": "HOUR",
	  "queryGranularity": "MINUTE",
	  "rollup": true
      }
    },
    "ioConfig": {
      "type": "index_parallel",
      "inputSource": {
        "type": "local",
        "baseDir": "/path/to/ingest_data_file/",
        "filter": "ingest.data"
      },
      "inputFormat": { "type": "csv" , "columns" : ["ts","word2","word4","word8","lat","lon","int1","int3","int5"]}
    }
  },
  "tuningConfig": {
    "type": "index_parallel",
    "maxRowsPerSegment": 500000
  }
}
