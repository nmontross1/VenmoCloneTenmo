
TRUNCATE users, accounts, transfers CASCADE;

--CREATE USERS
INSERT INTO users (user_id,username,password_hash)
VALUES (1001, 'test1','test'),
       (1002, 'test2','test'),
       (1003, 'test3','test'),
       (1004, 'test4','test');

--CREATE ACCOUNTS AND BALANCES
INSERT INTO accounts(account_id,user_id,balance)
values (2001,1001,1000), --TEST1 $1000
        (2002,1002,1000), --TEST2 $1000
        (2003,1003,900), --TEST3 $900
        (2004,1004,1100); -- TEST4 $1100

--CREATE TRANSFER HISTORY
INSERT INTO transfers(transfer_id,transfer_type_id,transfer_status_id,account_from,account_to,amount)
  VALUES (3001,2,2,2001,2003,100), --TEST1 sends to TEST 3 $100. APPROVED
         (3002,2,2,2001,2002,50.50), -- TEST1 sends to TEST 2 $50.50. APPROVED
         (3003,1,1,2002,2001,50),--TEST2 sends to TEST1 $50. APPROVED
         (3004,2,2,2001,2002,500); -- TEST1 sends to TEST2 $500. APPROVED
