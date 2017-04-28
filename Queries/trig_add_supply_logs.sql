create or replace trigger trig_add_supply_logs
after insert on supply
for each row
begin
   insert into logs values(s_logs.nextval,user,sysdate,'supply','insert',:new.sup#);
end;
/