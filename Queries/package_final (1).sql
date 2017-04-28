create or replace package project2 as
	type ref_cursor is ref cursor;

	function get_employees
	return ref_cursor;

	function get_customers
	return ref_cursor;

	function get_products
	return ref_cursor;

	function get_purchases
	return ref_cursor;

	function get_supply
	return ref_cursor;

	function get_suppliers
	return ref_cursor;
	
	/*To generate a report that shows monthly sale of product.*/
	function report_monthly_sale(p_pid in products.pid%type)
	return ref_cursor;
	
	/*To insert a detailed information about every valid purchase into purchases table.*/
	procedure proc_add_purchase
	(
	p_eid in purchases.eid%type,
	p_cid in purchases.cid%type,
	p_pid in purchases.pid%type,
	p_qty in purchases.qty%type
	);

	/*To add a new product that will be sold by retail business store.*/
    procedure proc_add_product
    (
    p_pid in products.pid%type,
    p_pname in products.pname%type,
    p_qoh in products.qoh%type,
    p_qoh_threshold in products.qoh_threshold%type,
    p_original_price in products.original_price%type,
    p_discnt_rate in products.discnt_rate%type
    );
	
end;
/

create or replace package body project2 as

function get_employees
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from employees order by eid;
    return rc;
end;

function get_customers
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from customers order by cid;
    return rc;
end;

function get_products
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from products order by pid;
    return rc;
end;

function get_purchases
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from purchases order by pur#;
    return rc;
end;

function get_supply
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from supply order by sup#;
    return rc;
end;

function get_suppliers
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select * from suppliers order by sid;
    return rc;
end;



function report_monthly_sale(p_pid in products.pid%type)
    return ref_cursor is
    rc ref_cursor;
begin
    open rc for
    select 
        pname as Product_Name, 
        mon as Month,
        year as Year , 
        qty_month as Total_Quantity, 
        amount_month as Total_Dollar_Amount, (amount_month/qty_month) as Average_Sale_Price
FROM 
    ( 
    SELECT   p.pname, 
             to_char(q.ptime,'MON') mon, 
              to_char(q.ptime,'yyyy') year, 
             sum(qty) as qty_month, 
             sum(total_price) as amount_month 
    FROM     products p, purchases q
    WHERE    p.pid=q.pid and p.pid=p_pid
    GROUP BY pname, to_char(q.ptime,'yyyy'), to_char(q.ptime,'MON')
  );


    return rc;
end;

procedure proc_add_purchase
(
    p_eid in purchases.eid%type,
    p_cid in purchases.cid%type,
    p_pid in purchases.pid%type,
    p_qty in purchases.qty%type
)
is 
    p_pur# purchases.pur#%type;
    p_total_price number;
    p_ptime purchases.ptime%type;
    p_original_price products.original_price%type;
    p_discnt_rate products.discnt_rate%type;
begin

    select s_purchases.nextval into p_pur# from dual;
    select original_price into p_original_price from products where pid=p_pid;
    select discnt_rate into p_discnt_rate from products where pid=p_pid;
    p_total_price:=((p_original_price*p_qty)-(p_original_price*p_qty*p_discnt_rate));
    select to_date(to_char(sysdate,'DD-MON-YYYY HH24:MI:SS'),'DD-MON-YYYY HH24:MI:SS') into p_ptime from dual;
    insert into purchases values(p_pur#,p_eid,p_pid,p_cid,p_qty,p_ptime,p_total_price);
end;

 procedure proc_add_product
(
    p_pid in products.pid%type,
    p_pname in products.pname%type,
    p_qoh in products.qoh%type,
    p_qoh_threshold in products.qoh_threshold%type,
    p_original_price in products.original_price%type,
    p_discnt_rate in products.discnt_rate%type
)
IS
begin
    insert into products values(p_pid,p_pname,p_qoh,p_qoh_threshold,p_original_price,p_discnt_rate);
end;



end;
/
