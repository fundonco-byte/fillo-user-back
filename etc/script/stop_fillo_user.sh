#!/bin/bash
cd /home/fillo/back/
KEYWORD="fillo-user-0.0.1-SNAPSHOT.jar"

find_java_svr_pid() {
  PID=$(ps aux | grep "$KEYWORD" | grep -v grep | awk '{print $2}')
}

kill_java_svr() {
  if [ -n "$PID" ]; then
    kill "$PID"
    echo "Fillo 유저 프로세스 프로세스 (PID: $PID)를 종료했습니다."
  else
    echo "실행 중인 Fillo 유저 프로세스 프로세스를 찾을 수 없습니다."
  fi
}

find_java_svr_pid
kill_java_svr