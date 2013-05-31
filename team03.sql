--team 03
--Casey Balzer
--Nick Mckeever
--4-3-13
--crb68@pitt.edu
--nmm47@pitt.edu

--part 1    sql


-- tables for final project
drop table Customer cascade constraints;
drop table Administrator cascade constraints;
drop table Product cascade constraints;
drop table Bidlog cascade constraints;
drop table Category cascade constraints;
drop table BelongsTo cascade constraints;
drop table OurDate cascade constraints;
DROP SEQUENCE product_sequence;
DROP SEQUENCE bidlog_sequence;
purge recyclebin;

create table Customer
(
login varchar2(10),
password varchar2(10),
name varchar2(20),
address varchar2(30),
email varchar2(20),
constraint pk_customer_login primary key(login)
);

create table Administrator
(
login varchar2(10),
password varchar2(10),
name varchar2(20),
address varchar2(30),
email varchar2(20),
constraint pk_administrator_login primary key(login)
);

create table Product
(
auction_id int,
name varchar2(20),
description varchar2(30),
seller varchar2(10),
start_date date,
min_price int,
number_of_days int,
status varchar2(15) not null,
buyer varchar2(10),
sell_date date,
amount int,
constraint pk_product_auction_id primary key(auction_id),
constraint fk_product_seller foreign key(seller) references Customer(login),
constraint fk_product_buyer foreign key(buyer) references Customer(login)
);

create table Bidlog
(
bidsn int,
auction_id int,
bidder varchar2(10),
bid_time date,
amount int,
constraint pk_bidlog_bidsn primary key(bidsn),
constraint fk_bidlog_auction_id foreign key(auction_id) references Product(auction_id),
constraint fk_bidlog_bidder foreign key(bidder) references Customer(login)
);

create table Category
(
name varchar2(20),
parent_category varchar2(20),
constraint pk_category_name primary key(name),
constraint fk_category_parent_category foreign key(parent_category) references Category(name)
);

create table BelongsTo
(
auction_id int,
category varchar2(20),
constraint pk_belongsto_aid_cat primary key(auction_id,category),
constraint fk_belongsto_auction_id foreign key(auction_id) references Product(auction_id),
constraint fk_belongsto_category foreign key(category) references Category(name)
);

Create sequence product_sequence
start with 0
increment by 1
minvalue 0
maxvalue 10000;

Create sequence bidlog_sequence
start with 0
increment by 1
minvalue 0
maxvalue 10000;

create table Ourdate(
	C_Date date
);
commit;

-- sample data

insert into ADMINISTRATOR values ('admin','root','administrator','6810 SENSQ','admin@1555.com');
                                                   
insert into CUSTOMER values ('user0','pwd','user0','6810 SENSQ',' user0@1555.com');  
insert into CUSTOMER values ('user1','pwd','user1','6811 SENSQ',' user1@1555.com');  
insert into CUSTOMER values ('user2','pwd','user2','6812 SENSQ',' user2@1555.com');  
insert into CUSTOMER values ('user3','pwd','user3','6813 SENSQ',' user3@1555.com');  
insert into CUSTOMER values ('user4','pwd','user4','6814 SENSQ',' user4@1555.com');                                                         
                                                                                
insert into PRODUCT values (product_sequence.nextval,'Database','SQL ER-design','user0',to_date('04-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'), '50',2, 'sold','user2',to_date('06-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),52);
insert into PRODUCT values (product_sequence.nextval,'17 inch monitor','17 inch monitor','user0',to_date('06-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),100,2,'sold','user4',to_date('08-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),110);
insert into PRODUCT values (product_sequence.nextval,'DELL INSPIRON 1100','DELL INSPIRON notebook','user0',to_date('07-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),500,7,'underauction',null,null,null);
insert into PRODUCT values (product_sequence.nextval,'Return of the King','fantasy','user1',to_date('07-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),40,2,'sold','user2',to_date('09-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),40);
insert into PRODUCT values (product_sequence.nextval,'The Sorcerer Stone','Harry Porter series','user1',to_date('08-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),40,2,'sold','user3',to_date('10-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),40);
insert into PRODUCT values (product_sequence.nextval,'DELL INSPIRON 1100','DELL INSPIRON notebook','user1',to_date('09-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),200,1,'withdrawn',null,null,null);
insert into PRODUCT values (product_sequence.nextval,'Advanced Database','SQL Transaction index','user1',to_date('10-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),50,2,'underauction',null,null,55);
   
insert into BIDLOG values (bidlog_sequence.nextval,0,'user2',to_date('04-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),50); 
insert into BIDLOG values (bidlog_sequence.nextval,0,'user3',to_date('04-dec-2012/09:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),53); 
insert into BIDLOG values (bidlog_sequence.nextval,0,'user2',to_date('05-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),60); 
insert into BIDLOG values (bidlog_sequence.nextval,1,'user4',to_date('06-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),100); 
insert into BIDLOG values (bidlog_sequence.nextval,1,'user2',to_date('07-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),110); 
insert into BIDLOG values (bidlog_sequence.nextval,1,'user4',to_date('07-dec-2012/09:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),120); 
insert into BIDLOG values (bidlog_sequence.nextval,3,'user2',to_date('07-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),40); 
insert into BIDLOG values (bidlog_sequence.nextval,4,'user3',to_date('09-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),40); 
insert into BIDLOG values (bidlog_sequence.nextval,6,'user2',to_date('07-dec-2012/08:00:00am','dd-mon-yyyy/hh12:mi:ssAM'),55);                                                  

insert into CATEGORY values ('Books',null);
insert into CATEGORY values ('Textbooks','Books');
insert into CATEGORY values ('Fiction books','Books');
insert into CATEGORY values ('Magazines','Books');
insert into CATEGORY values ('Computer Science','Textbooks');
insert into CATEGORY values ('Math','Textbooks');
insert into CATEGORY values ('Philosophy','Textbooks');
insert into CATEGORY values ('Computer Related',null);
insert into CATEGORY values ('Desktop PCs','Computer Related');
insert into CATEGORY values ('Laptops','Computer Related');
insert into CATEGORY values ('Monitors','Computer Related');
insert into CATEGORY values ('Computer books','Computer Related');                                                      

insert into BELONGSTO values(0,'Computer Science');
insert into BELONGSTO values(0,'Computer books');
insert into BELONGSTO values(1,'Monitors');
insert into BELONGSTO values(2,'Laptops');
insert into BELONGSTO values(3,'Fiction books');
insert into BELONGSTO values(4,'Fiction books');
insert into BELONGSTO values(5,'Laptops');
insert into BELONGSTO values(6,'Computer Science');
insert into BELONGSTO values(6,'Computer books');

insert into OURDATE values (to_date('11-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM'));

commit;

-- 1. customer interface
-- a

-- gets parent/root categories

select name
from category
where parent_category is null;

-- refine search down on selected category
-- example

select name
from category
where parent_category = 'Books';

select name
from category
where parent_category = 'Textbooks';

-- after category is selected/ find all products

-- sort by max bid
select p.auction_id,p.name,p.description,p.amount as Highest_Bid, p.min_price
from belongsto b join product p on b.auction_id = p.auction_id
where b.category = 'Computer Science'
order by case when p.amount is null then 1 else 0 end, p.amount desc;

-- sort by product name
select p.auction_id,p.name,p.description,p.amount as Highest_Bid, p.min_price
from belongsto b join product p on b.auction_id = p.auction_id
where b.category = 'Computer Science'
order by p.name;

-- b
-- search for keyword in description example/will implement with variable in java

-- single keyword
select *
from PRODUCT
where description like '%keyword%';

-- two keywords
select *
from PRODUCT
where description like '%keyword1%' and description like '%keyword2%';

-- c

-- put product for auction

create or replace procedure PUT_PRODUCT
	(name IN varchar2, description IN varchar2, seller IN varchar2, min_price IN int, number_of_days IN int,category IN varchar)
as
	last_auction_id int;		
	cur_date date;
begin
	select c_date into cur_date from OURDATE;
	select max(auction_id) into last_auction_id from PRODUCT;
	insert into PRODUCT values(last_auction_id+1,name,description,seller,cur_date,min_price,number_of_days,'underauction',null,null,null);
	insert into BELONGSTO values(last_auction_id+1,category);
end;
/

-- d
-- updates OURDATE time by 5 secs every time a new bid is inserted in to bidlog

create or replace trigger tri_bidTimeUpdate
after insert
on BIDLOG
declare
	old_date date;
begin
	select C_date into old_date from OURDATE;
	update OURDATE
	set C_date = old_date + 5/(24*60*60)
	where C_date = old_date;
end;
/


-- updates amount in Product is bid on

create or replace trigger tri_updateHighBid
after insert
on BIDLOG
for each row
begin
	update PRODUCT
	set product.amount = :new.amount
	where product.auction_id = :new.auction_id;
end;
/

-- sample bid
--insert into bidlog values (bidid,auction_id,bidder,bid_time,amount);

-- e. selling product

-- ex on auction_id = 0

-- find number of bids placed on item
select count(auction_id)
from bidlog
where auction_id = 0;


-- second highest bid, more then 1 bid
select amount from (
				select amount,rownum as snum
				from (select *
						from bidlog
						where auction_id = 0
						order by amount desc)
				where rownum <=2) 
where snum > 1;

-- second highest bidder more then 1 bid
select bidder from (
				select bidder,rownum as snum
				from (select *
						from bidlog
						where auction_id = 0
						order by amount desc)
				where rownum <=2) 
where snum > 1;

		
-- sell product more then 1 bid, updates status, sell_date, buyer(second highest), amount(second highest)
update product
set status = 'sold', sell_date = (select * from ourdate), amount = (select amount from (
																						select amount,rownum as snum
																						from (select *
																								from bidlog
																								where auction_id = 0
																								order by amount desc)
																						where rownum <=2) 
																	where snum > 1),
			buyer = (select bidder from (
										select bidder,rownum as snum
										from (select *
												from bidlog
												where auction_id = 0
												order by amount desc)
										where rownum <=2) 
					where snum > 1)
where auction_id = 0;


-- only one bid placed

select amount
from bidlog
where auction_id = 3;

-- only one bid, sell product
update product
set status = 'sold', sell_date = (select * from ourdate), amount = (select amount from bidlog where auction_id = 3), 
	buyer = (select bidder from bidlog where auction_id = 3)
where auction_id = 3;


-- withdraw product any number of bids
update product
set status = 'withdrawn', sell_date = null,buyer = null,amount = null
where auction_id = 0;

-- f
-- suggest products for user

-- example suggesting products for user2
select p.name, desirability
from product p join (select auction_id, count(Distinct bidder) as desirability
						from bidlog
						where auction_id in (select auction_id
												from bidlog
												where bidder = 'user2') and bidder != 'user2'
						group by auction_id) t2 on p.auction_id = t2.auction_id
order by desirability desc;

--Part 2 Administrator
--A
--(LOGIN,PASSWORD,NAME,ADDRESS,EMAIL)
--since login is a primary key, if login name already exists the insert will be rejected

insert into CUSTOMER values ('barry123','321barry','Barry Bonds','123 SanFran dr',' bb@giants.com');

--B
--In this example I just used the existing data for update
update Ourdate
set C_Date =to_date('11-dec-2012/12:00:00am','dd-mon-yyyy/hh12:mi:ssAM');

--c
--1 for all products
--first we would check the status of each item using java
select auction_id, status from product;
--if status is returned as sold we would use this query but add the aution_id in the where clause
select name,status,amount,buyer from product;
--if status is retuned underauction
select p.name, p.status, max(b.amount) as bmax, b.bidder from product p, bidlog b
where p.auction_id = b.auction_id and p.status='underauction'
group by p.name,p.status,b.bidder;

--for specific user or login
--2
--first we would check the status of each item using java   ex using user0
select auction_id, status from product where seller='user0';
--if status is returned as sold we would use this query using auction id from prev results
select name,status,amount,buyer from product where auction_id='0';
--if status is retuned underauction
select p.name, p.status, max(b.amount) as bmax, b.bidder from product p, bidlog b
where p.auction_id = b.auction_id and p.status='underauction' and p.auction_id ='2'
group by p.name,p.status,b.bidder;

--D
--a
CREATE OR REPLACE FUNCTION 
Product_Count(x IN int, c IN varchar2)
return int 
is
pcount int;
begin
select count(product.auction_id) into pcount from product,belongsto,ourdate
where product.auction_id = belongsto.auction_id and 
upper(belongsto.category) = upper(c) and product.sell_date between add_months(Ourdate.C_Date,-x) and Ourdate.C_Date; 
return (pcount);
end;
/

CREATE OR REPLACE FUNCTION
Bid_Count(x IN int, u IN varchar2)
return int 
is
bcount int;
begin
select count(bidlog.bidder) into bcount from bidlog,ourdate
where bidlog.bidder = u and bidlog.bid_time between add_months(Ourdate.C_Date,-x) and Ourdate.C_Date;
return (bcount);
end;
/

CREATE OR REPLACE FUNCTION
Buying_Amount(x IN number, u IN varchar2)
return number 
is
amount number;
begin
select sum(product.amount) into amount from product,ourdate
where product.buyer = u and product.sell_date between add_months(Ourdate.C_Date,-x) and Ourdate.C_Date;
return (amount);
end;
/
--i
--using java we would find all leaf categories 
select name from category;
-- the user would specify k for top number of categories and x for past months
-- for every leaf category we could then make this query and using java store the top results
-- x=6 and Fiction books for example 
select product_count(6,'Fiction books') from dual;

--ii
--using java we would find all distinct parent categories not including null
select distinct parent_category from category where parent_category !='null';
--for each parent we would find all of its leafs using Textbooks as example
select name from category where parent_category='Textbooks';
--for each parents categories leafs we can now use our function to tally the total
--of all leafs from the parent category using java to store the results for top k
--for parent category Textbooks, computer science would be 1 of the three function calls
--to add together for the final result to be stored
select product_count(6,'Computer Science') from dual;

--iii
--using java we would find all customers
select login from customer;
-- the user would specify k for top number of bidders and x for past months
-- for every user we could then make this query and using java store the top results
-- x=6 and user2 for example 
select bid_count(6,'user2') from dual;

--iV
--using java we would find all customers
select login from customer;
-- the user would specify k for top number of spenders and x for past months
-- for every user we could then make this query and using java store the top results
-- x=6 and user2 for example 
select buying_amount(6,'user2') from dual;
--part 3 
--a)   using user0 as example, if login and password match all fields will be returned 
select * from customer where customer.login='user0' and customer.password='pwd';

--b)

create or replace trigger closeAuctions
after update
on ourdate
begin
	update product p
set p.status='sold'
where p.start_date + p.number_of_days < 
(select c_date from ourdate o);
end;
/



