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
	  {"type": "longSum", "name": "skew_lo", "fieldName": "skew_lo"},
	  {"type": "longSum", "name": "skew_hi", "fieldName": "skew_hi"},
	  {"type": "longSum", "name": "lo_hi", "fieldName": "lo_hi"},
          {"type": "thetaSketch", "name": "int1_sketch", "fieldName": "int1"},
          {"type": "thetaSketch", "name": "int3_sketch", "fieldName": "int3"},
          {"type": "thetaSketch", "name": "int5_sketch", "fieldName": "int5"}
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
        "baseDir": "/Users/benkrug/dataGen.sav/",
        "filter": "ingest.data"
      },
      "inputFormat": { "type": "csv" , "columns" : ["ts","word2","word4","word8","lat","lon","int1","int3","int5","skew_lo","skew_hi","lo_hi"]},
      "appendToExisting": true
    }
  },
  "tuningConfig": {
    "type": "index_parallel",
    "maxRowsPerSegment": 500000,
    "maxNumConcurrentSubTasks": 5
  }
}
