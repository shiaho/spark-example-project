/*
 * Copyright (c) 2012-2013 SnowPlow Analytics Ltd. All rights reserved.
 *
 * This program is licensed to you under the Apache License Version 2.0,
 * and you may not use this file except in compliance with the Apache License Version 2.0.
 * You may obtain a copy of the Apache License Version 2.0 at http://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the Apache License Version 2.0 is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Apache License Version 2.0 for the specific language governing permissions and limitations there under.
 */
package com.snowplowanalytics.spark

// Spark
import spark._
import SparkContext._

object WordCountJob {
  
  def main(args: Array[String]) {
    
    // See http://spark-project.org/docs/latest/api/core/index.html#spark.SparkContext
    val sc = new SparkContext(
      master = System.getenv("MASTER"), // Set by Amazon's install-spark-shark.sh
      appName = "WordCountJob",
      sparkHome = System.getenv("SPARK_HOME"),
      jars = Seq(System.getenv("SPARK_EXAMPLE_JAR"))
    )
    
    // Adapted from Word Count example on http://spark-project.org/examples/
    val file = sc.textFile(args(1))
	  val words = file.flatMap(line => tokenize(line))
    val wordCounts = words.map(x => (x, 1)).reduceByKey(_ + _)
    wordCounts.saveAsTextFile(args(2))

    // Exit with success
    System.exit(0)
  }

  // Split a piece of text into individual words.
  def tokenize(text : String) : Array[String] = {
    // Lowercase each word and remove punctuation.
    text.toLowerCase.replaceAll("[^a-zA-Z0-9\\s]", "").split("\\s+")
  }
}
