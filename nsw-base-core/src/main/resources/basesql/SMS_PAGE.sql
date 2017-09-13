prompt Importing table SMS_PAGE...
set feedback off
set define off
-- Create table
create table SMS_PAGE
(
  id             VARCHAR2(36) default sys_guid() not null,
  code           VARCHAR2(100) not null,
  name           VARCHAR2(300),
  screen_size    VARCHAR2(300),
  tmp_flg        VARCHAR2(1),
  create_date    DATE,
  create_user_id VARCHAR2(36),
  update_date    DATE,
  update_user_id VARCHAR2(36)
)
tablespace FINANCE_DATA
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the columns 
comment on column SMS_PAGE.id
  is '页面主键';
comment on column SMS_PAGE.code
  is '页面编号';
comment on column SMS_PAGE.name
  is '页面名称';
comment on column SMS_PAGE.screen_size
  is '页面大小';
comment on column SMS_PAGE.tmp_flg
  is '模板标识';
comment on column SMS_PAGE.create_date
  is '创建时间';
comment on column SMS_PAGE.create_user_id
  is '创建用户ID';
comment on column SMS_PAGE.update_date
  is '更新时间';
comment on column SMS_PAGE.update_user_id
  is '更新用户ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SMS_PAGE
  add constraint SMS_PAGE_PK primary key (ID)
  using index 
  tablespace FINANCE_DATA
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
insert into SMS_PAGE (ID, CODE, NAME, SCREEN_SIZE, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, ROWID)
values ('45CF551103537231E0530100007F1742', 'NEWPAGE', '新页面', null, null, null, null, null, null, 'AAAnG9AASAAAACDAAA');

insert into SMS_PAGE (ID, CODE, NAME, SCREEN_SIZE, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, ROWID)
values ('43C1EFBF99D56624E0530100007F6C36', 'INDEX', 'INDEX', null, '0', to_date('11-01-2017', 'dd-mm-yyyy'), '01062317', to_date('11-01-2017', 'dd-mm-yyyy'), '01062317', 'AAAnG9AASAAAACFAAA');

prompt Done.
