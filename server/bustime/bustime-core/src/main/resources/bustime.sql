delimiter $$

CREATE TABLE `line` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水号，车次id',
  `lineGuid` varchar(64) NOT NULL COMMENT '车次标识',
  `lineNumber` varchar(32) NOT NULL COMMENT '车次号,如117',
  `lineInfo` varchar(128) NOT NULL COMMENT '车次描述:快线2号(独墅湖高教区首末站->火车站)',
  `link` varchar(256) NOT NULL COMMENT '查看车次站点详情链接',
  `trend` varchar(128) NOT NULL COMMENT '车次行驶方向，如 独墅湖高教区首末站->火车站',
  `status` enum('enable','disable') NOT NULL COMMENT '状态: 有效，无效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `line_lineGuid` (`lineGuid`),
  KEY `line_lineNumber` (`lineNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='线路信息表'$$


CREATE TABLE `station` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `standCode` varchar(32) NOT NULL COMMENT '站台编号',
  `standName` varchar(128) NOT NULL COMMENT '站台名称',
  `road` varchar(64) COMMENT '所在路名',
  `roadSection` varchar(128) COMMENT '所在路段',
  `area` varchar(64) COMMENT '所在区',
  `trend` varchar(16) COMMENT '所在方向',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `station_standCode` (`standCode`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='站台、车辆运行信息表'$$


CREATE TABLE `singleline` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水号，车次id',
  `lineGuid` varchar(64) NOT NULL COMMENT '车次标识',
  `standCode` varchar(32) NOT NULL COMMENT '站台编号,如PXX',
  `standName` varchar(128) NOT NULL COMMENT '站台名称',
  `link` varchar(256) NOT NULL COMMENT '查看车次站点详情链接',
  `status` enum('enable','disable') NOT NULL COMMENT '状态: 有效，无效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `name` (`lineGuid`),
  KEY `status` (`standCode`)
) ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8 COMMENT='车次信息表'$$

CREATE TABLE `station_bus` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '流水号',
  `lineGuid` varchar(64) NOT NULL COMMENT '车次标识',
  `standCode` varchar(128) NOT NULL COMMENT '站台编号',
  `link` varchar(256) NOT NULL COMMENT '详情链接',
  `status` enum('enable','disable') NOT NULL COMMENT '状态: 有效，无效',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  KEY `station_bus_lineGuid` (`lineGuid`),
  KEY `station_bus_standCode` (`standCode`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8 COMMENT='站台、车辆运行信息表'$$


