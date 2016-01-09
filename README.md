## odata-research
Goal: to make a POC of a .NET server and Java client

#To use OData server:

1. open ProductService solution in VS2013 or later and hit ctrl + F5

2. You can access with the following URLs:


http://localhost:62390/odata/Products?$select=Name,Price&$top=2&$orderby=Name%20desc
http://localhost:62390/odata/Products(1)?$select=Name,Price
http://localhost:62390/odata/Products
http://localhost:62390/odata/Products(1)
(filter for price >= 50)
http://localhost:62390/odata/Products?$filter=Price%20ge%2050 

For other OData URLs, see here:

http://www.asp.net/web-api/overview/odata-support-in-aspnet-web-api/supporting-odata-query-options

NOTE: this POC is based on the example found here:

http://www.asp.net/web-api/overview/odata-support-in-aspnet-web-api/odata-v4/create-an-odata-v4-endpoint

#To use Java client:

1. Open Eclipse (create a new workspace if necessary)

2. Import existing Maven project

3. Run the project to see examples work


NOTE: the Java client requires the .NET server to be up and running


