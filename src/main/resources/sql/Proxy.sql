
drop table if exists Cfg_Proxy;

create table Cfg_Proxy
(
   id                   int not null AUTO_INCREMENT comment '代理自增ID',
   ip                   varchar(20) not null comment '使用ip和port作为主键避免重复插入',
   port                 int not null comment '使用ip和port作为主键避免重复插入',
   anonymousType        varchar(20) not null comment 'transparent、anonymous、distorting、elite',
   protocolType         varchar(20) not null comment 'http、https、socks4、socks5、socks',
   country              varchar(50),
   area                 varchar(50),
   valid                boolean not null,
   invalidTime          bigint comment 'ms级别时间戳',
   lastSurviveTime      bigint comment 'ms级时间',
   checkTime            bigint comment 'ms级别时间戳',
   checkStatus          int not null comment '0:未验证；1:已验证',
   score                float,
   sourceSite           varbinary(50) not null,
   validTime            int,
   crawlTime            bigint not null comment 'ms级别时间戳',
   responseTime         bigint comment 'ms级时间',
   primary key (ip, port)
);