create table Member(
    `id` int not null primary key AUTO_INCREMENT,
    `nickname` varchar(30) not null,
    `pw` varchar(50) not null
);

create table board(
    `id` int not null primary key AUTO_INCREMENT,
    `title` varchar(30) not null,
    `writer` varchar(30) not null,
    `content` text not null,
    `startDate` datetime not null,
    `finDate` datetime not null
);

create table reply(
    `id` int not null primary key AUTO_INCREMENT,
    `writer` varchar(30) not null,
    `recontent` text not null
);

create table `file`(
    `id` int not null primary key AUTO_INCREMENT,
    `boardId` int not null,
    `filename` varchar(50) not null
);

create table `board_json`(
    `id` int primary key AUTO_INCREMENT,
    data JSON
);

Scaffold-DbContext "server=localhost;port=3306;user=root;password=Blizard5000@;database=androidDb" MySql.EntityFrameworkCore -OutputDir Entities -f
Scaffold-DbContext "server=localhost;port=3306;user=root;password=Blizard5000@;database=androidDb" Pomelo.EntityFrameworkCore.MySql -OutputDir Entities -f