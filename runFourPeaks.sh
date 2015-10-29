#!/bin/bash

CORES=7
OUTPUT=${OUTPUT:-output}
PACKAGE=assignment2
array=(
#    AssignmentTwo
    #ContinuousPeaksTest
    #CountOnesTest
    #FlipFlopTest
    FourPeaksTest
    #KnapsackTest
	#TravelingSalesmanTest
	#NQueensTest
	#MaxKColoringTest
	#NNoptimization
)

mkdir -p ${OUTPUT}

count=0

function set_header {
	if [[ "$1" == MaxKColoringTest ]]; then
		echo "algorithm,evaluations,N,L,K,optimal value,found optimum,time" > ${OUTPUT}/${1}.csv
	elif [[ "$1" == CountOnesTest ]]; then
		echo "algorithm,evaluations,N,optimal value,time" > ${OUTPUT}/${1}.csv		
	elif [[ "$1" == FlipFlopTest ]]; then
		echo "algorithm,evaluations,N,optimal value,time" > ${OUTPUT}/${1}.csv		
	elif [[ "$1" == FourPeaksTest ]]; then
		echo "algorithm,evaluations,N,T,optimal value,time" > ${OUTPUT}/${1}.csv			
	else
		echo "algorithm,iterations,training error,testing error,training time,testing time" > ${OUTPUT}/${1}.csv
	fi
}

for j in "${array[@]}";do
    #for i in 1 100 1000 10000 100000 1000000;do
	for T in 2 5 10 15 20;do
		for N in {50..200..50};do
			echo Running test $j with N=$N T=$T...
			if [[ $count -eq 0 ]]; then
				set_header ${j}
			fi
			count=$((count+1))
			
			java -cp ABAGAIL.jar $PACKAGE.${j} $N $T >| /tmp/${j}${N}${T}.csv &

			if [[ $(expr $count % ${CORES}) -eq 0 ]]; then
				# wait for cores to free up
				wait
			fi
		done
	done
    # catch overflow
    wait
    cat /tmp/${j}*.csv >> ${OUTPUT}/${j}.csv
    rm -f /tmp/${j}*.csv
done