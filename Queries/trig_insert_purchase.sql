create or replace trigger trig_insert_purchase
after insert on purchases
for each row
declare  
new_order number;
new_qoh number;
threshold number;  
low_qoh_threshold exception;
insufficient_qoh exception;
supplier_id suppliers.sid%type;
pur_ptime purchases.ptime%type;
lvm customers.last_visit_date%type;  
begin


select (qoh-:new.qty)  into new_qoh from products where pid=:new.pid;
select qoh_threshold into threshold  from products where pid=:new.pid;

if(new_qoh<0) then raise insufficient_qoh;
end if;
if(new_qoh<threshold) then raise low_qoh_threshold;
 else
	 update products set qoh=qoh-:new.qty
	 where pid=:new.pid;
 end if;

 select to_date(to_char(last_visit_date, 'DD-MON-YYYY'),'DD-MON-YYYY') into lvm from customers where cid = :new.cid;
select to_date(to_char(:new.ptime, 'DD-MON-YYYY'),'DD-MON-YYYY') into pur_ptime from dual;

if(pur_ptime<>lvm) then update customers set visits_made=(visits_made+1), last_visit_date=pur_ptime where cid=:new.cid;
end if;

exception
   when low_qoh_threshold then
       dbms_output.put_line('current qoh of the product is below the required threshold and new supply is required');
		select (threshold - new_qoh)+11 into new_order from dual;
		select min(sid) into supplier_id from supply where supply.pid=:new.pid;
		insert into supply values (s_supply.nextval,:new.pid,supplier_id,sysdate,new_order);
		update products set qoh=new_qoh+new_order where pid=:new.pid;
 	when insufficient_qoh then
 		dbms_output.put_line('Insufficient quantity in stock.');

end;
/