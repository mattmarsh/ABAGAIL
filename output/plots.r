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

#ANN 
p <- ggplot(data = nn_opt, aes(x=iterations, y=training.time, col=algorithm))
p <- p + geom_line() + geom_smooth(size=1)
p <- p + xlab("Iterations") + ylab("Training Time (s)") + ggtitle("ANN Weight Optimization: Training Time")
ggsave("nn_train_time.png",plot=p,scale = .6)

#ANN 
p <- ggplot(data = nn_opt, aes(x=iterations, y=testing.time, col=algorithm))
p <- p + geom_line() + geom_smooth(size=1)
p <- p + xlab("Iterations") + ylab("Testing Time (s)") + ggtitle("ANN Weight Optimization: Testing Time")
ggsave("nn_test_time.png",plot=p,scale = .6)

# nn_opt_sa <- read.csv("NNoptSA.csv", stringsAsFactors = FALSE)
# 
# p <- ggplot(data=nn_opt_sa, aes(x=cooling.rate, y=temperature)) 
# p <- p + geom_point(aes(color=testing.error)) + scale_y_log10() + scale_color_gradient(low="blue)
# p
