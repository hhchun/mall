create table if not exists ac_platform_menu
(
    id          bigint(64)        not null comment 'id'
        primary key,
    del         tinyint default 0 not null comment '是否删除',
    create_time datetime          not null comment '创建时间',
    update_time datetime          null comment '修改时间',
    symbol      varchar(64)       not null comment '菜单标识',
    name        varchar(32)       not null comment '菜单名称',
    des         varchar(255)      null comment '描述/备注',
    pid         bigint(64)        not null comment '父菜单id',
    overt       tinyint default 0 not null comment '公开的菜单,0-否、1-是',
    constraint uni_symbol
        unique (symbol)
)
    comment '平台菜单';

create table if not exists ac_platform_permission
(
    id          bigint(64)        not null comment 'id'
        primary key,
    del         tinyint default 0 not null comment '是否删除',
    create_time datetime          not null comment '创建时间',
    update_time datetime          null comment '修改时间',
    symbol      varchar(64)       not null comment '权限标识',
    name        varchar(32)       not null comment '权限名称',
    des         varchar(255)      null comment '描述/备注',
    subject     varchar(255)      null comment '权限主体',
    overt       tinyint default 0 not null comment '公开权限,0-否、1-是',
    constraint uni_symbol
        unique (symbol)
)
    comment '平台权限';

create table if not exists ac_platform_role
(
    id          bigint(64)        not null comment 'id'
        primary key,
    del         tinyint default 0 not null comment '是否删除',
    create_time datetime          not null comment '创建时间',
    update_time datetime          null comment '修改时间',
    symbol      varchar(64)       not null comment '角色标识',
    name        varchar(32)       not null comment '角色名',
    des         varchar(255)      null comment '描述/备注',
    constraint uni_symbol
        unique (symbol)
)
    comment '平台角色';

create table if not exists ac_platform_role_menu
(
    id          bigint(64)        not null comment 'id'
        primary key,
    del         tinyint default 0 not null comment '是否删除',
    create_time datetime          not null comment '创建时间',
    update_time datetime          null comment '修改时间',
    role_id     bigint(64)        not null comment '角色id',
    menu_id     bigint(64)        not null comment '菜单id',
    constraint uni_role_menu
        unique (role_id, menu_id)
)
    comment '平台角色关联菜单';

create table if not exists ac_platform_role_permission
(
    id            bigint(64)        not null comment 'id'
        primary key,
    del           tinyint default 0 not null comment '是否删除',
    create_time   datetime          not null comment '创建时间',
    update_time   datetime          null comment '修改时间',
    role_id       bigint(64)        not null comment '角色id',
    permission_id bigint(64)        not null comment '权限id',
    constraint uni_role_permission
        unique (role_id, permission_id)
)
    comment '平台角色关联权限';

create table if not exists ac_platform_user
(
    id          bigint(64)        not null comment 'id'
        primary key,
    del         tinyint default 0 not null comment '是否删除',
    create_time datetime          not null comment '创建时间',
    update_time datetime          null comment '修改时间',
    username    varchar(32)       not null comment '用户名',
    password    varchar(32)       not null comment '密码',
    nickname    varchar(64)       null comment '昵称',
    constraint uni_username
        unique (username)
)
    comment '平台用户';

create table if not exists ac_platform_user_role
(
    id          bigint(64)        not null comment 'id'
        primary key,
    del         tinyint default 0 not null comment '是否删除',
    create_time datetime          not null comment '创建时间',
    update_time datetime          null comment '修改时间',
    user_id     bigint(64)        not null comment '平台用户id',
    role_id     bigint(64)        not null comment '角色id'
)
    comment '平台用户关联角色';

