library(ggplot2)

setwd("/home/shotaro/myProjects/mcp/tcc-outputs/cost/per-project/")

interfacesData <- read.csv("AIO-per-interface-metrics.csv", header = T, sep = ";")

ggplot(interfacesData) +
  geom_bar(aes(x = interfaces, y = metric),
    stat = "identity", position = "dodge", fill="#25364d", width = 0.6) +
  theme_bw() +
  xlab("Pares de interfaces") +
  ylab("Métrica") +
  ggtitle("Custo por interfaces") +
  theme(plot.title = element_text(hjust = 0.5))

ggsave("plot-interfaces.png", width = 6.9375, height = 3.7916666667, unit = "in")

#######

library(ggplot2)

setwd("/home/shotaro/myProjects/mcp/tcc-outputs/cost/per-project/")

categoriesData <- read.csv("AIO-per-category-metrics.csv", header = T, sep = ";")

categories <- c("insertion", "deletion", "search", "access", "other")

for (ctgr in categories) {
  subdata <- subset(categoriesData, category==ctgr)
  ggplot(subdata) +
    geom_bar(aes(x = paste(from, to, sep = "-"), y = metric),
             stat = "identity", position = "dodge", fill="#25364d", width = 0.6) +
    theme_bw() +
    xlab("Pares de interfaces") +
    ylab("Métrica") +
    ggtitle(paste("Custo para a categoria", ctgr, sep=" ")) +
    theme(plot.title = element_text(hjust = 0.5))
  ggsave(paste("plot-",ctgr,".png",sep = ""), width = 6.9375, height = 3.7916666667, unit = "in")
}

#######

setwd("/home/shotaro/myProjects/mcp/tcc-outputs/cost/")

metricsData <- read.csv("AIO-metrics.csv", header = T, sep = ";")

matrixIntf <- matrix(c("List", "Set", "List", "Map", "Map", "List", "Map", "Set", "Set", "List", "Set", "Map"),
  nrow=6,
  ncol=2,
  byrow = TRUE
)

categories <- c("insertion", "deletion", "search", "access", "other")

print(matrixIntf)

for (row in 1:nrow(matrixIntf)) {
  for (ctgr in categories) {
    data <- subset(metricsData, category==ctgr & from==matrixIntf[row, 1] & to==matrixIntf[row, 2], select = metric)
    if(length(data$metric) < 3 || length(data$metric) >= 5000) {
      print("out of bounds")
      print(paste(ctgr, matrixIntf[row, 1], matrixIntf[row, 2], sep = "-"))
      next
    }
    if(length(unique(data$metric))==1) {
      print(paste("unique -> ",unique(data$metric), sep = ""))
      print(paste(ctgr, matrixIntf[row, 1], matrixIntf[row, 2], sep = "-"))
      next      
    }
    print("ok")
    print(paste(ctgr, matrixIntf[row, 1], matrixIntf[row, 2], sep = "-"))
    print(shapiro.test(data$metric))
  }
}