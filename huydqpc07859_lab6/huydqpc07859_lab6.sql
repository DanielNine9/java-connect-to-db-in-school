create database lab6_pc07859
go
use lab6_pc07859
go

create table book(
	id int identity(1,1) primary key,
	title nvarchar(50),
	price float
)

create table STUDENT(
	id int identity(1,1) PRIMARY KEY,
	nameStudent NVARCHAR(100),
	addressName NVARCHAR(100),
	parentName NVARCHAR(100),
	contactNo VARCHAR(20),
	standardName NVARCHAR(100),
	fees int,
	regdate datetime 
)


