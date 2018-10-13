
select * from country 

select * from currency

select * from account 

select * from stockexchange 

select count(*) from company
select * from company 

select * from marketcode where codedate is not null

select * from stock where symboldate is not null

select * from securitytype 

select count(*) from isin 
select * from isin order by isin limit 1000

select * from stock order by symbol
select * from future
select * from option
select * from fixedincome 

select count(1) from historicalquote
select * from historicalquote order by qdatetime, securityid

select * from earning 


--truncate table historicalquote
--truncate table future
--truncate table stock
--truncate table option 
--truncate table securityspeci cascade


select * from isin_seq 

select * from security_seq 


select * from stock order by symbol


select * 
from stock s inner join historicalquote hq
on s.securityid = hq.securityid
where s.symbol in('PET 4', 'PETR4')
order by qdatetime


select * 
from stock s inner join earning e
on s.securityid = e.securityid
where s.symbol in('PET 4', 'PETR4')
order by qdatetime


select * from stock s 
where securityid = 1213 s.symbol = 'PETR4'

select * from historicalquote where historicalquoteid =686054

select * from historicalquote where open=1.554 and close = 1.509

select * from company 
order by companyname
where companyname like '%petr%'

select * from company where marketname = 'PETROBRAS'

select * from stock order by securityid
select * from marketcode

select s.Symbol, s.securityID, s.symbolDate
from Company c inner join Isin i on c.companyID=i.companyID 
inner join Stock s on s.isinID=i.isinID 
where c.MarketName = 'BRASMOTOR' 
and s.stockSpecification = 'PR' --and s.symbolDate > '2007-12-03'
order by s.symbolDate desc

except
select s.Symbol, s.securityID, s.symbolDate
from Company c inner join Isin i on c.companyID=i.companyID 
inner join Stock s on s.isinID=i.isinID 
where c.MarketName = 'BRASMOTOR' 
and s.stockSpecification = 'PR' and s.symbolDate > '1996-12-03'






select m.marketcode,c.companyid
from marketcode m inner join company c on m.companyid=c.companyid
where c.marketname='PETROBRAS' and ('1999-02-14'>=m.codedate)
order by m.codedate desc

select s.Symbol, s.securityID
from Stock s
inner join (company c 
inner join Isin i on c.companyID=i.companyID)sc
on s.isinID=sc.isinID 
where sc.MarketName = 'PETROBRAS' and s.stockSpecification = 'PR'


update stock
set symboldate = null --"1995-05-04 00:00:00"
where securityid=24


update historicalquote 
set
ask=ask/100,
avgprice=avgprice/100,
bid=bid/100,
close=close/100,
high=high/100,
low=low/100,
open=open/100,
volume=volume/100

