
# Multicast

On OSX, you'll probably need to enable multicast on localhost:

    sudo route add -net 230.0.0.0/4 127.0.0.1

# Components

## `web-client`

Simple, normal `.war` deployment that serves the React.js-based
single-page-app, along with an async servlet to power the
Server-Sent-Events (SSE) for Ribbon service discovery.

The React.js components communicate directly with the `store`
and `library` services.

##  `store`

Book inventory (pulled from Project Gutenberg) served from
a JAX-RS resource from a CDI-injected service.  Uses the 
`pricing` service via Ribbon to determine the price of each
item in the store.

## `pricing`

Simple pricing service that indicates everything is $10 if 
you're browsing anonymously, or $9 if you're logged in.

## `library`

Tracks which items are bought by a user using JPA (via an h2 
database) from a JAX-RS resource.  Communicates with the `store`
service to associate details with a given book ID.

## `Vagrant`
A Vagrantfile and support scripts to install and run booker in a 
virtual machine. Requires Virtualbox and Vagrant be installed.

