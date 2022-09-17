# home-inventory
This application is created to manage items located at home

## Docker commands
docker run --detach --env MYSQL_ROOT_PASSWORD=dummypassword --env MYSQL_USER=home-inventory-user --env MYSQL_PASSWORD=dummypassword --env MYSQL_DATABASE=home-inventory-database --name mysql --publish 3306:3306 mysql:8-oracle