# odata-research
Goal: to make a POC of a .NET server and Java client

##To use .NET OData server:

1. open ProductService solution in VS2013 or later and hit ctrl + F5

2. You can access with the following URLs:


- all products

http://localhost:62390/odata/Products

- select all fields where the ID == 1

http://localhost:62390/odata/Products(1)

- select name and price fields from top 2 records sorted descending by Name

http://localhost:62390/odata/Products?$select=Name,Price&$top=2&$orderby=Name desc

- select name and price of the record where the ID == 1

http://localhost:62390/odata/Products(1)?$select=Name,Price

- filter for price >= 50

http://localhost:62390/odata/Products?$filter=Price ge 50 

For other OData URLs, see here:

http://www.asp.net/web-api/overview/odata-support-in-aspnet-web-api/supporting-odata-query-options

NOTE: this POC is based on the example found here:

http://www.asp.net/web-api/overview/odata-support-in-aspnet-web-api/odata-v4/create-an-odata-v4-endpoint

##To use Java client:

1. Open Eclipse (create a new workspace if necessary)

2. Import existing Maven project

3. Run the project to see examples work


NOTE: the Java client requires the .NET server to be up and running


