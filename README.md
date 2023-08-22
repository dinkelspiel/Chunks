# Keii's Chunks

A claim plugin based on FTB Chunks


# Database setup

Keii's Chunks requires a mysql database running on the server computer because of it's code strucutre. A sqlite one that works
out of the box is in the works but no promises on when it will be relased.

# Debian

## Install MariaDB Server
```bash
$ sudo apt update &&
$ sudo apt install mariadb-server &&
$ sudo mysql_secure_installation
```

## Configure a user for the mariadb server

Enter the mariadb installation
```bash
$ sudo mariadb
```

Create a database for keii's chunks
```
MariaDB [(none)]> CREATE DATABASE <database_name>;
```

Create a new user and give it access to the database
```
MariaDB [(none)]> CREATE USER '<user_name>'@'localhost' IDENTIFIED BY '<password>';
MariaDB [(none)]> GRANT ALL PRIVILEGES ON `<database_name>`.* TO '<user_name>'@'localhost';
MariaDB [(none)]> FLUSH PRIVILEGES;
```

## Update config.yml

In the server directory go to `plugins/KeiiChunks/config.yml`. Create the file if it doesn't exist and add/update this to the file.

All of the entries in arrows '<>' are the same as the inputs you put in the above mariadb configuration.

(If you installed the database on a machine that isn't the machine the server is running on you want to change the dbUrl to `jdbc:mysql://<machine_ip>:3306/`)
```yml
dbUrl: jdbc:mysql://localhost:3306/
dbName: <database_name>
dbUser: <user_name>
dbPassword: <password>
```

## Restart the server

Now restart the server and the database should be setup. If you already have custom server resource packs on the server then you will want to merge that resource pack with [the plugin resourcepack](https://github.com/shykeiichi/plugin-resourcepack/blob/main/release.zip) using something like https://merge.elmakers.com/.

## If your database wasn't setup automatically

If the database wasn't updated automatically then you will want to update the database manually by running.
```bash
$ mysql -u <user_name> -p <database_name> < database.sql
```
database.sql is located in plugin/KeiiChunks.

