-- 创建并使用物联网数据库
create database iotdb;
use iotdb;

-- 用户表
create table users (
                       id int unsigned primary key auto_increment comment 'ID',
                       username varchar(20) not null unique comment '用户名',
                       password varchar(32)  comment '密码',
                       nickname varchar(10)  default '' comment '昵称',
                       email varchar(128) default '' comment '邮箱',
                       description varchar(100) default '' comment '个人描述',
                       create_time datetime not null comment '创建时间',
                       update_time datetime not null comment '修改时间'
) comment '用户表';
insert into users value (1,'admin','admin','','','',now(),now());


create table categories(
                           id int unsigned primary key  auto_increment comment 'ID',
                           name varchar(32) not null comment '分类名称',
                           description varchar(100) default '' comment '分类描述',
                           count int unsigned default 0 comment '该种类设备数量',
                           create_time datetime not null comment '创建时间',
                           update_time datetime not null comment '修改时间'
) comment '设备分类表';
insert into categories value (1,'默认分类','未分类项目',0,now(),now());

-- 设备表
create table devices(
                        id int unsigned primary key  auto_increment comment 'ID',
                        name varchar(30) not null comment '设备名称',
                        state int unsigned not null default 2 comment '0-正常 1-警告 2-断线',
                        category_id int unsigned not null comment '设备分类',
                        description varchar(100) default '' comment '设备描述',
                        create_time datetime not null comment '创建时间',
                        update_time datetime not null comment '更新时间'
)comment '设备表';


-- 设备信息表
create table messages(
                         alert boolean not null comment '是否警告',
                         client_id varchar(20)  not null,
                         info varchar(200) not null ,
                         lat decimal(12,8) not null comment '纬度',
                         lng decimal(12,8) not null comment '经度',
                         timestamp long not null comment '时间戳',
                         value int not null comment '设备数值, 嗯~无效测试数据'
);

-- token表
create table tokens(
                       token varchar(160) not null primary key ,
                       expire datetime not null
);

-- 触发器

DELIMITER $$
create trigger devices_insert_trigger after insert on devices
    for each row
begin
    update categories set count = count + 1 where id = NEW.category_id;
end$$

create trigger devices_update_trigger after update on devices
    for each row
begin
    update categories set count = count +1 where id = NEW.category_id;
    update categories set count = count -1 where id = OLD.category_id;
end$$

create trigger devices_delete_trigger after delete on devices
    for each row
begin
    update categories set count = count -1 where id = OLD.category_id;
end$$

-- 事件

-- 删除过期token
create event check_expired_tokens
    on schedule every 1 minute
    do
    begin
        delete from tokens where expire < now();
    end$$
DELIMITER ;

-- 插入部分数据用于初始化
-- 插入分类 2 3 4 5 
insert into categories(name, description, create_time, update_time) values ('test', 'test', now(), now());
insert into categories(name, description, create_time, update_time) values ('光控', 'test', now(), now());
insert into categories(name, description, create_time, update_time) values ('温控',  null , now(), now());
insert into categories(name, description, create_time, update_time) values ('声控', 'test', now(), now());

insert into devices(name, state, category_id, description, create_time, update_time) values ('test1',0, 1, 'test1',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('test2',1, 1, 'test2',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('test3',2, 1, 'test3',now(),now());

insert into devices(name, state, category_id, description, create_time, update_time) values ('光纤',0, 3, 'test4',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('光线',0, 3, 'test4',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('红光',0, 3, 'test4',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('颜色控制',0, 3, 'test4',now(),now());

insert into devices(name, state, category_id, description, create_time, update_time) values ('冰冻',0, 4, 'temperature1',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('常温',0, 4, 'temperature2',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('滚烫',0, 4, 'temperature3',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('绝对零度',0, 4, 'afeeaf',now(),now());
insert into devices(name, state, category_id, description, create_time, update_time) values ('零度',0, 4, 'afeweeeeg',now(),now());

insert into devices(name, state, category_id, description, create_time, update_time) values ('声音',0, 5, 'voice',now(),now());
