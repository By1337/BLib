#!/bin/bash

cd ./V1_16_5
mvn org.by1337.bnms:bnms-plugin:1.3.1:init
cd ..

for v in V1_20_6 V1_21_5 V1_21_6 V1_17_1 V1_18_2 V1_19_4 V1_20_1 V1_20_2 V1_20_4; do
    echo "init $v..."
    cd "$v"
    mvn ca.bkaw:paper-nms-maven-plugin:1.4.8:init
    cd ..
done
