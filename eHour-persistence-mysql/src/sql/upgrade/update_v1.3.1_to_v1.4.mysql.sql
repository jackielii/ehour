INSERT INTO CONFIGURATION (CONFIG_KEY, CONFIG_VALUE) VALUES('reminderEnabled', 'false');
INSERT INTO CONFIGURATION (CONFIG_KEY, CONFIG_VALUE) VALUES('reminderBody', 'Our records show that you have not posted your weekly hours online. Please be sure to post your hours by 5:30PM Friday.');
INSERT INTO CONFIGURATION (CONFIG_KEY, CONFIG_VALUE) VALUES('reminderTime', '* 30 17 * * FRI');
INSERT INTO CONFIGURATION (CONFIG_KEY, CONFIG_VALUE) VALUES('reminderSubject', 'Missing hours');
INSERT INTO CONFIGURATION (CONFIG_KEY, CONFIG_VALUE) VALUES('reminderMinimalHours', '32');

UPDATE CONFIGURATION SET CONFIG_VALUE = '1.4' WHERE CONFIG_KEY = 'version';
