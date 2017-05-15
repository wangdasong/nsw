prompt Importing table SMS_CONTAINER...
set feedback off
set define off

-- Create table
create table SMS_CONTAINER
(
  id             VARCHAR2(36) default sys_guid() not null,
  code           VARCHAR2(100) not null,
  name           VARCHAR2(300),
  type           VARCHAR2(10) not null,
  page_id        VARCHAR2(36) not null,
  container_id   VARCHAR2(36),
  tmp_flg        VARCHAR2(1),
  create_date    DATE,
  create_user_id VARCHAR2(36),
  update_date    DATE,
  update_user_id VARCHAR2(36),
  sort           NUMBER(10) default 9999
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
comment on column SMS_CONTAINER.id
  is '容器主键';
comment on column SMS_CONTAINER.code
  is '容器编号';
comment on column SMS_CONTAINER.name
  is '容器名称';
comment on column SMS_CONTAINER.type
  is '容器类型';
comment on column SMS_CONTAINER.page_id
  is '所属页面';
comment on column SMS_CONTAINER.container_id
  is '所属容器';
comment on column SMS_CONTAINER.tmp_flg
  is '模板标识';
comment on column SMS_CONTAINER.create_date
  is '创建时间';
comment on column SMS_CONTAINER.create_user_id
  is '创建用户ID';
comment on column SMS_CONTAINER.update_date
  is '更新时间';
comment on column SMS_CONTAINER.update_user_id
  is '更新用户ID';
-- Create/Recreate primary, unique and foreign key constraints 
alter table SMS_CONTAINER
  add constraint SMS_CONTAINER_PK primary key (ID)
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

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('43C2106D7D146981E0530100007F5045', 'INDEX_SEARCH_FORM', '查询条件', '3', '43C1EFBF99D56624E0530100007F6C36', '43C2106D7D166981E0530100007F5045', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 1, 'AAAnHCAASAAAACTAAA');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('43C2106D7D156981E0530100007F5045', 'INDEX_RESULT_TABLE', '结果表格', '3', '43C1EFBF99D56624E0530100007F6C36', '43C2106D7D166981E0530100007F5045', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 3, 'AAAnHCAASAAAACTAAB');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45C9E64604CE50CEE0530100007FFF47', 'PAGE_LIST_SEARCH_FORM', '查询', '3', 'pagelist', 'pagelist', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 1, 'AAAnHCAASAAAACTAAC');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('systemmenu', 'INDEX_PAGE_MENU', '页面菜单', '2', 'systemmenu', 'systemmenu', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 9999, 'AAAnHCAASAAAACTAAD');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('editwidget', 'EDIT_WIDGET', '编辑控件弹出框', '3', 'editwidget', null, '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 9999, 'AAAnHCAASAAAACTAAE');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('4566E147345439D4E0530100007FE669', 'EDIT_WIDGET_SEARCH_FORM', '控件编辑', '3', 'editwidget', 'editwidget', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 1, 'AAAnHCAASAAAACTAAF');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('4566E147345539D4E0530100007FE669', 'EDIT_WIDGET_RESULT_TABLE', '属性编辑', '3', 'editwidget', 'editwidget', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 2, 'AAAnHCAASAAAACTAAG');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('editcontainer', 'EDIT_CONTAINER', '编辑容器弹出框', '3', 'editcontainer', null, '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 9999, 'AAAnHCAASAAAACTAAH');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45B82D0D4BC73C6BE0530100007F344A', 'EDIT_CONTAINER_ITEMS', '容器编辑', '3', 'editcontainer', 'editcontainer', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 2, 'AAAnHCAASAAAACTAAI');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('pagelist', 'PAGE_LIST', '页面列表弹出框', '3', 'pagelist', null, '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 9999, 'AAAnHCAASAAAACTAAJ');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45C9E64604CF50CEE0530100007FFF47', 'PAGE_LIST_RESULT_TABLE', '页面列表', '3', 'pagelist', 'pagelist', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 2, 'AAAnHCAASAAAACTAAK');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45CF551103547231E0530100007F1742', 'MAIN_CONTENT', '内容容器', '3', '45CF551103537231E0530100007F1742', '45CF551103537231E0530100007F1742', '1', to_date('11-01-2017 17:35:29', 'dd-mm-yyyy hh24:mi:ss'), '01062317', to_date('11-01-2017 17:35:29', 'dd-mm-yyyy hh24:mi:ss'), '01062317', 9999, 'AAAnHCAASAAAACTAAL');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45DCE2D7119464B1E0530100007F0E02', 'CONTAINER_OPREATION', '功能操作', '3', '43C1EFBF99D56624E0530100007F6C36', '43C2106D7D166981E0530100007F5045', null, to_date('12-01-2017 09:45:43', 'dd-mm-yyyy hh24:mi:ss'), '01062317', to_date('12-01-2017 09:45:43', 'dd-mm-yyyy hh24:mi:ss'), '01062317', 2, 'AAAnHCAASAAAACTAAM');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45CF592A3D5C722DE0530100007FC843', 'NEWCONTAINER', '查询', '3', '45CF551103537231E0530100007F1742', '45CF551103547231E0530100007F1742', null, to_date('11-01-2017 17:36:37', 'dd-mm-yyyy hh24:mi:ss'), '01062317', to_date('11-01-2017 17:36:37', 'dd-mm-yyyy hh24:mi:ss'), '01062317', 9999, 'AAAnHCAASAAAACUAAA');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('43C2106D7D166981E0530100007F5045', 'INDEX_PAGE_CONTENT', '页面内容', '3', '43C1EFBF99D56624E0530100007F6C36', '43C1EFBF99D56624E0530100007F6C36', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '1062317', to_date('16-12-2016', 'dd-mm-yyyy'), '1062317', 1, 'AAAnHCAASAAAACVAAA');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('editelement', 'EDIT_ELEMENT', '编辑元素弹出框', '3', 'editelement', null, '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 9999, 'AAAnHCAASAAAACWAAA');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('452EAFF747E95656E0530100007F1086', 'EDIT_ELEMENT_SEARCH_FORM', '元素编辑', '3', 'editelement', 'editelement', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 1, 'AAAnHCAASAAAACWAAB');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('452EAFF747EA5656E0530100007F1086', 'EDIT_ELEMENT_RESULT_TABLE', '属性编辑', '3', 'editelement', 'editelement', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 2, 'AAAnHCAASAAAACWAAC');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('editpage', 'EDIT_PAGE', '编辑页面弹出框', '3', 'editpage', null, '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 9999, 'AAAnHCAASAAAACXAAA');

insert into SMS_CONTAINER (ID, CODE, NAME, TYPE, PAGE_ID, CONTAINER_ID, TMP_FLG, CREATE_DATE, CREATE_USER_ID, UPDATE_DATE, UPDATE_USER_ID, SORT, ROWID)
values ('45C89D3FFBC22AB0E0530100007F2AB4', 'EDIT_PAGE_ITEMS', '页面编辑', '3', 'editpage', 'editpage', '0', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', to_date('16-12-2016', 'dd-mm-yyyy'), '01062317', 2, 'AAAnHCAASAAAACXAAB');

prompt Done.
