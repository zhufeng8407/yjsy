password.c��
	˵������ȡ��Ļ���룬ʹ�� Windows ����������������ʱ����������ʾ����Ļ�ϡ�password.obj �� password.exe Ϊ�������ļ���
	drop-table.bat��dump.bat �� load.bat �����������ʹ���˸ù��ܡ�
	
drop-table.sh��
	˵����Linux shell �ű�������ɾ��ĳ�����ݿ������еı�ע�����øýű���
		Ȩ�ޣ�`chmod a+x drop-table.sh`��
	������
		--mysql-username������Ҫ���ʵ����ݿ���û�����Ĭ��ֵΪ 'root'��
		--mysql-database������Ҫɾ��������ݿ�����ݿ�����Ĭ��ֵΪ 'yjsy'��
		--mysql-host������Ҫ�������ݿ�� Host ��ַ��Ĭ��ֵΪ 'localhost'��
		-h���鿴�����ĵ�
	����ɾ���û���Ϊ 'root'��Host Ϊ 'localhost'�����ݿ���Ϊ 'yjsy' �����ݿ�����б�
		`./drop-table.sh --mysql-username root --mysql-database yjsy --mysql-host localhost`
		
drop-table.bat��
	drop-table.sh �� Windows ���������汾���÷��� drop-table.sh ��ͬ��

dump.sh��
	˵����Linux shell �ű������ڱ���ָ�������ݿ⡣ע�����øýű���
		Ȩ�ޣ�`chmod a+x dump.sh`��
	����:
		--mysql-username������Ҫ���ʵ����ݿ���û�����Ĭ��ֵΪ 'root'��
		--mysql-database������Ҫɾ��������ݿ�����ݿ�����Ĭ��ֵΪ 'yjsy'��
		--mysql-host������Ҫ�������ݿ�� Host ��ַ��Ĭ��ֵΪ 'localhost'��
		-f��ָ������ı����ļ���Ĭ��ֵΪ 'yjsy.sql'��
		-h���鿴�����ĵ�
	���������û���Ϊ 'root'��Host Ϊ 'localhost'�����ݿ���Ϊ 'yjsy' �����ݿ⣬�����ļ�Ϊ '/home/yjsyTest/yjsy.sql'��
		`./dump.sh --mysql-username root --mysql-database yjsy --mysql-host localhost -f /home/yjsyTest/yjsy.sql`
	
dump.bat��
	dump.sh �� Windows ���������汾���÷��� dump.sh ��ͬ��
	
load.sh��
	˵����Linux shell �ű������ڼ��ر����ļ������ݿ��С�ע�����øýű���
		Ȩ�ޣ�`chmod a+x load.sh`��
	����:
		--mysql-username������Ҫ���ʵ����ݿ���û�����Ĭ��ֵΪ 'root'��
		--mysql-database������Ҫɾ��������ݿ�����ݿ�����Ĭ��ֵΪ 'yjsy'��
		--mysql-host������Ҫ�������ݿ�� Host ��ַ��Ĭ��ֵΪ 'localhost'��
		-f��ָ�������ļ��ĵ�ַ��Ĭ��ֵΪ 'yjsy.sql'��
		-d�����ָ���� '-d' ��������ɾ�����ؽ�ָ�������ݿ⣬�����ɾ��ָ�����ݿ�����б�
		-h���鿴�����ĵ�
	������Ҫ���ر��ݵ����ݿ���û���Ϊ 'root'��Host Ϊ 'localhost'�����ݿ���Ϊ 'yjsy'��Ҫ���صı����ļ�Ϊ 
	'/home/yjsyTest/yjsy.sql'��ָ�� '-d' ��ɾ�����ؽ� 'yjsy' ���ݿ⣺
		`./load.sh --mysql-username root --mysql-database yjsy --mysql-host localhost -f /home/yjsyTest/yjsy.sql -d`
		
load.bat��
	load.sh �� Windows ���������汾���÷��� load.sh ��ͬ��

backup-full.sh��
	˵����Linux shell �ű�������ʵ�����ݿ��ȫ���ݣ������ļ�����ѹ������������������ÿ��
		ȫ���ݺ�ɾ��֮ǰ���е����������ļ����鿴 mysqlbak.log ��־�ļ���鱸���Ƿ�ɹ���
	������
		--mysql-username������Ҫ�������ݿ���û�����Ĭ��ֵΪ 'root'��
		--mysql-database������Ҫɾ��������ݿ⣬Ĭ��ֵΪ 'yjsy'��
		--mysql-host������Ҫ�������ݿ�� Host ��ַ��Ĭ��ֵΪ 'localhost'��
		--mysql-password������Ҫ�������ݿ��û������룬Ĭ��ֵΪ 'root'��
		--bakdir��ָ�������ļ��Ĵ洢Ŀ¼��Ĭ��ֵΪ '/home/yjsyTest/mysqlDump'��
		-h���鿴�����ĵ�
	������Ҫ���ݵ����ݿ���û���Ϊ 'root'��Host Ϊ 'localhost'�����ݿ���Ϊ 'yjsy'������Ϊ 'root'�������ļ��Ĵ洢Ŀ¼Ϊ
		'/home/yjsyTest/mysqlDump'��
		`./backup-full.sh --mysql-username root --mysql-database yjsy --mysql-host localhost --mysql-password root  \ 
		--bakdir /home/yjsyTest/mysqlDump`
		
backup-incremental.sh��
	˵����Linux shell �ű�������ʵ�����ݿ���������ݡ����������ļ��洢��ָ�������ļ�Ŀ¼
		�µ� daily Ŀ¼�С��鿴 mysqlbak.log ��־�ļ���鱸���Ƿ�ɹ���
	������
		--datadir��ָ�����ݿ��������־�ļ��Ĵ洢Ŀ¼��Ĭ��ֵΪ '/var/lib/mysql'��
		--filename��ָ�����ݿ��������־�����ļ���ǰ׺���� '/etc/my.cnf' �����ļ��� 
			"log_bin" ���õ�ֵһ�£�Ĭ��ֵΪ 'mysqld'����ʱ�����ļ����ļ�����Ϊ 
			'mysqld-bin.index'��
		--mysql-password������Ҫ�������ݿ��û������룬Ĭ��ֵΪ 'root'��
		--bakdir��ָ�������ļ��Ĵ洢Ŀ¼��Ĭ��ֵΪ '/home/yjsyTest/mysqlDump'��
		-h���鿴�����ĵ�
	������Ҫ���ݵ����ݿ�����Ϊ 'root'���������������ִ�� 'mysqladmin flush-logs -p' ���Mysql ��־�ļ�����Ŀ¼Ϊ 
		'var/lib/mysql'�������ļ�ǰ׺Ϊ 'mysqld'�������ļ��Ĵ洢Ŀ¼Ϊ'/home/yjsyTest/mysqlDump'�����������ļ����� 
		'/home/yjsyTest/mysqlDump/daily' Ŀ¼�У�
		`./backup-incremental --mysql-password root --bakdir /home/yjsyTest/mysqlDump --datadir /var/lib/mysql --filename mysqld`