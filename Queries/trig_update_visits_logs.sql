create or replace trigger trig_update_visits_logs
after update of visits_made on customers
for each row
begin
   insert into logs values(s_logs.nextval,user,sysdate,'customers','update',:new.cid);
end;
/