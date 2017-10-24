#!/bin/bash
#初始化mysql
rm -rf /var/lib/mysql/*
mysql_install_db --user=mysql --ldata=/var/lib/mysql
#启动mysql服务
echo "#############################开始启动mysql#########################"
mysqld --user=mysql --datadir=/var/lib/mysql --skip-networking &
pid="$!"
mysql=( mysql --protocol=socket -uroot )
for i in {30..0}; do
    if echo 'SELECT 1' | "${mysql[@]}" &> /dev/null; then
        break
    fi
    echo 'MySQL init process in progress...'
    sleep 1
done
if [ "$i" = 0 ]; then
    echo >&2 'MySQL init process failed.'
    exit 1
fi
echo "#############################结束启动mysql#########################"

#启动redis
echo "#############################开始启动redis#########################"
service redis start
echo "#############################结束启动redis#########################"

#修改数据库root用户的密码
mysql -u root << !
UPDATE mysql.user SET Password = PASSWORD('123456') WHERE User = 'root';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'%' IDENTIFIED BY '123456' WITH GRANT OPTION;
flush privileges;
!
#设置数据库相关环境变量
HOSTNAME="127.0.0.1"                                           #数据库信息
PORT="3306"
USERNAME="root"
PASSWORD="123456"
DBNAME="framework"                                                       #数据库名称

#创建数据库
create_db_sql="create database IF NOT EXISTS ${DBNAME}"
mysql -u${USERNAME} -p${PASSWORD} -e "${create_db_sql}"
#导入数据
mysql -u${USERNAME} -p${PASSWORD} -D ${DBNAME} < /opt/src-nsw/nsw-base-web/src/main/resources/docker/framework.sql

/opt/tomcat7/bin/startup.sh
tail -f /opt/tomcat7/logs/catalina.out