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
  KEY `name` (`lineGuid`),
  KEY `status` (`lineNumber`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='车次信息表'$$

