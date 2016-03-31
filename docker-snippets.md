# docker-snippets

### es
+ 
```bash
docker run -d -p 2222:22 -p 9300:9300 -p 9200:9200 -v /root/opt/es:/mnt/es --name es-docker dishui/centos6.7-java
```
