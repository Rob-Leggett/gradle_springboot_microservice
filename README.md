# Microservice - Payslip
[![Build Status](https://travis-ci.org/Rob-Leggett/gradle_springboot_microservice.svg?branch=master)](https://travis-ci.org/Rob-Leggett/gradle_springboot_microservice)

Supports employees retrieving there payslip information.

# Interface Specification

***Individual Monthly Payslip***

POST - /payslip/monthly/summary 

This endpoint is used to return an employee payslip with is calculated with the income and month details provided.

***Request Body***

```
{
  "employee": {
    "firstName": "Sample",
    "lastName": "User"
  },
  "month": "JUNE",
  "income": {
    "gross": 60050,
    "superannuationPercentage": 9
  }
}
```

***Response Body***

```
{
  "employee": {
    "firstName": "Sample",
    "lastName": "User"
  },
  "month": "JUNE",
  "salary": {
    "gross": 5004,
    "net": 4082,
    "tax": 922,
    "superannuation": 450
  }
}
```

***Group Monthly Payslip***

POST - /payslip/monthly/summaries 

This endpoint is used to return an array of employee payslip with is calculated with the income and month details provided.

***Request Body***

```
{
  "incomeSummaries": [{
    "employee": {
      "firstName": "Sample",
      "lastName": "User"
    },
    "month": "JUNE",
    "income": {
      "gross": 60050,
      "superannuationPercentage": 9
    }
  },
  {
    "employee": {
      "firstName": "Test",
      "lastName": "User"
    },
    "month": "JULY",
    "income": {
      "gross": 120000,
      "superannuationPercentage": 10
    }
  }]
}

```

***Response Body***

```
{
  "payslipSummaries": [
    {
      "employee": {
        "firstName": "Sample",
        "lastName": "User"
      },
      "month": "JUNE",
      "salary": {
        "gross": 5004,
        "net": 4082,
        "tax": 922,
        "superannuation": 450
      }
    },
    {
      "employee": {
        "firstName": "Test",
        "lastName": "User"
      },
      "month": "JULY",
      "salary": {
        "gross": 10000,
        "net": 7304,
        "tax": 2696,
        "superannuation": 1000
      }
    }
  ]
}
```

***Group Monthly Payslip (Batch)***

POST - /payslip/monthly/summaries/batch

This endpoint is used to return an csv of employee payslips with is calculated with the income and month details provided.

***Request***

This request must be sent as form-data.  It expects it to be sent across with the key being 'file' and the value as File of type text/csv or application/csv.

Example Data

```
David,Rudd,60050,9,MARCH
Ryan,Chen,120000,10,MARCH
```

***Response***

This response will either return a attachment file of type text/csv
 
Example Data

```
David,Rudd,MARCH,5004,922,4082,450
Ryan,Chen,MARCH,10000,2696,7304,1000
```

# Setup

The following is required to build:

* gradle 2.14.1
* Java 8 (Oracle)

# Userful commands
## Build
Run all unit and functional tests, and build a war file.

	gradle build


## Run the server stand alone
Useful during development. Needs to be manually restarted if the war changes.

	gradle bootRun

Locally logs will come out in the build/logs directory. This is set using a system property in the build.gradle file.
You can tail the log file when running to startServer to get consistent logging in your terminal.

## Code Coverage

To get code coverage run the following command after your build.

	gradle jacocoTestReport

# Additional Information

## Swagger

http://localhost:8080/v2/api-docs

http://localhost:8080/_admin/swagger/index.html

Donations
====================

### How you can help?

Any donations received will be able to assist me provide more blog entries and examples via GitHub, any contributions provided is greatly appreciated.

Thanks for your support.

[![paypal](https://www.paypal.com/en_US/i/btn/btn_donateCC_LG.gif)](https://www.paypal.com/cgi-bin/webscr?cmd=_donations&business=EV2ZLZBABFJ34&lc=AU&item_name=Research%20%26%20Development&currency_code=AUD&bn=PP%2dDonationsBF%3abtn_donateCC_LG%2egif%3aNonHosted)
