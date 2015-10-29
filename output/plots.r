library(ggplot2)
nn_opt <- read.csv("NNoptimization.csv", stringsAsFactors = FALSE)

#ANN train error
p <- ggplot(data = nn_opt, aes(x=iterations, y=training.error, col=algorithm))
p <- p + geom_line() + geom_smooth(size=1)
p <- p + xlab("Iterations") + ylab("Training Error Rate") + ggtitle("ANN Weight Optimization: Train Set Error")
ggsave("nn_train_error.png",plot=p,scale = .6)

#ANN test error
p <- ggplot(data = nn_opt, aes(x=iterations, y=testing.error, col=algorithm))
p <- p + geom_line() + geom_smooth(size=1)
p <- p + xlab("Iterations") + ylab("Testing Error Rate") + ggtitle("ANN Weight Optimization: Test Set Error")
ggsave("nn_test_error.png",plot=p,scale = .6)

#ANN train time
p <- ggplot(data = nn_opt, aes(x=iterations, y=training.time, col=algorithm))
p <- p + geom_line() + geom_smooth(size=1) + scale_y_log10()
p <- p + xlab("Iterations") + ylab("Training Time (s)") + ggtitle("ANN Weight Optimization: Training Time")
ggsave("nn_train_time.png",plot=p,scale = .6)

#ANN test time
p <- ggplot(data = nn_opt, aes(x=iterations, y=testing.time, col=algorithm))
p <- p + geom_line() + geom_smooth(size=1) 
p <- p + xlab("Iterations") + ylab("Testing Time (s)") + ggtitle("ANN Weight Optimization: Testing Time")
ggsave("nn_test_time.png",plot=p,scale = .6)

# nn_opt_sa <- read.csv("NNoptSA.csv", stringsAsFactors = FALSE)
# 
# p <- ggplot(data=nn_opt_sa, aes(x=cooling.rate, y=temperature)) 
# p <- p + geom_point(aes(color=testing.error)) + scale_y_log10() + scale_color_gradient(low="blue)
# p

# K coloring

kcolor <- read.csv("MaxKColoringTest.csv", stringsAsFactors = FALSE)
p1 <- ggplot(data = subset(kcolor, N==100), aes(x=evaluations, y=optimal.value, col=algorithm))
p1 <- p1 + geom_line() + geom_smooth(size=1) 
p1 <- p1 + xlab("Function Evaluations") + ylab("Maximum value") + ggtitle("Max K Coloring: Max Value (N=100)")
#ggsave("maxkcolor_opt_100.png",plot=p,scale = .6)

p2 <- ggplot(data = subset(kcolor, N==500), aes(x=evaluations, y=optimal.value, col=algorithm))
p2 <- p2 + geom_line() + geom_smooth(size=1) 
p2 <- p2 + xlab("Function Evaluations") + ylab("Maximum value") + ggtitle("Max K Coloring: Max Value (N=500)")
#ggsave("maxkcolor_opt_500.png",plot=p,scale = .6)

p3 <- ggplot(data = subset(kcolor, N==100), aes(x=time, y=optimal.value, col=algorithm))
p3 <- p3 + geom_line() + geom_smooth(size=1) 
p3 <- p3 + xlab("Execution Time") + ylab("Maximum Value") + ggtitle("Max K Coloring: Execution Time (N=100)")

p4 <- ggplot(data = subset(kcolor, N==500), aes(x=time, y=optimal.value, col=algorithm))
p4 <- p4 + geom_line() + geom_smooth(size=1) 
p4 <- p4 + xlab("Execution Time") + ylab("Maximum Value") + ggtitle("Max K Coloring: Execution Time (N=500)")
multiplot(p1,p2,p3,p4, cols = 2)

#count Ones

count_ones <- read.csv("CountOnesTest.csv", stringsAsFactors = FALSE)
p1 <- ggplot(data = subset(count_ones, N==150), aes(x=evaluations, y=optimal.value, col=algorithm))
p1 <- p1 + geom_line() + geom_smooth(size=1) 
p1 <- p1 + xlab("Function Evaluations") + ylab("Maximum value") + ggtitle("Count Ones: Max Value (N=150)")
p1
ggsave("count_ones_opt.png",plot=p1,scale = .6)

p3 <- ggplot(data = subset(count_ones, N==150), aes(x=time, y=optimal.value, col=algorithm))
p3 <- p3 + geom_line() + geom_smooth(size=1) 
p3 <- p3 + xlab("Execution Time") + ylab("Maximum Value") + ggtitle("Count Ones: Execution Time (N=150)")
ggsave("count_ones_time.png",plot=p3,scale = .6)

#flipflop test

fourpeaks <- read.csv("FourPeaksTest.csv", stringsAsFactors = FALSE)
p1 <- ggplot(data = subset(fourpeaks, N==100 & T == 5), aes(x=evaluations, y=optimal.value, col=algorithm))
p1 <- p1 + geom_line() + geom_smooth(size=1) 
p1 <- p1 + xlab("Function Evaluations") + ylab("Maximum value") + ggtitle("Four Peaks: Max Value (N=100)")
p1
ggsave("flipflop_opt.png",plot=p1,scale = .6)

p3 <- ggplot(data = subset(fourpeaks, N==100 & T == 5), aes(x=time, y=optimal.value, col=algorithm))
p3 <- p3 + geom_line() + geom_smooth(size=1) 
p3 <- p3 + xlab("Execution Time") + ylab("Maximum Value") + ggtitle("Flip Flop: Execution Time (N=100)")
ggsave("flipflop_time.png",plot=p3,scale = .6)
