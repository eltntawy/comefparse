
-- query to find IPs that mode more than a certain number of requests for a given time period.
select ip
from log_access_ip
where date BETWEEN DATE_FORMAT('2017-01-01.00:00:00','%y-%m-%d.%H:%i:%s') and DATE_FORMAT('2017-01-01.14:00:00','%y-%m-%d.%H:%i:%s')
GROUP BY ip
HAVING count(ip) > 20;


-- query to find requests made by a given IP
select *
from log_access_ip
where ip = '192.168.169.194';