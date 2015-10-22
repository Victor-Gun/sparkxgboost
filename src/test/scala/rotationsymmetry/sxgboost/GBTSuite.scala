package rotationsymmetry.sxgboost

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.regression.GBTRegressor
import org.apache.spark.mllib.util.MLUtils
import org.apache.spark.{SparkContext, SparkConf}
import org.scalatest.FunSuite


class GBTSuite extends FunSuite{
  test("GBT") {
    val conf = new SparkConf().setAppName("Test").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)
    import sqlContext.implicits._

    val data = MLUtils.loadLibSVMFile(sc, "sample_libsvm_data.txt").toDF()

    val featureIndexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("indexedFeatures")
      .setMaxCategories(4)
      .fit(data)

    val Array(trainingData, testData) = data.randomSplit(Array(0.7, 0.3))

    val gbt = new GBTRegressor()
      .setFeaturesCol("indexedFeatures")
      .setMaxIter(10)

    val pipeline = new Pipeline()
      .setStages(Array(featureIndexer, gbt))

    val model = pipeline.fit(trainingData)

    val predictions = model.transform(testData)
    val x = 1
  }
}