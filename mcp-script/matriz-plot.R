setwd(system("pwd", intern = T)) # setando o diretorio corrente como wordir
dados  <- read.csv2(file="matriz.csv",header=F, sep = ",") # importar da matriz
retorno <- c()
for(i in 1:nrow(dados))
  for(j in 1:ncol(dados)){
    peso = dados[i,j];
    if(peso!=0){ # usando apenas os nos que possuem chamadas
      soma = sum(dados[j,]) + peso; # somatorio de nos que partem de B
      metric = peso / soma;
      expoent = exp(-1*(metric));
      retorno<-rbind(retorno, c(i,j,metric,expoent)) # print(paste(i, j, peso, soma, metric, sep=" "));
    }
  }
plot(sort(retorno[,3])) # plotando o gráfico
