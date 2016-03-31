# docker-Dockerfile

### supervisor
+ 
```
FROM       centos:6.7
MAINTAINER dishui_git@126.com
#
ENV TZ "Asia/Shanghai"
ENV TERM xterm
#
ADD Centos-6.repo /etc/yum.repos.d/CentOS-Base.repo
ADD epel-6.repo /etc/yum.repos.d/epel.repo
#
RUN yum install -y curl wget tar bzip2 unzip vim-enhanced passwd sudo yum-utils hostname net-tools rsync man && \
    yum install -y gcc gcc-c++ git make automake cmake patch logrotate python-devel libpng-devel libjpeg-devel && \
    yum install -y --enablerepo=epel pwgen python-pip python-setuptools && \
    yum clean all
RUN easy_install supervisor
ADD supervisord.conf /etc/supervisord.conf
RUN mkdir -p /etc/supervisor.conf.d && \
    mkdir -p /var/log/supervisor
#
EXPOSE 22
#
ENTRYPOINT ["/usr/bin/supervisord", "-n", "-c", "/etc/supervisord.conf"]
```