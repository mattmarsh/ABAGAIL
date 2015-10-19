#!/bin/bash

CORES=4
OUTPUT=${OUTPUT:-output}
PACKAGE=assignment2

array=(
#    AssignmentTwo
    # ContinuousPeaksTest
    # CountOnesTest
    # FlipFlopTest
    # FourPeaksTest
    # KnapsackTest
	NNoptimization
)

mkdir -p ${OUTPUT}

count=0

function set_header {
	echo "algorithm,iterations,training error,testing error,training time,testing time" > ${OUTPUT}/${1}.csv

}

for j in "${array[@]}";do
    #for i in 1 100 1000 10000 100000 1000000;do
	for i in {1..10001..500};do
        echo Running test with $i iterations...
        if [[ $count -eq 0 ]]; then
            set_header ${j}
        fi
		count=$((count+1))
		
        nice -n 1 java -cp ABAGAIL.jar $PACKAGE.${j} ${i} >| /tmp/${j}${i}.csv &

        if [[ $(expr $count % ${CORES}) -eq 0 ]]; then
            # wait for cores to free up
            wait
        fi
    done
    # catch overflow
    wait
    cat /tmp/${j}*.csv >> ${OUTPUT}/${j}.csv
    rm -f /tmp/${j}*.csv
done