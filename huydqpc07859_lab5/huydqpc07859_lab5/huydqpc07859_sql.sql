create database QLSV_PC07859

use QLSV_PC07859

create table STUDENTS(
	masv INT IDENTITY(1,1) PRIMARY KEY,
	NAME NVARCHAR(100),
	EMAIL NVARCHAR(50),
	SODT VARCHAR(12),
	GIOITINH BIT,
	DIACHI NVARCHAR(100)
)


