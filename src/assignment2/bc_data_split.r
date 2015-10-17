library(caret)
set.seed(1234)

# normalize function
normalize <- function(x) {
  return ((x - min(x)) / (max(x) - min(x)))
}

# load breast cancer data
bc <- read.csv("wisc_bc_data.csv", stringsAsFactors = FALSE)
bc <- bc[-1] # remove first id col
bc$diagnosis <- factor(bc$diagnosis, levels = c("B", "M"), labels = c(0, 1))

# normalize to 0-1 and also calculate z-score of data
bc_n <- as.data.frame(lapply(bc[-1], normalize))
#bc_n$diagnosis <- bc$diagnosis
#bc_z <- as.data.frame(scale(bc[-1]))
#bc_z$diagnosis <- bc$diagnosis

# split into test and train sets
train_index <- createDataPartition(bc$diagnosis, p=0.8, list=FALSE, times=1)
bc_train_n <- bc_n[train_index,]
bc_test_n <- bc_n[-train_index,]
bc_train_labels <- bc[train_index,1]
bc_test_labels <- bc[-train_index,1]

write.table(bc_train_n, file = "bcTrainData.csv",row.names=FALSE, na="",col.names=FALSE, sep=",")
write.table(bc_test_n, file = "bcTestData.csv",row.names=FALSE, na="",col.names=FALSE, sep=",")
write.table(bc_train_labels, file = "bcTrainLabels.csv",row.names=FALSE, na="",col.names=FALSE, sep=",",quote=FALSE)
write.table(bc_test_labels, file = "bcTestLabels.csv",row.names=FALSE, na="",col.names=FALSE, sep=",",quote=FALSE)
