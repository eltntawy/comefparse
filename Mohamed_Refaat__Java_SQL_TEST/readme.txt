you can run this application with the below command line
* please note that you should create the database schem 'access_log_db' from file [access_log_db schema.txt] on local mysql server with username/password -> root/root 

project source code -> comefparse-master.zip

project on github -> https://github.com/eltntawy/comefparse

~$ java -cp "parser.jar" com.ef.Parser startDate=2017-01-01.00:00:11 --duration=daily --threshold=250 --accesslog=access.log