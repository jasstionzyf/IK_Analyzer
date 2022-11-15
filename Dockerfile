FROM  exoplatform/ubuntu:20.04
LABEL maintainer="eXo Platform <docker@exoplatform.com>"




RUN apt update -y
RUN apt upgrade -y
RUN apt install -y curl software-properties-common
# opencv
run apt install -y libglib2.0-0 libgl1-mesa-glx

RUN apt install -y vim htop net-tools git-all screen sysstat xfsprogs zip iputils-ping telnet psmisc wget
# Install OpenJdk Java 11 SDK
RUN apt-get update && apt-get -y install openjdk-11-jdk-headless && rm -rf /var/lib/apt

WORKDIR /data/projects/ik_analyzer/

ENTRYPOINT ["/usr/local/bin/tini", "--", "/usr/bin/java"]
