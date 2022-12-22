#!/bin/bash
sudo su
yum -y install docker
usermod -a -G docker ec2-user
id ec2-user
newgrp docker
systemctl enable docker.service
systemctl start docker.service

#docker run --name jenkins -p 80:8080 -d -v /your/home:/var/jenkins_home jenkins/jenkins
#docker exec <container> cat /var/jenkins_home/secrets/initialAdminPassword