/*
a program to print out pseudo-random data for ingestion into druid
*/

import java.util.Scanner;
import java.util.Random;
import java.util.Date;
import java.sql.Timestamp;
import java.io.FileWriter;
import java.io.IOException;
//import java.nio.file.Path;
import java.nio.file.Paths;
import java.lang.Runtime;

class DataGen {

  public static int getNumRows() {
    Scanner scan1 = new Scanner(System.in);
    System.out.println();
    System.out.print("Total number of rows to generate: ");
    int numRows = scan1.nextInt();
    return numRows;
  }

  public static int getNumDups() {
    Scanner scan2 = new Scanner(System.in);
    System.out.println();
    System.out.print("Number of rows for each dimension group (for rollup testing): ");
    int numDups = scan2.nextInt();
    return numDups;

  } 
  public static String MakeWord(int Length) {
    String Vowels = "aeiouy";
    String Consonants = "bcdfghjklmnpqrstvwxz";
    String Word = "";

    int i;
    Random random = new Random();
    for (i=0;i<Length;i++) {
    if (i%2 == 0) Word += Consonants.charAt(random.nextInt(19));
      else Word += Vowels.charAt(random.nextInt(4));
      }
    return(Word);
  }

  public static void GenerateData() {
  }

  public static void main(String args[]) {

  int numRows = getNumRows();
  int numDups = getNumDups();

  Random random = new Random();

  Date myDate = new Date();

  String word2 = "";
  String word4 = "";
  String word8 = "";
  int lat = 0;
  int lon = 0;
  int skew_lo = 0;
  int skew_hi = 0;
  int lo_hi = 0;

  try {
    FileWriter dataFileWriter = new FileWriter("ingest.data");

      for (int row=0;row<numRows;) {
        word2 = MakeWord(2);
        word4 = MakeWord(4);
        word8 = MakeWord(8);
        int int1 = random.nextInt(10);
        int int3 = random.nextInt(1000);
        int int5 = random.nextInt(1000000);
        lat = (random.nextInt(179)-90);
        lon = (random.nextInt(359)-180);

        skew_lo = 0;
        skew_hi = 0;
        for (int dup=0;dup<numDups;dup++) {
          Timestamp ts = new Timestamp(myDate.getTime());
          if ((row%10)==0) {
            skew_lo = random.nextInt(10000000);
            skew_hi = random.nextInt(100);
            }
          else {
            skew_lo = random.nextInt(100);
            skew_hi = random.nextInt(10000000);
          }
          if ((row%2)==0) {
            lo_hi = random.nextInt(100);
            }
          else {
            lo_hi = random.nextInt(10000000);
          }
        row++;
      dataFileWriter.write(ts+","+word2+","+word4+","+word8+","+lat+","+lon+","+int1+","+int3+","+int5+","+skew_lo+","+skew_hi+","+lo_hi);
      dataFileWriter.write(System.lineSeparator());
    System.out.println(ts+","+word2+","+word4+","+word8+","+lat+","+lon+","+int1+","+int3+","+int5+","+skew_lo+","+skew_hi+","+lo_hi);
          }

        }
      dataFileWriter.close();

      } catch (IOException e) {
      System.out.println("Failed to write to file 'ingest.data'.");
      e.printStackTrace();
      } 

//  Path currentRelativePath = Paths.get("");
  String pwd = Paths.get("").toAbsolutePath().toString();
  String newline = System.lineSeparator();
  try {
    FileWriter specFileWriter = new FileWriter("ingest.spec");
    specFileWriter.write("{"+newline);         
    specFileWriter.write(" \"type\": \"index_parallel\","+newline);         
    specFileWriter.write(" \"spec\": { "+newline);         
    specFileWriter.write("    \"dataSchema\" : {"+newline);         
    specFileWriter.write("      \"dataSource\": \"dataTest\","+newline);         
    specFileWriter.write("      \"timestampSpec\": {"+newline);         
    specFileWriter.write("        \"format\": \"auto\","+newline);         
    specFileWriter.write("        \"column\": \"ts\""+newline);         
    specFileWriter.write("      },"+newline);         
    specFileWriter.write("      \"dimensionsSpec\": {"+newline);         
    specFileWriter.write("        \"dimensions\": ["+newline);         
    specFileWriter.write("	  {\"name\": \"word2\", \"type\": \"string\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"word4\", \"type\": \"string\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"word8\", \"type\": \"string\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"int1\", \"type\": \"long\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"int3\", \"type\": \"long\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"int5\", \"type\": \"long\"}"+newline);         
    specFileWriter.write("        ],"+newline);         
    specFileWriter.write("        \"spatialDimensions\": [{"+newline);         
    specFileWriter.write("          \"dimName\": \"coordinates\","+newline);         
    specFileWriter.write("          \"dims\": [\"lat\", \"lon\"]"+newline);         
    specFileWriter.write("        }]"+newline);         
    specFileWriter.write("      },"+newline);         
    specFileWriter.write("      \"metricsSpec\": ["+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"skew_lo\", \"fieldName\": \"skew_lo\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"skew_hi\", \"fieldName\": \"skew_hi\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"lo_hi\", \"fieldName\": \"lo_hi\"},"+newline);         
    specFileWriter.write("          {\"type\": \"thetaSketch\", \"name\": \"int1_sketch\", \"fieldName\": \"int1\"},"+newline);         
    specFileWriter.write("          {\"type\": \"thetaSketch\", \"name\": \"int3_sketch\", \"fieldName\": \"int3\"},"+newline);         
    specFileWriter.write("          {\"type\": \"thetaSketch\", \"name\": \"int5_sketch\", \"fieldName\": \"int5\"}"+newline);         
    specFileWriter.write("      ],"+newline);         
    specFileWriter.write("      \"granularitySpec\": {"+newline);         
    specFileWriter.write("	  \"type\": \"uniform\","+newline);         
    specFileWriter.write("	  \"segmentGranularity\": \"HOUR\","+newline);         
    specFileWriter.write("	  \"queryGranularity\": \"MINUTE\","+newline);         
    specFileWriter.write("	  \"rollup\": true"+newline);         
    specFileWriter.write("      }"+newline);         
    specFileWriter.write("    },"+newline);         
    specFileWriter.write("    \"ioConfig\": {"+newline);         
    specFileWriter.write("      \"type\": \"index_parallel\","+newline);         
    specFileWriter.write("      \"inputSource\": {"+newline);         
    specFileWriter.write("        \"type\": \"local\","+newline);         
    specFileWriter.write("        \"baseDir\": \""+pwd+"/\","+newline);         
    specFileWriter.write("        \"filter\": \"ingest.data\""+newline);         
    specFileWriter.write("      },"+newline);         
    specFileWriter.write("      \"inputFormat\": { \"type\": \"csv\" , \"columns\" : [\"ts\",\"word2\",\"word4\",\"word8\",\"lat\",\"lon\",\"int1\",\"int3\",\"int5\",\"skew_lo\",\"skew_hi\",\"lo_hi\"]},"+newline);
    specFileWriter.write("      \"appendToExisting\": true"+newline);         
    specFileWriter.write("    }"+newline);         
    specFileWriter.write("  },"+newline);         
    specFileWriter.write("  \"tuningConfig\": {"+newline);         
    specFileWriter.write("    \"type\": \"index_parallel\","+newline);         
    specFileWriter.write("    \"maxRowsPerSegment\": 500000,"+newline);         
    specFileWriter.write("    \"maxNumConcurrentSubTasks\": 5"+newline);         
    specFileWriter.write("  }"+newline);         
    specFileWriter.write("}"+newline);         
    specFileWriter.close();
    } catch (IOException e) {
    System.out.println("Failed to write to file 'ingest.spec'.");
    e.printStackTrace();
    }

    try {
      Process submit = Runtime.getRuntime().exec("curl -X POST -H Content-Type:application/json -d @ingest.spec http://localhost:8081/druid/indexer/v1/task");
    } catch (IOException e) {
    System.out.println("Failed to submit task.");
    e.printStackTrace();
    }

  }
}

