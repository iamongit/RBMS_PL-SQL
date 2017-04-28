create or replace trigger trig_update_qoh_logs
after update of qoh on products
for each row
begin
   insert into logs values(s_logs.nextval,user,sysdate,'products','update',:new.pid);
end;
/