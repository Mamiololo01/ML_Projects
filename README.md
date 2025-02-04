# ML Projects

This repository contains a collection of machine learning projects. Each project demonstrates different aspects of machine learning, from data preprocessing and feature engineering to model training and evaluation.

## Table of Contents
- [House Prediction](#house-prediction)
- [Loan Prediction](#loan-prediction)
- [Dependencies](#dependencies)

## House Prediction

### Project Description
This project involves building and training a machine learning model to predict house prices. The model is trained and tested using a 70/30 split, and the prediction accuracy is evaluated using the Root Mean Square Error (RMSE).

### Key Concepts
- **Linear Regression Model**: A technique for modeling the relationship between a dependent variable and one or more independent variables.
- **Ridge and Lasso Regression**: Types of regularization techniques used to prevent overfitting.

### Terms Used
- `randomSplit`
- `VectorAssembler()`
- `transform()`
- `setMaxIter()`
- `setRegParams()`

### Dataset
- The file `train.csv` contains 1460 rows and 81 columns. Each row corresponds to housing details.
- Key column: `MSSubClass` - Identifies the type of dwelling involved in the sale.

### Steps
1. **Prepare the Training Data**: Use `VectorAssembler` to transform the dataset into a vector of numeric features and a label column.
2. **Define the Pipeline**: Create a pipeline with stages that include a `StringIndexer` for categorical features and a `VectorAssembler` to combine features into a single vector.
3. **Train and Evaluate the Model**: Train the model on the training data and evaluate its performance on the test data.

For more details, see the [House Prediction README](House_prediction/README.md).

## Loan Prediction

### Project Description
This project involves building machine learning models to predict whether a loan will be approved or not. The models used are Logistic Regression and Random Forest Classifier.

### Background
Lending services companies allow individual investors to partially fund personal loans and trade notes backing the loans. This project uses historical loan data to classify the risk level of given loans.

### Dataset
- The data is located in the `Resources` folder: `lending_data.csv`.

### Steps
1. **Retrieve the Data**: Import the data using Pandas.
2. **Consider the Models**: Create and compare Logistic Regression and Random Forest Classifier models.
3. **Fit and Evaluate the Models**: Train the models on the data and evaluate their performance using appropriate metrics.

For more details, see the [Loan Prediction README](Loan_prediction/README.md).

## Dependencies

- **sparkML libraries**
- **Databricks Community & Notebook**: Create a cluster for running the models.
- **Python Libraries**: Ensure you have the following Python libraries installed:
  - `pandas`
  - `sklearn`
  - `numpy`

## Usage

1. **Clone the Repository**:
   ```sh
   git clone https://github.com/Mamiololo01/ML_Projects.git
   cd ML_Projects

### Set Up Your Environment

Install the required Python libraries using pip:

pip install pandas scikit-learn numpy

If using Databricks, create a cluster and upload the project files.

### Run the Projects:

Follow the instructions in each project's README to run the code and train the models.

### Contributing

We welcome contributions to this repository. Please follow these steps to contribute:

### Fork the Repository:

Click the "Fork" button in the top-right corner of the repository page to create a copy of the repository in your GitHub account.

### Clone the Forked Repository:

git clone https://github.com/your-username/ML_Projects.git

cd ML_Projects

### Create a New Branch:

git checkout -b my-feature-branch

### Make Your Changes:

Implement your changes or additions to the repository code.

### Commit and Push Your Changes:

git add .

git commit -m "Description of the changes"

git push origin my-feature-branch

### Create a Pull Request:

Go to your forked repository on GitHub and click on the "Compare & pull request" button to create a pull request to the original repository.

## License

This project is licensed under the MIT License - see the LICENSE file for details.