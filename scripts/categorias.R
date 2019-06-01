library(ggplot2)
setwd("/home/shotaro/myProjects/mcp/tcc-outputs/statistic/")

data <- read.csv("all.csv", header = T, sep = ",")

print(data)

ggplot(data) +
  geom_bar(aes(x = category, y = quantity, fill = interface),
           stat = "identity", position = "dodge") +
  scale_y_continuous("", expand = c(0, 0)) +
  scale_x_discrete("Categorias") +
  scale_fill_manual(values = c("#468189", "#9DBEBB", "#9ea8a7")) +
  theme_classic(base_size = 18) +
  theme(axis.text.x = element_text(angle = 90, 
                                   hjust = 1, vjust = 0),
        axis.line = element_blank(),
        axis.ticks.x = element_blank())
