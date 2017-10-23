#!/bin/bash
#初始化mysql
#rm -rf /var/lib/mysql.* rm -rf /var/lib/mysql/*
mysql_install_db --user=mysql --ldata=/var/lib/mysql
#启动mysql服务
mysqld_safe &
#修改数据库root用户的密码
mysql -u root  << !
UPDATE mysql.user SET Password = PASSWORD('123456') WHERE User = 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
flush privileges;
exit;
!
#设置数据库相关环境变量
HOSTNAME="127.0.0.1"                                           #数据库信息
PORT="3306"
USERNAME="root"
PASSWORD="123456"
DBNAME="framework"                                                       #数据库名称

#创建数据库
create_db_sql="create database IF NOT EXISTS ${DBNAME}"
mysql -h${HOSTNAME}  -P${PORT}  -u${USERNAME} -p${PASSWORD} -e "${create_db_sql}"
#导入数据
mysql -h${HOSTNAME}  -P${PORT}  -u${USERNAME} -p${PASSWORD} -D ${DBNAME} -e "/opt/src-nsw/nsw-base-web/src/main/resources/docker/framework.sql"

#启动redis
service redis start
/opt/tomcat7/bin/startup.sh
tail -f /opt/tomcat7/logs/catalina.out