//Installation et lancement
mvn clean package
mvn wildfly-swarm:run


// URL (brochain.hd.free.fr ou localhost)
http://brochain.hd.free.fr:8181/OHLC?grain=5
http://brochain.hd.free.fr:8181/AlerteMACD?grain=5
http://brochain.hd.free.fr:8181/macd/?grain=5

// Arrêt du serveur
mvn wildfly-swarm:stop
netstat -antu | grep 8181
jps
kill -9


// Configuration
L'adresse d'apel de l'API est modifiable dans les fichiers resources/config.properties 