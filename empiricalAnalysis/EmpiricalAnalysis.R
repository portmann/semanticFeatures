rm(list = ls())
dev.off() 
cat("\014")  
library(zoo)
library(ellipse)
Sys.setenv(JAVA_HOME='C:\\Program Files\\Java\\jdk1.8.0_73\\jre')
library(xlsx)
library(XLConnect)

Name <- c("Apple","Amazon","Boeing","Facebook","Ford","Intel","Microsoft","Netflix")
Name <- "Apple"
Name <- "Amazon"
Name <- "Boeing"
Name <- "Facebook"
Name <- "Ford"
Name <- "Intel"
Name <- "Microsoft"
Name <- "Netflix"
RunRegression(Name,1)


ddd <- NULL
for (variable in Name) {
  print(RunRegression(variable))
  ddd <- rbind(ddd, RunRegression(variable))
}

ddd$PB <- (ddd$PB-mean(ddd$PB))/sd(ddd$PB)
ddd$Vol <- (ddd$Vol-mean(ddd$Vol))/sd(ddd$Vol)
ddd$SIZE <- (ddd$SIZE-mean(ddd$SIZE))/sd(ddd$SIZE)
ddd$ROA <- (ddd$ROA-mean(ddd$ROA))/sd(ddd$ROA)
ddd$SUE <- (ddd$SUE-mean(ddd$SUE))/sd(ddd$SUE)
fit <- lm(ddd$CAR ~ ddd$CumPos + ddd$CumNeg + ddd$CumSurprise + ddd$CumUncertainty + ddd$PB + ddd$Vol +ddd$SIZE +ddd$ROA +ddd$SUE)
summary(fit)

na.lomf <- function(x, a=!is.na(x)){
  
  x[which(a)[c(1,1:sum(a))][cumsum(a)+1]]
  
}
getCumsum <- function(IND, Vector) {
  lis <- list()
  for (i in 1:(length(IND)-1)) {
    lis[[i]] <- cumsum(Vector[IND[i]:(IND[i+1]-1)])
  }
  lis
}
#This function calculate volatility between two EADs
getVol <- function(IND, Vector){
  lis <- list()
  for (i in 1:(length(IND)-1)) {
    r <- Vector[IND[i]:(IND[i+1]-1)]
    r_bar <- mean(r)
    RV <- sqrt((sum((r-r_bar)^2))/(length(r)-1))
    lis[[i]] <- RV*rep(1,length(r))
  }
  lis
}
List2Vec <- function(List){
  Vec <- c()
  for (i in 1:length(List)) {
    Vec <- c(Vec,List[[i]])
  }
  Vec
}

RunRegression <- function(Name,case = 1){

  ##### Load Earning Information #####
  dat <- readWorksheet(loadWorkbook("EarningSurprise.xlsx"),sheet = 1)
  dat <- dat[294:nrow(dat),]
  dat$Col1 <- as.Date(as.character(dat$Col1),"%Y-%m-%d")
  Earning <- list()
  Earning[["Amazon"]]<- dat[dat$X.AMAZON.COM.INCORPORATED.!="NA",][,c(1,12)]
  Earning[["Apple"]]<- dat[dat$X.APPLE.INCORPORATED.!="NA",][,c(1,2)]
  Earning[["Boeing"]]<- dat[dat$X.THE.BOEING.COMPANY.!="NA",][,c(1,10)]
  Earning[["Facebook"]]<- dat[dat$X.FACEBOOK.INCORPORATED.!="NA",][,c(1,11)]
  Earning[["Ford"]]<- dat[dat$X.FORD.MOTOR.COMPANY.!="NA",][,c(1,9)]
  Earning[["Intel"]]<- dat[dat$X.INTEL.CORPORATION.!="NA",][,c(1,7)]
  Earning[["Microsoft"]]<- dat[dat$X.MICROSOFT.CORPORATION.!="NA",][,c(1,8)]
  Earning[["Netflix"]]<- dat[dat$X.NETFLIX.INCORPORATED.!="NA",][,c(1,6)]
  for (name in c("Apple","Amazon","Boeing","Facebook","Ford","Intel","Microsoft","Netflix")) {
    colnames(Earning[[name]]) <- c("Date",name)
  }
  ##### Earning Announcement Day #####
  EAD <-  Earning[[Name]][,1]
  ES <- as.double(Earning[[Name]][,2])
  ##### Load ROA Informaiton #####
  datROA <- readWorksheet(loadWorkbook("Variables.xlsx"),sheet = 1)
  datROA$ROA <- as.Date(as.character(datROA$ROA),"%Y-%m-%d")
  colnames(datROA)[1] <- "Date"
  ROA <- list()
  for (name in c("Apple","Amazon","Boeing","Facebook","Ford","Intel","Microsoft","Netflix")) {
    col <- c("Date",name)
    ROA[[name]] <- datROA[c(col)]
  }
  ##### Load SIZE Informaiton #####
  datSIZE <- readWorksheet(loadWorkbook("Variables.xlsx"),sheet = 2)
  datSIZE$SIZE <- as.Date(as.character(datSIZE$SIZE),"%Y-%m-%d")
  colnames(datSIZE)[1] <- "Date"
  SIZE <- list()
  for (name in c("Apple","Amazon","Boeing","Facebook","Ford","Intel","Microsoft","Netflix")) {
    col <- c("Date",name)
    SIZE[[name]] <- datSIZE[c(col)]
  }
  ##### Stock Beta #####
  BetaSet <- list()
  BetaSet[["Amazon"]] <- 1.0422
  BetaSet[["Apple"]] <- 1.55
  BetaSet[["Boeing"]] <- 1.0542
  BetaSet[["Facebook"]] <- 0.7362
  BetaSet[["Ford"]] <- 1.0686
  BetaSet[["Intel"]] <- 0.9332
  BetaSet[["Microsoft"]] <- 0.8532
  BetaSet[["Netflix"]] <- 0.8954
  ##### Stock Price #####
  file <-  paste("Stock\\", paste(Name, " - Copy.csv",sep = ""), sep = "")
  # file <-  paste("Stock\\", paste(Name, ".csv",sep = ""), sep = "")
  StockPrice <- read.csv(file, header = TRUE, sep = ",")
  StockPrice$Date <- as.Date(StockPrice$Date,"%m/%d/%Y")
  StockPrice <- StockPrice[order(StockPrice$Date),]
  R <- diff(StockPrice$Adj.Close)/(StockPrice$Adj.Close[1:length(StockPrice$Adj.Close)-1])*100
  R <- c(0, R)
  StockPrice$R <- R
  ##### Calculate the volatility between two EADs #####
  EADIndexVol <- c(1,match(EAD,StockPrice$Date),nrow(StockPrice)+1)
  Vol <- List2Vec(getVol(EADIndexVol, StockPrice$R))
  StockPrice$Vol <- Vol
  ##### Risk-Free Rate #####
  file3 <- "DTB1YR.csv"
  Rf <- read.csv(file3,header = TRUE, sep = ",")
  colnames(Rf) <- c("Date","Rf")
  Rf$Date <- as.Date(Rf$Date,"%Y-%m-%d")
  ##### Benchmark Rate #####
  file4 <-"SP500.csv"
  SP500 <- read.csv(file4, header = TRUE, sep = ",")
  SP500$Date <- as.Date(SP500$Date,"%Y-%m-%d")
  SP500 <- SP500[order(SP500$Date),]
  Rm <- diff(SP500$Adj.Close)/(SP500$Adj.Close[1:length(SP500$Adj.Close)-1])*100
  Rm <- c(0, Rm)
  SP500$Rm <- Rm
  ##### Creating Price dataframe #####
  Price <- merge(x = StockPrice[,c("Date","Adj.Close","Volume","R","PB","Vol")], y = Rf, by = "Date", all.x = T)
  Price <- merge(x = Price, y = SP500[c("Date","Rm")], by = "Date", all.x = T)
  Price <- merge(x = Price, y = ROA[[Name]], by = "Date", all.x = T)
  colnames(Price)[colnames(Price) == Name] = "ROA"
  Price <- merge(x = Price, y = SIZE[[Name]], by = "Date", all.x = T)
  colnames(Price)[colnames(Price) == Name] = "SIZE"
  Price <- merge(x = Price, y = Earning[[Name]], by = "Date", all.x = T)
  colnames(Price)[colnames(Price) == Name] = "SUE"
  Price$ROA <- na.lomf(Price$ROA)
  Price$SIZE <- na.lomf(log(Price$SIZE))
  Price$SUE <- na.lomf(as.double(Price$SUE))
  Price$Rf <- na.lomf (Price$Rf)
  Beta = BetaSet[[Name]]
  Price$Re <- Price$Rf + Beta*(Price$Rm - Price$Rf) 
  Price$Ra <- Price$R - Price$Re
  Price$Rd <- ifelse(Price$Ra > 0, 1, 0)
  ##### Feature #####
  file2 <-  paste("Features\\", paste(Name, "Reuters.csv",sep = ""), sep = "")
  FeaturesRaw <- read.csv(file2, header = TRUE, sep = ",")
  FeaturesRaw$Date <- as.Date(FeaturesRaw$Date,"%Y-%m-%d")
  FeaturesRaw <- FeaturesRaw[order(FeaturesRaw$Date),]
  FeaturesRaw <- FeaturesRaw[2:nrow(FeaturesRaw),]
  FeaturesRaw$Counts <- rep(1,nrow(FeaturesRaw))
  features1 <- aggregate(FeaturesRaw[c("Value_Positive","Uncertainty","Surprise","Counts")],by = list(FeaturesRaw$Date),FUN = sum)
  features2 <- aggregate(FeaturesRaw[c("Valence_Pos","Valence_Neg")],by = list(FeaturesRaw$Date),FUN = mean)
  colnames(features1)[1] <- "Date"
  colnames(features2)[1] <- "Date"
  Features <- merge(x = features1, y = features2, by = "Date", all = TRUE)
  Features$Date <- as.Date(Features$Date,"%Y-%m-%d")
  file2.2 <-  paste("Features\\", paste(Name, "Seeking_Alpha.csv",sep = ""), sep = "")
  FeaturesRaw <- read.csv(file2.2, header = TRUE, sep = ",")
  FeaturesRaw$Date <- as.Date(FeaturesRaw$Date,"%Y-%m-%d")
  FeaturesRaw <- FeaturesRaw[order(FeaturesRaw$Date),]
  FeaturesRaw <- FeaturesRaw[2:nrow(FeaturesRaw),]
  FeaturesRaw$Counts <- rep(1,nrow(FeaturesRaw))
  features1 <- aggregate(FeaturesRaw[c("Value_Positive","Uncertainty","Surprise","Counts")],by = list(FeaturesRaw$Date),FUN = sum)
  features2 <- aggregate(FeaturesRaw[c("Valence_Pos","Valence_Neg")],by = list(FeaturesRaw$Date),FUN = mean)
  colnames(features1)[1] <- "Date"
  colnames(features2)[1] <- "Date"
  Features2 <- merge(x = features1, y = features2, by = "Date", all = TRUE)
  Features2$Date <- as.Date(Features2$Date,"%Y-%m-%d")
  Feature <- rbind(Features,Features2)
  Feature <- Feature[order(Feature$Date),]
  features1 <- aggregate(Feature[c("Value_Positive","Uncertainty","Surprise","Counts")],by = list(Feature$Date),FUN = sum)
  features2 <- aggregate(Feature[c("Valence_Pos","Valence_Neg")],by = list(Feature$Date),FUN = mean)
  colnames(features1)[1] <- "Date"
  colnames(features2)[1] <- "Date"
  Feature <- merge(x = features1, y = features2, by = "Date", all = TRUE)
  ##### Calculate the culmulative return between two earning announcement days #####
  EADIndex <- c(1,match(EAD,Price$Date),nrow(Price)+1)
  Price$CAR <- List2Vec(getCumsum(EADIndex,Price$Ra))
  ##### Calculate the culmulative value of features between two earning announcement days #####
  EADIndex2 <- c(1,match(EAD,Feature$Date),nrow(Feature)+1)  #This are the indices of EADs
  EADIndex2 <- EADIndex2[!is.na(EADIndex2)]
  Feature$CumPos <- List2Vec(getCumsum(EADIndex2,Feature$Valence_Pos))
  Feature$CumNeg <- List2Vec(getCumsum(EADIndex2,Feature$Valence_Neg))
  Feature$CumUncertainty <- List2Vec(getCumsum(EADIndex2,Feature$Uncertainty))
  Feature$CumSurprise <- List2Vec(getCumsum(EADIndex2,Feature$Surprise))
  ##### Integrate df #####
  if (case == 1) {
    df <- merge(x = Feature, y = Price[,c("Date","Volume","Ra","CAR","PB","Vol","SIZE","ROA","SUE")], by = "Date", all.x = T)
  }
  if (case == 2) {
    df <- merge(x = Features, y = Price[,c("Date","Adj.Close","Volume","Ra","CAR")], by = "Date", all.x = T)
  }
  if (case == 3) {
    df <- merge(x = Features2, y = Price[,c("Date","Adj.Close","Volume","Ra","CAR")], by = "Date", all.x = T)
    
  }

  df$Ra <- na.lomf(df$Ra)
  df$CAR <- na.lomf(df$CAR)
  df$Volume <- na.lomf(df$Volume)
  df$PB <- na.lomf(df$PB)
  df$Vol <- na.lomf(df$Vol)
  df$SIZE  <- na.lomf(df$SIZE )
  df$ROA <- na.lomf(df$ROA)
  df$SUE <- na.lomf(df$SUE)
  
  
  df[is.na(df)] <- 0
  df$Ra <- (df$Ra-mean(df$Ra))/sd(df$Ra)
  df$CAR <- (df$CAR-mean(df$CAR))/sd(df$CAR)
  df$Valence_Pos <- (df$Valence_Pos-mean(df$Valence_Pos))/sd(df$Valence_Pos)
  df$Valence_Neg <- (df$Valence_Neg-mean(df$Valence_Neg))/sd(df$Valence_Neg)
  df$CumPos <- (df$CumPos-mean(df$CumPos))/sd(df$CumPos)
  df$CumNeg <- (df$CumNeg-mean(df$CumNeg))/sd(df$CumNeg)
  df$Volume <- (df$Volume-mean(df$Volume))/sd(df$Volume)
  df$CumSurprise <- (df$CumSurprise-mean(df$CumSurprise))/sd(df$CumSurprise)
  df$CumUncertainty <- (df$CumUncertainty-mean(df$CumUncertainty))/sd(df$CumUncertainty)
  # df <- df[(df$Surprise!=0 )&( df$Uncertainty !=0),]
  ##### Extract the data for regression #####
  if (case == 1) {
    ExtractDat <- function(df,IND){
      dff <- NULL
      for (i in 2:length(IND)) {
        if (IND[i] + 56 < nrow(df)) {
          dff <- rbind(dff, df[IND[i]+56,])
        }
      }
      dff
    }
    dff <- ExtractDat(df, EADIndex2)
  }
  ##### Regression Analysis of one company#####
  # plotcorr(cor(df[c("Ra","Valence_Pos","Valence_Neg","Value_Positive","Surprise","Uncertainty","Counts","Volume")]))

  fit <- lm(df$CAR ~ df$CumPos + df$CumNeg + +df$CumSurprise + df$CumUncertainty  + df$PB)
  # fit <- lm(df$CAR ~ df$Valence_Pos + df$Valence_Neg + df$Surprise +  df$Uncertainty +df$Counts)
  # fit <- lm(df$CAR ~ df$Valence_Pos + df$Valence_Neg + df$Surprise +  df$Uncertainty)
  # fit <- lm(df$Ra ~ df$Valence_Pos + df$Valence_Neg + df$Surprise +  df$Uncertainty)
  # fit <- lm(df$CAR ~ df$Valence_Pos + df$Valence_Neg + df$CumUncertainty +  df$CumSurprise)
  summary(fit)
  dff
}

##### Regression Analysis of many companies#####

fit <- lm(dff$CAR ~ dff$Valence_Pos + dff$Valence_Neg + dff$CumSurprise +  dff$CumUncertainty + dff$Counts + dff$Volume)
summary(fit)

###### Create DataFrame for plots #####
df2 <- merge(x = Price[,c("Date","Adj.Close","Volume","Ra","CAR")], y = Feature, by = "Date", all.x = T)
df2[is.na(df2)] <- 0
##### 
# df2$Ra <- na.lomf(df2$Ra)
# df2$Adj.Close <- na.lomf(df2$Adj.Close)
# df2$R <- na.lomf(df2$R)
# df2$Rd <- na.lomf(df2$Rd)
# df2$CAR <- na.lomf(df2$CAR)
# df2$Volume <- na.lomf(df2$Volume)
# df2[is.na(df2)] <- 0

IMD <- df2$Date[df2$R > 3]
DED <- df2$Date[df2$R < -3]
#################### plot CAR ###################

par(mfrow = c(2,1))
par(mar = c(5,5,1,1))
plot(df2$Date, df2$CAR, xlab = NA, ylab = "CAR_Apple", type = "l", xaxt = "n")

plot(df2$Date, df2$CAR, xlab = "Date", ylab = "CAR_Microsoft", type = "l", xaxt = "n")

axis.Date(side = 1, at = EAD,format = "%Y-%m-%d")

for (i in EAD) {
  abline(v = i, lty = 4)
}

#################### plot Counts ###################
par(mfrow = c(1,1))
par(mar = c(5,5,2,5))
plot(df2$Date, df2$Adj.Close, xlab = "Date", ylab = "Stock Price", type = "l", xaxt = "n")
# plot(df$Date, df$Volume, type = "l")
axis.Date(side = 1, at = EAD,format = "%Y-%m-%d")

for (i in EAD) {
  abline(v = i, lty = 4)
}

par(mar = c(5,5,20,5))
par(new = T)
plot(df2$Date,df2$Counts, pch = 16, axes = F, xlab = NA, ylab = NA, cex = 0.5, col = 'red')
lines(df2$Date,df2$Counts, col = "red")
axis(side = 4)
mtext(side = 4, text = "News Coverage", line = 3)

for (i in IMD) {
  abline(v = i, lty = 4, col = "blue")
}

for (i in DED) {
  abline(v = i, lty = 1, col = "green")
}
#################### plot Surprise ###################
par(mar = c(1,1,1,5))
# plot(df$Date, df$Adj.Close,type = "l")
plot(df2$Date, df2$Adj.Close, xlab = "Date", ylab = "Stock Price", type = "l", xaxt = "n")
axis.Date(side = 1, at = EAD,format = "%Y-%m-%d")

for (i in EAD) {
  abline(v = i, lty = 4)
}
par(mar = c(5,5,20,5))
par(new = T)
plot(df2$Date, df2$Surprise, pch = 16, axes = F, xlab = NA, ylab = NA, cex = 0.5, col = 'purple')
lines(df2$Date, df2$Surprise, col = "purple")
axis(side = 4)
mtext(side = 4, text = "Surprise", line = 3)

for (i in IMD) {
  abline(v = i, lty = 4, col = "red")
}
#################### plot Uncertainty ###################
par(mar = c(5,5,2,5))
plot(df2$Date, df2$Adj.Close,type = "l")
axis.Date(side = 1, at = EAD,format = "%Y-%m-%d")

for (i in EAD) {
  abline(v = i, lty = 4)
}
par(mar = c(5,5,20,5))

par(mfrow = c(2,1))
par(new = T)
plot(df2$Date,df2$Uncertainty, pch = 16, cex = 0.5, axes = F, xlab = "Date", ylab = NA, col = 'blue')
lines(df2$Date,df2$Uncertainty, col = "blue")
axis(side = 4)
mtext(side = 4, text = "Uncertainty", line = 3)

for (i in IMD) {
  abline(v = i, lty = 4, col = "red")
}
#################### plot Positive ###################
par(mar = c(5,5,2,5))
plot(df$Date, df$Adj.Close,type = "l")
# plot(df$Date, df$Volume, type = "l")
axis.Date(side = 1, at = EAD,format = "%Y-%m-%d")

for (i in EAD) {
  abline(v = i, lty = 4)
}

par(mar = c(5,5,20,5))
par(new = T)
plot(df$Date,df$Valence_Pos, pch = 16, axes = F, xlab = NA, ylab = NA, cex = 0.5, col = 'red')
lines(df$Date,df$Valence_Pos, col = "red")
axis(side = 4)

fit <- lm(df$Volume~df$Counts)
summary(fit)

fit2 <- lm(df$Volume~df$Surprise)
summary(fit2)

for (i in IMD) {
  abline(v = i, lty = 4, col = "blue")
}

for (i in DED) {
  abline(v = i, lty = 1, col = "green")
}
##### plot abnormal returns #####
plot(df$Date, df$Ra, type = "l")
axis.Date(side = 1, at = EAD,format = "%Y-%m-%d")
for (i in EAD) {
  abline(v = i, lty = 4)
}


