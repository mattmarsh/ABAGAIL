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
	#NNoptimization
	NNoptSA
)

mkdir -p ${OUTPUT}

count=0

function set_header {
	echo "algorithm,iterations,training error,testing error,temperature,cooling rate" > ${OUTPUT}/${1}.csv

}

for TEST in "${array[@]}";do
    #for i in 1 100 1000 10000 100000 1000000;do
	ITER=1000
	for i in {1..10};do
		for T in 1 10 100 1000 10000 100000 1e6 1e7 1e8 1e9 1e10 1e11 1e12 1e13;do
			for COOLING in 0.5 0.55 0.6 0.65 0.7 0.75 0.8 0.85 0.9 0.95;do
			
				printf "."
				if [[ $count -eq 0 ]]; then
					set_header $TEST
				fi
				count=$((count+1))
				
				java -cp ABAGAIL.jar $PACKAGE.$TEST $ITER $T $COOLING >| /tmp/${TEST}${T}${COOLING}.csv &

				if [[ $(expr $count % ${CORES}) -eq 0 ]]; then
					# wait for cores to free up
					wait
				fi
			done
		done
	done
    # catch overflow
    wait
    cat /tmp/${TEST}*.csv >> ${OUTPUT}/${TEST}.csv
    rm -f /tmp/${TEST}*.csv
done