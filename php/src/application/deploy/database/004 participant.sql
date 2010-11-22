--
-- netbout.com
--
-- Redistribution and use in source and binary forms, with or
-- without modification, are PROHIBITED without prior written
-- permission from the author. This product may NOT be used
-- anywhere and on any computer except the server platform of
-- netbout.com. located at www.netbout.com. If you received this
-- code occasionally and without intent to use it, please report
-- this incident to the author by email: privacy@netbout.com
--
-- Interlink between USER and NETBOUT, used for control access to NetBout-s
--
-- @category Data
-- @package MySQL
-- @see NetBout
-- @see ActorUser
-- @see Model_NetBout_Participant
-- @see Model_NetBout
-- @see Model_User
-- @version $Id$
--

CREATE TABLE IF NOT EXISTS `participant`
(
    -- INT UNSIGNED is used, because we have MEDIUMINT UNSIGNED in netBout.id column
    -- and here will be stored some records per each one NETBOUT
    `id` MEDIUMINT UNSIGNED NOT NULL AUTO_INCREMENT
        COMMENT "Unique ID of the link",
    `created` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
        COMMENT "Date when the row was created",

    -- MEDIUMINT UNSIGNED is used - @see netBout.id column
    `netBout` MEDIUMINT UNSIGNED NOT NULL
        COMMENT "Unique ID of the NetBout",

    -- MEDIUMINT UNSIGNED is used - @see user.id column
    `invited` MEDIUMINT UNSIGNED NOT NULL
        COMMENT "Invited participant",

    -- MEDIUMINT UNSIGNED is used - @see user.id column
    -- In case when participant.status = "creator" we will have here inviter = NULL, because
    -- creator of the NetBout is not invited by any other user.
    `inviter` MEDIUMINT UNSIGNED DEFAULT NULL
        COMMENT "Who send this invitation",

    -- We using SHA256 hash with, so it's always 64 chars,
    `secretKey` CHAR(64) NOT NULL
        COMMENT "Secret key used for invitation identification",

    -- Participant status in this NetBout
    `status` ENUM('creator', 'pending', 'invited', 'accepted', 'rejected') NOT NULL
        COMMENT "Participant status",

    -- Links are identified by ID
    PRIMARY KEY(`id`),

    -- Link to the NetBout
    FOREIGN KEY(`netBout`) REFERENCES `netBout`(`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    -- Link to the invited user
    FOREIGN KEY(`invited`) REFERENCES `user`(`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    -- Link to the invited user
    FOREIGN KEY(`inviter`) REFERENCES `user`(`id`)
        ON UPDATE CASCADE
        ON DELETE CASCADE,

    UNIQUE(`netBout`, `invited`),
    INDEX(`secretKey`)
)
AUTO_INCREMENT=1
DEFAULT CHARSET=utf8
ENGINE=InnoDB
COMMENT="Interlink between USER and NETBOUT";
