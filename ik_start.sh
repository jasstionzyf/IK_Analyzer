#!/bin/bash
export app_path=$PWD

cd ${app_path}/target/
exec nohup java -jar ./target/IK_Analyzer-0.0.1-SNAPSHOT.jar >./ik.out &
