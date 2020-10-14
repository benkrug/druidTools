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
import java.util.Arrays;
import java.text.ParseException;

class DataGen {

  // method to get user input for number of rows to generate
  public static int getNumRows() {
    Scanner scan1 = new Scanner(System.in);
    System.out.println();
    System.out.print("Total number of rows to generate: ");
    int numRows = scan1.nextInt();
    return numRows;
  }

  // method to get user import for the number of rows per dimension group
  public static int getNumDups() {
    Scanner scan2 = new Scanner(System.in);
    System.out.println();
    System.out.print("Number of rows for each dimension group (for rollup testing): ");
    int numDups = scan2.nextInt();
    return numDups;
  } 

  // get datafile output format - default is csv
  public static String getOutputFormat() {
    Scanner scan3 = new Scanner(System.in);
    System.out.println();
    System.out.print("Choose datafile format - enter for csv: ");
    String outputFormat = scan3.nextLine();
    if (outputFormat != "csv") outputFormat = "csv";
    return outputFormat;
  }

  // method to make a fake "word" of a given Length
  public static String MakeWord(int Length) {
    String Vowels = "aeiouy";
    String Consonants = "bcdfghjklmnpqrstvwxz";
    String Word = "";

    int i;
    Random random = new Random();
    for (i=0;i<Length;i++) {
    // alternate between consonants and vowels
    if (i%2 == 0) Word += Consonants.charAt(random.nextInt(19));
      else Word += Vowels.charAt(random.nextInt(4));
      }
    return(Word);
  }

//  public static void GenerateData() {
//  }

  public static void main(String args[]) {

  int numRows = getNumRows();
  int numDups = getNumDups();
  String outputFormat = getOutputFormat();

  Random random = new Random();

  Date myDate = new Date();

  String word2 = "";
  String word4 = "";
  String email = "";
  int lat = 0;
  int lon = 0;
  int skew_lo = 0;
  int skew_hi = 0;
  int lo_hi = 0;

  // this "try" block writes the data file that will be ingested.
  // it writes to the directory the program is invoked from.

  System.out.println();
  System.out.println("Writing a data file, ingest.data, to the current directory.");
  try {
    FileWriter dataFileWriter = new FileWriter("ingest.data");

      int startRow = 0;  // used to save which row entered a "dup" loop
      for (int row=0;row<numRows;) {
        word2 = MakeWord(2);
        word4 = MakeWord(4);
        email = word4+"@"+word2+".com";
        lat = (random.nextInt(179)-90);
        lon = (random.nextInt(359)-180);

        skew_lo = 0;
        skew_hi = 0;

        startRow = row;  // save starting row number, to handle "dup" rows based on starting row in some cases
        for (int dup=0;dup<numDups;dup++) {
          Timestamp ts = new Timestamp(myDate.getTime());

          int int1 = random.nextInt(10);
          int int3 = random.nextInt(1000);
          int int5 = random.nextInt(1000000);
          if (((startRow/numDups)%10)==0) { // skew opposite every 10 groups of rows

            skew_lo = random.nextInt(10000000);
            skew_hi = random.nextInt(100);
            }
          else {
            skew_lo = random.nextInt(100);
            skew_hi = random.nextInt(10000000);
          }
	  if (((startRow/numDups)%2)==0) {
            lo_hi = random.nextInt(100);
            }
          else {
            lo_hi = random.nextInt(10000000);
          }
        row++;

      if (outputFormat == "csv") {
        if (((startRow/numDups)%100)==0) { // make email null every 100 rows (for all "dups" of that row, too)
          dataFileWriter.write(ts+","+ts+",,"+word2+","+word4+","+word2+"^A"+word4+","+lat+","+lon+","+int1+","+int3+","+int5+","+skew_lo+","+skew_hi+","+lo_hi);
          } else { // include email value
          dataFileWriter.write(ts+","+ts+","+email+","+word2+","+word4+","+word2+"^A"+word4+","+lat+","+lon+","+int1+","+int3+","+int5+","+skew_lo+","+skew_hi+","+lo_hi);
          } 
        dataFileWriter.write(System.lineSeparator());
      }
          }

        }
      dataFileWriter.close();

      } catch (IOException e) {
      System.out.println("Failed to write to file 'ingest.data'.");
      e.printStackTrace();
      } 

  // this "try" block writes the spec file for ingestion.
  // it writes to the directory the program is invoked from.

  System.out.println(); 
  System.out.println("Writing an ingestion spec file, ingest.spec, to the current directory.");
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
    specFileWriter.write("	  {\"name\": \"d2_ts2\", \"type\": \"timestamp\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"d3_email\", \"type\": \"string\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"d4_word2\", \"type\": \"string\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"d5_word4\", \"type\": \"string\"},"+newline);         
    specFileWriter.write("	  {\"name\": \"d6_mvd\", \"type\": \"string\"}"+newline);         
    specFileWriter.write("        ],"+newline);         
    specFileWriter.write("        \"spatialDimensions\": [{"+newline);         
    specFileWriter.write("          \"dimName\": \"d1_coordinates\","+newline);         
    specFileWriter.write("          \"dims\": [\"lat\", \"lon\"]"+newline);         
    specFileWriter.write("        }]"+newline);         
    specFileWriter.write("      },"+newline);         
    specFileWriter.write("      \"metricsSpec\": ["+newline);         
    specFileWriter.write("	  {\"type\": \"longMin\", \"name\": \"m1_int1_min\", \"fieldName\": \"int1\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longMax\", \"name\": \"m2_int3_max\", \"fieldName\": \"int3\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"m3_int5_sum\", \"fieldName\": \"int5\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"m4_lo_hi\", \"fieldName\": \"lo_hi\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"m5_skew_lo\", \"fieldName\": \"skew_lo\"},"+newline);         
    specFileWriter.write("	  {\"type\": \"longSum\", \"name\": \"m6_skew_hi\", \"fieldName\": \"skew_hi\"},"+newline);         
    specFileWriter.write("          {\"type\": \"thetaSketch\", \"name\": \"m7_int1_sketch\", \"fieldName\": \"int1\"},"+newline);         
    specFileWriter.write("          {\"type\": \"thetaSketch\", \"name\": \"m8_int3_sketch\", \"fieldName\": \"int3\"},"+newline);         
    specFileWriter.write("          {\"type\": \"quantilesDoublesSketch\", \"name\": \"m9_int5_quantile_sketch\", \"fieldName\": \"int5\"}"+newline);         
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

    specFileWriter.write("      \"inputFormat\": {"+newline);
    specFileWriter.write("         \"type\": \"csv\","+newline);
    specFileWriter.write("         \"listDelimiter\": \"^A\","+newline);
    specFileWriter.write("         \"columns\" : [\"ts\",\"d2_ts2\",\"d3_email\",\"d4_word2\",\"d5_word4\",\"d6_mvd\",\"lat\",\"lon\",\"int1\",\"int3\",\"int5\",\"skew_lo\",\"skew_hi\",\"lo_hi\"]"+newline);
    specFileWriter.write("        },"+newline);

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
    System.out.println();
    System.out.println("Failed to write to file 'ingest.spec'.");
    e.printStackTrace();
    }

    // This "try" block attempts to load the data file, using the ingestion file.
    // It tries to load it to druid on localhost (eg, a quickstart), with not auth.


    System.out.println();
    System.out.println("Sending a POST request to load the data to druid on localhost.");
    System.out.println("No authentication is used.  If there is no druid on localhost,");
    System.out.println("or you use authentication, or if there are other issues, it will likely silently fail.");
    System.out.println("You can use the ingest.data file and ingest.spec file to load the data yourself.");
    System.out.println("(You may want to edit the spec file first.)");
    try {
      Process submit = Runtime.getRuntime().exec("curl -X POST -H Content-Type:application/json -d @ingest.spec http://localhost:8081/druid/indexer/v1/task");
    } catch (IOException e) {
    System.out.println("Failed to submit task.");
    e.printStackTrace();
    }

    System.out.println();
    System.out.println("DataGen is complete.");
  }
}

