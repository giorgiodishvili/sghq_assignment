# Coding Exercise for Swiss Re position

## Description

Application gets Employee CSV as an input and calculates different tasks

- analyzes if salaries are properly distributed
- analyzes long reporting line

## Requirements
- `Maven`

## Installation

```bash
git clone https://github.com/giorgiodishvili/sghq_assignment
cd sghq_assignment
mvn clean install
````

## Usage

### CSV File structure

```agsl
Id,firstName,lastName,salary,managerId
123,Joe,Doe,60000,
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
300,Alice,Hasacat,50000,124
305,Brett,Hardleaf,34000,300
```
#### Assumption on CSV File entries
`Entries might not be sorted by id and lower id employee might have higher id manager, so it is taken care of`

```agsl
Id,firstName,lastName,salary,managerId
300,Alice,Hasacat,50000,124
124,Martin,Chekov,45000,123
125,Bob,Ronstad,47000,123
305,Brett,Hardleaf,34000,306
123,Joe,Doe,60000,
306,Foo,Bar,34000,300
307,Car,Flat,34000,303
308,Right,Left,34000,309
303,Austin,K,34000,308
309,Eugene,Goodread,34000,124
```

### Project execution
#### Possible arguments
```agsl
1. filePathFromRoot (Mandatory) - path to the employee.csv file
2. maxDepthAllowed (Optional default: 4) - MAX Reporting Line length inclusive 
3. minSalaryPercentage (Optional default: 1.2) - MIN Percent of salary manager can have based on its subordinates average salary
4. maxSalaryPercentage (Optional default: 1.5) - MAX Percent of salary manager can have based on its subordinates average salary
```

#### Run template
```bash
mvn clean install
java -jar "target/sghq_assignment-1.0-SNAPSHOT.jar" ${filePathFromRoot} ${maxDepthAllowed} ${minSalaryPercentage} ${maxSalaryPercentage}
```

#### Example:

```bash
mvn clean install
java -jar "target/sghq_assignment-1.0-SNAPSHOT.jar" /Users/giorgiodishvili/Development/sghq_assignment/src/main/resources/employee.csv 4 1.2 1.5
```

### Dummy Employee CSV generation script
#### Description
- `Generates dummy CSV file of employee X size`
- `prints progress of employeeIds and generated file abosulute path` 

#### Run template

```bash
python3 scripts/generate_employee_csv.py ${amount}
```
#### Example

```bash
python3 scripts/generate_employee_csv.py 100
```
