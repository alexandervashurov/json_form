# this will automatically create a 'jenkins_home'
# volume on docker host,
# that will survive container stop/restart/deletion.
docker run -p 8080:8080 -p 50000:50000 \
-v /home/jenkins_data:/var/jenkins_home \
#
# bind mounting the host’s docker’s unix socket
-v /var/run/docker.sock:/var/run/docker.sock \
#
# logging in docker as root and then
# everything inside container run also as root
--rm -u root \
h1kkan/jenkins-docker:lts