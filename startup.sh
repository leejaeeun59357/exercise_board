#!/bin/bash

WAR_NAME="exercise_board-0.0.1-SNAPSHOT.war"
echo "> build 파일명: $WAR_NAME" >> /home/ec2-user/deploy.log

echo "> build 파일 복사" >> /home/ec2-user/deploy.log
DEPLOY_PATH=/home/ec2-user/
cp /home/ec2-user/build/*.war $DEPLOY_PATH

echo "> 현재 실행중인 애플리케이션 PID 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(pgrep -f $WAR_NAME)

JAVA_OPTS=""

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> kill -9 $CURRENT_PID"
  kill -9 $CURRENT_PID
  JAVA_OPTS="-Dspring.jpa.hibernate.ddl-auto=none"
  sleep 5
fi

BUILD_WAR=$DEPLOY_PATH/$WAR_NAME
echo "> BUILD_WAR 배포"   >> /home/ec2-user/deploy.log
nohup java $JAVA_OPTS -jar $BUILD_WAR >> /home/ec2-user/deploy.log 2>/home/ec2-user/deploy_err.log &