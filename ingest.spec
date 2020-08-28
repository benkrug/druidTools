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
	  {"name": "word8", "type": "string"},
	  {"name": "int1", "type": "long"},
	  {"name": "int3", "type": "long"},
	  {"name": "int5", "type": "long"}
        ],
        "spatialDimensions": [{
          "dimName": "coordinates",
          "dims": ["lat", "lon"]
        }]
      },
      "metricsSpec": [
      ],
      "granularitySpec": {
      }
    },
    "ioConfig": {
      "type": "index_parallel",
      "inputSource": {
        "type": "local",
        "baseDir": "/path/to/ingest_data_file/",
        "filter": "ingest.data"
      },
      "inputFormat": { "type": "csv" , "columns" : ["ts","word2","word4","word8","int1","int3","int5","lat","lon"]}
    }
  },
  "tuningConfig": {
    "type": "index_parallel",
    "maxRowsPerSegment": 500000
  }
}
