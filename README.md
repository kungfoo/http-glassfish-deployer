# Deploy to glassfish using HTTP

## Build status

<a href="https://travis-ci.org/kungfoo/http-glassfish-deployer"><img src="https://travis-ci.org/kungfoo/http-glassfish-deployer.svg?branch=master"/></a>

## What?

This is a gradle plugin uses the glassfish admin console REST-like API to deploy apps,
instead of you having to click around to do it. Or rely on autodeploy (which doesn't work with properly versioned
deployment archives...)

## How?

Add the following to your gradle build:

    plugins {
      id "ch.mollusca.glassfish.http.deployer"
    }

This adds two tasks that should be rather self-explanatory.

- deploy
- undeploy

### Mandatory configuration

As of now, you have to tell the plugin what to deploy:

    # Add to build.gradle
    
    apply plugin: 'war' # or whatever you use to assemble a deployment archive
    
    http_deployer {
        deploymentArchive = war.archivePath
    }

### Other configuration

Other settings that are available (they all have defaults):

    secure = false              # default: true         Do not validate SSL certificates at all
    host = "can.has.host.com"   # default: localhost    Use another hostname
    scheme = "http"             # default: https        Use http instead of https
    port = 9898                 # default: 4848         Use another admin console port
    force = true                # default: false        Force (re-)deployment
    contextRoot = 'sample'      # default: ''           Use a context root
    applicationName = 'sample'  # default: ''           Use a application name
    timeoutDuration = 3         # default: 12           Use a different timeout
    timeoutUnit                 # default: SECONDS      Use a different timeout duration unit
    user = 'foo'                # default: 'admin'      Use another user name
    password = 'asdf'           # default: ''           Use a password


## Why?

Because cargo is not cutting it for us and it's very heavy-weight.
Plus it seems that the gradle plugin is abandonware.

## Why not curl?

Curl commands are awesome, don't get me wrong. This is a gradle plugin however and it uses okhttp to do requests.

