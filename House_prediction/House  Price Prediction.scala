// Databricks notebook source
// MAGIC %md ## Creating a Regression Model
// MAGIC
// MAGIC In this Project, you will implement a regression model that will predict the House Sale Prices Prediction based on many attributes available in House Sale Data
// MAGIC
// MAGIC ### Import Spark SQL and Spark ML Libraries
// MAGIC
// MAGIC First, import the libraries you will need:

// COMMAND ----------


import org.apache.spark.sql.functions._
import org.apache.spark.sql.Row
import org.apache.spark.sql.types._

import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.feature.VectorAssembler

// COMMAND ----------

// MAGIC %md ### Load Source Data
// MAGIC The data for this Project is provided as a CSV file containing details of House details we need to Predict the SalePrice . 
// MAGIC
// MAGIC You will load this data into a DataFrame and display it.

// COMMAND ----------


val data = spark.read.option("inferSchema","true").option("header","true").csv("dbfs:/FileStore/shared_uploads/jaycees10000@gmail.com/model_data.csv")

display(data)

// COMMAND ----------

data.printSchema()

// COMMAND ----------

// MAGIC %md ### Prepare the Training Data
// MAGIC To train the regression model, you need a training data set that includes a vector of numeric features, and a label column. In this project, you will use the **VectorAssembler** class to transform the feature columns into a vector, and then rename the **SalePrice** column to **label**.

// COMMAND ----------

// MAGIC %md ###VectorAssembler()
// MAGIC
// MAGIC VectorAssembler():  is a transformer that combines a given list of columns into a single vector column. It is useful for combining raw features and features generated by different feature transformers into a single feature vector, in order to train ML models like logistic regression and decision trees. 
// MAGIC
// MAGIC **VectorAssembler** accepts the following input column types: **all numeric types, boolean type, and vector type.** 
// MAGIC
// MAGIC In each row, the **values of the input columns will be concatenated into a vector** in the specified order.

// COMMAND ----------

// DBTITLE 1,List all String Data Type Columns in an Array in further processing

var StringfeatureCol = Array("MSZoning", "LotFrontage", "Street", "Alley", "LotShape", "LandContour", "Utilities", "LotConfig", "LandSlope", "Neighborhood", "Condition1", "Condition2", "BldgType", "HouseStyle", "RoofStyle", "RoofMatl", "Exterior1st", "Exterior2nd", "MasVnrType", "MasVnrArea", "ExterQual", "ExterCond", "Foundation", "BsmtQual", "BsmtCond", "BsmtExposure", "BsmtFinType1", "BsmtFinType2", "Heating", "HeatingQC", "CentralAir", "Electrical", "KitchenQual", "Functional", "FireplaceQu", "GarageType", "GarageYrBlt", "GarageFinish", "GarageQual", "GarageCond", "PavedDrive", "PoolQC", "Fence", "MiscFeature")

// COMMAND ----------

// MAGIC %md
// MAGIC
// MAGIC ###StringIndexer
// MAGIC
// MAGIC StringIndexer encodes a string column of labels to a column of label indices.

// COMMAND ----------

// DBTITLE 1,Example of StringIndexer
import org.apache.spark.ml.feature.StringIndexer

val df = spark.createDataFrame(
  Seq((0, "a"), (1, "b"), (2, "c"), (3, "a"), (4, "a"), (5, "c"))
).toDF("id", "category")

val indexer = new StringIndexer()
  .setInputCol("category")
  .setOutputCol("categoryIndex")

val indexed = indexer.fit(df).transform(df)

display(indexed)

// COMMAND ----------

// MAGIC %md ### Define the Pipeline
// MAGIC A predictive model often requires multiple stages of feature preparation. 
// MAGIC
// MAGIC A pipeline consists of a series of *transformer* and *estimator* stages that typically prepare a DataFrame for modeling and then train a predictive model. 
// MAGIC
// MAGIC In this case, you will create a pipeline with stages:
// MAGIC
// MAGIC - A **StringIndexer** estimator that converts string values to indexes for categorical features
// MAGIC - A **VectorAssembler** that combines categorical features into a single vector

// COMMAND ----------


import org.apache.spark.ml.attribute.Attribute
import org.apache.spark.ml.feature.{IndexToString, StringIndexer}
import org.apache.spark.ml.{Pipeline, PipelineModel}

val indexers = StringfeatureCol.map { colName =>
  new StringIndexer().setInputCol(colName).setOutputCol(colName + "_indexed")
}

val pipeline = new Pipeline()
                    .setStages(indexers)      

val HouseDF = pipeline.fit(data).transform(data)

// COMMAND ----------

HouseDF.printSchema()

// COMMAND ----------

HouseDF.show()

// COMMAND ----------

// MAGIC %md ### Split the Data
// MAGIC It is common practice when building supervised machine learning models to split the source data, using some of it to train the model and reserving some to test the trained model. In this project, you will use 70% of the data for training, and reserve 30% for testing. In the testing data, the **label** column is renamed to **trueLabel** so you can use it later to compare predicted labels with known actual values.

// COMMAND ----------


val splits = HouseDF.randomSplit(Array(0.7, 0.3))
val train = splits(0)
val test = splits(1)
val train_rows = train.count()
val test_rows = test.count()
println("Training Rows: " + train_rows + " Testing Rows: " + test_rows)

// COMMAND ----------

// DBTITLE 1,VectorAssembler() that combines categorical features into a single vector

val assembler = new VectorAssembler().setInputCols(Array("Id", "MSSubClass", "LotArea", "OverallQual", "OverallCond", "YearBuilt", "YearRemodAdd", "BsmtFinSF1", "BsmtFinSF2", "BsmtUnfSF", "TotalBsmtSF", "1stFlrSF", "2ndFlrSF", "LowQualFinSF", "GrLivArea", "BsmtFullBath","BsmtHalfBath", "FullBath", "HalfBath", "BedroomAbvGr", "KitchenAbvGr", "TotRmsAbvGrd", "Fireplaces", "GarageCars", "GarageArea", "WoodDeckSF", "OpenPorchSF", "EnclosedPorch", "3SsnPorch", "ScreenPorch", "PoolArea", "MiscVal", "MoSold", "YrSold", "MSZoning_indexed", "LotFrontage_indexed", "Street_indexed", "Alley_indexed", "LotShape_indexed","LandContour_indexed", "Utilities_indexed", "LotConfig_indexed", "LandSlope_indexed", "Neighborhood_indexed", "Condition1_indexed", "Condition2_indexed", "BldgType_indexed", "HouseStyle_indexed", "RoofStyle_indexed", "RoofMatl_indexed", "Exterior1st_indexed", "Exterior2nd_indexed", "MasVnrType_indexed", "MasVnrArea_indexed", "ExterQual_indexed", "ExterCond_indexed", "Foundation_indexed", "BsmtQual_indexed", "BsmtCond_indexed", "BsmtExposure_indexed", "BsmtFinType1_indexed", "BsmtFinType2_indexed", "Heating_indexed", "HeatingQC_indexed", "CentralAir_indexed", "Electrical_indexed", "KitchenQual_indexed", "Functional_indexed", "FireplaceQu_indexed", "GarageType_indexed", "GarageYrBlt_indexed", "GarageFinish_indexed", "GarageQual_indexed", "GarageCond_indexed", "PavedDrive_indexed", "PoolQC_indexed", "Fence_indexed", "MiscFeature_indexed" )).setOutputCol("features")
val training = assembler.transform(train).select($"features", $"SalePrice".alias("label"))
training.show()

// COMMAND ----------

// MAGIC %md ### Train a Regression Model
// MAGIC Next, you need to train a regression model using the training data. To do this, create an instance of the regression algorithm you want to use and use its **fit** method to train a model based on the training DataFrame. In this Project, you will use a *Linear Regression* algorithm - though you can use the same technique for any of the regression algorithms supported in the spark.ml API.

// COMMAND ----------


val lr = new LinearRegression().setLabelCol("label").setFeaturesCol("features").setMaxIter(10).setRegParam(0.3)
val model = lr.fit(training)
println("Model Trained!")

// COMMAND ----------

// MAGIC %md ### Prepare the Testing Data
// MAGIC Now that you have a trained model, you can test it using the testing data you reserved previously. First, you need to prepare the testing data in the same way as you did the training data by transforming the feature columns into a vector. This time you'll rename the **SalePrice** column to **trueLabel**.

// COMMAND ----------


val testing = assembler.transform(test).select($"features", $"SalePrice".alias("trueLabel"))
testing.show()

// COMMAND ----------

// MAGIC %md ### Test the Model
// MAGIC Now you're ready to use the **transform** method of the model to generate some predictions. But in this case you are using the test data which includes a known true label value, so you can compare the predicted Sale Price. 

// COMMAND ----------


val prediction = model.transform(testing)
val predicted = prediction.select("features", "prediction", "trueLabel")
predicted.show()

// COMMAND ----------

// MAGIC %md Looking at the result, the **prediction** column contains the predicted value for the label, and the **trueLabel** column contains the actual known value from the testing data. It looks like there is some variance between the predictions and the actual values (the individual differences are referred to as *residuals*) you'll learn how to measure the accuracy of a model.

// COMMAND ----------

// MAGIC %md ## Evaluating a Regression Model
// MAGIC
// MAGIC In this Project, we have created pipeline for a linear regression model, and then test and evaluate the model.
// MAGIC
// MAGIC ### Prepare the Data
// MAGIC
// MAGIC First, import the libraries you will need and prepare the training and test data:

// COMMAND ----------

// MAGIC %md ### Examine the Predicted and Actual Values
// MAGIC You can plot the predicted values against the actual values to see how accurately the model has predicted. In a perfect model, the resulting scatter plot should form a perfect diagonal line with each predicted value being identical to the actual value - in practice, some variance is to be expected.
// MAGIC Run the cells below to create a temporary table from the **predicted** DataFrame and then retrieve the predicted and actual label values using SQL. You can then display the results as a scatter plot, specifying **-** as the function to show the unaggregated values.

// COMMAND ----------


predicted.createOrReplaceTempView("HousePrice")

// COMMAND ----------

// MAGIC %sql
// MAGIC
// MAGIC select prediction, trueLabel from HousePrice

// COMMAND ----------

// MAGIC %md ### Retrieve the Root Mean Square Error (RMSE)
// MAGIC There are a number of metrics used to measure the variance between predicted and actual values. Of these, the root mean square error (RMSE) is a commonly used value that is measured in the same units as the predicted and actual values - so in this case, the RMSE indicates the average number of minutes between predicted and actual Sale Price values. You can use the **RegressionEvaluator** class to retrieve the RMSE.

// COMMAND ----------

import org.apache.spark.ml.evaluation.RegressionEvaluator

val evaluator = new RegressionEvaluator().setLabelCol("trueLabel").setPredictionCol("prediction").setMetricName("rmse")
val rmse = evaluator.evaluate(prediction)
println("Root Mean Square Error (RMSE): " + (rmse))