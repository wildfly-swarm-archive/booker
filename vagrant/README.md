
# Vagrant

Vagrant enables the rapid and repeatable creation of virtual machines.

Use Vagrant if you want to run booker in a pre-canned virtual machine.
This is useful to quickly get demos up and running.

This requires that you download Vagrant (see https://www.vagrantup.com) and
Virtualbox (see https://www.virtualbox.org/).

After both are installed, simply change to the booker vagrant sub-directory
and run 'vagrant up'. The installation and setup will take a while since it
is downloading roughly 2.5-3GB of bits (including the Fedora OS, booker code,
maven artifacts, etc).

# Security
LOL! What security?? To prove that point, here are the login credentials:

###Root login
user: root  
password: root

###Demo login:
user: demo  
password: demo

Changing the demo and root passwords will not have a negative impact on
the demo (unless you forget the password).

# Running the demo
* After 'vagrant up' is done running, log in as demo (password demo)
* Open a terminal within the virtual machine and type 'run_all.sh'
* Open Firefox (or Chrome) within the virtual machine and go to <http://localhost:8080>.

# Speeding up the vagrant build
WildFly Swarm is so rockin' cool, we know that you really want to improve this
demo and post pull requests.  If you want to make modifications to the
image in an iterative fashion (during development), then it helps to use local bits
instead of the setup script downloading them for each iteration. To do this,
after you clone booker (and before you run 'vagrant up'), download the following
bits into the project's vagrant/provisioning/downloads directory and the setup script
will copy the archives instead of downloading them:
* <http://mirror.olnevhost.net/pub/apache/maven/maven-3/3.3.3/binaries/apache-maven-3.3.3-bin.zip>
* <http://downloads.jboss.org/keycloak/1.5.0.Final/keycloak-1.5.0.Final.zip>
* <https://download.elastic.co/kibana/kibana/kibana-4.1.2-linux-x64.tar.gz>
* <https://download.elastic.co/logstash/logstash/logstash-1.5.4.zip>
* .m2 directory containing populated Maven booker project repository

# JBoss Developer Studio

If you download jboss-devstudio-8.1.0.GA-installer-standalone.jar
(see <http://www.jboss.org/products/devstudio/download/>) into the 
vagrant/provisioning/downloads directory, then the setup script will also
install JBoss Developer Studio. It will not be automatically downloaded.
Alternatively, once Vagrant has created the virtual machine, you can log
in and install it.

