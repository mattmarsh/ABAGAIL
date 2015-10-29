#!/bin/bash

CORES=4
OUTPUT=${OUTPUT:-output}
PACKAGE=assignment2
array=(
#    AssignmentTwo
    #ContinuousPeaksTest
    #CountOnesTest
    #FlipFlopTest
    #FourPeaksTest
    #KnapsackTest
	#TravelingSalesmanTest
	#NQueensTest
	MaxKColoringTest
	#NNoptimization
)

mkdir -p ${OUTPUT}

count=0

function set_header {
	if [[ $1 -eq MaxKColoringTest ]]; then
		echo "algorithm,evaluations,N,L,K,optimal value,found optimum,time" > ${OUTPUT}/${1}.csv
	else
		echo "algorithm,iterations,training error,testing error,training time,testing time" > ${OUTPUT}/${1}.csv
	fi

}

for j in "${array[@]}";do
    #for i in 1 100 1000 10000 100000 1000000;do
	for N in {100..500..100};do
		echo Running test $j with N=$N...
		if [[ $count -eq 0 ]]; then
			set_header ${j}
		fi
		count=$((count+1))
		
		java -cp ABAGAIL.jar $PACKAGE.${j} $N >| /tmp/${j}${N}${i}.csv &

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