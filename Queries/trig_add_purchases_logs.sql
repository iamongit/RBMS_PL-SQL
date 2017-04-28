  create or replace trigger trig_add_purchases_logs
  after insert on purchases
  for each row
  begin
     insert into logs values(s_logs.nextval,user,sysdate,'purchases','insert',:new.pur#);
  end;
/