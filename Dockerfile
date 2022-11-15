FROM  exoplatform/ubuntu:20.04
LABEL maintainer="eXo Platform <docker@exoplatform.com>"




RUN apt update -y
RUN apt upgrade -y
RUN apt install -y curl software-properties-common


RUN apt install -y vim htop net-tools git-all screen sysstat xfsprogs zip iputils-ping telnet psmisc wget
# Install OpenJdk Java 11 SDK
RUN apt-get -y install openjdk-11-jdk-headless && rm -rf /var/lib/apt


ARG mvn_version=3.6.3
RUN wget --no-verbose -O /tmp/apache-maven-${mvn_version}-bin.tar.gz http://www-eu.apache.org/dist/maven/maven-3/${mvn_version}/binaries/apache-maven-${mvn_version}-bin.tar.gz && \
    tar xzf /tmp/apache-maven-${mvn_version}-bin.tar.gz -C /opt/ && \
    ln -s /opt/apache-maven-${mvn_version} /opt/maven && \
    ln -s /opt/maven/bin/mvn /usr/local/bin  && \
    rm -f /tmp/apache-maven-${mvn_version}-bin.tar.gz

ENV MAVEN_HOME /opt/maven
ENV PATH="$PATH:${MAVEN_HOME}/bin"

WORKDIR /data/projects/

RUN git clone https://jasstionzyf:139jasstion@gitee.com/jasstionzyf/ik_analyzer.git
WORKDIR /data/projects/ik_analyzer
RUN git fetch -a && git checkout master
RUN mvn clean install
CMD ["/data/projects/ik_analyzer/ik_start.sh"]
